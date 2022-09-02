package com.natsu.blog.controller;

import com.natsu.blog.model.params.PageParams;
import com.natsu.blog.model.vo.*;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CategoryService;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

//JSON数据交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    /*首页文章列表*/
    @GetMapping
    public Result getHomeArticleList(PageParams params) {
        PageResult<HomeArticleList> pageResult = articleService.getHomeArticleList(params);
        System.out.println(new Date()+"访问网站！");
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
        Integer articleCount = articleService.getPublicArticleCount();
        return Result.success(articleCount);
    }

    /*阅读文章*/
    @GetMapping("/read")
    public Result getArticleReadVOById(@RequestParam int id){
        ArticleReadVO article = articleService.getArticleReadVOById(id);
        if (article == null) {
            return Result.fail(404,"没有这篇文章！");
        }
        return Result.success(article);
    }

    /*归档*/
    @GetMapping("/archives")
    public Result getArchives(){
        Map map = articleService.getArchives();
        return Result.success(map);
    }

    /*根据分类获取文章*/
    @GetMapping("/category")
    public Result getArticleByCategoryId(PageParams params , int categoryId) {
        if (categoryService.getCategoryById(categoryId) == null) {
            return Result.fail(404,"没有这个分类");
        }
        PageResult<HomeArticleList> articles = articleService.getArticlesByCategoryId(params,categoryId);
        return Result.success(articles);
    }

    /*根据标签获取文章*/
    @GetMapping("/tag")
    public Result getArticlesByTagId(PageParams params , int tagId) {
        if(tagService.getTagById(tagId) == null) {
            return Result.fail(404,"此标签不存在");
        }
        PageResult<HomeArticleList> articles = articleService.getArticlesByTagId(params,tagId);
        return Result.success(articles);
    }

    /*获取文章评论*/
    @GetMapping("/read/comments")
    public Result getArticleComments(int articleId) {
        if(articleService.getArticleById(articleId) == null) {
            return Result.fail(404,"此文章不存在");
        }
        Map comments = commentService.getCommentsVOByArticleId(articleId);
        return Result.success(comments);
    }
}
