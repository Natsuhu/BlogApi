package com.natsu.blog.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.OperationLog;
import com.natsu.blog.service.OperationLogService;
import com.natsu.blog.utils.IPUtils;
import com.natsu.blog.utils.JwtUtils;
import com.natsu.blog.utils.SpringContextUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(10)
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    private OperationLogService operationLogService;

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
            //记录失败日志
            OperationLogger operationLogger = handlerMethod.getMethodAnnotation(OperationLogger.class);
            OperationLog operationLog = new OperationLog();
            operationLog.setUsername(claims.getSubject());
            operationLog.setType(operationLogger.type().getOperationTypeCode());
            operationLog.setDescription(operationLogger.description());
            operationLogService.saveOperationLog(handleLog(request, operationLog));
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 设置LoginLog对象属性
     *
     * @param request      请求对象
     * @param operationLog 操作日志对象
     * @return OperationLog
     */
    private OperationLog handleLog(HttpServletRequest request, OperationLog operationLog) {
        String ip = IPUtils.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        //填充属性
        operationLog.setIp(ip);
        operationLog.setUserAgent(userAgent);
        operationLog.setUri(request.getRequestURI());
        operationLog.setMethod(request.getMethod());
        operationLog.setTimes(0);
        operationLog.setStatus(Constants.COM_NUM_ZERO);
        return operationLog;
    }
}
