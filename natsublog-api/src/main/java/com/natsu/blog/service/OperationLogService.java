package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.OperationLogDTO;
import com.natsu.blog.model.dto.OperationLogQueryDTO;
import com.natsu.blog.model.entity.OperationLog;

public interface OperationLogService extends IService<OperationLog> {

    void saveOperationLog(OperationLog operationLog);

    IPage<OperationLogDTO> getOperationLogTable(OperationLogQueryDTO queryCond);

    void deleteOperationLog(OperationLogDTO operationLogDTO);
}
