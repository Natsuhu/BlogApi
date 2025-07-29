package com.natsu.blog.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.vo.AnnexDownloadVO;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.utils.CommonUtils;
import com.natsu.blog.utils.HttpRangeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    public void getResource(@PathVariable("annexId") String annexId, HttpServletRequest request, HttpServletResponse response) {
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
                //关闭流，设置状态为403
                is.close();
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("Cache-Control", "public, must-revalidate, max-age=86400");
            //内容类型 - 设定适合的类型（配合在线展示判定）
            //response.setContentType("audio/mpeg;charset=UTF-8");
            response.setContentType(CommonUtils.getContentType(fileName));
            //设置为UTF-8
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            //判断是否断分片传输
            String range = request.getHeader("range");
            if (StrUtil.isNotBlank(range) && range.startsWith("bytes=")) {
                long fileSize = Long.parseLong(size);
                //rangeStart不能小于0且大于文件体积
                long rangeStart = HttpRangeUtils.getRangeStart(range);
                if (rangeStart < 0 || rangeStart >= fileSize -1) {
                    rangeStart = 0L;
                }
                //rangeEnd不能为空且大于文件体积
                Long rangeEnd = HttpRangeUtils.getRangeEnd(range);
                if (rangeEnd == null || rangeEnd >= fileSize - 1 || rangeEnd <= rangeStart) {
                    rangeEnd = fileSize - 1L;
                }
                long rangeLength = rangeEnd - rangeStart + 1;
                response.addHeader("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + size);
                response.addHeader("Content-Length", Long.toString(rangeLength));
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                //分片流处理
                bis = new BufferedInputStream(is, Constants.FILE_BUFFER_SIZE);
                os = response.getOutputStream();

                int len;
                byte[] buffer = new byte[(int) rangeLength];
                bis.skip(rangeStart);
                while ((len = bis.read(buffer, 0, (int) rangeLength)) != -1) {
                    os.write(buffer, 0, len);
                }
            } else {
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
            }
        } catch (HttpException e) {
            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            log.error("文件：[{}]，下载失败：{}", annexId, e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("文件：[{}]，下载失败：{}", annexId, e.getMessage());
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
