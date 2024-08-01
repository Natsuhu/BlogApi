package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.AnnexDTO;
import com.natsu.blog.model.dto.AnnexQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.vo.AnnexDownloadVO;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.utils.HttpRangeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/admin/annex")
@Slf4j
public class AdminAnnexController {

    @Autowired
    private AnnexService annexService;

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPLOAD, description = "文件")
    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file") MultipartFile multipartFile,
                         @RequestParam(value = "isPublished", defaultValue = "false", required = false) Boolean isPublished) {
        try {
            String annexId = annexService.upload(multipartFile, isPublished);
            return Result.success(annexService.getAnnexAccessAddress(annexId));
        } catch (Exception e) {
            log.error("上传失败：{}", e.getMessage());
            return Result.fail("上传失败：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.DOWNLOAD, description = "文件")
    @GetMapping("/download/{annexId}")
    public void getResource(@PathVariable("annexId") String annexId, HttpServletRequest request, HttpServletResponse response) {
        OutputStream os = null;
        BufferedInputStream bis = null;
        try {
//            Enumeration<String> headerNames = request.getHeaderNames();
//            while (headerNames.hasMoreElements()) {
//                String a = headerNames.nextElement();
//                System.out.println("请求头：" + a + "：" + request.getHeader(a));
//            }
            //获取文件信息
            AnnexDownloadVO result = annexService.download(annexId);
            String fileName = result.getName();
            String size = result.getSize().toString();
            InputStream is = result.getInputStream();
            //获取流
            bis = new BufferedInputStream(is, Constants.FILE_BUFFER_SIZE);
            os = response.getOutputStream();
            //设置公共请求头
            response.addHeader("Accept-Ranges", "bytes");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            //判断是否断分片传输
            String range = request.getHeader("range");
            if (StrUtil.isNotBlank(range) && range.startsWith("bytes=")) {
                try {
                    long rangeStart = HttpRangeUtils.getRangeStart(range);
                    Long rangeEnd = HttpRangeUtils.getRangeEnd(range);
                    long rangeLength = Long.parseLong(size) - rangeStart;
                    //response.addHeader("Digest", "sha-256=" + sha256Hex);
                    response.addHeader("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + size);
                    response.addHeader("Content-Length", Long.toString(rangeLength));
                    int len;
                    byte[] buffer = new byte[(int) rangeLength];
                    bis.skip(rangeStart);
                    while ((len = bis.read(buffer, 0, (int) rangeLength)) != -1) {
                        os.write(buffer, 0, len);
                    }
                } catch (HttpException e) {
                    response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                }
            } else {
                //文件大小 (可以不设置长度，这样在size字段和文件实际大小不同情况下，也可保证下载成功)
                response.addHeader("Content-Length", size);
                String utf8FileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                response.addHeader("Content-Disposition", Constants.CONTENT_DISPOSITION_ANNEX + ";filename=" + utf8FileName);
                //流处理
                int len;
                byte[] buffer = new byte[Constants.FILE_BUFFER_SIZE];
                while ((len = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
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

    @OperationLogger(type = OperationTypeEnum.QUERY, description = "文件管理")
    @PostMapping("/getAnnexTable")
    public Result getAnnexTable(@RequestBody AnnexQueryDTO annexQueryDTO) {
        try {
            IPage<AnnexDTO> result = annexService.getAnnexTable(annexQueryDTO);
            return Result.success(result.getPages(), result.getTotal(), result.getRecords());
        } catch (Exception e) {
            log.error("获取文件管理表格失败，{}", e.getMessage());
            return Result.fail("获取文件管理表格G了：" + e.getMessage());
        }
    }

    @PostMapping("/getSuffixSelector")
    public Result getSuffixSelector() {
        try {
            List<String> result = annexService.getSuffixSelector();
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取文件管理下拉框失败，{}", e.getMessage());
            return Result.fail("获取文件管理下拉框G了：" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "文件")
    @PostMapping("/updateAnnex")
    public Result updateAnnex(@RequestBody AnnexDTO annexDTO) {
        if (annexDTO.getId() == null) {
            return Result.fail("参数错误，必须填写文件ID");
        }
        try {
            annexService.updateAnnex(annexDTO);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("文件更新失败！{}", e.getMessage());
            return Result.fail("文件更新失败！" + e.getMessage());
        }
    }

    @Admin
    @OperationLogger(type = OperationTypeEnum.DELETE, description = "文件")
    @PostMapping("deleteAnnex")
    public Result deleteAnnex(@RequestBody AnnexDTO annexDTO) {
        if (annexDTO.getId() == null) {
            return Result.fail("参数错误，必须填写文件ID");
        }
        try {
            annexService.deleteAnnex(annexDTO);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除文件失败！{}", e.getMessage());
            return Result.fail("删除文件失败！" + e.getMessage());
        }
    }
}
