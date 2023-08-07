package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentQueryDTO extends BaseQueryDTO {

    private Integer page;

    private Long articleId;

    private Boolean isPublished;

    private Long parentCommentId;

    /**
     * 时间范围，开始时间
     */
    private Date startTime;

    /**
     * 时间范围，结束时间
     */
    private Date endTime;
}

