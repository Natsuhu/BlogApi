package com.natsu.blog.handler;

import com.natsu.blog.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Result doException(Exception e) {
        log.error("系统异常：{}", e.getMessage());
        return Result.fail(500, e.getMessage());
    }
}
