package com.natsu.blog.service.async;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.natsu.blog.mapper.ArticleMapper;
import com.natsu.blog.model.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步任务Service
 *
 * 异步任务的调用者和被调用者不能在同一个类中，否则会导致@Async注解失效
 * @author NatsuKaze
 * */
@Service
@Async("taskExecutor")
@Slf4j
public class AsyncTaskService {

    /**
     * 更新文章阅读数量
     * */
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
        int viewCount = article.getViews();
        Article articleUpdate = new Article();
        articleUpdate.setViews(viewCount+1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //多设置一个，保证在多线程环境下，线程安全
        updateWrapper.eq(Article::getViews,viewCount);
        articleMapper.update(articleUpdate,updateWrapper);
        try {
            Thread.sleep(2000);
            log.info("更新文章阅读数完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
