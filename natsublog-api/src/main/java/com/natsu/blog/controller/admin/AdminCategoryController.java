package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.dto.admin.CategoryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Category;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save")
    public Result saveCategory(@RequestBody CategoryDTO categoryDTO) {
        //参数校验
        if (categoryDTO.getId() != null) {
            return Result.fail("请勿携带ID");
        }
        //开始保存
        categoryDTO.setUpdateTime(new Date());
        boolean result = categoryService.save(categoryDTO);
        if (result) {
            return Result.success("新增成功");
        }else {
            return Result.fail("新增失败");
        }
    }

    @PostMapping("/getCategoryTable")
    public Result getCategoryTable(@RequestBody BaseQueryDTO baseQueryDTO) {
        IPage<Category> page = new Page<>(baseQueryDTO.getPageNo(), baseQueryDTO.getPageSize());
        LambdaQueryWrapper<Category> categoryQuery = new LambdaQueryWrapper<>();
        //判断是否携带关键词
        if (!StringUtils.isEmpty(baseQueryDTO.getKeyword())) {
            categoryQuery.like(Category::getName, baseQueryDTO.getKeyword());
        }
        IPage<Category> pageResult = categoryService.page(page, categoryQuery);
        return Result.success(pageResult.getPages(), pageResult.getTotal(), pageResult.getRecords());
    }

    @PostMapping("/delete")
    public Result deleteCategory(@RequestBody CategoryDTO categoryDTO) {
        //参数校验
        if (categoryDTO.getId() == null) {
            return Result.fail("删除分类失败，ID必填");
        }
        //验证该分类下有文章
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(Article::getCategoryId, categoryDTO.getId());
        Article article = articleService.getOne(articleQuery);
        if (!ObjectUtils.isEmpty(article)) {
            return Result.fail("删除分类失败，请先删除该分类下的文章");
        }
        //开始删除分类
        boolean result = categoryService.removeById(categoryDTO.getId());
        if (result) {
            return Result.success("删除分类成功");
        }
        return Result.fail("删除分类失败");
    }

    @PostMapping("/update")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        //参数校验
        if (categoryDTO.getId() == null) {
            return Result.fail("更新失败，ID必填");
        }
        //验证分类存在
        Category category = categoryService.getById(categoryDTO.getId());
        if (ObjectUtils.isEmpty(category)) {
            return Result.fail("更新失败，要更新的分类不存在");
        }
        //开始更新
        category.setUpdateTime(new Date());
        category.setName(categoryDTO.getName());
        boolean result = categoryService.updateById(category);
        if (result) {
            return Result.success("更新分类成功");
        }
        return Result.fail("更新分类失败");
    }

}
