package com.natsu.blog.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.natsu.blog.annotation.AccessLimit;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.User;
import com.natsu.blog.service.UserService;
import com.natsu.blog.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserService userService;

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
        Map<String, Object> stringObjectMap = null;
        try {
            stringObjectMap = JwtUtils.checkToken(token);
        } catch (Exception e) {
            Result result = Result.fail("登录超时，请重新登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //获取用户权限
        String username = (String) stringObjectMap.get("username");
        System.out.println(username);
        User user = userService.findUserByUsername(username);
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Admin admin = handlerMethod.getMethodAnnotation(Admin.class);
        if (user.getRole().equals("visitor") && admin != null) {
            Result result = Result.fail("访客模式下无法进行此操作");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }


        //如果redis中存在token，则通过token从redis中取得用户信息，若用户不存在则需要登录
//        User user = JSON.parseObject(userJSON, User.class);
//        if (user == null) {
//            Result result = Result.fail(1000, "未登录");
//            response.setContentType("application/json;charset=utf-8");
//            response.getWriter().print(JSON.toJSONString(result));
//            return false;
//        }
        //验证用户处在登录状态，将用户信息存在TreadLocal中
        //UserThreadLocal.put(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //若不删除用完的用户信息，会有内存泄露的风险
        //UserThreadLocal.remove();
    }
}
