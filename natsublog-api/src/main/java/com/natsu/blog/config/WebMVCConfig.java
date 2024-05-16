package com.natsu.blog.config;

import com.natsu.blog.interceptor.AccessLimitInterceptor;
import com.natsu.blog.interceptor.AdminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 *
 * @author NatsuKaze
 * @since 2023/1/19 新增注释
 */
@Configuration
@EnableWebMvc
public class WebMVCConfig implements WebMvcConfigurer {

    /**
     * 登录拦截器
     */
    @Autowired
    private AdminInterceptor adminInterceptor;

    /**
     * 限制访问拦截器
     */
    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    /**
     * 跨域配置，因为前后端端口不同，需要允许前端服务器访问后端端口
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    /**
     * 使拦截器生效
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor);
        registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**");
    }

    /**
     * 静态资源映射
     */
    //TODO 测试用，生产环境统一使用Nginx代理静态资源。2023/7/21 为测试博客文件管理框架，现关闭静态资源映射
/*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadProperties.getAccessPath()).addResourceLocations(uploadProperties.getResourcesLocations());
    }*/
}
