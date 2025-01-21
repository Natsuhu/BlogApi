package com.natsu.blog.service.async;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.natsu.blog.mapper.ArticleMapper;
import com.natsu.blog.mapper.OperationLogMapper;
import com.natsu.blog.mapper.VisitLogMapper;
import com.natsu.blog.mapper.VisitorMapper;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Friend;
import com.natsu.blog.model.entity.Moment;
import com.natsu.blog.model.entity.OperationLog;
import com.natsu.blog.model.entity.VisitLog;
import com.natsu.blog.model.entity.Visitor;
import com.natsu.blog.service.FriendService;
import com.natsu.blog.service.MomentService;
import com.natsu.blog.utils.IPUtils;
import com.natsu.blog.utils.UserAgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 异步任务Service
 * <p>
 * 异步任务的调用者和被调用者不能在同一个类中，否则会导致@Async注解失效
 *
 * @author NatsuKaze
 */
@Service
@Async("taskExecutor")
@Slf4j
public class AsyncTaskService {

    @Autowired
    private UserAgentUtils userAgentUtils;

    /**
     * 更新文章阅读数量, 后续改为redis
     */
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCount = article.getViews();
        Article articleUpdate = new Article();
        articleUpdate.setViews(viewCount + 1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        //多设置一个，保证在多线程环境下，线程安全
        updateWrapper.eq(Article::getViews, viewCount);
        try {
            Thread.sleep(2000);
            articleMapper.update(articleUpdate, updateWrapper);
            log.info("更新文章阅读数完成，文章ID：{}", article.getId());
        } catch (InterruptedException e) {
            log.error("更新文章阅读数量失败：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 更新友链点击次数，后续改为redis
     *
     * @param friendService f
     * @param nickname n
     */
    public void updateFriendClickCount(FriendService friendService, String nickname) {
        try {
            LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Friend::getNickname, nickname);
            Friend friend = friendService.getOne(wrapper);
            if (friend != null) {
                friend.setClick(friend.getClick() + 1);
                friendService.updateById(friend);
            }
        } catch (Exception e) {log.error(e.getMessage());}
    }

    /**
     * 点赞动态，后续改为redis
     * @param momentService m
     * @param id id
     */
    public void momentLike(MomentService momentService, String id) {
        Moment moment = momentService.getById(id);
        if (moment != null) {
            moment.setLikes(moment.getLikes() + 1);
            momentService.updateById(moment);
        }
    }

    /**
     * 保存访客
     */
    public void saveVisitor(VisitorMapper visitorMapper, Visitor visitor) {
        HashMap<String, String> userAgent = userAgentUtils.parseOsAndBrowser(visitor.getUserAgent());
        visitor.setBrowser(userAgent.get("browser"));
        visitor.setOs(userAgent.get("os"));
        //设置省与市
        String ipSource = IPUtils.getCityInfo(visitor.getIp());
        if (ipSource.startsWith("中国")) {
            String[] split = ipSource.split("\\|");
            if (split.length == 4) {
                visitor.setProvince(split[1]);
                visitor.setCity(split[2]);
            }
        }
        visitor.setIpSource(ipSource);
        try {
            visitorMapper.insert(visitor);
        } catch (Exception e) {
            log.error("保存访客记录失败：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 保存访问记录
     */
    public void saveVisitLog(VisitLogMapper visitLogMapper, VisitLog visitLog) {
        HashMap<String, String> userAgent = userAgentUtils.parseOsAndBrowser(visitLog.getUserAgent());
        visitLog.setIpSource(IPUtils.getCityInfo(visitLog.getIp()));
        visitLog.setBrowser(userAgent.get("browser"));
        visitLog.setOs(userAgent.get("os"));
        try {
            visitLogMapper.insert(visitLog);
        } catch (Exception e) {
            log.error("保存访客记录失败：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 保存操作记录
     * @param operationLogMapper m
     * @param operationLog o
     */
    public void saveOperationLog(OperationLogMapper operationLogMapper, OperationLog operationLog) {
        HashMap<String, String> userAgent = userAgentUtils.parseOsAndBrowser(operationLog.getUserAgent());
        operationLog.setIpSource(IPUtils.getCityInfo(operationLog.getIp()));
        operationLog.setBrowser(userAgent.get("browser"));
        operationLog.setOs(userAgent.get("os"));
        try {
            operationLogMapper.insert(operationLog);
        } catch (Exception e) {
            log.error("保存操作记录失败：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
