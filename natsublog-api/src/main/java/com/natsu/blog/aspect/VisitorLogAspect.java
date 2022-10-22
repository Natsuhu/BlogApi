package com.natsu.blog.aspect;

import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.VisitLog;
import com.natsu.blog.model.vo.ReadArticle;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.VisitLogService;
import com.natsu.blog.utils.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class VisitorLogAspect {

    @Autowired
    private VisitLogService visitLogService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    @Pointcut("@annotation(visitorLogger)")
    public void pointcut(VisitorLogger visitorLogger) {
    }

    @Around("pointcut(visitorLogger)")
    public Object around(ProceedingJoinPoint joinPoint, VisitorLogger visitorLogger) throws Throwable {
        /*计算响应时间，毫秒*/
        currentTime.set(System.currentTimeMillis());
        Result result = (Result) joinPoint.proceed();
        int times = (int) (System.currentTimeMillis() - currentTime.get());
        currentTime.remove();

        /*获取request对象,处理VisitLog对象并保存*/
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        VisitLog visitLog = handleLog(joinPoint, visitorLogger, request, result, times);
        visitLogService.saveVisitLog(visitLog);
        return result;
    }

    private VisitLog handleLog(ProceedingJoinPoint joinPoint, VisitorLogger visitorLogger, HttpServletRequest request, Result result, int times) {
        /*获取并处理需要的属性*/
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String ip = IPUtils.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        VisitorBehavior behavior = visitorLogger.value();
        Object requestParams = handleParams(joinPoint);
        String remark = handleBehavior(visitorLogger.value(), requestParams, result);

        /*填充属性*/
        VisitLog visitLog = new VisitLog();
        visitLog.setUri(uri);
        visitLog.setMethod(method);
        visitLog.setIp(ip);
        visitLog.setUserAgent(userAgent);
        if (requestParams != null) {
            visitLog.setParam(requestParams.toString());
        }
        visitLog.setBehavior(visitorLogger.value().getBehavior());
        visitLog.setRemark(remark);
        visitLog.setContent(behavior.getContent());
        visitLog.setTimes(times);
        return visitLog;
    }

    private Object handleParams(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }
        return args[0];
    }

    private String handleBehavior(VisitorBehavior behavior, Object requestParams, Result result) {
        String content = null;
        BaseQueryDTO baseQueryDTO;
        ArticleQueryDTO articleQueryDTO;
        switch (behavior) {
            case INDEX:
                baseQueryDTO = (BaseQueryDTO) requestParams;
                content = "文章第" + baseQueryDTO.getPageNo() + "页";
                break;
            case MOMENT:
                baseQueryDTO = (BaseQueryDTO) requestParams;
                content = "动态第" + baseQueryDTO.getPageNo() + "页";
                break;
            case ARTICLE:
                if (result.getCode() == 200) {
                    ReadArticle article = (ReadArticle) result.getData();
                    String title = article.getTitle();
                    content = "文章标题：" + title;
                }
                break;
            case CATEGORY:
                articleQueryDTO = (ArticleQueryDTO) requestParams;
                content = "分类ID：" + articleQueryDTO.getCategoryId();
                break;
            case TAG:
                articleQueryDTO = (ArticleQueryDTO) requestParams;
                content = "标签ID：" + articleQueryDTO.getTagIds();
                break;
        }
        return content;
    }

}
