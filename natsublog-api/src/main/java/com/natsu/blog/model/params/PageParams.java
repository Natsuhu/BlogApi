package com.natsu.blog.model.params;

import lombok.Data;

@Data
public class PageParams {

    private int page = 1;

    private int pageSize = 5;
}
