package com.natsu.blog.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.OperationLog;
import com.natsu.blog.model.entity.User;
import com.natsu.blog.service.OperationLogService;
import com.natsu.blog.utils.IPUtils;
import com.natsu.blog.utils.JwtUtils;
import com.natsu.blog.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT登录过滤器 处理后台系统登录请求
 *
 * @author NatsuKaze
 * @since 2024-05-08
 */
@Slf4j
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final OperationLogService operationLogService = SpringContextUtils.getBean(OperationLogService.class);

    private final ThreadLocal<User> currentUsername = new ThreadLocal<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (!"POST".equals(request.getMethod())) {
                throw new RuntimeException("请求方法错误");
            }
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            currentUsername.set(user);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            log.error("用户认证失败：{}", e.getMessage());
            response.setContentType("application/json;charset=utf-8");
            Result result = Result.fail(400, "用户认证失败");
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(result));
            out.flush();
            out.close();
            //保存登录日志
            operationLogService.saveOperationLog(handleLog(request, false, "登录失败-用户认证失败"));
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        user.setPassword(null);
        //无论是在User类中，还是数据库表中，都是按照每个用户只有唯一一个权限来写的，所以这里只遍历一遍。
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        GrantedAuthority next = authorities.iterator().next();
        //生成一个带有用户权限的Token
        String token = JwtUtils.createToken(user.getUsername(), next.getAuthority());
        //组装响应体
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);
        Result result = Result.success(map);
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
        //保存登录日志
        operationLogService.saveOperationLog(handleLog(request, true, "登录成功"));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        String msg = exception.getMessage();
        //登录不成功时，会抛出对应的异常
        if (exception instanceof LockedException) {
            msg = "账号被锁定";
        } else if (exception instanceof CredentialsExpiredException) {
            msg = "密码过期";
        } else if (exception instanceof AccountExpiredException) {
            msg = "账号过期";
        } else if (exception instanceof DisabledException) {
            msg = "账号被禁用";
        } else if (exception instanceof BadCredentialsException) {
            msg = "用户名或密码错误";
        }
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(Result.fail(401, msg)));
        out.flush();
        out.close();
        //保存登录日志
        operationLogService.saveOperationLog(handleLog(request, false, "登录失败-" + StrUtil.sub(msg, 0, 50)));
    }

    /**
     * 设置LoginLog对象属性
     *
     * @param request     请求对象
     * @param isSuccess   登录状态
     * @param description 操作描述
     * @return OperationLog
     */
    private OperationLog handleLog(HttpServletRequest request, boolean isSuccess, String description) {
        User user = currentUsername.get();
        currentUsername.remove();
        String ip = IPUtils.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        //填充属性
        OperationLog operationLog = new OperationLog();
        operationLog.setUsername(user.getUsername());
        operationLog.setIp(ip);
        operationLog.setUserAgent(userAgent);
        operationLog.setStatus(!isSuccess ? 0 : 1);
        operationLog.setDescription(description);
        operationLog.setUri(request.getRequestURI());
        operationLog.setMethod(request.getMethod());
        operationLog.setParam(user.getUsername() + "-" + user.getPassword());
        operationLog.setType(OperationTypeEnum.LOGIN.getOperationTypeCode());
        operationLog.setTimes(0);
        return operationLog;
    }
}