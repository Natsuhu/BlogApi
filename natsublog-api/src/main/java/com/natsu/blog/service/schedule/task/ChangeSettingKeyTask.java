package com.natsu.blog.service.schedule.task;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.natsu.blog.service.SettingService;
import com.natsu.blog.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeSettingKeyTask implements JobRunner {

    private String param;

    @Override
    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public void run() {
        log.info("开始执行任务：修改网站配置");
        if (StrUtil.isNotEmpty(param)) {
            JSONObject map = JSON.parseObject(param);
            //更新配置
            SettingService settingService = SpringContextUtils.getBean(SettingService.class);
            settingService.updateSetting(map.getString("key"), map.getString("value"));
        }
        log.info("任务执行完毕");
    }
}
