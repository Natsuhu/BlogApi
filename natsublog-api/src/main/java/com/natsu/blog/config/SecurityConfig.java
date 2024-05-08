//package com.natsu.blog.config;
//
//import com.natsu.blog.config.properties.BlogProperties;
//import com.natsu.blog.service.impl.UserServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @Autowired
//    private EntryPointConfig entryPointConfig;
//
//    @Autowired
//    private BlogProperties blogProperties;
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                //禁用 csrf 防御
//                .csrf().disable()
//                //开启跨域支持
//                .cors().and()
//                //基于Token，不创建会话
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                //放行获取网页标题后缀的请求
//                .antMatchers("/admin/webTitleSuffix").permitAll()
//                //任何 /admin 开头的路径下的请求都需要经过JWT验证
//                .antMatchers("/admin/**").hasAnyRole("admin", "visitor")
//                .antMatchers("/admin/**").hasRole("admin")
//                //其它路径全部放行
//                .anyRequest().permitAll()
//                .and()
//                //自定义JWT过滤器
//                .addFilterBefore(new JwtLoginFilter("/admin/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
//                //未登录时，返回json，在前端执行重定向
//                .exceptionHandling().authenticationEntryPoint(entryPointConfig);
//    }
//
////    @Bean
////    CorsConfigurationSource corsConfigurationSource() {
////        List<String> allowOrigin = new ArrayList<>();
////        allowOrigin.add(blogProperties.getMs());
////
////        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowedOrigins(allowOrigin); // 允许的来源
////        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许的方法
////        configuration.setAllowedHeaders(Arrays.asList("*")); // 允许的请求头
////        configuration.setAllowCredentials(true); // 是否允许携带cookie
////        configuration.setMaxAge(3600L); // 预检请求的结果缓存时间（秒）
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/admin/**", configuration); // 对Admin路径应用CORS配置
////        return source;
////    }
//
//}
