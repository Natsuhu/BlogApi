package com.natsu.blog.model.dto;

import com.natsu.blog.model.entity.Moment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MomentDTO extends Moment {

    /**
     * 评论数量
     */
    private Integer commentCount;

}
