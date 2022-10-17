package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.CommentQueryDTO;
import com.natsu.blog.model.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<Comment> getCommentsByQueryParams(IPage<Comment> page , @Param("commentQueryDTO") CommentQueryDTO commentQueryDTO);

}
