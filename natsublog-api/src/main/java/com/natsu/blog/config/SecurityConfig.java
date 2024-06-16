package com.natsu.blog.config;

import com.natsu.blog.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EntryPointConfig entryPointConfig;

    /**
     * 配置密码匹配实现类
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        //要想自定义密码加密方式，写一个PasswordEncoder的实现类即可
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //禁用 csrf 防御
                .csrf().disable()
                //开启跨域支持
                .cors().and()
                //基于Token，不创建会话
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //任何 /admin 开头的路径下的请求都需要经过JWT验证
                .antMatchers("/admin/**").hasAnyRole("admin", "visitor")
                //其它路径全部放行
                .anyRequest().permitAll()
                .and()
                //自定义JWT过滤器
                .addFilterBefore(new JwtLoginFilter("/admin/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                //未登录时，返回json，在前端执行重定向
                .exceptionHandling().authenticationEntryPoint(entryPointConfig);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // 允许cookies跨域
        configuration.addAllowedOriginPattern("*"); // 允许所有源，实际生产中应具体指定来源
        configuration.addAllowedHeader("*"); // 允许所有头部
        configuration.addAllowedMethod("*"); // 允许所有方法
        configuration.addExposedHeader("Authorization"); // 暴露特定的header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 对所有URL应用此配置
        return source;
    }

}
