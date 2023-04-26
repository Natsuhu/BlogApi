package com.natsu.blog.model.dto;

import com.natsu.blog.model.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagDTO extends Tag {

    /**
     * 文章数量
     */
    private Integer articleCount;

}
