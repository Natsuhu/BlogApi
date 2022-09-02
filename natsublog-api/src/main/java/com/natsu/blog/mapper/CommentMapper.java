package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.pojo.Comment;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    Integer getPublicCommentCount();

    List<Comment> getPublicCommentsByPage(int page);

    List<Comment> getPublicCommentsByArticleId(int articleId);
}
