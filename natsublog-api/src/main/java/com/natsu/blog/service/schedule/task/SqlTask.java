package com.natsu.blog.service.schedule.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SqlTask implements JobRunner {

    private String param;

    @Override
    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public void run() {
        log.info("SQL任务正在开发中：{}", param);
    }
}
