package com.natsu.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    /**
     * 混淆字符串
     */
    private static String salt;

    /**
     * Token过期时间
     */
    private static Long expireTime;

    @Value("${token.salt:salt}")
    public void setSalt(String salt) {
        JwtUtils.salt = salt;
    }

    @Value("${token.expireTime:86400000}")
    public void setExpireTime(Long expireTime) {
        JwtUtils.expireTime = expireTime;
    }

    /**
     * 常规Token
     *
     * @param username username
     * @return String
     */
    public static String createToken(String username) {
        return Jwts.builder()
//                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken
                .signWith(SignatureAlgorithm.HS512, salt)
                .setSubject(username)
                .claim("username", username) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
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
                .signWith(SignatureAlgorithm.HS512, salt)
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .compact();
    }

    public static Claims checkToken(String token) {
        Jws<Claims> parse = Jwts.parser().setSigningKey(salt).parseClaimsJws(token);
        return parse.getBody();
    }

}
