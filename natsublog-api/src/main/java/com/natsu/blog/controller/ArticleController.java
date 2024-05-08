package com.natsu.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.ArticleDTO;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 博客前台，文章接口
 *
 * @author NatsuKaze
 */
@RestController
@RequestMapping("articles")
public class ArticleController {

    /**
     * ArticleService
     */
    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     */
    @VisitorLogger(VisitorBehavior.INDEX)
    @GetMapping
    public Result getHomeArticles(ArticleQueryDTO queryCond) {
        //清空筛选
        queryCond.setTagIds(null);
        queryCond.setCategoryId(null);
        queryCond.setKeyword(null);
        //首页文章
        IPage<ArticleDTO> pageResult = articleService.getArticles(queryCond);
        return Result.success(pageResult.getPages(), pageResult.getTotal(), pageResult.getRecords());
    }

    /**
     * 随机文章
     */
    @GetMapping("/random")
    public Result getRandomArticles() {
        List<ArticleDTO> articles = articleService.getRandomArticles(5);
        return Result.success(articles);
    }

    /**
     * 文章数量
     */
    @GetMapping("/count")
    public Result getPublicArticleCount() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getIsPublished, Constants.PUBLISHED);
        Integer articleCount = articleService.count(wrapper);
        return Result.success(articleCount);
    }

    /**
     * 阅读文章
     */
    @VisitorLogger(VisitorBehavior.ARTICLE)
    @GetMapping("/read")
    public Result getReadArticleById(@RequestParam("id") Long articleId) {
        ArticleDTO article = articleService.getReadArticle(articleId);
        return Result.success(article);
    }

    /**
     * 归档
     */
    @VisitorLogger(VisitorBehavior.ARCHIVE)
    @GetMapping("/archives")
    public Result getArchives() {
        Map<?, ?> map = articleService.getArchives();
        return Result.success(map);
    }

    /**
     * 根据分类获取文章
     */
    @VisitorLogger(VisitorBehavior.CATEGORY)
    @GetMapping("/category")
    public Result getArticlesByCategoryId(ArticleQueryDTO queryCond) {
        //参数校验
        if (queryCond.getCategoryId() == null) {
            return Result.fail("分类ID必填");
        }
        //配置参数
        queryCond.setTagIds(null);
        queryCond.setKeyword(null);
        IPage<ArticleDTO> articles = articleService.getArticles(queryCond);
        return Result.success(articles.getPages(), articles.getTotal(), articles.getRecords());
    }

    /**
     * 根据标签获取文章
     */
    @VisitorLogger(VisitorBehavior.TAG)
    @GetMapping("/tag")
    public Result getArticlesByTagId(ArticleQueryDTO queryCond) {
        //参数校验
        if (queryCond.getTagIds() == null || queryCond.getTagIds().size() == 0) {
            return Result.fail("参数错误！");
        }
        //配置参数
        queryCond.setCategoryId(null);
        queryCond.setKeyword(null);
        IPage<ArticleDTO> articles = articleService.getArticles(queryCond);
        return Result.success(articles.getPages(), 0, articles.getRecords());
    }

}
