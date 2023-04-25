package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.CategoryDTO;
import com.natsu.blog.model.dto.CategoryQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/saveCategory")
    public Result saveCategory(@RequestBody CategoryDTO categoryDTO) {
        //参数校验
        if (categoryDTO.getId() != null) {
            return Result.fail("请勿携带ID");
        }
        if (StringUtils.isBlank(categoryDTO.getName())) {
            return Result.fail("分类名称必填");
        }
        //开始保存
        try {
            categoryService.saveCategory(categoryDTO);
            return Result.success("新增分类成功");
        } catch (Exception e) {
            log.error("新增分类失败：{}", e.getMessage());
            return Result.fail("新增分类失败，" + e.getMessage());
        }
    }

    @PostMapping("/deleteCategory")
    public Result deleteCategory(@RequestBody CategoryDTO categoryDTO) {
        //参数校验
        if (categoryDTO.getId() == null) {
            return Result.fail("删除分类失败，ID必填");
        }
        //开始删除分类
        try {
            categoryService.deleteCategory(categoryDTO);
            return Result.success("删除分类成功");
        } catch (Exception e) {
            log.error("删除分类失败：{}", e.getMessage());
            return Result.fail("删除分类失败，" + e.getMessage());
        }
    }

    @PostMapping("/updateCategory")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        //参数校验
        if (categoryDTO.getId() == null) {
            return Result.fail("更新失败，ID必填");
        }
        //开始更新
        try {
            categoryService.updateCategory(categoryDTO);
            return Result.success("更新分类成功");
        } catch (Exception e) {
            log.error("更新分类失败：{}", e.getMessage());
            return Result.fail("更新分类失败，" + e.getMessage());
        }
    }

    @PostMapping("/getCategoryTable")
    public Result getCategoryTable(@RequestBody CategoryQueryDTO queryDTO) {
        try {
            IPage<CategoryDTO> pageResult = categoryService.getCategoryTable(queryDTO);
            return Result.success(pageResult.getPages(), pageResult.getTotal(), pageResult.getRecords());
        } catch (Exception e) {
            log.error("获取分类表格失败，{}", e.getMessage());
            return Result.fail("获取分类表格G了" + e.getMessage());
        }
    }

}
