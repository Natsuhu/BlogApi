package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AnnexQueryDTO extends BaseQueryDTO{

    private String suffix;

}
