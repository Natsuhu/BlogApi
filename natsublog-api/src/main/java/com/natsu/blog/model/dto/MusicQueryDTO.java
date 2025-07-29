package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MusicQueryDTO extends BaseQueryDTO {

    /**
     * 歌单ID
     */
    private String catalogId;


}
