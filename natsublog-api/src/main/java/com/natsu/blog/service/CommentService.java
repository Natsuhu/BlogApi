package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Comment;

import java.util.Map;

public interface CommentService extends IService<Comment> {

    IPage<Comment> getCommentsByQueryParams(CommentQueryDTO commentQueryDTO);

    Map<String , Object> buildCommentTree(CommentQueryDTO commentQueryDTO);

}
