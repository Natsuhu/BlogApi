package com.natsu.blog.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.natsu.blog.annotation.AccessLimit;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.Comment;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
     * 获取评论
     */
    @GetMapping("/getComments")
    public Result getComments(CommentQueryDTO commentQueryDTO) {
        Map<String, Object> commentMap = commentService.getComments(commentQueryDTO);
        return Result.success(commentMap);
    }

    /**
     * 保存评论
     */
    @AccessLimit(seconds = 20, maxCount = 1, msg = "20秒内只能评论一次")
    @PostMapping("/save")
    public Result saveComment(@RequestBody CommentDTO commentDTO, HttpServletRequest req) {
        //参数校验
        if (StrUtil.isBlank(commentDTO.getContent()) || commentDTO.getContent().length() > 250) {
            return Result.fail("评论内容超长！");
        }
        if (StrUtil.isBlank(commentDTO.getQq())) {
            return Result.fail("昵称必填！");
        }
        if (commentDTO.getParentCommentId() == null) {
            return Result.fail("参数错误!");
        }
        //开始保存
        try {
            //设置IP地址
            commentDTO.setIp(IPUtils.getIpAddress(req));
            commentService.saveComment(commentDTO);
            return Result.success("评论成功");
        } catch (Exception e) {
            log.error("保存评论失败，{}", e.getMessage());
            return Result.fail("评论失败！" + e.getMessage());
        }
    }
}
