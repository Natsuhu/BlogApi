package com.natsu.blog.interceptor;

import com.alibaba.fastjson.JSON;
import com.natsu.blog.annotation.AccessLimit;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.utils.IPUtils;
import com.natsu.blog.utils.RedisUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 访问控制拦截器
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
        //无AccessLimit注解直接放行
        if (accessLimit == null) {
            return true;
        }
        int seconds = accessLimit.seconds();
        int maxCount = accessLimit.maxCount();
        String ip = IPUtils.getIpAddress(request);
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String redisKey = ip + ":" + method + ":" + requestURI;
        //从redis获取访问次数，若无这个值，则表示第一次访问
        Integer count = (Integer) RedisUtils.get(redisKey);
        if (count == null) {
            //在规定周期内第一次访问，存入redis
            RedisUtils.set(redisKey, 1);
            RedisUtils.expire(redisKey, seconds);
        } else {
            if (count >= maxCount) {
                //超出访问限制次数
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                Result result = Result.fail(accessLimit.msg());
                out.write(JSON.toJSONString(result));
                out.flush();
                out.close();
                return false;
            } else {
                //没超出访问限制次数
                RedisUtils.incr(redisKey, 1);
            }
        }
        return true;
    }
}
