package com.natsu.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String jwtToken = "NatsuKaze@root";

    public static String createToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        JwtBuilder jwtBuilder = Jwts.builder()
//                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken
                .signWith(SignatureAlgorithm.HS512, jwtToken)
                .setClaims(claims) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000L));// 一天的有效时间
        return jwtBuilder.compact();
    }

    public static Map<String, Object> checkToken(String token) {
        try {
            Jwt<?, ?> parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * 生成带角色权限的token
//     *
//     * @param subject subject
//     * @param authorities auth
//     * @return String
//     */
//    public static String createToken(String subject, Collection<? extends GrantedAuthority> authorities) {
//        StringBuilder sb = new StringBuilder();
//        for (GrantedAuthority authority : authorities) {
//            sb.append(authority.getAuthority());
//        }
//        String jwt = Jwts.builder()
//                .setSubject(subject)
//                .claim("authorities", sb)
//                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000L))
//                .signWith(SignatureAlgorithm.HS512, jwtToken)
//                .compact();
//        return jwt;
//    }
//
//    /**
//     * 获取tokenBody同时校验token是否有效（无效则会抛出异常）
//     *
//     * @param token token
//     * @return Claims
//     */
//    public static Claims getTokenBody(String token) {
//        return Jwts.parser().setSigningKey(jwtToken).parseClaimsJws(token.replace("Bearer", "")).getBody();
//    }

}
