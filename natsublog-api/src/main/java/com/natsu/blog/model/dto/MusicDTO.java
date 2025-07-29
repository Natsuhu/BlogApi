package com.natsu.blog.model.dto;

import com.natsu.blog.model.entity.Music;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MusicDTO extends Music {

    /**
     * 封面预览地址，用于列表展示
     */
    private String picViewUrl;

}
