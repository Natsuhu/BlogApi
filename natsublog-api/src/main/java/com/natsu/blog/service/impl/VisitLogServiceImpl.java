package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.VisitLogMapper;
import com.natsu.blog.model.dto.VisitLogDTO;
import com.natsu.blog.model.dto.VisitLogQueryDTO;
import com.natsu.blog.model.entity.VisitLog;
import com.natsu.blog.service.VisitLogService;
import com.natsu.blog.service.async.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog> implements VisitLogService {

    @Autowired
    private VisitLogMapper visitLogMapper;

    @Autowired
    private AsyncTaskService asyncTaskService;

    public void saveVisitLog(VisitLog visitLog) {
        asyncTaskService.saveVisitLog(visitLogMapper, visitLog);
    }

    @Override
    public IPage<VisitLogDTO> getVisitLogTable(VisitLogQueryDTO queryCond) {
        IPage<VisitLogDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        return visitLogMapper.getVisitLogTable(page, queryCond);
    }

    @Override
    public void deleteVisitLog(VisitLogDTO visitLogDTO) {
        visitLogMapper.deleteById(visitLogDTO);
    }


}
