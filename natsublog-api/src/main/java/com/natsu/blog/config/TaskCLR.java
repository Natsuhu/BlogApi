package com.natsu.blog.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.entity.Task;
import com.natsu.blog.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TaskCLR implements CommandLineRunner {

    @Autowired
    private TaskService taskService;

    @Override
    public void run(String... args) throws Exception {
        log.info("断电/重启恢复定时任务 开始");
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getStatus, Constants.COM_NUM_ONE);
        List<Task> taskList = taskService.list(queryWrapper);
        for (Task task : taskList) {
            task.setStatus(Constants.COM_NUM_ZERO);
            taskService.updateTaskStatus(task);
        }
        log.info("断电/重启恢复定时任务 结束");
    }
}
