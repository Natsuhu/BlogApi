package com.natsu.blog.controller;

import com.natsu.blog.model.vo.Result;
import com.natsu.blog.pojo.Comment;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.utils.QQInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private QQInfoUtils qqInfoUtils;

    @GetMapping("/count")
    public Result getCommentCount() {
        return Result.success(commentService.getPublicCommentCount());
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
        String result = commentService.saveComment(comment);
        return Result.success(result);
    }

}
