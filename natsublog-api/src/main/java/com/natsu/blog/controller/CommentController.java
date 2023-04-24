package com.natsu.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Comment;
import com.natsu.blog.model.entity.SiteSetting;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.service.SiteSettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 博客前台，评论接口
 *
 * @author NatsuKaze
 */
@RestController
@RequestMapping("comments")
@Slf4j
public class CommentController {

    /**
     * CommentService
     */
    @Autowired
    private CommentService commentService;

    /**
     * ArticleService
     */
    @Autowired
    private ArticleService articleService;

    /**
     * SiteSettingService
     */
    @Autowired
    private SiteSettingService siteSettingService;

    /**
     * 获取评论数量
     */
    @GetMapping("/count")
    public Result getCommentCount() {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getIsPublished, Constants.PUBLISHED);
        Integer commentCount = commentService.count(wrapper);
        return Result.success(commentCount);
    }

    /**
     * 获取文章评论
     */
    @GetMapping("/articleComments")
    public Result getArticleComments(CommentQueryDTO commentQueryDTO) {
        //参数校验
        if (commentQueryDTO.getPage() == null || !commentQueryDTO.getPage().equals(Constants.PAGE_READ_ARTICLE)) {
            return Result.fail("页面请求错误！");
        }
        if (commentQueryDTO.getArticleId() == null) {
            return Result.fail("请求参数错误！");
        }
        //检查页面是否可评论
        if (!checkPageIsComment(commentQueryDTO.getPage(), commentQueryDTO.getArticleId())) {
            return Result.fail("文章不存在或禁止评论");
        }
        //配置参数并构建评论树
        commentQueryDTO.setKeyword(null);
        commentQueryDTO.setIsPublished(Constants.PUBLISHED);
        commentQueryDTO.setParentCommentId(Constants.TOP_COMMENT_PARENT_ID);
        Map<String, Object> commentMap = commentService.buildCommentTree(commentQueryDTO);
        return Result.success(commentMap);
    }

    /**
     * 获取页面评论，友情链接页面或关于我页面
     */
    @GetMapping("/pageComments")
    public Result getPageComments(CommentQueryDTO commentQueryDTO) {
        Integer pageType = commentQueryDTO.getPage();
        //参数校验
        if (pageType == null || !pageType.equals(Constants.PAGE_FRIEND) && !pageType.equals(Constants.PAGE_ABOUT)) {
            return Result.fail("页面请求错误！");
        }
        //检查页面是否可评论
        if (!checkPageIsComment(commentQueryDTO.getPage(), commentQueryDTO.getArticleId())) {
            return Result.fail("此页面禁止评论！");
        }
        //配置参数并构建评论树
        commentQueryDTO.setKeyword(null);
        commentQueryDTO.setArticleId(null);
        commentQueryDTO.setIsPublished(Constants.PUBLISHED);
        commentQueryDTO.setParentCommentId(Constants.TOP_COMMENT_PARENT_ID);
        Map<String, Object> commentMap = commentService.buildCommentTree(commentQueryDTO);
        return Result.success(commentMap);
    }

    /**
     * 保存评论
     */
    @PostMapping("/save")
    public Result saveComment(@RequestBody CommentDTO commentDTO) {
        //参数校验
        if (StringUtils.isEmpty(commentDTO.getContent()) || commentDTO.getContent().length() > 250) {
            return Result.fail("评论内容超过长度！");
        }
        List<Integer> pages = new ArrayList<>(3);
        pages.add(Constants.PAGE_READ_ARTICLE);
        pages.add(Constants.PAGE_FRIEND);
        pages.add(Constants.PAGE_ABOUT);
        Integer pageType = commentDTO.getPage();
        if (pageType == null || !pages.contains(pageType)) {
            return Result.fail("页面请求错误！");
        }
        if (pageType.equals(Constants.PAGE_READ_ARTICLE) && commentDTO.getArticleId() == null) {
            return Result.fail("页面请求错误！");
        }
        if (commentDTO.getParentCommentId() == null || commentDTO.getOriginId() == null) {
            return Result.fail("参数错误!");
        }
        //检查页面是否可评论
        if (!checkPageIsComment(pageType, commentDTO.getArticleId())) {
            return Result.fail("请求的页面禁止评论！");
        }
        //开始保存
        try {
            commentService.saveComment(commentDTO);
            return Result.success("评论成功");
        } catch (Exception e) {
            log.error("保存评论失败，{}", e.getMessage());
            return Result.fail("评论失败！" + e.getMessage());
        }
    }

    /**
     * 验证目标页面能否评论
     */
    private Boolean checkPageIsComment(Integer page, Long articleId) {
        switch (page) {
            case 0:
                Article article = articleService.getById(articleId);
                if (article == null) {
                    return false;
                }
                return article.getIsPublished().equals(Constants.PUBLISHED) && article.getIsCommentEnabled().equals(Constants.ALLOW_COMMENT);
            case 1:
                LambdaQueryWrapper<SiteSetting> friendPageQuery = new LambdaQueryWrapper<>();
                friendPageQuery.eq(SiteSetting::getNameEn, "isComment");
                friendPageQuery.eq(SiteSetting::getPage, Constants.PAGE_SETTING_FRIEND);
                SiteSetting friendPageSetting = siteSettingService.getOne(friendPageQuery);
                return !friendPageSetting.getContent().equals("false");
            case 2:
                LambdaQueryWrapper<SiteSetting> aboutPageQuery = new LambdaQueryWrapper<>();
                aboutPageQuery.eq(SiteSetting::getNameEn, "isComment");
                aboutPageQuery.eq(SiteSetting::getPage, Constants.PAGE_SETTING_ABOUT);
                SiteSetting aboutPageSetting = siteSettingService.getOne(aboutPageQuery);
                return !aboutPageSetting.getContent().equals("false");
            default:
                return false;
        }
    }
}
