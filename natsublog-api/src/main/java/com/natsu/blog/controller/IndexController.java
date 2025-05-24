package com.natsu.blog.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.natsu.blog.enums.PageEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.vo.AnnexDownloadVO;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.SettingService;
import com.natsu.blog.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private AnnexService annexService;

    /**
     * 获取首页配置项
     * @return settingVO
     */
    @GetMapping("/getSetting")
    public Result getIndexSetting() {
        try {
            SettingVO pageSetting = settingService.getPageSetting(PageEnum.INDEX.getPageCode());
            String headerImageURL = pageSetting.getHeaderImage();
            if (StrUtil.isNotBlank(headerImageURL)) {
                if ("true".equals(pageSetting.getIsHeaderImageBase64())) {
                    if (headerImageURL.contains("http")) {
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(headerImageURL, byte[].class);
                        byte[] body = responseEntity.getBody();
                        String headerImageBase64 = Base64.getEncoder().encodeToString(body);
                        pageSetting.setHeaderImage("data:image/png;base64," + headerImageBase64);
                    } else {
                        AnnexDownloadVO download = annexService.download(headerImageURL);
                        String fileSuffix = FileUtils.getFileSuffix(download.getName());
                        String headerImageBase64 = Base64.getEncoder().encodeToString(IoUtil.readBytes(download.getInputStream()));
                        pageSetting.setHeaderImage("data:image/" + fileSuffix + ";base64," + headerImageBase64);
                    }
                } else {
                    pageSetting.setHeaderImage(annexService.getAnnexAccessAddress(headerImageURL));
                }
            } else {
                pageSetting.setHeaderImage(annexService.getAnnexAccessAddress(headerImageURL));
            }
            return Result.success(pageSetting);
        } catch (Exception e) {
            log.error("获取首页配置失败：{}", e.getMessage());
            return Result.fail("获取首页配置失败：" + e.getMessage());
        }
    }

    /**
     * 获取资料卡配置项
     * @return settingVO
     */
    @GetMapping("/getCardSetting")
    public Result getCardSetting() {
        try {
            SettingVO pageSetting = settingService.getPageSetting(PageEnum.CARD.getPageCode());
            return Result.success(pageSetting);
        } catch (Exception e) {
            log.error("获取资料卡配置失败：{}", e.getMessage());
            return Result.fail("获取资料卡配置失败：" + e.getMessage());
        }
    }

}
