package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.TaskMapper;
import com.natsu.blog.model.dto.TaskDTO;
import com.natsu.blog.model.dto.TaskQueryDTO;
import com.natsu.blog.model.entity.Task;
import com.natsu.blog.service.TaskService;
import com.natsu.blog.service.schedule.ScheduleService;
import com.natsu.blog.service.schedule.task.CmdTask;
import com.natsu.blog.service.schedule.task.JobRunner;
import com.natsu.blog.service.schedule.task.SqlTask;
import com.natsu.blog.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public IPage<TaskDTO> getTaskTable(TaskQueryDTO queryCond) {
        Page<TaskDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        return taskMapper.getTaskTable(page, queryCond);
    }

    @Override
    public void createTask(TaskDTO taskDTO) {
        taskDTO.setCurrentCount(Constants.COM_NUM_ZERO);
        taskDTO.setStatus(Constants.COM_NUM_ONE);
        //前置检查
        checkMaxCount(taskDTO);
        checkExpireTime(taskDTO);
        //计算下次执行时间
        setNextTime(taskDTO);
        //保存并触发
        taskMapper.insert(taskDTO);
        scheduleService.createScheduleJob(taskDTO.getId(), taskDTO.getCron());
    }

    @Override
    public void exec(String id) {
        Task task = getById(id);
        exec(task);
    }

    @Override
    public void exec(Task task) {
        JobRunner jobRunner = null;
        if (Constants.COM_NUM_ZERO.equals(task.getType())) {
            jobRunner = (JobRunner) SpringContextUtils.getBean(task.getContent());
            jobRunner.setParam(task.getParam());
        } else if (Constants.COM_NUM_ONE.equals(task.getType())) {
            jobRunner = SpringContextUtils.getBean(SqlTask.class);
            jobRunner.setParam(task.getContent());
        } else {
            jobRunner = SpringContextUtils.getBean(CmdTask.class);
            jobRunner.setParam(task.getContent());
        }
        //获取线程池
        Executor executor = (Executor) SpringContextUtils.getBean("taskExecutor");
        executor.execute(jobRunner);
    }

    @Override
    public void updateTask(TaskDTO taskDTO) {

    }

    @Override
    public void updateTaskStatus(String id) {
        updateTaskStatus(getById(id));
    }

    @Override
    public void updateTaskStatus(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(task, taskDTO);
        if (Constants.COM_NUM_ZERO.equals(task.getStatus())) {
            //原本是禁用，现启用
            taskDTO.setStatus(Constants.COM_NUM_ONE);
            //前置检查
            checkMaxCount(taskDTO);
            checkExpireTime(taskDTO);
            //计算下次执行时间
            setNextTime(taskDTO);
            //恢复或新建
            if (scheduleService.getCronTrigger(taskDTO.getId()) == null) {
                log.info("恢复任务：[创建]");
                scheduleService.createScheduleJob(taskDTO.getId(), taskDTO.getCron());
            } else {
                log.info("恢复任务：[启动]");
                scheduleService.resumeJob(taskDTO.getId());
            }
        } else {
            log.info("暂停任务：{}", taskDTO.getId());
            //原本是启用，现禁用
            taskDTO.setStatus(Constants.COM_NUM_ZERO);
            taskDTO.setNextTime(null);
            scheduleService.pauseJob(task.getId());
        }
        updateById(taskDTO);
    }

    @Override
    public void deleteTask(TaskDTO taskDTO) {
        if (scheduleService.getCronTrigger(taskDTO.getId()) != null) {
            scheduleService.deleteScheduleJob(taskDTO.getId());
        }
        taskMapper.deleteById(taskDTO);
    }

    private void checkMaxCount(TaskDTO taskDTO) {
        if (taskDTO.getMaxCount() != null && taskDTO.getCurrentCount() >= taskDTO.getMaxCount()) {
            //达到最大次数，删除该任务
            if (scheduleService.getCronTrigger(taskDTO.getId()) != null) {
                scheduleService.deleteScheduleJob(taskDTO.getId());
            }
            throw new RuntimeException("任务已达最大执行次数");
        }
    }

    private void checkExpireTime(TaskDTO taskDTO) {
        if (taskDTO.getExpireTime() != null && taskDTO.getExpireTime().before(new Date())) {
            //达到截至时间，删除该任务
            if (scheduleService.getCronTrigger(taskDTO.getId()) != null) {
                scheduleService.deleteScheduleJob(taskDTO.getId());
            }
            throw new RuntimeException("任务已达截止时间");
        }
    }

    private void setNextTime(TaskDTO taskDTO) {
        Date nextTime;
        try {
            CronExpression cronExpression = new CronExpression(taskDTO.getCron());
            nextTime = cronExpression.getNextValidTimeAfter(new Date());
        } catch (Exception e) {
            throw new RuntimeException("计算下次执行时间出错：" + e);
        }
        taskDTO.setNextTime(nextTime);
    }

}
