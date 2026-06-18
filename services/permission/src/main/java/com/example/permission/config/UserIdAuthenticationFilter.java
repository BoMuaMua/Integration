package com.example.permission.config;

import com.example.permission.entity.CustomUserDetails;
import com.example.permission.entity.User;
import com.example.permission.mapper.UserMapper;
import com.example.permission.service.PermissionsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

/**
 * 从Gateway传递的用户ID Header中提取用户信息并设置到SecurityContext
 */
@Component
@Slf4j
@Order(1) // 确保在其他过滤器之前执行
public class UserIdAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PermissionsService permissionsService;

    // Gateway传递用户ID的Header名称（与网关配置保持一致）
    private static final String USER_ID_HEADER = "X-USER-ID";

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, java.io.IOException {
        
        String userId = request.getHeader(USER_ID_HEADER);
        
        if (userId != null && !userId.isEmpty()) {
            log.debug("从Gateway接收到用户ID: {}", userId);
            try {
                // 从数据库加载用户信息
                User user = userMapper.selectById(Long.parseLong(userId));
                if (user != null) {
                    log.debug("找到用户: id={}, username={}", user.getId(), user.getUsername());
                    
                    // 获取用户权限列表
                    List<String> permissions = permissionsService.getPermissionsByUserId(user.getId());
                    log.debug("用户权限数量: {}", permissions.size());
                    
                    // 创建CustomUserDetails
                    CustomUserDetails userDetails = new CustomUserDetails(user, permissions);
                    
                    // 创建Authentication对象
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    
                    // 设置到SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("用户认证信息已设置到SecurityContext: userId={}, username={}", userId, userDetails.getUsername());
                } else {
                    log.warn("未找到用户: userId={}", userId);
                }
            } catch (NumberFormatException e) {
                log.error("用户ID格式错误: userId={}, error={}", userId, e.getMessage());
            } catch (Exception e) {
                log.error("处理用户认证信息失败: userId={}, error={}", userId, e.getMessage(), e);
            }
        } else {
            log.debug("请求中未包含用户ID Header");
        }
        
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
}
