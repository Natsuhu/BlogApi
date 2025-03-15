package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.dto.UserDTO;
import com.natsu.blog.model.dto.UserQueryDTO;
import com.natsu.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "用户列表")
    @PostMapping("/getUserTable")
    public Result getTaskTable(@RequestBody UserQueryDTO queryCond) {
        try {
            IPage<UserDTO> pageResult = userService.getUserTable(queryCond);
            return Result.success(pageResult.getPages(), pageResult.getTotal(), pageResult.getRecords());
        } catch (Exception e) {
            log.error("获取用户表格失败，{}", e.getMessage());
            return Result.fail("获取用户表格G了" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "密码")
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody JSONObject userDTO) {
        try {
            if (StrUtil.isBlank(userDTO.getString("id"))) {
                return Result.fail("id必填");
            }
            if (StrUtil.isBlank(userDTO.getString("password"))) {
                return Result.fail("密码必填");
            }
            userService.updatePassword(userDTO);
            return Result.success("修改密码成功");
        } catch (Exception e) {
            log.error("修改用户密码失败，{}", e.getMessage());
            return Result.fail("修改用户密码G了" + e.getMessage());
        }
    }

}
