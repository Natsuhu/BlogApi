package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.entity.VisitLog;

public interface VisitLogService extends IService<VisitLog> {

    void saveVisitLog(VisitLog visitLog);

}
