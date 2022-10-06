package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.pojo.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> getCategoryList();

    Category getCategoryById(int id);
}
