package com.natsu.blog.service.annex;

import com.natsu.blog.enums.StorageType;
import com.natsu.blog.model.dto.AnnexDTO;
import com.natsu.blog.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 文件管理框架--本次存储方式
 *
 * @author NatsuKaze
 * @date 2023/7/21
 */
@Component
@Slf4j
public class LocalChannel implements AnnexChannel {

    /**
     * 获取存储类型
     *
     * @return 存储方式代码
     */
    @Override
    public Integer getType() {
        return StorageType.LOCAL.getType();
    }

    /**
     * 获取存储方式的描述
     *
     * @return 存储方式描述
     */
    @Override
    public String getDescription() {
        return StorageType.LOCAL.getDescription();
    }

    /**
     * 存储文件。
     * 必填参数：parentPath(文件上级路径)，path(文件完整路径)
     *
     * @param multipartFile 分片文件
     * @param annexDTO      存储信息
     */
    @Override
    public void store(MultipartFile multipartFile, AnnexDTO annexDTO) {
        try {
            //建立本地文件存储目录
            String parentPath = annexDTO.getParentPath();
            File file = new File(parentPath);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
            //转为文件
            multipartFile.transferTo(new File(annexDTO.getPath()));
        } catch (Exception e) {
            log.error("保存MultipartFile出错：{}", e.getMessage());
            throw new RuntimeException("分片文件转换出错：" + e.getMessage());
        }

    }

    /**
     * 存储文件
     * 必填项：reflectId(映射ID)，parentPath(存放目录)
     *
     * @param is       输入流
     * @param annexDTO 存储信息
     */
    @Override
    public void store(InputStream is, AnnexDTO annexDTO) {
        try {
            FileUtils.streamToFile(is, annexDTO.getReflectId(), annexDTO.getParentPath());
        } catch (Exception e) {
            log.error("保存InputStream出错：{}", e.getMessage());
            throw new RuntimeException("文件流转换出错：" + e.getMessage());
        }
    }

    /**
     * 存储文件
     * 必填项：name(文件名)，parentPath(存储目录)
     *
     * @param file     文件对象
     * @param annexDTO 存储信息
     */
    @Override
    public void store(File file, AnnexDTO annexDTO) {
        try {
            InputStream is = new FileInputStream(file);
            FileUtils.streamToFile(is, file.getName(), annexDTO.getParentPath());
        } catch (Exception e) {
            log.error("保存file出错：{}", e.getMessage());
            throw new RuntimeException("文件转换出错：" + e.getMessage());
        }

    }

    /**
     * 拿取文件
     * 必填项：path(文件全路径)
     *
     * @param annexDTO 存储信息
     * @return 输入流
     */
    @Override
    public InputStream fetch(AnnexDTO annexDTO) {
        try {
            File file = new File(annexDTO.getPath());
            return new FileInputStream(file);
        } catch (Exception e) {
            log.error("获取文件流出错：{}", e.getMessage());
            throw new RuntimeException("获取文件流出错：" + e.getMessage());
        }
    }

    /**
     * 移除文件
     * 必填项：path(文件完整路径)
     *
     * @param annexDTO 额外信息
     */
    @Override
    public Boolean remove(AnnexDTO annexDTO) {
        try {
            return FileUtils.moveFile(annexDTO.getPath());
        } catch (Exception e) {
            log.error("删除文件出错：{}", e.getMessage());
            throw new RuntimeException("删除文件出错：" + e.getMessage());
        }
    }


}
