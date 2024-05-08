//package com.natsu.blog.config;
//
//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.natsu.blog.model.dto.Result;
//import com.natsu.blog.model.entity.User;
//import com.natsu.blog.utils.JwtUtils;
//import org.springframework.security.authentication.AccountExpiredException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.CredentialsExpiredException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.LockedException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import javax.servlet.FilterChain;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * JWT登录过滤器
// *
// * @author NatsuKaze
// * @since 2024-05-08
// */
//public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    ThreadLocal<String> currentUsername = new ThreadLocal<>();
//
//    protected JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
//        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
//        setAuthenticationManager(authenticationManager);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        try {
//            if (!"POST".equals(request.getMethod())) {
//                throw new RuntimeException("请求方法错误");
//            }
//            User user = objectMapper.readValue(request.getInputStream(), User.class);
//            currentUsername.set(user.getUsername());
//            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setContentType("application/json;charset=utf-8");
//            Result result = Result.fail(400, "非法请求");
//            PrintWriter out = response.getWriter();
//            out.write(JSON.toJSONString(result));
//            out.flush();
//            out.close();
//        }
//        return null;
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            FilterChain chain, Authentication authResult) throws IOException {
//        String jwt = JwtUtils.createToken(authResult.getName(), authResult.getAuthorities());
//        response.setContentType("application/json;charset=utf-8");
//        User user = (User) authResult.getPrincipal();
//        user.setPassword(null);
//        Map<String, Object> map = new HashMap<>();
//        map.put("user", user);
//        map.put("token", jwt);
//        Result result = Result.success(map);
//        PrintWriter out = response.getWriter();
//        out.write(JSON.toJSONString(result));
//        out.flush();
//        out.close();
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                              AuthenticationException exception) throws IOException {
//        response.setContentType("application/json;charset=utf-8");
//        String msg = exception.getMessage();
//        //登录不成功时，会抛出对应的异常
//        if (exception instanceof LockedException) {
//            msg = "账号被锁定";
//        } else if (exception instanceof CredentialsExpiredException) {
//            msg = "密码过期";
//        } else if (exception instanceof AccountExpiredException) {
//            msg = "账号过期";
//        } else if (exception instanceof DisabledException) {
//            msg = "账号被禁用";
//        } else if (exception instanceof BadCredentialsException) {
//            msg = "用户名或密码错误";
//        }
//        PrintWriter out = response.getWriter();
//        out.write(JSON.toJSONString(Result.fail(401, msg)));
//        out.flush();
//        out.close();
//    }
//}