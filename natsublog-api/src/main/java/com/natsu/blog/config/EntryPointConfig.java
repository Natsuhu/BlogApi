//package com.natsu.blog.config;
//
//import com.alibaba.fastjson.JSON;
//import com.natsu.blog.model.dto.Result;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * 未登录 拒绝访问
// * @author NatsuKaze
// * @since 20240508
// */
//@Component
//public class EntryPointConfig implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
//            throws IOException {
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        Result result = Result.fail(403, "请登录");
//        out.write(JSON.toJSONString(result));
//        out.flush();
//        out.close();
//    }
//
//}
