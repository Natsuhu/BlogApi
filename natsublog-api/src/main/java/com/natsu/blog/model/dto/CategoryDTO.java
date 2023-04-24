package com.natsu.blog.model.dto;

import com.natsu.blog.model.entity.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryDTO extends Category {

    /**
     * 文章数量
     */
    private Integer articleCount;

}
