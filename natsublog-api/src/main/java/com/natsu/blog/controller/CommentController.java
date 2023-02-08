package com.natsu.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Comment;
import com.natsu.blog.model.entity.SiteSetting;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.service.SiteSettingService;
import com.natsu.blog.utils.QQInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 博客前台，评论接口
 *
 * @author NatsuKaze
 * */
@RestController
@RequestMapping("comments")
public class CommentController {

    /**
     * CommentService
     * */
    @Autowired
    private CommentService commentService;

    /**
     * ArticleService
     * */
    @Autowired
    private ArticleService articleService;

    /**
     * SiteSettingService
     * */
    @Autowired
    private SiteSettingService siteSettingService;

    /**
     * QQInfoUtils
     * */
    @Autowired
    private QQInfoUtils qqInfoUtils;

    /**
     * 获取评论数量
     * */
    @GetMapping("/count")
    public Result getCommentCount() {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getIsPublished , Constants.PUBLISHED);
        Integer commentCount = commentService.count(wrapper);
        return Result.success(commentCount);
    }

    /**
     * 获取文章评论
     * */
    @GetMapping("/articleComments")
    public Result getArticleComments(CommentQueryDTO commentQueryDTO) {
        commentQueryDTO.setPage(Constants.PAGE_READ_ARTICLE);
        Article article = articleService.getById(commentQueryDTO.getArticleId());
        if (article == null || !article.getIsPublished()) {
            return Result.fail(404,"没有此文章");
        }
        Map<String , Object> commentMap = commentService.buildCommentTree(commentQueryDTO);
        return Result.success(commentMap);
    }

    /**
     * 获取页面评论，友情链接页面或关于我页面
     * */
    @GetMapping("/pageComments")
    public Result getPageComments(CommentQueryDTO commentQueryDTO) {
        commentQueryDTO.setArticleId(null);
        //请求页面校验
        if (commentQueryDTO.getPage().equals(Constants.PAGE_READ_ARTICLE)) {
            return Result.fail(500,"页面请求错误");
        }
        //查询页面是否可评论
        Integer pageType = commentQueryDTO.getPage();
        LambdaQueryWrapper<SiteSetting> wrapper = new LambdaQueryWrapper<>();
        if (pageType.equals(Constants.PAGE_FRIEND)) {
            wrapper.eq(SiteSetting::getPage , Constants.PAGE_SETTING_FRIEND);
        }else if (pageType.equals(Constants.PAGE_ABOUT)){
            wrapper.eq(SiteSetting::getPage , Constants.PAGE_SETTING_ABOUT);
        }else {
            return Result.fail(500 , "页面请求错误");
        }
        wrapper.eq(SiteSetting::getNameEn , "isComment");
        SiteSetting siteSetting = siteSettingService.getOne(wrapper);
        if (siteSetting.getContent().equals("false")) {
            return Result.fail(500,"此页面暂时不能评论");
        }
        //构建评论树
        Map<String , Object> commentMap = commentService.buildCommentTree(commentQueryDTO);
        return Result.success(commentMap);
    }

    /**
     * 保存评论
     * */
    // TODO 保存评论需要大改
    @PostMapping("/save")
    public Result saveComment(@RequestBody Comment comment) throws Exception {
        String qq = comment.getQq();
        if (comment.getId() != null) {
            return Result.fail(500,"评论失败");
        }
        if (comment.getPage() < 0 || comment.getPage() > 2){
            return Result.fail(500,"评论失败");
        }
        if (comment.getPage() != 0 && comment.getArticleId() != null) {
            return Result.fail(500,"评论失败");
        }
        if (comment.getContent().equals("") || comment.getContent() == null) {
            return Result.fail(500,"评论失败");
        }
        if (comment.getParentCommentId() == -1 && comment.getReplyNickname() != null) {
            return Result.fail(500,"评论失败");
        } else if (comment.getParentCommentId() != -1 && comment.getReplyNickname() == null) {
            return Result.fail(500,"评论失败");
        }
        if (qq == null || qq.equals("")) {
            return Result.fail(500,"评论失败");
        }
        if(!qqInfoUtils.isQQNumber(qq)){
            return Result.fail(500,"QQ号格式错误");
        }
        if (comment.getPage() == 0 && comment.getArticleId() != null) {
            Article article = articleService.getById(comment.getArticleId());
            if (article == null || !article.getIsPublished()) {
                return Result.fail(500,"评论失败");
            }
        }
        if (comment.getParentCommentId() == -1) {
            String uuid = UUID.randomUUID().toString();
            Integer id = uuid.replace("-","").substring(0,8).hashCode();
            comment.setOriginId(id);
        }
        comment.setAvatar(qqInfoUtils.getQQAvatarUrl(qq));
        comment.setNickname(qqInfoUtils.getQQNickname(qq));
        comment.setCreateTime(new Date());
        comment.setIsPublished(true);
        comment.setIsAdminComment(false);
        commentService.save(comment);
        return Result.success("ok");
    }

}
