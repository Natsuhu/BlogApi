package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class AnnexQueryDTO extends BaseQueryDTO{

    private String suffix;

    private Date startTime;

    private Date endTime;

    private Boolean isPublished;
}
