package com.natsu.blog.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //后台管理路径外的请求直接跳过
        if (!request.getRequestURI().startsWith("/admin")) {
            filterChain.doFilter(request, response);
            return;
        }
        //从请求体中获取Token，若没有则从请求参数中获取Token。主要是下载文件接口是使用的请求参数携带Token。
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        if (StrUtil.isNotBlank(token)) {
            try {
                //尝试从token解析出用户名和权限
                Claims claims = JwtUtils.checkToken(token);
                String username = claims.getSubject();
                //角色一定要带上ROLE_前缀，不然就会报403，这与配置类中的hasAnyRole方法有关。由于每个用户只有一个角色，无需遍历给每个加上ROLE
                List<GrantedAuthority> roles = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + claims.get("roles"));
                //存入上下文中
                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(username, null, roles);
                SecurityContextHolder.getContext().setAuthentication(userToken);
            } catch (Exception e) {
                response.setContentType("application/json;charset=utf-8");
                Result result = Result.fail(403, "凭证已失效，请重新登录！");
                PrintWriter out = response.getWriter();
                out.write(JSON.toJSONString(result));
                out.flush();
                out.close();
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
