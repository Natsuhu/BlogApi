package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.pojo.Category;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {

    Category getCategoryById(int id);

    List<Category> getCategoryList();
}
