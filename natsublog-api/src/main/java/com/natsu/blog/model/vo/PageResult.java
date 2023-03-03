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
     * List数据
     * */
    private List<T> dataList;

}
