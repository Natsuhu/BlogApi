package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Comment;
import com.natsu.blog.model.vo.PageResult;

import java.util.Map;

public interface CommentService extends IService<Comment> {

    PageResult<Comment> getCommentsByQueryParams(CommentQueryDTO commentQueryDTO);

    Map<String , Object> buildCommentTree(CommentQueryDTO commentQueryDTO);

}
