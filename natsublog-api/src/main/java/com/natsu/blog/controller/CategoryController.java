package com.natsu.blog.controller;

import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类菜单
     * */
    @GetMapping
    public Result getCategories(){
        return Result.success(categoryService.list());
    }
}
