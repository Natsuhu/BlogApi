package com.natsu.blog.aspect;

import com.alibaba.fastjson.JSON;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.OperationLog;
import com.natsu.blog.service.OperationLogService;
import com.natsu.blog.utils.IPUtils;
import com.natsu.blog.utils.JwtUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * AOP切面配置，记录博客后台操作行为
 *
 * @author NatsuKaze
 * @since 2024/06/16
 */
@Component
@Aspect
public class OperationLogAspect {

    /**
     * operationLogService
     */
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 记录每次请求的响应时间
     */
    private final ThreadLocal<Long> currentTime = new ThreadLocal<>();

    /**
     * 切入点配置
     *
     * @param operationLogger 操作记录注解
     **/
    @Pointcut("@annotation(operationLogger)")
    public void pointcut(OperationLogger operationLogger) {
    }

    /**
     * 环绕
     *
     * @param operationLogger 访问记录注解
     * @param joinPoint 切入点
     * @return Object
     */
    @Around(value = "pointcut(operationLogger)", argNames = "joinPoint,operationLogger")
    public Object around(ProceedingJoinPoint joinPoint, OperationLogger operationLogger) throws Throwable {
        //计算响应时间，毫秒
        currentTime.set(System.currentTimeMillis());
        Result result = (Result) joinPoint.proceed();
        int times = (int) (System.currentTimeMillis() - currentTime.get());
        currentTime.remove();

        //获取request对象,处理OperationLogger对象并保存
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLog operationLog = handleLog(joinPoint, operationLogger, request, result, times);
        operationLogService.saveOperationLog(operationLog);
        return result;
    }

    private OperationLog handleLog(ProceedingJoinPoint joinPoint, OperationLogger operationLogger, HttpServletRequest request, Result result, int times) {
        //通用属性
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String ip = IPUtils.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        //请求参数
        Object requestParams = handleParams(joinPoint);

        //保存实体对象
        OperationLog operationLog = new OperationLog();
        operationLog.setTimes(times);
        operationLog.setUri(uri);
        operationLog.setMethod(method);
        operationLog.setIp(ip);
        operationLog.setUserAgent(userAgent);
        if (requestParams != null) {
            operationLog.setParam(JSON.toJSONString(requestParams));
        }
        operationLog.setDescription(operationLogger.description());
        operationLog.setType(operationLogger.type().getOperationTypeCode());
        operationLog.setStatus(!result.isSuccess() ? 0 : 1);
        operationLog.setUsername(JwtUtils.checkToken(request.getHeader("Authorization")).getSubject());
        return operationLog;
    }

    /**
     * 处理请求参数
     */
    private Object handleParams(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }
        return args[0];
    }

}
