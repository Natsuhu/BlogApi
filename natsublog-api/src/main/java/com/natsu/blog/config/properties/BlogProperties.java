package com.natsu.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 博客配置
 *
 * @author NatsuKaze
 * @since 2023/1/19新增注释
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "blog")
public class BlogProperties {

    /**
     * 博客名
     */
    private String name;

    /**
     * 博客后端URL
     */
    private String api;

    /**
     * 博客前端前台URL
     */
    private String front;

    /**
     * 博客管理系统URL
     */
    private String ms;

    /**
     * 博客文件管理
     */
    private HashMap<String, String> annex;

}
