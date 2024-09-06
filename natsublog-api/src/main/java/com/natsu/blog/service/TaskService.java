package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.TaskDTO;
import com.natsu.blog.model.dto.TaskQueryDTO;
import com.natsu.blog.model.entity.Task;

public interface TaskService extends IService<Task> {

    IPage<TaskDTO> getTaskTable(TaskQueryDTO queryDTO);

    void createTask(TaskDTO taskDTO);

    void updateTask(TaskDTO taskDTO);

    void updateTaskStatus(String id);

    void updateTaskStatus(Task task);

    void deleteTask(TaskDTO taskDTO);

    void exec(Task task);

    void exec(String id);
}
