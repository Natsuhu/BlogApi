//package com.natsu.blog.config;
//
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.natsu.blog.model.dto.Result;
//import com.natsu.blog.utils.JwtUtils;
//import io.jsonwebtoken.Claims;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//
//public class JwtFilter extends GenericFilterBean {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        //后台管理路径外的请求直接跳过
//        if (!request.getRequestURI().startsWith("/admin")) {
//            filterChain.doFilter(request, servletResponse);
//            return;
//        }
//        String jwt = request.getHeader("Authorization");
//        if (StrUtil.isNotBlank(jwt)) {
//            try {
//                Claims claims = JwtUtils.getTokenBody(jwt);
//                String username = claims.getSubject();
//                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
//                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                SecurityContextHolder.getContext().setAuthentication(token);
//            } catch (Exception e) {
//                response.setContentType("application/json;charset=utf-8");
//                Result result = Result.fail(403, "凭证已失效，请重新登录！");
//                PrintWriter out = response.getWriter();
//                out.write(JSON.toJSONString(result));
//                out.flush();
//                out.close();
//                return;
//            }
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
