package com.natsu.blog.model.dto;

import lombok.Data;

@Data
public class BaseQueryDTO {

    private int pageNo = 1;

    private int pageSize = 10;

    private String keyword;
}
