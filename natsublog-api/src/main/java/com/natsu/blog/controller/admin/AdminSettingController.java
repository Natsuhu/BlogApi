package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/setting")
public class AdminSettingController {

    @Autowired
    private SettingService settingService;

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "网站设置-通用")
    @PostMapping("/getCommonSetting")
    public Result getCommonSetting() {
        try {
            SettingVO settingVO = new SettingVO();
            settingVO.setBlogName(settingService.getSetting(Constants.SETTING_KEY_BLOG_NAME));
            settingVO.setWebTitleSuffix(settingService.getSetting(Constants.SETTING_KEY_WEB_TITLE_SUFFIX));
            settingVO.setHeaderTitle(settingService.getSetting(Constants.SETTING_KET_HEADER_TITLE));
            settingVO.setHeaderImage(settingService.getSetting(Constants.SETTING_KET_HEADER_IMAGE));
            settingVO.setBodyImage(settingService.getSetting(Constants.SETTING_KET_BODY_IMAGE));
            settingVO.setAdminCommentLabel(settingService.getSetting(Constants.SETTING_KEY_ADMIN_COMMENT_LABEL));
            settingVO.setFrontLoginImage(settingService.getSetting(Constants.SETTING_KET_FRONT_LOGIN_IMAGE));
            settingVO.setBackLoginImage(settingService.getSetting(Constants.SETTING_KET_BACK_LOGIN_IMAGE));
            settingVO.setIsDeleteCommentInDeleteArticle(settingService.getSetting(Constants.SETTING_KEY_IS_DELETE_COMMENT_IN_DELETE_ARTICLE));
            return Result.success(settingVO);
        } catch (Exception e) {
            log.error("获取通用设置失败：{}", e.getMessage());
            return Result.fail("获取通用设置失败：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "网站设置-通用")
    @PostMapping("/updateCommonSetting")
    public Result updateCommonSetting(@RequestBody SettingVO settingVO) {
        try {
            if (StrUtil.isBlank(settingVO.getBlogName())) {
                return Result.fail("博客名称不能为空");
            }
            if (StrUtil.isBlank(settingVO.getWebTitleSuffix())) {
                return Result.fail("网页标题后缀不能为空");
            }
            settingService.updateSetting(Constants.SETTING_KEY_BLOG_NAME, settingVO.getBlogName());
            settingService.updateSetting(Constants.SETTING_KEY_WEB_TITLE_SUFFIX, settingVO.getWebTitleSuffix());
            settingService.updateSetting(Constants.SETTING_KET_HEADER_TITLE, settingVO.getHeaderTitle());
            settingService.updateSetting(Constants.SETTING_KET_HEADER_IMAGE, settingVO.getHeaderImage());
            settingService.updateSetting(Constants.SETTING_KET_BODY_IMAGE, settingVO.getBodyImage());
            settingService.updateSetting(Constants.SETTING_KEY_ADMIN_COMMENT_LABEL, settingVO.getAdminCommentLabel());
            settingService.updateSetting(Constants.SETTING_KET_FRONT_LOGIN_IMAGE, settingVO.getFrontLoginImage());
            settingService.updateSetting(Constants.SETTING_KET_BACK_LOGIN_IMAGE, settingVO.getBackLoginImage());
            settingService.updateSetting(Constants.SETTING_KEY_IS_DELETE_COMMENT_IN_DELETE_ARTICLE, settingVO.getIsDeleteCommentInDeleteArticle());
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新通用配置失败：{}", e.getMessage());
            return Result.fail("更新通用配置失败：" + e.getMessage());
        }
    }

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "网站设置-资料卡")
    @PostMapping("/getCardSetting")
    public Result getCardSetting() {
        try {
            SettingVO settingVO = new SettingVO();
            settingVO.setCardAvatar(settingService.getSetting(Constants.SETTING_KEY_CARD_AVATAR));
            settingVO.setCardName(settingService.getSetting(Constants.SETTING_KEY_CARD_NAME));
            settingVO.setCardSignature(settingService.getSetting(Constants.SETTING_KEY_CARD_SIGNATURE));
            settingVO.setGithub(settingService.getSetting(Constants.SETTING_KET_GITHUB));
            settingVO.setQq(settingService.getSetting(Constants.SETTING_KET_QQ));
            settingVO.setBilibili(settingService.getSetting(Constants.SETTING_KET_BILIBILI));
            settingVO.setNetease(settingService.getSetting(Constants.SETTING_KET_NETEASE));
            settingVO.setEmail(settingService.getSetting(Constants.SETTING_KET_EMAIL));
            settingVO.setCardCustom(JSON.parseArray(settingService.getSetting(Constants.SETTING_KEY_CARD_CUSTOM)));
            return Result.success(settingVO);
        } catch (Exception e) {
            log.error("获取资料卡设置失败：{}", e.getMessage());
            return Result.fail("获取资料卡设置失败：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "网站设置-资料卡")
    @PostMapping("/updateCardSetting")
    public Result updateCardSetting(@RequestBody SettingVO settingVO) {
        try {
            if (StrUtil.isBlank(settingVO.getCardAvatar())) {
                return Result.fail("头像URL不能为空");
            }
            if (StrUtil.isBlank(settingVO.getCardName())) {
                return Result.fail("资料卡名称不能为空");
            }
            settingService.updateSetting(Constants.SETTING_KEY_CARD_AVATAR, settingVO.getCardAvatar());
            settingService.updateSetting(Constants.SETTING_KEY_CARD_NAME, settingVO.getCardName());
            settingService.updateSetting(Constants.SETTING_KEY_CARD_SIGNATURE, settingVO.getCardSignature());
            settingService.updateSetting(Constants.SETTING_KET_GITHUB, settingVO.getGithub());
            settingService.updateSetting(Constants.SETTING_KET_QQ, settingVO.getQq());
            settingService.updateSetting(Constants.SETTING_KET_BILIBILI, settingVO.getBilibili());
            settingService.updateSetting(Constants.SETTING_KET_NETEASE, settingVO.getNetease());
            settingService.updateSetting(Constants.SETTING_KET_EMAIL, settingVO.getEmail());
            settingService.updateSetting(Constants.SETTING_KEY_CARD_CUSTOM, settingVO.getCardCustom().toJSONString());
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新资料卡配置失败：{}", e.getMessage());
            return Result.fail("更新资料卡配置失败：" + e.getMessage());
        }
    }

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "网站设置-页脚")
    @PostMapping("/getFooterSetting")
    public Result getFooterSetting() {
        try {
            SettingVO settingVO = new SettingVO();
            settingVO.setIcpInfo(settingService.getSetting(Constants.SETTING_KET_ICPINFO));
            settingVO.setCopyright(JSON.parseObject(settingService.getSetting(Constants.SETTING_KET_COPYRIGHT)));
            settingVO.setBadgeList(JSON.parseArray(settingService.getSetting(Constants.SETTING_KET_BADGELIST)));
            return Result.success(settingVO);
        } catch (Exception e) {
            log.error("获取页脚设置失败：{}", e.getMessage());
            return Result.fail("获取页脚设置失败：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "网站设置-页脚")
    @PostMapping("/updateFooterSetting")
    public Result updateFooterSetting(@RequestBody SettingVO settingVO) {
        try {
            settingService.updateSetting(Constants.SETTING_KET_ICPINFO, settingVO.getIcpInfo());
            settingService.updateSetting(Constants.SETTING_KET_COPYRIGHT, settingVO.getCopyright().toJSONString());
            settingService.updateSetting(Constants.SETTING_KET_BADGELIST, settingVO.getBadgeList().toJSONString());
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新页脚设置失败：{}", e.getMessage());
            return Result.fail("更新页脚设置失败：" + e.getMessage());
        }
    }

}
