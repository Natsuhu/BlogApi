package com.natsu.blog.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BaseQueryDTO {

    /**
     * 分页-页码
     */
    private Integer pageNo = 1;

    /**
     * 分页-每页大小
     */
    private Integer pageSize = 10;

    /**
     * 关键字查询
     */
    private String keyword;

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
