package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.VisitLogMapper;
import com.natsu.blog.model.entity.VisitLog;
import com.natsu.blog.service.VisitLogService;
import com.natsu.blog.utils.IPUtils;
import com.natsu.blog.utils.UserAgentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper , VisitLog> implements VisitLogService {

    @Autowired
    private VisitLogMapper visitLogMapper;

    @Autowired
    private UserAgentUtils userAgentUtils;

    @Async("taskExecutor")
    public void saveVisitLog(VisitLog visitLog) {
        String city = IPUtils.getCityInfo(visitLog.getIp());
        HashMap<String , String> userAgent = userAgentUtils.parseOsAndBrowser(visitLog.getUserAgent());
        visitLog.setIpSource(city);
        visitLog.setBrowser(userAgent.get("browser"));
        visitLog.setOs(userAgent.get("os"));
        visitLogMapper.insert(visitLog);
    }

}
