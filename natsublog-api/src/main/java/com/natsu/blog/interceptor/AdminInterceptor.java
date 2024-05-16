package com.natsu.blog.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    //调用controller方法之前执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果handler为非controller的方法，可以直接放行
        if (!(handler instanceof HandlerMethod)) {
            //handler可能为访问资源的handler
            //springboot访问静态资源默认resources/static
            return true;
        }
        //如果访问被拦截的controller，但token为空，则需要登录
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        //打印一手日志，控制台打印
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}", requestURI);
        log.info("request method:{}", request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end=============================");
        if (StringUtils.isBlank(token)) {
            Result result = Result.fail(1000, "需要登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //解析Token信息
        Claims claims;
        try {
            claims = JwtUtils.checkToken(token);
        } catch (Exception e) {
            Result result = Result.fail("登录超时，请重新登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //获取用户权限
        String role = (String) claims.get("roles");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Admin admin = handlerMethod.getMethodAnnotation(Admin.class);
        if (role.equals("visitor") && admin != null) {
            Result result = Result.fail("访客模式下无法进行此操作");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
