package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.dto.VisitLogDTO;
import com.natsu.blog.model.dto.VisitLogQueryDTO;
import com.natsu.blog.service.VisitLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/visitLog")
public class AdminVisitLogController {

    @Autowired
    private VisitLogService visitLogService;

    @PostMapping("/getVisitLogTable")
    public Result getVisitLogTable(@RequestBody VisitLogQueryDTO queryCond) {
        try {
            IPage<VisitLogDTO> result = visitLogService.getVisitLogTable(queryCond);
            return Result.success(result.getPages(), result.getTotal(), result.getRecords());
        } catch (Exception e) {
            log.error("获取访客记录表格失败，{}", e.getMessage());
            return Result.fail("获取访客记录表格G了：" + e.getMessage());
        }
    }

    @Admin
    @PostMapping("/deleteVisitLog")
    public Result deleteVisitLog(@RequestBody VisitLogDTO visitLogDTO) {
        try {
            visitLogService.deleteVisitLog(visitLogDTO);
            return Result.success("删除记录成功");
        } catch (Exception e) {
            log.error("删除记录失败：{}", e.getMessage());
            return Result.fail("删除记录失败，" + e.getMessage());
        }
    }

}
