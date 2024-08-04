package com.natsu.blog.controller;

import com.natsu.blog.annotation.AccessLimit;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.User;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.SettingService;
import com.natsu.blog.service.UserService;
import com.natsu.blog.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录控制器
 *
 * @author NK
 * @since 2024-08-04
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private AnnexService annexService;

    @Autowired
    private UserService userService;

    /**
     * 博客前台登录
     *
     * @return result
     */
    @AccessLimit(seconds = 3, maxCount = 1, msg = "请稍后再试")
    @PostMapping
    public Result login(@RequestBody User user) {
        try {
            User loginUser = userService.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
            if (!"admin".equals(loginUser.getRole())) {
                return Result.fail(403, "无权限");
            }
            loginUser.setPassword(null);
            return Result.success(JwtUtils.createToken(loginUser.getUsername(), loginUser.getRole()));
        } catch (Exception e) {
            log.error("登录失败：{}", e.getMessage());
            return Result.fail("登录失败：" + e.getMessage());
        }
    }

    /**
     * 获取前台登录页配置项
     *
     * @return string
     */
    @GetMapping("/getFrontLoginSetting")
    public Result getFrontLoginSetting() {
        try {
            return Result.success(annexService.getAnnexAccessAddress(settingService.getSetting(Constants.SETTING_KET_FRONT_LOGIN_IMAGE)));
        } catch (Exception e) {
            log.error("获取前台登录页配置失败：{}", e.getMessage());
            return Result.fail("获取前台登录页配置失败：" + e.getMessage());
        }
    }

    /**
     * 获取后台登录页配置
     *
     * @return string
     */
    @GetMapping("/getBackLoginSetting")
    public Result getBackLoginSetting() {
        try {
            return Result.success(annexService.getAnnexAccessAddress(settingService.getSetting(Constants.SETTING_KET_BACK_LOGIN_IMAGE)));
        } catch (Exception e) {
            log.error("获取后台登录页配置失败：{}", e.getMessage());
            return Result.fail("获取后台登录页配置失败：" + e.getMessage());
        }
    }

}
