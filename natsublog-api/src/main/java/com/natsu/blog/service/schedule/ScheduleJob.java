package com.natsu.blog.service.schedule;

import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.entity.Task;
import com.natsu.blog.service.TaskService;
import com.natsu.blog.service.schedule.task.CmdTask;
import com.natsu.blog.service.schedule.task.JobRunner;
import com.natsu.blog.service.schedule.task.SqlTask;
import com.natsu.blog.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
public class ScheduleJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        TaskService taskService = SpringContextUtils.getBean(TaskService.class);
        ScheduleService scheduleService = SpringContextUtils.getBean(ScheduleService.class);
        //从上下文拿到任务ID
        String taskId = (String) context.getMergedJobDataMap().get(Constants.QUARTZ_MAP_KEY);
        //拿到任务实体
        Task task = taskService.getById(taskId);
        //运行前检查
        if (Constants.COM_NUM_ZERO.equals(task.getStatus())) {
            //禁用状态，删除任务
            log.warn("执行任务-前置检查：任务为禁用状态：[{}]", taskId);
            updateTask(taskService, scheduleService, task);
            return;
        }
        Date expireTime = task.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())) {
            //有截至时间，且截止时间已过，删除任务
            log.warn("执行任务-前置检查：任务已过期：[{}]", taskId);
            updateTask(taskService, scheduleService, task);
            return;
        }
        Integer maxCount = task.getMaxCount();
        Integer currentCount = task.getCurrentCount();
        if (maxCount != null && maxCount <= currentCount) {
            //超过最大执行次数，删除任务
            log.warn("执行任务-前置检查：任务已达最大执行次数：[{}]", taskId);
            updateTask(taskService, scheduleService, task);
            return;
        }
        //开始执行任务
        taskService.exec(task);
        //运行后检查
        currentCount += 1;
        task.setCurrentCount(currentCount);
        if (maxCount != null && maxCount <= currentCount) {
            //超过最大执行次数，删除任务
            log.warn("执行任务-后置检查：任务已达最大执行次数：[{}]", taskId);
            updateTask(taskService, scheduleService, task);
            return;
        }
        //获取下次执行时间
        CronTrigger cronTrigger = scheduleService.getCronTrigger(taskId);
        Date nextTime = cronTrigger.getNextFireTime();
        if (expireTime != null && expireTime.before(nextTime)) {
            //有截至时间，且截止时间已过，删除任务
            log.warn("执行任务-后置检查：任务已达截至时间：[{}]", taskId);
            updateTask(taskService, scheduleService, task);
            return;
        }
        task.setNextTime(nextTime);
        taskService.updateById(task);
    }

    private void updateTask(TaskService taskService, ScheduleService scheduleService, Task task) {
        task.setStatus(Constants.COM_NUM_ZERO);
        task.setNextTime(null);
        scheduleService.deleteScheduleJob(task.getId());
        taskService.updateById(task);
    }

}
