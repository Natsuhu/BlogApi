package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.CategoryDTO;
import com.natsu.blog.model.dto.CategoryQueryDTO;
import com.natsu.blog.model.entity.Category;

public interface CategoryService extends IService<Category> {

    /**
     * 保存分类
     */
    void saveCategory(CategoryDTO categoryDTO);

    /**
     * 更新分类
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 删除分类
     */
    void deleteCategory(CategoryDTO categoryDTO);

    /**
     * 获取分类表格
     */
    IPage<CategoryDTO> getCategoryTable(CategoryQueryDTO queryCond);
}
