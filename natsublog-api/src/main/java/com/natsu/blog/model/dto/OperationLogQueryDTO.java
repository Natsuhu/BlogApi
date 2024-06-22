package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLogQueryDTO extends BaseQueryDTO {

    /**
     * 操作类型
     */
    private String type;

    /**
     * 状态：成功-失败
     */
    private String status;

}
