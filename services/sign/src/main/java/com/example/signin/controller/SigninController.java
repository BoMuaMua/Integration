package com.example.signin.controller;


import com.example.signin.entity.*;
import com.example.signin.service.SigninService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/signin")
public class SigninController {

    @Autowired
    SigninService signinService;

    /**
     * 创建签到
     * @param SigninMessage 包含签到信息的对象
     * @return 返回操作结果
     */
    @PostMapping("/createSignin")
    public ResponseResult createSignIn(@RequestBody SigninMessage SigninMessage) throws JsonProcessingException {
        return signinService.createSignIn(SigninMessage);
    }

    /**
     * 获取所有签到信息列表
     * @return 返回签到信息列表的结果
     */
    @GetMapping("/signinList")
    public ResponseResult getAll() {
        return signinService.signinList();
    }

    @GetMapping("/signinUserList")
    public ResponseResult getUserList(HttpServletRequest request) {
        return signinService.userList(request);
    }

    /**
     * 停止签到
     * @param activityName 需要停止签到的活动名称
     * @return 返回操作结果
     */
    @PostMapping("/stopSignin")
    public ResponseResult stopSignIn(@RequestBody ActivityName activityName) {
        return signinService.stopSignIn(activityName);
    }

    /**
     * 获取签到详细信息
     * @return 返回签到详细信息的结果
     */
    @GetMapping("/signinDetail")
    public ResponseResult getDetail(@RequestParam String activityName) {
        return signinService.getDetail(activityName);
    }

    /**
     * 用户签到
     * @param userSignIn 包含用户签到信息的对象
     * @return 返回操作结果
     */
    @PostMapping("/signin")
    public ResponseResult SignIn(@RequestBody UserSignIn userSignIn, HttpServletRequest request) throws JsonProcessingException {
        return signinService.SignIn(userSignIn, request);
    }

    /**
     * 更改用户签到状态。
     * 用于更改用户对特定活动的签到状态（如从未签到改为已签到）。
     * @param signInChange 包含需要更改签到状态的用户和活动信息。
     * @return 返回操作结果，成功或失败及原因。
     */
    @PostMapping("/changeUserSignInStatus1")
    public ResponseResult changeUserSignInStatus(@RequestBody SignInChange signInChange) {
        return signinService.changeUserSignInStatus1(signInChange);
    }

    @PostMapping("/changeUserSignInStatus0")
    public ResponseResult changeUserSignInStatus0(@RequestBody SignInChange signInChange) {
        return signinService.changeUserSignInStatus0(signInChange);
    }

    /**
     * 删除签到。
     * 用于更改用户对特定活动的签到状态（如从未签到改为已签到）。
     * @param activityName 即活动名称。
     * @return 返回操作结果，成功或失败及原因。
     */
    @PostMapping("/delAct")
    public ResponseResult delAct(@RequestBody Activity activityName) {
        return signinService.delateActivity(activityName);
    }

    /**
     * 更新签到。
     * 用于更改用户对特定活动的签到状态（如从未签到改为已签到）。
     * @param activity 即Activity类属性。
     * @return 返回操作结果，成功或失败及原因。
     */
    @PostMapping("/uptAct")
    public ResponseResult uptAct(@RequestBody upActivity activity) {
        return signinService.updateActivity(activity);
    }

    @GetMapping("/getStringForQRCode")
    public ResponseResult getStringForQRCode(@RequestParam String activityName) {
        return signinService.getStringForQRCode(activityName);
    }

    @GetMapping("/getSignInStatus")
    public ResponseResult getSignInStatus(@RequestParam String activityName, HttpServletRequest request) {
        return signinService.getSignInStatus(activityName,request);
    }

    @GetMapping("/statistics")
    public ResponseResult statistics(HttpServletResponse response) throws IOException {
        return signinService.statistics(response);
    }
}
