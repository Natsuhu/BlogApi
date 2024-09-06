package com.natsu.blog.service.schedule;

import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 任务前缀
     */
    private static final String QUARTZ_TASK_SUFFIX = "TASK_";

    /**
     * 获取触发器key
     */
    public TriggerKey getTriggerKey(String taskId) {
        return TriggerKey.triggerKey(QUARTZ_TASK_SUFFIX + taskId);
    }

    /**
     * 获取JobKey
     *
     * @param taskId 任务ID
     * @return JobKey
     */
    public JobKey getJobKey(String taskId) {
        return JobKey.jobKey(QUARTZ_TASK_SUFFIX + taskId);
    }

    /**
     * 获取CRON触发器
     *
     * @param taskId 任务ID
     * @return CronTrigger
     */
    public CronTrigger getCronTrigger(String taskId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(taskId));
        } catch (SchedulerException e) {
            log.error("获取CRON触发器出错：" + e);
            throw new RuntimeException("获取CRON触发器出错：", e);
        }
    }

    /**
     * 创建定时任务
     *
     * @param taskId Task实体对象
     * @param cron   CRON表达式
     */
    public void createScheduleJob(String taskId, String cron) {
        try {
            //构建JobDetail
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(taskId)).build();
            //使用CRON触发器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(taskId)).withSchedule(scheduleBuilder).build();
            //将taskId放入上下文，以便执行时获取
            jobDetail.getJobDataMap().put(Constants.QUARTZ_MAP_KEY, taskId);
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("成功创建定时任务：[{}]", taskId);
        } catch (SchedulerException e) {
            log.error("创建定时任务失败：TaskId：[{}]，原因：[{}]", taskId, e.getMessage());
            throw new RuntimeException("创建定时任务失败", e);
        }
    }

    /**
     * 更新定时任务
     *
     * @param taskId 任务实体对象
     * @param cron   CRON表达式
     */
    public void updateScheduleJob(String taskId, String cron) {
        try {
            TriggerKey triggerKey = getTriggerKey(taskId);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = getCronTrigger(taskId);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            trigger.getJobDataMap().put(Constants.QUARTZ_MAP_KEY, taskId);
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务失败：TaskId：[{}]，原因：[{}]", taskId, e.getMessage());
            throw new RuntimeException("更新定时任务失败", e);
        }
    }

    /**
     * 立即执行任务
     *
     * @param taskId 任务ID
     */
    public void runNow(String taskId) {
        try {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(Constants.QUARTZ_MAP_KEY, taskId);
            scheduler.triggerJob(getJobKey(taskId), dataMap);
        } catch (SchedulerException e) {
            log.error("立即执行定时任务失败：TaskId：[{}]，原因：[{}]", taskId, e.getMessage());
            throw new RuntimeException("立即执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     *
     * @param taskId 任务ID
     */
    public void pauseJob(String taskId) {
        try {
            scheduler.pauseJob(getJobKey(taskId));
        } catch (SchedulerException e) {
            log.error("暂停定时任务失败：TaskId：[{}]，原因：[{}]", taskId, e.getMessage());
            throw new RuntimeException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     *
     * @param taskId 任务ID
     */
    public void resumeJob(String taskId) {
        try {
            scheduler.resumeJob(getJobKey(taskId));
        } catch (SchedulerException e) {
            log.error("恢复定时任务失败：TaskId：[{}]，原因：[{}]", taskId, e.getMessage());
            throw new RuntimeException("恢复定时任务失败", e);
        }
    }

    /**
     * 删除任务
     *
     * @param taskId 任务ID
     */
    public void deleteScheduleJob(String taskId) {
        try {
            scheduler.deleteJob(getJobKey(taskId));
        } catch (SchedulerException e) {
            log.error("删除定时任务失败：TaskId：[{}]，原因：[{}]", taskId, e.getMessage());
            throw new RuntimeException("删除定时任务失败", e);
        }
    }


}
