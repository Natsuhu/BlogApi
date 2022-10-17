package com.natsu.blog.utils;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.natsu.blog.mapper.ArticleMapper;
import com.natsu.blog.model.entity.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ThreadUtils {

    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper,Article article){
        int viewCount = article.getViews();
        Article articleUpdate = new Article();
        articleUpdate.setViews(viewCount+1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //多设置一个，保证在多线程环境下，线程安全
        updateWrapper.eq(Article::getViews,viewCount);
        articleMapper.update(articleUpdate,updateWrapper);
        try {
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName()+"更新文章阅读数完成"+new Date());
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
