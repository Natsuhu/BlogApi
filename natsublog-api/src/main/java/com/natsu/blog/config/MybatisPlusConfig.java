package com.natsu.blog.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * MP配置
 *
 * @author NatsuKaze
 * @since 2023/1/19 新增注释
 */
@Configuration
@MapperScan("com.natsu.blog.mapper")
public class MybatisPlusConfig implements MetaObjectHandler {

    /**
     * mybatis的分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 自动注入create_time字段
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.setFieldValByName("createTime", date, metaObject);
        this.setFieldValByName("updateTime", date, metaObject);
    }


    /**
     * 自动注入update_time字段，不要开启此功能，会导致更新文章阅读数量时一并更新时间
     */
    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
