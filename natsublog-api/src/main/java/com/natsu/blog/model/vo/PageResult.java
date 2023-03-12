package com.natsu.blog.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 总页数
     * */
    private Long totalPage;

    /**
     * 数据总量
     * */
    private Long total;

    /**
     * List数据
     * */
    private List<T> dataList;

}
