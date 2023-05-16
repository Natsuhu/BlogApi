package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class MomentQueryDTO extends BaseQueryDTO{

    /**
     * 是否公开
     */
    private Boolean isPublished;

    /**
     * 时间范围，开始时间
     */
    private Date startTime;

    /**
     * 时间范围，结束时间
     */
    private Date endTime;

}
