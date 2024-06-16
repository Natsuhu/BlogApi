package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.entity.OperationLog;

public interface OperationLogService extends IService<OperationLog> {

    void saveOperationLog(OperationLog operationLog);

}
