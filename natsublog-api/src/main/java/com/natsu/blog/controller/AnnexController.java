package com.natsu.blog.controller;

import com.natsu.blog.constant.Constants;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/annex")
@Slf4j
public class AnnexController {

    @Autowired
    private AnnexService annexService;

    @GetMapping("/resource/{annexId}")
    public void getResource(@PathVariable("annexId") String annexId, HttpServletResponse response) {
        try {
            OutputStream os = null;
            BufferedInputStream bis = null;

            //获取文件信息
            HashMap<String, Object> result = annexService.download(annexId);
            String fileName = result.get("fileName").toString();
            String size = result.get("size").toString();
            InputStream is = (InputStream) result.get("inputStream");
            Boolean isPublished = (Boolean) result.get("isPublished");
            //无权限直接结束
            if (!isPublished) {
                return;
            }
            //内容类型 - 设定适合的类型（配合在线展示判定）
            response.setContentType(getContentType(fileName));
            //设置为UTF-8
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            //文件大小
            response.addHeader("Content-Length", size);
            //文件名 - 判定是否支持在线展示
            response.addHeader("Content-Disposition", getContentDisposition(fileName) + ";filename=" +
                    new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
            // 流处理
            bis = new BufferedInputStream(is, Constants.FILE_BUFFER_SIZE);
            os = response.getOutputStream();

            int len;
            byte[] buffer = new byte[Constants.FILE_BUFFER_SIZE];
            while ((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            bis.close();
            os.close();
        } catch (Exception e) {
            log.error("文件下载失败：{}", e.getMessage());
        }
    }

    /**
     * 获取适合的ContentType
     *
     * @param filename 文件名
     * @return 返回适合的ContentType
     */
    private String getContentType(String filename) {
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        // 防呆 - 默认就是流格式
        try {
            Optional<MediaType> optionalMediaType = MediaTypeFactory.getMediaType(filename);
            if (optionalMediaType.isPresent()) {
                contentType = optionalMediaType.get().toString();
            }
        } catch (Exception e) {
            log.warn("文件下载-ContentType判定异常: {}", e.getMessage());
        }
        return contentType;
    }

    /**
     * AttachmentOrInLine
     *
     * @param filename 文件名
     * @return 返回结果
     */
    private String getContentDisposition(String filename) {
        String suffix = FileUtils.getFileSuffix(filename);

        if (StringUtils.isNotBlank(suffix)) {
            if (Constants.FILE_EXTENSION_PDF.equalsIgnoreCase(suffix)) {
                return Constants.CONTENT_DISPOSITION_INLINE;
            } else if (Arrays.stream(Constants.FILE_EXTENSION_IMG).anyMatch(s -> s.equalsIgnoreCase(suffix))) {
                return Constants.CONTENT_DISPOSITION_INLINE;
            } else {
                return Constants.CONTENT_DISPOSITION_ATTACHMENT;
            }
        } else {
            return Constants.CONTENT_DISPOSITION_ATTACHMENT;
        }
    }


}
