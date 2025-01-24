package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.dto.TaskDTO;
import com.natsu.blog.model.dto.TaskQueryDTO;
import com.natsu.blog.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/task")
public class AdminTaskController {

    @Autowired
    private TaskService taskService;

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "定时任务")
    @PostMapping("/getTaskTable")
    public Result getTaskTable(@RequestBody TaskQueryDTO queryDTO) {
        try {
            IPage<TaskDTO> pageResult = taskService.getTaskTable(queryDTO);
            return Result.success(pageResult.getPages(), pageResult.getTotal(), pageResult.getRecords());
        } catch (Exception e) {
            log.error("获取任务表格失败，{}", e.getMessage());
            return Result.fail("获取任务表格G了" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.INSERT, description = "定时任务")
    @PostMapping("/createTask")
    public Result createTask(@RequestBody TaskDTO taskDTO) {
        //必填校验
        if (StrUtil.isNotEmpty(taskDTO.getId())) {
            return Result.fail("不能填写ID");
        }
        if (StrUtil.isBlank(taskDTO.getName())) {
            return Result.fail("任务名称必填");
        }
        if (taskDTO.getType() == null) {
            return Result.fail("任务类型必填");
        }
        if (StrUtil.isBlank(taskDTO.getCron())) {
            return Result.fail("CRON必填");
        }
        if (StrUtil.isBlank(taskDTO.getContent())) {
            return Result.fail("任务内容必填");
        }
        //新增定时任务
        try {
            taskService.createTask(taskDTO);
            return Result.success("添加定时任务成功");
        } catch (Exception e) {
            log.error("添加定时任务失败，{}", e.getMessage());
            return Result.fail("添加定时任务G了" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.EXEC, description = "定时任务")
    @PostMapping("/exec")
    public Result execTask(@RequestBody TaskDTO taskDTO) {
        if (StrUtil.isBlank(taskDTO.getId())) {
            return Result.fail("ID必填");
        }
        //立即执行任务
        try {
            taskService.exec(taskDTO.getId());
            return Result.success("立即执行定时任务");
        } catch (Exception e) {
            log.error("立即执行定时任务失败，{}", e.getMessage());
            return Result.fail("立即执行定时任务G了" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "定时任务-启动/停止")
    @PostMapping("/updateTaskStatus")
    public Result updateTaskStatus(@RequestBody TaskDTO taskDTO) {
        if (StrUtil.isBlank(taskDTO.getId())) {
            return Result.fail("ID必填");
        }
        //启停定时任务
        try {
            taskService.updateTaskStatus(taskDTO.getId());
            return Result.success("更新定时任务状态成功");
        } catch (Exception e) {
            log.error("更新定时任务状态失败，{}", e.getMessage());
            return Result.fail("更新定时任务状态失败：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "定时任务更新")
    @PostMapping("/updateTask")
    public Result updateTask(@RequestBody TaskDTO taskDTO) {
        if (StrUtil.isBlank(taskDTO.getId())) {
            return Result.fail("ID必填");
        }
        //更新定时任务
        try {
            taskService.updateTask(taskDTO);
            return Result.success("更新定时任务成功");
        } catch (Exception e) {
            log.error("更新定时任务失败，{}", e.getMessage());
            return Result.fail("更新定时任务失败：" + e);
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.DELETE, description = "定时任务")
    @PostMapping("/deleteTask")
    public Result deleteTask(@RequestBody TaskDTO taskDTO) {
        if (StrUtil.isBlank(taskDTO.getId())) {
            return Result.fail("ID必填");
        }
        try {
            taskService.deleteTask(taskDTO);
            return Result.success("删除定时任务成功");
        } catch (Exception e) {
            log.error("删除定时任务失败，{}", e.getMessage());
            return Result.fail("删除定时任务G了" + e.getMessage());
        }
    }

}
