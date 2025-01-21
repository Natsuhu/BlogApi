package com.natsu.blog.service.schedule.task;

import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.entity.Visitor;
import com.natsu.blog.service.VisitorService;
import com.natsu.blog.utils.RedisUtils;
import com.natsu.blog.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SyncVisitorInfoToDB implements JobRunner {

    @Override
    public void setParam(String param) {}

    @Override
    public void run() {
        log.info("开始执行任务：同步Redis访客信息至数据库");
        //清空Redis访客标识
        RedisUtils.del(Constants.IDENTIFICATION_SET);
        //获取昨天的访客信息并更新
        VisitorService visitorService = SpringContextUtils.getBean(VisitorService.class);
        List<Visitor> visitorList = visitorService.getYesterdayVisitorInfo();
        visitorService.updateBatchById(visitorList);
        log.info("同步Redis访客信息至数据库，执行任务结束");
    }
}
