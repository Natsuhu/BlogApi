package com.natsu.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.vo.*;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CategoryService;
import com.natsu.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

//JSON数据交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    /*首页文章列表*/
    @VisitorLogger(VisitorBehavior.INDEX)
    @GetMapping
    public Result getHomeArticles(BaseQueryDTO baseQueryDTO) {
        baseQueryDTO.setKeyword(null);
        PageResult<HomeArticles> pageResult = articleService.getHomeArticles(baseQueryDTO);
        return Result.success(pageResult);
    }

    /*随机文章*/
    @GetMapping("/random")
    public Result getRandomArticles(@RequestParam(defaultValue = "5") int count) {
        List<RandomArticles> articles = articleService.getRandomArticles(count);
        return Result.success(articles);
    }

    /*文章数量*/
    @GetMapping("/count")
    public Result getPublicArticleCount(){
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getIsPublished , 1);
        Integer articleCount = articleService.count(wrapper);
        return Result.success(articleCount);
    }

    /*阅读文章*/
    @VisitorLogger(VisitorBehavior.ARTICLE)
    @GetMapping("/read")
    public Result getReadArticleById(@RequestParam int id){
        if (id < 0) {
            Result.fail(404,"没有这篇文章");
        }
        ReadArticle article = articleService.getReadArticleById(id);
        if (article == null) {
            return Result.fail(404,"没有这篇文章！");
        }
        return Result.success(article);
    }

    /*归档*/
    @VisitorLogger(VisitorBehavior.ARCHIVE)
    @GetMapping("/archives")
    public Result getArchives(){
        Map map = articleService.getArchives();
        return Result.success(map);
    }

    /*根据分类获取文章*/
    @VisitorLogger(VisitorBehavior.CATEGORY)
    @GetMapping("/category")
    public Result getArticlesByCategoryId(ArticleQueryDTO articleQueryDTO) {
        articleQueryDTO.setTagIds(null);
        articleQueryDTO.setKeyword(null);
        articleQueryDTO.setIsPublished(true);
        if (categoryService.getById(articleQueryDTO.getCategoryId()) == null) {
            return Result.fail(404,"没有这个分类");
        }
        PageResult<HomeArticles> articles = articleService.getArticlesByQueryParams(articleQueryDTO);
        return Result.success(articles);
    }

    /*根据标签获取文章*/
    @VisitorLogger(VisitorBehavior.TAG)
    @GetMapping("/tag")
    public Result getArticlesByTagId(ArticleQueryDTO articleQueryDTO) {
        articleQueryDTO.setCategoryId(null);
        articleQueryDTO.setKeyword(null);
        articleQueryDTO.setIsPublished(true);
        if(tagService.getById(articleQueryDTO.getTagIds().get(0)) == null) {
            return Result.fail(404,"没有这个标签");
        }
        PageResult<HomeArticles> articles = articleService.getArticlesByQueryParams(articleQueryDTO);
        return Result.success(articles);
    }

}
