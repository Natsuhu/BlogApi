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

@RestController
@RequestMapping("comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SiteSettingService siteSettingService;

    @Autowired
    private QQInfoUtils qqInfoUtils;


    @GetMapping("/count")
    public Result getCommentCount() {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getIsPublished , Constants.PUBLISHED);
        Integer commentCount = commentService.count(wrapper);
        return Result.success(commentCount);
    }

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

    @GetMapping("/pageComments")
    public Result getPageComments(CommentQueryDTO commentQueryDTO) {
        if (commentQueryDTO.getPage() == Constants.PAGE_READ_ARTICLE) {
            return Result.fail(500,"页面请求错误");
        }
        commentQueryDTO.setArticleId(null);
        LambdaQueryWrapper<SiteSetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SiteSetting::getPage , commentQueryDTO.getPage()+2);
        wrapper.eq(SiteSetting::getNameEn , "isComment");
        SiteSetting siteSetting = siteSettingService.getOne(wrapper);
        if (siteSetting.getContent().equals("false")) {
            return Result.fail(500,"此页面暂时不能评论");
        }
        Map<String , Object> commentMap = commentService.buildCommentTree(commentQueryDTO);
        return Result.success(commentMap);
    }

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
