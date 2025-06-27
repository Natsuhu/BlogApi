package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService extends IService<Comment> {

    Integer getCommentCount(Integer page, Long id);

    void updateComment(CommentDTO commentDTO);

    Integer deleteComment(CommentDTO commentDTO);

    IPage<CommentDTO> getCommentTable(CommentQueryDTO commentQueryDTO);

    void saveComment(CommentDTO commentDTO);

    Map<String, Object> getComments(CommentQueryDTO commentQueryDTO);

    List<Article> getArticleSelector();

}
