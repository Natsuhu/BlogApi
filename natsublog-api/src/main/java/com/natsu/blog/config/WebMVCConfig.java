package com.natsu.blog.config;

import com.natsu.blog.config.properties.UploadProperties;
import com.natsu.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 *
 * @author NatsuKaze
 * @since 2023/1/19 新增注释
 * */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    /**
     * 登录拦截器
     * */
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 文件上传
     * */
    @Autowired
    private UploadProperties uploadProperties;

    /**
     * 跨域配置，因为前后端端口不同，需要允许前端服务器访问后端端口
     * */
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**").allowedOrigins("*");
    }

    /**
     * 使拦截器生效
     * */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/test");
    }

    /**
     * 静态资源映射
     * */
    //TODO 测试用，生产环境统一使用Nginx代理静态资源
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadProperties.getAccessPath()).addResourceLocations(uploadProperties.getResourcesLocations());
    }
}
