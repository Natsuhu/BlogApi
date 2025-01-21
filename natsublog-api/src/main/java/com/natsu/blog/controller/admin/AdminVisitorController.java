package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.dto.VisitorDTO;
import com.natsu.blog.model.dto.VisitorQueryDTO;
import com.natsu.blog.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/visitor")
public class AdminVisitorController {

    @Autowired
    private VisitorService visitorService;

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "访客统计")
    @PostMapping("/getVisitorTable")
    public Result getVisitorTable(@RequestBody VisitorQueryDTO queryCond) {
        try {
            IPage<VisitorDTO> result = visitorService.getVisitorTable(queryCond);
            return Result.success(result.getPages(), result.getTotal(), result.getRecords());
        } catch (Exception e) {
            log.error("获取访客统计表格失败，{}", e.getMessage());
            return Result.fail("获取访客统计表格G了：" + e.getMessage());
        }
    }

}
