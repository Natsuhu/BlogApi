package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 评论管理控制层
 *
 * @author NatsuKaze
 * @since 2025/01/20
 */
@RestController
@RequestMapping("/admin/comment")
@Slf4j
public class AdminCommentController {

    @Autowired
    private CommentService commentService;

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "评论管理")
    @PostMapping("/getCommentTable")
    public Result getCommentTable(@RequestBody CommentQueryDTO commentQueryDTO) {
        try {
            IPage<CommentDTO> result = commentService.getCommentTable(commentQueryDTO);
            return Result.success(result.getPages(), result.getTotal(), result.getRecords());
        } catch (Exception e) {
            log.error("获取评论表格失败，{}", e.getMessage());
            return Result.fail("获取评论表格G了：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "评论")
    @PostMapping("/updateComment")
    public Result updateComment(@RequestBody CommentDTO commentDTO) {
        //参数校验
        if (commentDTO.getId() == null) {
            return Result.fail("更新失败，ID必填");
        }
        //开始更新
        try {
            commentService.updateComment(commentDTO);
            return Result.success("更新评论成功");
        } catch (Exception e) {
            log.error("更新评论失败：{}", e.getMessage());
            return Result.fail("更新评论失败，" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.DELETE, description = "评论")
    @PostMapping("/deleteComment")
    public Result deleteComment(@RequestBody CommentDTO commentDTO) {
        //参数校验
        if (commentDTO.getId() == null) {
            return Result.fail("删除失败，ID必填");
        }
        //开始删除
        try {
            Integer count = commentService.deleteComment(commentDTO);
            return Result.success(count);
        } catch (Exception e) {
            log.error("删除评论失败：{}", e.getMessage());
            return Result.fail("删除评论失败，" + e.getMessage());
        }
    }

    @PostMapping("/getArticleSelector")
    public Result getArticleSelector() {
        try {
            List<Article> articleSelector = commentService.getArticleSelector();
            return Result.success(articleSelector);
        } catch (Exception e){
            log.error("获取文章下拉框失败：{}", e.getMessage());
            return Result.fail("获取文章下拉框失败，" + e.getMessage());
        }
    }

}
