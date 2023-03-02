package com.natsu.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Tag;
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

/**
 * 博客前台，文章接口
 *
 * @author NatsuKaze
 * */
@RestController
@RequestMapping("articles")
public class ArticleController {

    /**
     * ArticleService
     * */
    @Autowired
    private ArticleService articleService;

    /**
     * CategoryService
     * */
    @Autowired
    private CategoryService categoryService;

    /**
     * TagService
     * */
    @Autowired
    private TagService tagService;

    /**
     * 首页文章列表
     * */
    @VisitorLogger(VisitorBehavior.INDEX)
    @GetMapping
    public Result getHomeArticles(BaseQueryDTO baseQueryDTO) {
        //无需校验参数
        baseQueryDTO.setKeyword(null);
        PageResult<HomeArticle> pageResult = articleService.getHomeArticles(baseQueryDTO);
        return Result.success(pageResult.getTotalPage() , pageResult.getDataList());
    }

    /**
     * 随机文章
     * */
    @GetMapping("/random")
    public Result getRandomArticles(@RequestParam(defaultValue = "5") Integer count) {
        List<RandomArticle> articles = articleService.getRandomArticles(count);
        return Result.success(articles);
    }

    /**
     * 文章数量
     * */
    @GetMapping("/count")
    public Result getPublicArticleCount(){
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getIsPublished , Constants.PUBLISHED);
        Integer articleCount = articleService.count(wrapper);
        return Result.success(articleCount);
    }

    /**
     * 阅读文章
     * */
    @VisitorLogger(VisitorBehavior.ARTICLE)
    @GetMapping("/read")
    public Result getReadArticleById(@RequestParam Integer id){
        if (id == null) {
            return Result.fail("参数错误！");
        }
        ReadArticle article = articleService.getReadArticleById(id);
        if (article == null) {
            return Result.fail("参数错误!");
        }
        return Result.success(article);
    }

    /**
     * 归档
     * */
    @VisitorLogger(VisitorBehavior.ARCHIVE)
    @GetMapping("/archives")
    public Result getArchives(){
        Map map = articleService.getArchives();
        return Result.success(map);
    }

    /**
     * 根据分类获取文章
     * */
    @VisitorLogger(VisitorBehavior.CATEGORY)
    @GetMapping("/category")
    public Result getArticlesByCategoryId(ArticleQueryDTO articleQueryDTO) {
        //参数校验
        if (articleQueryDTO.getCategoryId() == null) {
            return Result.fail("参数错误！");
        }
        //验证分类存在
        if (categoryService.getById(articleQueryDTO.getCategoryId()) == null) {
            return Result.fail("无此分类！");
        }
        //配置参数
        articleQueryDTO.setTagIds(null);
        articleQueryDTO.setKeyword(null);
        articleQueryDTO.setIsPublished(Constants.PUBLISHED);
        PageResult<HomeArticle> articles = articleService.getArticlesByQueryParams(articleQueryDTO);
        return Result.success(articles);
    }

    /**
     * 根据标签获取文章
     * */
    @VisitorLogger(VisitorBehavior.TAG)
    @GetMapping("/tag")
    public Result getArticlesByTagId(ArticleQueryDTO articleQueryDTO) {
        //参数校验
        if (articleQueryDTO.getTagIds() == null || articleQueryDTO.getTagIds().size() == 0) {
            return Result.fail("参数错误！");
        }
        //验证标签存在
        LambdaQueryWrapper<Tag> tagQuery = new LambdaQueryWrapper<>();
        tagQuery.in(Tag::getId , articleQueryDTO.getTagIds());
        List<Tag> tags = tagService.list(tagQuery);
        if(articleQueryDTO.getTagIds().size() != tags.size()) {
            return Result.fail("参数错误！");
        }
        //配置参数
        articleQueryDTO.setCategoryId(null);
        articleQueryDTO.setKeyword(null);
        articleQueryDTO.setIsPublished(Constants.PUBLISHED);
        PageResult<HomeArticle> articles = articleService.getArticlesByQueryParams(articleQueryDTO);
        return Result.success(articles);
    }

}
