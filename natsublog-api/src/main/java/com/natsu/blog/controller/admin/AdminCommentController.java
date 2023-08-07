package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/comment")
@Slf4j
public class AdminCommentController {

    @Autowired
    private CommentService commentService;

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

}