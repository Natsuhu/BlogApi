package com.natsu.blog.handler;

import com.natsu.blog.model.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * 对所有的controller进行拦截处理，是AOP的实现
 *
 * @author NatsuKaze
 * @since 2023/1/19 新增注释
 */
@ControllerAdvice
@Slf4j
public class AllExceptionHandler {

    /**
     * 监控异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(HttpServletRequest request, Exception e) {
        log.error("Request URL : {}, Exception : {}", request.getRequestURL(), e.getMessage());
        return Result.fail(e.getMessage());
    }
}
