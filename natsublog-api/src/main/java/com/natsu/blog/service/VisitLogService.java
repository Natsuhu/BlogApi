package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.entity.VisitLog;
import org.springframework.scheduling.annotation.Async;

public interface VisitLogService extends IService<VisitLog> {

    @Async("taskExecutor")
    void saveVisitLog(VisitLog visitLog);

}
