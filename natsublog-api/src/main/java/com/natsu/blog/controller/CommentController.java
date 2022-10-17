package com.natsu.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        wrapper.eq(Comment::getIsPublished , true);
        Integer commentCount = commentService.count(wrapper);
        return Result.success(commentCount);
    }

    @GetMapping("/articleComments")
    public Result getArticleComments(CommentQueryDTO commentQueryDTO) {
        commentQueryDTO.setPage(0);
        Article article = articleService.getById(commentQueryDTO.getArticleId());
        if (article == null || !article.getIsPublished()) {
            return Result.fail(404,"没有此文章");
        }
        Map<String , Object> commentMap = commentService.buildCommentTree(commentQueryDTO);
        return Result.success(commentMap);
    }

    @GetMapping("/pageComments")
    public Result getPageComments(CommentQueryDTO commentQueryDTO) {
        if (commentQueryDTO.getPage() == 0) {
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
        if(!qqInfoUtils.isQQNumber(qq)){
            return Result.fail(500,"QQ号格式错误");
        }
        comment.setAvatar(qqInfoUtils.getQQAvatarUrl(qq));
        comment.setNickname(qqInfoUtils.getQQNickname(qq));
        comment.setCreateTime(new Date());
        comment.setIsPublished(true);
        commentService.save(comment);
        return Result.success("ok");
    }

}
