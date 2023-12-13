package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentQueryDTO extends BaseQueryDTO {

    private Integer page;

    private Long articleId;

    private Long parentCommentId;

}

