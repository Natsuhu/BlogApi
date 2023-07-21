package com.natsu.blog.service.annex;

import com.natsu.blog.model.dto.AnnexDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

public interface AnnexChannel {

    /**
     * 获取存储类型
     *
     * @return 存储方式代码
     */
    Integer getType();

    /**
     * 获取存储方式的描述
     *
     * @return 存储方式描述
     */
    String getDescription();

    /**
     * 存储文件
     *
     * @param multipartFile 分片文件
     * @param annexDTO 存储信息
     */
    void store(MultipartFile multipartFile, AnnexDTO annexDTO);

    /**
     * 存储文件
     *
     * @param is 输入流
     * @param annexDTO 存储信息
     */
    void store(InputStream is, AnnexDTO annexDTO);

    /**
     * 存储文件
     *
     * @param file 文件对象
     * @param annexDTO 存储信息
     */
    void store(File file, AnnexDTO annexDTO);

    /**
     * 获取文件流
     *
     * @param annexDTO 存储信息
     * @return 流
     */
    InputStream fetch(AnnexDTO annexDTO);

    /**
     * 移除文件
     *
     * @param annexDTO 额外信息
     */
    Boolean remove(AnnexDTO annexDTO);

}
