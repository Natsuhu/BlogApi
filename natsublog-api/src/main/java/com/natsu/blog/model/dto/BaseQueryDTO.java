package com.natsu.blog.model.dto;

import lombok.Data;

@Data
public class BaseQueryDTO {

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private String keyword;
}
