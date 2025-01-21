package com.natsu.blog.aspect;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.PageEnum;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.ArticleDTO;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.entity.VisitLog;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.Visitor;
import com.natsu.blog.service.VisitLogService;
import com.natsu.blog.service.VisitorService;
import com.natsu.blog.utils.IPUtils;
import com.natsu.blog.utils.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * AOP切面配置
 *
 * @author NatsuKaze
 * @since 2023/1/19 新增注解
 */
@Component
@Aspect
public class VisitorLogAspect {

    /**
     * 注入VisitLogService
     */
    @Autowired
    private VisitLogService visitLogService;

    /**
     * 注入VisitorService
     */
    @Autowired
    private VisitorService visitorService;

    /**
     * 记录每次请求的响应时间
     */
    private final ThreadLocal<Long> currentTime = new ThreadLocal<>();

    /**
     * 切入点配置
     *
     * @param visitorLogger 访问记录注解
     **/
    @Pointcut("@annotation(visitorLogger)")
    public void pointcut(VisitorLogger visitorLogger) {
    }

    /**
     * 环绕
     *
     * @param visitorLogger 访问记录注解
     * @param joinPoint 切入点
     * @return Object
     */
    @Around(value = "pointcut(visitorLogger)", argNames = "joinPoint,visitorLogger")
    public Object around(ProceedingJoinPoint joinPoint, VisitorLogger visitorLogger) throws Throwable {
        //计算响应时间，毫秒
        currentTime.set(System.currentTimeMillis());
        Result result = (Result) joinPoint.proceed();
        int times = (int) (System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //保存访客和访客记录
        String visitorId = getVisitorId(request);
        VisitLog visitLog = handleLog(joinPoint, visitorLogger, request, result, times);
        visitLog.setVisitorId(visitorId);
        visitLogService.saveVisitLog(visitLog);
        //添加访客标识码UUID至响应头
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addHeader("Identification", visitorId);
        response.addHeader("Access-Control-Expose-Headers", "Identification");
        return result;
    }

    private String getVisitorId(HttpServletRequest request) {
        String ip = IPUtils.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        String visitorId = request.getHeader("Identification");
        //判断访客标识是否存在
        if (StrUtil.isNotBlank(visitorId)) {
            //校验Redis中是否存在
            boolean redisHas = RedisUtils.sHasKey(Constants.IDENTIFICATION_SET, visitorId);
            if (redisHas) {
                return visitorId;
            }
            //校验数据库中是否存在
            Visitor visitor = visitorService.getById(visitorId);
            if (visitor != null) {
                RedisUtils.sSet(Constants.IDENTIFICATION_SET, visitorId);
                return visitorId;
            }
        }
        //生成访客标识
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String timestamp = Long.toString(calendar.getTimeInMillis() / 1000);
        String nameUUID = timestamp + ip + userAgent;
        visitorId = UUID.nameUUIDFromBytes(nameUUID.getBytes()).toString().replace("-", "");
        //保存至Redis
        RedisUtils.sSet(Constants.IDENTIFICATION_SET, visitorId);
        //保存至数据库
        if (visitorService.getById(visitorId) == null) {
            Visitor visitor = new Visitor();
            visitor.setId(visitorId);
            visitor.setPv(0L);
            visitor.setIp(ip);
            visitor.setUserAgent(userAgent);
            visitor.setLastTime(new Date());
            visitorService.saveVisitor(visitor);
        }
        return visitorId;
    }

    /**
     * 处理日志，组装参数
     */
    private VisitLog handleLog(ProceedingJoinPoint joinPoint, VisitorLogger visitorLogger, HttpServletRequest request, Result result, int times) {
        //获取并处理需要的属性
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String ip = IPUtils.getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        VisitorBehavior behavior = visitorLogger.value();
        Object requestParams = handleParams(joinPoint);
        String remark = handleBehavior(visitorLogger.value(), requestParams, result);

        //填充属性
        VisitLog visitLog = new VisitLog();
        visitLog.setUri(uri);
        visitLog.setMethod(method);
        visitLog.setIp(ip);
        visitLog.setUserAgent(userAgent);
        if (requestParams != null) {
            visitLog.setParam(JSON.toJSONString(requestParams));
        }
        visitLog.setBehavior(visitorLogger.value().getBehavior());
        visitLog.setRemark(remark);
        visitLog.setContent(behavior.getContent());
        visitLog.setTimes(times);
        return visitLog;
    }

    /**
     * 处理参数
     */
    private Object handleParams(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }
        return args[0];
    }

    /**
     * 翻译访客行为
     */
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
                    ArticleDTO article = (ArticleDTO) result.getData();
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
            case CLICK_FRIEND:
                String nickname = (String) requestParams;
                content = "朋友昵称：" + nickname;
                break;
            case LIKE_MOMENT:
                String id = (String) requestParams;
                content = "动态ID：" + requestParams;
                break;
            case COMMENT:
                CommentDTO commentDTO = (CommentDTO) requestParams;
                Integer page = commentDTO.getPage();
                if (page.equals(PageEnum.ARTICLE.getPageCode())) {
                    content = "文章ID：" + commentDTO.getArticleId();
                }
                if (page.equals(PageEnum.FRIEND.getPageCode())) {
                    content = "位置：友情链接";
                }
                if (page.equals(PageEnum.ABOUT.getPageCode())) {
                    content = "位置：关于我";
                }
                break;
            default:
                content = VisitorBehavior.UNKNOWN.getContent();
        }
        return content;
    }

}
