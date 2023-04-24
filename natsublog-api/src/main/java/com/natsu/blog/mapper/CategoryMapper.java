package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.CategoryDTO;
import com.natsu.blog.model.dto.CategoryQueryDTO;
import com.natsu.blog.model.entity.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    IPage<CategoryDTO> getCategoryTable(IPage<CategoryDTO> page, @Param("queryCond") CategoryQueryDTO queryCond);

}
