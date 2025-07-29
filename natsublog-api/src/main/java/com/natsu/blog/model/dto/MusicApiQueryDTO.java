package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 音乐信息查询DTO
 *
 * @author NatsuKaze
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MusicApiQueryDTO extends BaseQueryDTO {

    /**
     * 服务 来自于 meting.js 的请求参数
     */
    private String server;

    /**
     * 类型 来自于 meting.js 的请求参数
     */
    private String type;

    /**
     * 歌曲ID，对应本系统的文件ID 来自于 meting.js 的请求参数
     */
    private String id;

    /**
     * 权限 来自于 meting.js 的请求参数
     */
    private String auth;

    /**
     * 随机数 来自于 meting.js 的请求参数
     */
    private String r;

}
