package com.natsu.blog.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PageResult<T> {

    //总页数
    private Long totalPage;
    //数据列表
    private List<T> dataList;

    public PageResult(long totalPage , List<T> dataList){
        this.totalPage = totalPage;
        this.dataList = dataList;
    }
}
