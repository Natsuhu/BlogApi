package com.natsu.blog.model.dto;

import com.natsu.blog.model.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentDTO extends Comment {

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 城市名称
     */
    private String city;

}
