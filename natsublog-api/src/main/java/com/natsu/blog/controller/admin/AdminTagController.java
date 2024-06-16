package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.dto.TagDTO;
import com.natsu.blog.model.dto.TagQueryDTO;
import com.natsu.blog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/tag")
public class AdminTagController {

    @Autowired
    private TagService tagService;

    @Admin
    @OperationLogger(type = OperationTypeEnum.INSERT, description = "标签")
    @PostMapping("/saveTag")
    public Result saveTag(@RequestBody TagDTO TagDTO) {
        //参数校验
        if (TagDTO.getId() != null) {
            return Result.fail("请勿携带ID");
        }
        if (StringUtils.isBlank(TagDTO.getName())) {
            return Result.fail("标签名称必填");
        }
        //开始保存
        try {
            tagService.saveTag(TagDTO);
            return Result.success("新增标签成功");
        } catch (Exception e) {
            log.error("新增标签失败：{}", e.getMessage());
            return Result.fail("新增标签失败，" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.DELETE, description = "标签")
    @PostMapping("/deleteTag")
    public Result deleteTag(@RequestBody TagDTO TagDTO) {
        //参数校验
        if (TagDTO.getId() == null) {
            return Result.fail("删除标签失败，ID必填");
        }
        //开始删除分类
        try {
            tagService.deleteTag(TagDTO);
            return Result.success("删除标签成功");
        } catch (Exception e) {
            log.error("删除标签失败：{}", e.getMessage());
            return Result.fail("删除标签失败，" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "标签")
    @PostMapping("/updateTag")
    public Result updateTag(@RequestBody TagDTO tagDTO) {
        //参数校验
        if (tagDTO.getId() == null) {
            return Result.fail("更新失败，ID必填");
        }
        //开始更新
        try {
            tagService.updateTag(tagDTO);
            return Result.success("更新标签成功");
        } catch (Exception e) {
            log.error("更新标签失败：{}", e.getMessage());
            return Result.fail("更新标签失败，" + e.getMessage());
        }
    }

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "标签管理")
    @PostMapping("/getTagTable")
    public Result getTagTable(@RequestBody TagQueryDTO queryDTO) {
        try {
            IPage<TagDTO> pageResult = tagService.getTagTable(queryDTO);
            return Result.success(pageResult.getPages(), pageResult.getTotal(), pageResult.getRecords());
        } catch (Exception e) {
            log.error("获取标签表格失败，{}", e.getMessage());
            return Result.fail("获取标签表格G了" + e.getMessage());
        }
    }

}
