package com.natsu.blog.controller;

import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 博客前台，分类接口
 *
 * @author NatsuKaze
 * */
@RestController
@RequestMapping("categories")
public class CategoryController {

    /**
     * CategoryService
     * */
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类菜单
     * */
    @GetMapping
    public Result getCategories(){
        return Result.success(categoryService.list());
    }
}
