package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.pojo.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    Integer getPublicCommentCount();

    List<Comment> getPublicCommentsByPage(int page);

    List<Comment> getPublicCommentsByArticleId(int articleId);
}
