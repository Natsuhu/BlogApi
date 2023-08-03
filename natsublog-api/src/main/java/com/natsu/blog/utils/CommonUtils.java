package com.natsu.blog.utils;

import cn.hutool.crypto.digest.DigestUtil;
import com.natsu.blog.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class CommonUtils {

    /**
     * 对比SHA256值，判断内容是否变化
     *
     * @param contentA 文本A
     * @param contentB 文本B
     * @return Boolean
     */
    public static Boolean compareString(String contentA, String contentB) {
        String aSha256 = DigestUtil.sha256Hex(contentA);
        String bSha256 = DigestUtil.sha256Hex(contentB);
        return aSha256.equals(bSha256);
    }

    /**
     * 获取适合的ContentType
     *
     * @param filename 文件名
     * @return 返回适合的ContentType
     */
    public static String getContentType(String filename) {
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
     * 获取适合的ContentDisposition
     *
     * @param filename 文件名
     * @return 返回结果
     */
    public static String getContentDisposition(String filename) {
        String suffix = FileUtils.getFileSuffix(filename);

        if (StringUtils.isNotBlank(suffix)) {
            if (Constants.FILE_EXTENSION_PDF.equalsIgnoreCase(suffix)) {
                return Constants.CONTENT_DISPOSITION_INLINE;
            } else if (Arrays.stream(Constants.FILE_EXTENSION_IMG).anyMatch(s -> s.equalsIgnoreCase(suffix))) {
                return Constants.CONTENT_DISPOSITION_INLINE;
            } else {
                return Constants.CONTENT_DISPOSITION_ANNEX;
            }
        } else {
            return Constants.CONTENT_DISPOSITION_ANNEX;
        }
    }

}
