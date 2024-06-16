package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.OperationLogMapper;
import com.natsu.blog.model.entity.OperationLog;
import com.natsu.blog.service.OperationLogService;
import com.natsu.blog.service.async.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Override
    public void saveOperationLog(OperationLog operationLog) {
        asyncTaskService.saveOperationLog(operationLogMapper, operationLog);
    }
}
