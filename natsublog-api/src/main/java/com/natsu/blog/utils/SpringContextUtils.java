package com.natsu.blog.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringContext工具，获取到Spring容器中的对象
 *
 * @author NatsuKaze
 * @since 2023/1/23
 * */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    /**
     * spring应用上下文
     * */
    private static ApplicationContext applicationContext;

    /**
     * setApplicationContext
     * */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * getBean
     * */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * getBean
     * */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * containsBean
     * */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * isSingleton
     * */
    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    /**
     * getType
     * */
    public static Class<? extends Object> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }
}
