package com.example.signin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.signin.entity.*;
import com.example.signin.mapper.*;
import com.example.signin.service.SigninService;
import com.example.signin.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.InfoUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class SigninServiceImpl implements SigninService {

    @Autowired
    RequestUtil requestUtil;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    SigninUserMapper signinUserMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ActivityTypeMapper activityTypeMapper;
    @Autowired
    ActivityDetailMapper activityDetailMapper;

    /**
     * 用户签到方法，用于处理签到请求并将其存储到数据库中。
     * 若locationDTO中的管理员标识为"true"，则执行完整的签到逻辑，包括存储活动信息、获取当前时间、遍历部门ID以及记录用户签到信息。
     *
     * @param signinMessage 包含签到相关信息的数据传输对象，其中包含是否为管理员的标识字段yesOrNoAdministrator。
     * @return 若执行了管理员签到逻辑，则返回null；否则（非管理员签到）同样返回null。
     * @throws JsonProcessingException 当Gson处理JSON数据时可能出现的异常。
     */
    @Override
    public ResponseResult createSignIn(SigninMessage signinMessage) throws JsonProcessingException {
        //检查活动名称是否重复
        if (activityMapper.selectByActivityName(signinMessage.getActivity().getActivityName()) != 0) {
            return new ResponseResult(400, "活动名称重复");
        }

        // 检查活动类型合法性
        String typeName = signinMessage.getActivity().getTypeName();
        if (typeName == null || typeName.equals("")) {
            return new ResponseResult(400, "活动类型不能为空");
        }
        // 计算活动时间长度
        LocalDateTime startTime = signinMessage.getActivity().getStartTime();
        LocalDateTime endTime = signinMessage.getActivity().getEndTime();

        Duration nowToBegin = Duration.between(LocalDateTime.now(ZoneId.of("Asia/Shanghai")), startTime);
        Duration beginToEnd = Duration.between(startTime, endTime);

        // 检查活动时间是否有效（开始时间晚于当前时间）
        if (startTime.isBefore(LocalDateTime.now(ZoneId.of("Asia/Shanghai")))) {
            return new ResponseResult(400, "活动的开始时间必须晚于当前时间");
        }

        // 检查活动时间是否有效（开始时间早于结束时间）
        if (startTime.isAfter(endTime)) {
            return new ResponseResult(400, "活动的开始时间必须早于结束时间");
        }

        QueryWrapper<ActivityType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_name", typeName);

        ObjectMapper objectMapper = new ObjectMapper(); // 创建一个ObjectMapper实例
        String sponsorLocationJson = objectMapper.writeValueAsString(signinMessage.getSponsorLocation());

        // 检查地址信息
        if (activityTypeMapper.selectOne(queryWrapper).getTypeId() == 1) {
            if (signinMessage.getSponsorLocation().getLatitude() == null || signinMessage.getSponsorLocation().getLongitude() == null || signinMessage.getSponsorLocation().getDistance() == null) {
                return new ResponseResult(400, "地址信息错误");
            }

            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
                // 设置Redis过期时间
                redisUtil.setWithExpire(signinMessage.getActivity().getActivityName(), sponsorLocationJson, beginToEnd.toSeconds(), TimeUnit.SECONDS);
            }, nowToBegin.toSeconds(), TimeUnit.SECONDS);
        }
        if (activityTypeMapper.selectOne(queryWrapper).getTypeId() == 2) {
            RegularUpdatesUtil.scheduleStartAndShutdown(signinMessage.getActivity().getActivityName(), beginToEnd.toSeconds(), signinMessage.getActivity().getStartTime());
        }

        // 选择不同的插入方式
        boolean isAdmin = Boolean.parseBoolean(signinMessage.getYesOrNoAdministrator());
        List<Integer> departmentIdList = signinMessage.getActivity().getDepartmentId();
        List<Long> userIdList = signinMessage.getActivity().getUserId();
        if (userIdList != null && !userIdList.isEmpty()) {
            for (Long userIdStr : userIdList) {
                User user;
                if (isAdmin) {
                    user = userMapper.selectById(userIdStr);
                } else {
                    user = userMapper.selectByIdAndRoleId(userIdStr);
                }
                if (user != null) {
                    int insertResult = signinUserMapper.insert(user, signinMessage.getActivity().getActivityName());
                    if (insertResult == 0) {
                        return new ResponseResult(400, "签到过程中发生错误：插入失败");
                    }
                } else {
                    return new ResponseResult(400, "签到过程中发生错误：某个用户不存在或角色关联不匹配");
                }
            }
        }
        if (departmentIdList != null && !departmentIdList.isEmpty()) {
            for (Integer departmentIdStr : departmentIdList) {
                List<User> userList;
                if (isAdmin) {
                    userList = userMapper.selectByDepartmentId(departmentIdStr);
                } else {
                    userList = userMapper.selectByDepartmentIdAndRoleId(departmentIdStr);
                }
                if (userList != null && !userList.isEmpty()) {
                    for (User user : userList) {
                        int insertResult = signinUserMapper.insert(user, signinMessage.getActivity().getActivityName());
                        if (insertResult == 0) {
                            return new ResponseResult(500, "签到过程中发生错误：插入失败");
                        }
                    }
                } else {
                    return new ResponseResult(500, "签到过程中发生错误：某个部门下没有用户或角色关联不匹配");
                }
            }
        }

        // 转换userIdList为String类型数组
        List<String> userIdStrings = signinMessage.getActivity().getUserId()
                .stream()
                .map(String::valueOf) // 将Long类型映射为String类型
                .toList(); // 转换为List<String>

        // 转换departmentIdList为String类型数组
        List<String> departmentIdStrings = signinMessage.getActivity().getDepartmentId()
                .stream()
                .map(String::valueOf) // 将Integer类型映射为String类型
                .toList(); // 转换为List<String>

        // 插入活动信息至数据库
        int rowsAffected = activityMapper.insertActivity(signinMessage.getActivity(), String.join(",", userIdStrings), String.join(",", departmentIdStrings), LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        if (rowsAffected == 0) {
            return new ResponseResult(500, "插入活动信息失败");
        }
        return new ResponseResult(200, "签到创建成功", signinMessage);
    }


    @Override
    public ResponseResult signinList() {
        List<ActivityList> activities = activityMapper.selectAllActivities();//所有活动
        if (activities.isEmpty()) {
            return new ResponseResult(200, "没有签到活动");
        }
        for (ActivityList activity : activities) {
            LocalDateTime endTime = activity.getEndTime();
            LocalDateTime startTime = activity.getStartTime();
            if (LocalDateTime.now(ZoneId.of("Asia/Shanghai")).isBefore(startTime)) {
                activity.setStatus("未开始");
            } else if (LocalDateTime.now(ZoneId.of("Asia/Shanghai")).isAfter(endTime) || LocalDateTime.now(ZoneId.of("Asia/Shanghai")).equals(endTime)) {
                activity.setStatus("已完成");
            } else if (LocalDateTime.now(ZoneId.of("Asia/Shanghai")).isBefore(endTime)) {
                activity.setStatus("正在进行");
            }

            String activityName = activity.getActivityName();
            ActivityList medium = userMapper.countByActivityName(activityName); //拿到参与此活动的签到人数和未签到人数
            if (medium == null) {
                activity.setRate("0%");
                activity.setSigned(0);
                activity.setNotSigned(0);
            } else {
                Integer signed = medium.getSigned();  //已经签到的人数
                Integer notSigned = medium.getNotSigned();  //已未签到的人数

//                计算签到率
                if (signed + notSigned == 0) {
                    activity.setRate("0%");
                } else {
                    BigDecimal signedNum = new BigDecimal(signed);
                    BigDecimal sumNum = new BigDecimal(signed + notSigned);
                    BigDecimal resultBigDecimal = signedNum.divide(sumNum, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                    String rate = String.format("%.1f%%", resultBigDecimal);
                    activity.setRate(rate);
                }
                activity.setSigned(signed);
                activity.setNotSigned(notSigned);
            }

        }
        activities.sort((o1, o2) -> {
            if (o1.getStatus().equals("未开始") && o2.getStatus().equals("未开始")) {
                return 0; // 保持原有顺序
            } else if (o1.getStatus().equals("未开始")) {
                return -1; // o1 在 o2 之前
            } else if (o2.getStatus().equals("未开始")) {
                return 1; // o2 在 o1 之前
            } else if (o1.getStatus().equals("正在进行") && o2.getStatus().equals("已完成")) {
                return -1; // o1 在 o2 之前
            } else if (o1.getStatus().equals("已完成") && o2.getStatus().equals("正在进行")) {
                return 1; // o2 在 o1 之前
            } else {
                return 0; // 保持原有顺序
            }
        });

        return new ResponseResult(200, "签到列表获取成功", activities);
    }

    @Override
    public ResponseResult getDetail(String activityName) {
//        判断有没有这个活动
        SignDetail signDetail = activityMapper.selectActivitiesByName(activityName);
        if (signDetail == null) {
            return new ResponseResult(200, "没有这个签到活动");
        }
        //查此活动人员列表，分为有人参加和美人参加加两种情况
        SignDetail activityDetail = activityMapper.selectActivityDetailByName(activityName);
        if (activityDetail == null) {
            signDetail.setPeopleNumber("0/0");
            signDetail.setSignData(new ArrayList<>());
            signDetail.setDepartmentName(new ArrayList<>());
            return new ResponseResult(200, "活动信息获取成功", signDetail);
        } else {
            ActivityList medium = userMapper.countByActivityName(activityName); //拿到参与此活动的签到人数和未签到人数
            activityDetail.setPeopleNumber(medium.getSigned() + "/" + medium.getNotSigned());//人数比
            List<SignUserDetail> signData = activityDetail.getSignData();//拿到所有参加此活动的人员,根据这个集合拿部门
            LinkedHashSet<String> departmentName = new LinkedHashSet<>();//存部门的有序集合
            signData.forEach(signUserDetail -> {
                String name = signUserDetail.getDepartmentName();
                departmentName.add(name);
            });
            activityDetail.setDepartmentName(new ArrayList<>(departmentName));
        }


        return new ResponseResult(200, "活动信息获取成功", activityDetail);
    }


    @Override
    public ResponseResult stopSignIn(ActivityName activityName) {
        //检查是否有这项活动
        if (activityMapper.selectByActivityName(activityName.getActivityName()) == 0) {
            return new ResponseResult(400, "未查询到这项活动");
        }

        QueryWrapper<ActivityDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_name", activityName.getActivityName());
        ActivityDetail activityDetail = activityDetailMapper.selectOne(queryWrapper);

        if (LocalDateTime.now(ZoneId.of("Asia/Shanghai")).isAfter(activityDetail.getEndTime())){
            return new ResponseResult(400, "活动已结束");
        }

        int typeId = activityMapper.selectTypeByActivityName(activityName.getActivityName());
        if (typeId == 1) {
            redisUtil.del(activityName.getActivityName());
        } else if (typeId == 2) {
            boolean isCancel = RegularUpdatesUtil.cancelScheduledTaskIfExists(activityName.getActivityName());
            if (!isCancel){
                return new ResponseResult(400, "签到活动已经被取消");
            }
        }
        return new ResponseResult(200, "签到结束");

    }

    @Override
    public ResponseResult SignIn(UserSignIn userSignIn, HttpServletRequest request) throws JsonProcessingException {
        InfoUser user = requestUtil.getUserInfo(request.getHeader("X-USER-ID"));

        // 获取并格式化当前系统时间
        LocalDateTime currentTime =  LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String signinTime = currentTime.format(formatter);

        Integer userStatus = signinUserMapper.selectStatusByIdAndActivityName(user.getId(), userSignIn.getActivityName());

        if (userStatus == null){
            return new ResponseResult(400, "当前用户未参加此次活动，无需签到");
        }

        if (userStatus == 1) {
            return new ResponseResult(400, "你已经签到了");
        }

        QueryWrapper<ActivityDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_name", userSignIn.getActivityName());
        ActivityDetail activityDetail = activityDetailMapper.selectOne(queryWrapper);

        if (currentTime.isAfter(activityDetail.getEndTime()) || currentTime.isBefore(activityDetail.getStartTime())) {
            return new ResponseResult(400, "获取失败，当前活动时间未开始或已结束");
        }

        int typeId = activityDetail.getTypeId();

        if (typeId == 1) {
            String signInSponsorLocation = (String) redisUtil.get(userSignIn.getActivityName());
            SponsorLocation sponsorLocation = new ObjectMapper().readValue(signInSponsorLocation, SponsorLocation.class);
            double distance = DistanceCalculatorUtil.calculateDistance(userSignIn.getUserSponsorLocation(), sponsorLocation);
            if (distance <= sponsorLocation.getDistance()) {
                signinUserMapper.updateByIdAndActivityName(user.getId(), signinTime, userSignIn.getActivityName());
                return new ResponseResult(200, "签到成功");
            } else {
                return new ResponseResult(400, "签到失败：距离过远");
            }
        } else {
            if (userSignIn.getCharacter().equals(RegularUpdatesUtil.getRandomStringByKey(userSignIn.getActivityName()))) {
                signinUserMapper.updateByIdAndActivityName(user.getId(), signinTime, userSignIn.getActivityName());
                return new ResponseResult(200, "签到成功");
            } else {
                return new ResponseResult(400, "签到失败：二维码错误");
            }
        }
    }


    @Override
    public ResponseResult changeUserSignInStatus1(SignInChange signInChange) {
        // 获取并格式化当前系统时间
        LocalDateTime currentTime =  LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String signinTime = currentTime.format(formatter);

        if (signinUserMapper.selectStatusByIdAndActivityName(signInChange.getId(), signInChange.getActivityName()) == 1) {
            return new ResponseResult(400, "该用户已经签到了");
        }

        signinUserMapper.updateByIdAndActivityName(signInChange.getId(),signinTime,signInChange.getActivityName());
        return new ResponseResult(200, "签到成功");
    }

    public ResponseResult delateActivity (Activity activityName) {
        //这里判断这个活动还在不在
        boolean a =  activityMapper.selectByActivityName1(activityName.getActivityName());
        if (a) {
            activityMapper.delateActivity1(activityName.getActivityName());
            signinUserMapper.delateActivity(activityName.getActivityName());
            return new ResponseResult(200, "活动和人员删除成功");
        }else {
            return new ResponseResult(400, "活动不存在或已删除");
        }
    }

    @Override
    public ResponseResult updateActivity(upActivity activity) {
        List<String> UserId=activity.getUserId();
        List<String> DepartmentId= activity.getDepartmentId();
        if (activity.getUserId() == null || activity.getDepartmentId() == null){
            return new ResponseResult(400, "参数输入错误");}
        upActivity updateActivity = new upActivity();
        //用来去掉,
        String newValueUserId = null;
        String newValueDepartmentId = null;
        for (String userId : UserId) {
            newValueUserId = String.join(",", userId);}
        for (String departmentId : DepartmentId) {
            newValueDepartmentId = String.join(",", departmentId);}
        List<String> linshi = new ArrayList<>();
        List<String> linshi1 = new ArrayList<>();
        linshi.add(newValueUserId);
        updateActivity.setUserId(linshi);
        linshi1.add(newValueDepartmentId);
        updateActivity.setDepartmentId(linshi1);
        updateActivity.setActivityName(activity.getActivityName());
        updateActivity.setActivityId(activity.getActivityId());
        updateActivity.setDescription(activity.getDescription());
        try{
            String a = activity.getEndTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(a, formatter);
            updateActivity.setEndTime(a);
        }catch (Exception ex) {
            // 捕获解析异常，返回错误消息
            return new ResponseResult(200, "时间格式错误，参考格式为:2024-04-23 23:37:08");
        }
        Map<String, Object> ActivityData = new HashMap<>(2);
        //把用户输入字符转为id
        String typeName1 = activityTypeMapper.getActivityData(activity.getTypeName());
        updateActivity.setTypeName(typeName1);
        //先通过获得旧的name
        String oldActivityName = activityMapper.getActivityNameByActivityId(activity.getActivityId());
        activityMapper.updateActivity(updateActivity);
        signinUserMapper.updateSigninUserActivityName(oldActivityName,activity.getActivityName());

        ActivityData.put("新的签到类型:", activity.getTypeName());
        return new ResponseResult(200, "修改成功", ActivityData);
    }

    @Override
    public ResponseResult changeUserSignInStatus0(SignInChange signInChange) {
        UpdateWrapper<SigninUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", signInChange.getId()).eq("activity_name", signInChange.getActivityName()).set("status", 0);

        UpdateWrapper<SigninUser> deleteWrapper = new UpdateWrapper<>();
        deleteWrapper.eq("id", signInChange.getId()).eq("activity_name", signInChange.getActivityName()).set("signin_time", null);

        if (signinUserMapper.selectStatusByIdAndActivityName(signInChange.getId(), signInChange.getActivityName()) == 0) {
            return new ResponseResult(400, "该用户本来就没签到");
        }
            signinUserMapper.update(deleteWrapper);
            signinUserMapper.update(updateWrapper);
            return new ResponseResult(200, "修改成功");
    }

    @Override
    public ResponseResult getStringForQRCode(String activityName) {
        QueryWrapper<ActivityDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_name", activityName);
        ActivityDetail activityDetail = activityDetailMapper.selectOne(queryWrapper);
        if (activityDetail == null) {
            return new ResponseResult(400, "获取失败，当前活动不存在");
        }
        if (activityDetail.getTypeId() == 1) {
            return new ResponseResult(400, "获取失败，当前活动并非二维码签到");
        }
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        if (currentTime.isAfter(activityDetail.getEndTime()) || currentTime.isBefore(activityDetail.getStartTime())) {
            return new ResponseResult(400, "获取失败，当前活动时间未开始或已结束");
        }
        return new ResponseResult(200, "获取成功", RegularUpdatesUtil.getRandomStringByKey(activityName));
    }

    @Override
    public ResponseResult userList(HttpServletRequest request) {
        InfoUser user = requestUtil.getUserInfo(request.getHeader("X-USER-ID"));
        Long id = user.getId();

        List<ActivityList> activities = activityMapper.selectActivityByUserId(id);
        if (activities.isEmpty()) {
            return new ResponseResult(200, "没有签到活动");
        }
        for (ActivityList activity : activities) {
            LocalDateTime endTime = activity.getEndTime();
            LocalDateTime startTime = activity.getStartTime();
            if (LocalDateTime.now(ZoneId.of("Asia/Shanghai")).isBefore(startTime)) {
                activity.setStatus("未开始");
            } else if (LocalDateTime.now(ZoneId.of("Asia/Shanghai")).isAfter(endTime) || LocalDateTime.now(ZoneId.of("Asia/Shanghai")).equals(endTime)) {
                activity.setStatus("已完成");
            } else if (LocalDateTime.now(ZoneId.of("Asia/Shanghai")).isBefore(endTime)) {
                activity.setStatus("正在进行");
            }
            String activityName = activity.getActivityName();
            ActivityList medium = userMapper.countByActivityName(activityName); //拿到参与此活动的签到人数和未签到人数
            if (medium == null) {
                activity.setRate("0%");
                activity.setSigned(0);
                activity.setNotSigned(0);
            } else {
                Integer signed = medium.getSigned();  //已经签到的人数
                Integer notSigned = medium.getNotSigned();  //已未签到的人数

//                计算签到率
                if (signed + notSigned == 0) {
                    activity.setRate("0%");
                } else {
                    BigDecimal signedNum = new BigDecimal(signed);
                    BigDecimal sumNum = new BigDecimal(signed + notSigned);
                    BigDecimal resultBigDecimal = signedNum.divide(sumNum, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                    String rate = String.format("%.1f%%", resultBigDecimal);
                    activity.setRate(rate);
                }
                activity.setSigned(signed);
                activity.setNotSigned(notSigned);
            }

        }
        activities.sort((o1, o2) -> {
            if (o1.getStatus().equals("未开始") && o2.getStatus().equals("未开始")) {
                return 0; // 保持原有顺序
            } else if (o1.getStatus().equals("未开始")) {
                return -1; // o1 在 o2 之前
            } else if (o2.getStatus().equals("未开始")) {
                return 1; // o2 在 o1 之前
            } else if (o1.getStatus().equals("正在进行") && o2.getStatus().equals("已完成")) {
                return -1; // o1 在 o2 之前
            } else if (o1.getStatus().equals("已完成") && o2.getStatus().equals("正在进行")) {
                return 1; // o2 在 o1 之前
            } else {
                return 0; // 保持原有顺序
            }
        });
        return new ResponseResult(200, "获取成功", activities);
    }

    @Override
    public ResponseResult getSignInStatus(String activityName, HttpServletRequest request) {
        if (activityMapper.selectByActivityName(activityName) == 0) {
            return new ResponseResult(400, "活动不存在");
        }
        InfoUser user = requestUtil.getUserInfo(request.getHeader("X-USER-ID"));
        Integer signInUserStatus = signinUserMapper.selectStatusByIdAndActivityName(user.getId(), activityName);
        if (signInUserStatus != null) {
            return new ResponseResult(200, "获取成功", signInUserStatus);
        } else {
            return new ResponseResult(404, "未找到签到信息", null);
        }
    }

    @Override
    public ResponseResult statistics(HttpServletResponse response) throws IOException {
        List<ExcelDTO> excelDTOS = new ArrayList<>();
        //获取总签到人员记录
        List<SigninUser> signinUserList = signinUserMapper.selectAll();

        //计算总签到率
        long checkedIn = signinUserList.stream().filter(signinUser -> signinUser.getStatus() == 1).count();
        double CheckInRate =  (double) checkedIn / signinUserList.size();
        CheckInRate = roundToTwoDecimal(CheckInRate);

        //各部门签到率
        long Informatization = signinUserList.stream().filter(signinUser -> signinUser.getDepartmentId() == 1).count();
        long InfCheckedIn = signinUserList.stream().filter(signinUser -> signinUser.getDepartmentId() == 1 && signinUser.getStatus() == 1).count();

        long site = signinUserList.stream().filter(signinUser -> signinUser.getDepartmentId() == 2).count();
        long siteCheckedIn = signinUserList.stream().filter(signinUser -> signinUser.getDepartmentId() == 2 && signinUser.getStatus() == 1).count();

        long Internet = signinUserList.stream().filter(signinUser -> signinUser.getDepartmentId() == 3).count();
        long InternetCheckedIn = signinUserList.stream().filter(signinUser -> signinUser.getDepartmentId() == 3 && signinUser.getStatus() == 1).count();

        long administrative = signinUserList.stream().filter(signinUser -> signinUser.getDepartmentId() == 4).count();
        long administrativeCheckedIn = signinUserList.stream().filter(signinUser -> signinUser.getDepartmentId() == 4 && signinUser.getStatus() == 1).count();

        double InformatizationCheckInRate = (double) InfCheckedIn / Informatization;
        InformatizationCheckInRate = roundToTwoDecimal(InformatizationCheckInRate);

        double siteCheckInRate = (double) siteCheckedIn / site;
        siteCheckInRate = roundToTwoDecimal(siteCheckInRate);

        double InternetCheckInRate = (double) InternetCheckedIn / Internet;
        InternetCheckInRate = roundToTwoDecimal(InternetCheckInRate);

        double administrativeCheckInRate = (double) administrativeCheckedIn / administrative;
        administrativeCheckInRate = roundToTwoDecimal(administrativeCheckInRate);

        excelDTOS.add(new ExcelDTO("ALL", "总签到率", CheckInRate));
        excelDTOS.add(new ExcelDTO("1", "信息化运维部", InformatizationCheckInRate));
        excelDTOS.add(new ExcelDTO("2", "网站运维部", siteCheckInRate));
        excelDTOS.add(new ExcelDTO("3", "网络运维部", InternetCheckInRate));
        excelDTOS.add(new ExcelDTO("4", "行政秘书部", administrativeCheckInRate));

        // 新建一个Map集合用于存放不重复的ID和名称对应关系
        Map<Long, String> idNameMap = new HashMap<>();

        // 遍历原始列表，将ID和名称添加到Map集合中（要求ID重复则不添加）
        for (SigninUser signinUser : signinUserList) {
            if (!idNameMap.containsKey(signinUser.getId())) {
                idNameMap.put(signinUser.getId(), signinUser.getUserName());
            }
        }

        for (Map.Entry<Long, String> entry : idNameMap.entrySet()) {
            long user = signinUserList.stream().filter(signinUser -> signinUser.getId().equals(entry.getKey())).count();
            long userRate = signinUserList.stream().filter(signinUser -> signinUser.getId().equals(entry.getKey()) && signinUser.getStatus() == 1).count();
            double userCheckInRate = (double) userRate / user;
            userCheckInRate = roundToTwoDecimal(userCheckInRate);
            excelDTOS.add(new ExcelDTO(entry.getKey().toString(), entry.getValue(), userCheckInRate));
        }

        EasyExcelUtil.easyWrite(excelDTOS, response, ExcelDTO.class);

        return new ResponseResult(200, "获取成功", excelDTOS);
    }

    private double roundToTwoDecimal(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
