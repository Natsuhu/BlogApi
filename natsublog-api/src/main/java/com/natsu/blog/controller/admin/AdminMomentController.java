package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.MomentDTO;
import com.natsu.blog.model.dto.MomentQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.MomentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/moment")
public class AdminMomentController {

    @Autowired
    private MomentService momentService;

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "动态管理-编辑")
    @PostMapping("/getUpdateMoment")
    public Result getUpdateMoment(@RequestParam("id") Long momentId) {
        try {
            MomentDTO moment = momentService.getUpdateMoment(momentId);
            return Result.success(moment);
        } catch (Exception e) {
            log.error("获取更新动态失败，{}", e.getMessage());
            return Result.fail("获取更新动态G了，" + e.getMessage());
        }
    }

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "动态管理")
    @PostMapping("/getMomentTable")
    public Result getMomentTable(@RequestBody MomentQueryDTO momentQueryDTO) {
        try {
            IPage<MomentDTO> result = momentService.getMomentTable(momentQueryDTO);
            return Result.success(result.getPages(), result.getTotal(), result.getRecords());
        } catch (Exception e) {
            log.error("获取动态表格失败，{}", e.getMessage());
            return Result.fail("获取动态表格G了，" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.INSERT, description = "动态")
    @PostMapping("/saveMoment")
    public Result saveMoment(@RequestBody MomentDTO momentDTO) {
        //参数校验
        Result result = checkParam(momentDTO);
        if (result != null) {
            return result;
        }
        //开始保存
        try {
            Long momentId = momentService.saveMoment(momentDTO);
            return Result.success(momentId);
        } catch (Exception e) {
            log.error("保存动态失败，{}", e.getMessage());
            return Result.fail("保存失败，" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "动态")
    @PostMapping("/updateMoment")
    public Result updateMoment(@RequestBody MomentDTO momentDTO) {
        //参数校验
        if (momentDTO.getId() == null) {
            return Result.fail("更新动态失败，ID必填");
        }
        //开始更新
        try {
            momentService.updateMoment(momentDTO);
            return Result.success("更新成功！");
        } catch (Exception e) {
            log.error("更新动态失败，{}", e.getMessage());
            return Result.fail("更新失败，" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.DELETE, description = "动态")
    @PostMapping("/deleteMoment")
    public Result deleteMoment(@RequestBody MomentDTO momentDTO) {
        //参数校验
        if (momentDTO.getId() == null) {
            return Result.fail("删除动态失败，ID必填");
        }
        //开始删除
        try {
            momentService.deleteMoment(momentDTO);
            return Result.success("删除成功！");
        } catch (Exception e) {
            log.error("删除动态失败，{}", e.getMessage());
            return Result.fail("删除失败，" + e.getMessage());
        }
    }

    private Result checkParam(MomentDTO momentDTO) {
        if (StringUtils.isBlank(momentDTO.getContent())) {
            return Result.fail("参数错误！动态必须包含内容");
        }
        if (momentDTO.getIsPublished() == null) {
            return Result.fail("参数错误！动态必须选择是否公开");
        }
        if (momentDTO.getIsCommentEnabled() == null) {
            return Result.fail("参数错误！动态必须选择是否允许评论");
        }
        return null;
    }

}
