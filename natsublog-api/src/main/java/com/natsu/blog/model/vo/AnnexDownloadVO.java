package com.natsu.blog.model.vo;

import lombok.Data;

import java.io.InputStream;

/**
 * 文件下载VO
 *
 * @since 2023/12/12
 * @author NK
 */
@Data
public class AnnexDownloadVO {

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 是否公开
     */
    private Boolean isPublished;

    /**
     * 流
     */
    private InputStream inputStream;

}
