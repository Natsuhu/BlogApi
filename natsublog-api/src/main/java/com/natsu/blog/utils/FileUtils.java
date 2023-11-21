package com.natsu.blog.utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

@Slf4j
public class FileUtils {

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return String
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            int point = fileName.lastIndexOf(".");
            if (point == -1) {
                return null;
            }
            return fileName.substring(point + 1);
        }
        return null;
    }

    /**
     * 流转文件对象
     *
     * @param in       输入流
     * @param fileName 文件名
     * @param path     目标路径
     * @return 文件对象
     */
    public static File streamToFile(InputStream in, String fileName, String path) throws Exception {
        //1KB缓冲区
        byte[] buffer = new byte[1024];
        int len = 0;
        //建立文件夹
        File tempContent = new File(path);
        if (!tempContent.exists() || !tempContent.isDirectory()) {
            tempContent.mkdirs();
        }
        //读取文件到文件夹
        OutputStream os = new FileOutputStream(path + File.separator + fileName);
        while ((len = in.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        //关流
        in.close();
        os.close();
        return new File(path + File.separator + fileName);
    }

    /**
     * 删除文件或文件夹
     * 直接调用hutool
     *
     * @param path 文件路径或文件夹路径
     * @return 删除成功或失败
     */
    public static boolean moveFile(String path) {
        return FileUtil.del(path);
    }

    /**
     * 格式文件大小
     *
     * @param fileSize 文件大小Byte
     * @return 格式化结果
     */
    public static String formatFileSize(Long fileSize) {
        String result = "0 B";
        if (fileSize == null || fileSize < 0) {
            return result;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileSize < 1024L) {
            result = fileSize + " B";
        } else if (fileSize < 1048576L) {
            result = df.format((double) fileSize / 1024L) + " KB";
        } else if (fileSize < 1073741824L) {
            result = df.format((double) fileSize / 1048576L) + " MB";
        } else {
            result = df.format((double) fileSize / 1073741824L) + " GB";
        }
        return result;
    }

}
