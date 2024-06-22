package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.OperationLogMapper;
import com.natsu.blog.model.dto.OperationLogDTO;
import com.natsu.blog.model.dto.OperationLogQueryDTO;
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

    @Override
    public IPage<OperationLogDTO> getOperationLogTable(OperationLogQueryDTO queryCond) {
        IPage<OperationLogDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        return operationLogMapper.getOperationLogTable(page, queryCond);
    }

    @Override
    public void deleteOperationLog(OperationLogDTO operationLogDTO) {
        operationLogMapper.deleteById(operationLogDTO);
    }
}
