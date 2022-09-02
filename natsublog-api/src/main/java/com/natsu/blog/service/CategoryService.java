package com.natsu.blog.service;

import com.natsu.blog.pojo.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getCategoryList();

    Category getCategoryById(int id);
}
