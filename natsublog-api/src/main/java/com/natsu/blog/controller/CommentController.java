package com.natsu.blog.controller;

import cn.hutool.core.util.StrUtil;
import com.natsu.blog.annotation.AccessLimit;
import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.utils.IPUtils;
import com.natsu.blog.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping("/getCommentCount")
    public Result getCommentCount(CommentQueryDTO commentQueryDTO) {
        return Result.success(commentService.getCommentCount(commentQueryDTO.getObjectType(), commentQueryDTO.getObjectId()));
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
    @VisitorLogger(VisitorBehavior.COMMENT)
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
        //验证是否博主评论
        String token = req.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            commentDTO.setIsAdminComment(false);
        } else {
            String role = null;
            try {
                 role = (String) JwtUtils.checkToken(token).get("roles");
            } catch (Exception ignore) {}
            commentDTO.setIsAdminComment("admin".equals(role));
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
