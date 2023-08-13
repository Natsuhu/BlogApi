package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.CommentDTO;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<Comment> getRootComments(IPage<Comment> page, @Param("queryCond") CommentQueryDTO queryCond);

    IPage<CommentDTO> getCommentTable(IPage<CommentDTO> page, @Param("queryCond") CommentQueryDTO queryCond);

    List<Article> getArticleSelector();

}
