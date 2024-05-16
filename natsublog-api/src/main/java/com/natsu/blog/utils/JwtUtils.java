package com.natsu.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {

    /**
     * 混淆字符串
     */
    private static final String jwtToken = "NatsuKaze@root";

    /**
     * Token过期时间
     */
    private static final Long defaultExpTime = 24 * 60 * 60 * 60 * 1000L;

    /**
     * 常规Token
     *
     * @param username username
     * @return String
     */
    public static String createToken(String username) {
        return Jwts.builder()
//                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken
                .signWith(SignatureAlgorithm.HS512, jwtToken)
                .setSubject(username)
                .claim("username", username) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + defaultExpTime))
                .compact();// 一天的有效时间
    }

    /**
     * 生成带角色权限的token
     *
     * @param username username
     * @param roles    roles
     * @return String
     */
    public static String createToken(String username, String roles) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtToken)
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + defaultExpTime))
                .compact();
    }

    public static Claims checkToken(String token) {
        Jws<Claims> parse = Jwts.parser().setSigningKey(jwtToken).parseClaimsJws(token);
        return parse.getBody();
    }

}
