package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.CategoryMapper;
import com.natsu.blog.pojo.Category;
import com.natsu.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper , Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> getCategoryList(){
        return categoryMapper.getCategoryList();
    }

    public Category getCategoryById(int id) {
        return categoryMapper.getCategoryById(id);
    }

}