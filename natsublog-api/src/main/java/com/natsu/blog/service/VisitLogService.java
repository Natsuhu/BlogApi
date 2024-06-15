package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.VisitLogDTO;
import com.natsu.blog.model.dto.VisitLogQueryDTO;
import com.natsu.blog.model.entity.VisitLog;

public interface VisitLogService extends IService<VisitLog> {

    void saveVisitLog(VisitLog visitLog);

    IPage<VisitLogDTO> getVisitLogTable(VisitLogQueryDTO queryCond);

    void deleteVisitLog(VisitLogDTO visitLogDTO);

}
