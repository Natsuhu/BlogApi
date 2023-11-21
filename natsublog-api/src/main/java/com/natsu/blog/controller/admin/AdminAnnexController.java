package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.dto.AnnexDTO;
import com.natsu.blog.model.dto.AnnexQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin/annex")
@Slf4j
public class AdminAnnexController {

    @Autowired
    private AnnexService annexService;

    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file") MultipartFile multipartFile,
                         @RequestParam(value = "isPublished", defaultValue = "false", required = false) Boolean isPublished) {
        try {
            String annexId = annexService.upload(multipartFile, isPublished);
            return Result.success(annexId);
        } catch (Exception e) {
            log.error("上传失败：{}", e.getMessage());
            return Result.fail("上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/download/{annexId}")
    public void getResource(@PathVariable("annexId") String annexId, HttpServletResponse response) {
        OutputStream os = null;
        BufferedInputStream bis = null;

        try {
            //获取文件信息
            HashMap<String, Object> result = annexService.download(annexId);
            String fileName = result.get("fileName").toString();
            String size = result.get("size").toString();
            InputStream is = (InputStream) result.get("inputStream");

            //暴露请求头，允许fetch api拿到
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            //内容类型 - 设定适合的类型（配合在线展示判定）
            response.setContentType(CommonUtils.getContentType(fileName));
            //设置为UTF-8
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            //文件大小 (可以不设置长度，这样在size字段和文件实际大小不同情况下，也可保证下载成功)
            response.addHeader("Content-Length", size);
            //文件名 - 判定是否支持在线展示
            String utf8FileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.addHeader("Content-Disposition", CommonUtils.getContentDisposition(fileName) + ";filename=" +
                    utf8FileName);
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
