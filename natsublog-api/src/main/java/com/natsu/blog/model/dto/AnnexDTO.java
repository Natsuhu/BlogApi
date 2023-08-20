package com.natsu.blog.model.dto;

import com.natsu.blog.model.entity.Annex;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AnnexDTO extends Annex {

    /**
     * 自己所在的上级路径
     */
    private String parentPath;

    private String downloadAddress;

}
