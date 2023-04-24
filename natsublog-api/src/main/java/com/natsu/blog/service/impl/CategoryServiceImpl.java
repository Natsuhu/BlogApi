package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.CategoryMapper;
import com.natsu.blog.model.dto.CategoryDTO;
import com.natsu.blog.model.dto.CategoryQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.Category;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CategoryService;
import com.natsu.blog.utils.SpringContextUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        //校验名称重复
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, categoryDTO.getName().trim());
        if (!ObjectUtils.isEmpty(categoryMapper.selectOne(queryWrapper))) {
            throw new RuntimeException("此分类已存在");
        }
        //组装实体
        categoryDTO.setCreateTime(new Date());
        categoryDTO.setUpdateTime(new Date());
        //开始保存
        categoryMapper.insert(categoryDTO);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        //校验名称重复
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Category::getId, categoryDTO.getId());
        queryWrapper.eq(Category::getName, categoryDTO.getName().trim());
        if (!ObjectUtils.isEmpty(categoryMapper.selectOne(queryWrapper))) {
            throw new RuntimeException("此分类已存在");
        }
        //组装实体
        categoryDTO.setUpdateTime(new Date());
        //开始更新
        categoryMapper.updateById(categoryDTO);
    }

    @Override
    public void deleteCategory(CategoryDTO categoryDTO) {
        //验证该分类下有文章
        ArticleService articleService = SpringContextUtils.getBean(ArticleService.class);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCategoryId, categoryDTO.getId());
        if (!ObjectUtils.isEmpty(articleService.getOne(queryWrapper))) {
            throw new RuntimeException("该分类下存在文章");
        }
        //删除分类
        categoryMapper.deleteById(categoryDTO);
    }

    @Override
    public IPage<CategoryDTO> getCategoryTable(CategoryQueryDTO queryCond) {
        IPage<CategoryDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        return categoryMapper.getCategoryTable(page, queryCond);
    }

}
