package com.natsu.blog.config;

import com.natsu.blog.config.properties.UploadProperties;
import com.natsu.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private UploadProperties uploadProperties;

    //跨域配置，因为前后端端口不同，需要允许前端服务器访问后端端口
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**").allowedOrigins("*");
    }

    //使拦截器生效
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/test");
    }

    //静态资源映射
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadProperties.getAccessPath()).addResourceLocations(uploadProperties.getResourcesLocations());
    }
}
