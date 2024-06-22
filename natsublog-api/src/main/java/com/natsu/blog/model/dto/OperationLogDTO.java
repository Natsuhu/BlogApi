package com.natsu.blog.model.dto;

import com.natsu.blog.model.entity.OperationLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLogDTO extends OperationLog {

    /**
     * 操作类型中文
     */
    private String typeString;

    /**
     * 操作状态中文
     */
    private String statusString;

}
