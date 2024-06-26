package com.natsu.blog.controller;

import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.vo.AnnexDownloadVO;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/annex")
@Slf4j
public class AnnexController {

    @Autowired
    private AnnexService annexService;

    @GetMapping("/resource/{annexId}")
    public void getResource(@PathVariable("annexId") String annexId, HttpServletResponse response) {
        OutputStream os = null;
        BufferedInputStream bis = null;

        try {
            //获取文件信息
            AnnexDownloadVO result = annexService.download(annexId);
            String fileName = result.getName();
            String size = result.getSize().toString();
            InputStream is = result.getInputStream();
            Boolean isPublished = result.getIsPublished();
            //无权限直接结束
            if (!isPublished) {
                is.close();
                return;
            }
            response.addHeader("Accept-Ranges","bytes");
            response.addHeader("Cache-Control", "public, must-revalidate, max-age=86400");
            //内容类型 - 设定适合的类型（配合在线展示判定）
            //response.setContentType("audio/mpeg;charset=UTF-8");
            response.setContentType(CommonUtils.getContentType(fileName));
            //设置为UTF-8
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            //文件大小
            response.addHeader("Content-Length", size);
            //文件名 - 判定是否支持在线展示
            response.addHeader("Content-Disposition", CommonUtils.getContentDisposition(fileName) + ";filename=" +
                    new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
            //流处理
            bis = new BufferedInputStream(is, Constants.FILE_BUFFER_SIZE);
            os = response.getOutputStream();

            int len;
            byte[] buffer = new byte[Constants.FILE_BUFFER_SIZE];
            while ((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("文件：[{}]，下载失败：{}",annexId, e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception ignored) {}
        }
    }
}
