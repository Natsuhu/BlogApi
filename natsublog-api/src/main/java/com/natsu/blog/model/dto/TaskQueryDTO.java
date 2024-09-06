package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskQueryDTO extends BaseQueryDTO {

    private String type;

    private String status;

}
