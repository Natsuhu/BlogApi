package com.natsu.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "blog")
public class BlogProperties {

    /*博客名*/
    private String name;

    /*博客后端URL*/
    private String api;

    /*博客前端前台URL*/
    private String front;

}
