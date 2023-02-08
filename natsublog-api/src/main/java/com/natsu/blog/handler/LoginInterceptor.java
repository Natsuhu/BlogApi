package com.natsu.blog.handler;

import com.alibaba.fastjson.JSON;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.model.entity.User;
import com.natsu.blog.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器，登录功能暂时搁置----
 * 注意，在WebMVCConfig中配置具体的拦截的路径
 * */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //调用controller方法之前执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果handler为非controller的方法，可以直接放行
        if(!(handler instanceof HandlerMethod)){
            //handler可能为访问资源的handler
            //springboot访问静态资源默认resources/static
            return true;
        }
        //如果访问被拦截的controller，但token为空，则需要登录
        String token = request.getHeader("Authorization");

        //打印一手日志，控制台打印
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end=============================");

        if(StringUtils.isBlank(token)) {
            Result result = Result.fail(1000,"未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //如果token非空，先判断redis中判断token是否存在
        String userJSON = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isBlank(userJSON)) {
            Result result = Result.fail(1000,"未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //如果redis中存在token，则通过token从redis中取得用户信息，若用户不存在则需要登录
        User user = JSON.parseObject(userJSON,User.class);
        if (user == null) {
            Result result = Result.fail(1000,"未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //验证用户处在登录状态，将用户信息存在TreadLocal中
        UserThreadLocal.put(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //若不删除用完的用户信息，会有内存泄露的风险
        UserThreadLocal.remove();
    }
}
