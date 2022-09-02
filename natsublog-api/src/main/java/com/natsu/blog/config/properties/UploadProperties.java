package com.natsu.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "upload.file")
public class UploadProperties {

    /*本地文件路径*/
    private String path;

    /*请求地址映射*/
    private String accessPath;

    /*本地文件路径映射*/
    private String resourcesLocations;
}
