package com.natsu.blog.service;

import com.natsu.blog.pojo.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {

    Integer getPublicCommentCount();

    List<Comment> getPublicCommentsByPage(int page);

    List<Comment> getPublicCommentsByArticleId(int articleId);

    Map getCommentsVOByPage(int page);

    Map getCommentsVOByArticleId(int articleId);

    String saveComment(Comment comment);
}
