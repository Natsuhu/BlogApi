package com.natsu.blog.controller.admin;

import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.AnnexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/annex")
@Slf4j
public class AdminAnnexController {

    @Autowired
    private AnnexService annexService;

    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file") MultipartFile multipartFile,
                         @RequestParam(value = "isPublished") Boolean isPublished) {
        try {
            String annexId = annexService.upload(multipartFile, isPublished);
            return Result.success("上传成功");
        } catch (Exception e) {
            log.error("上传失败：{}", e.getMessage());
            return Result.fail("上传失败：" + e.getMessage());
        }
    }


}
