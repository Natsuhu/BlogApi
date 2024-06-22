package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.OperationLogDTO;
import com.natsu.blog.model.dto.OperationLogQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/operationLog")
public class AdminOperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @PostMapping("/getOperationLogTable")
    public Result getOperationLogTable(@RequestBody OperationLogQueryDTO queryCond) {
        try {
            IPage<OperationLogDTO> result = operationLogService.getOperationLogTable(queryCond);
            return Result.success(result.getPages(), result.getTotal(), result.getRecords());
        } catch (Exception e) {
            log.error("获取操作记录表格失败，{}", e.getMessage());
            return Result.fail("获取操作记录表格G了：" + e.getMessage());
        }
    }

    @Admin
    @PostMapping("/deleteOperationLog")
    public Result deleteOperationLog(@RequestBody OperationLogDTO operationLogDTO) {
        try {
            operationLogService.deleteOperationLog(operationLogDTO);
            return Result.success("删除记录成功");
        } catch (Exception e) {
            log.error("删除记录失败：{}", e.getMessage());
            return Result.fail("删除记录失败，" + e.getMessage());
        }
    }

}
