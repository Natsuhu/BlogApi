package com.natsu.blog.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class QQInfoUtils {

    private static final String QQ_NICKNAME_URL = "https://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins={1}";
    private static final String QQ_AVATAR_URL = "https://q.qlogo.cn/g?b=qq&nk=%s&s=100";
    private static final RestTemplate restTemplate = new RestTemplate();

    /**
     * 判断是否为QQ
     */
    public static Boolean isQQNumber(String qq) {
        if (StringUtils.isNotBlank(qq)) {
            return qq.matches("^[1-9][0-9]{4,10}$");
        }
        return false;
    }

    /**
     * 获取QQ昵称
     */
    public static String getQQNickname(String qq) {
        if (!isQQNumber(qq)) {
            throw new RuntimeException("非标准格式QQ");
        }
        //发送请求
        String res = restTemplate.getForObject(QQ_NICKNAME_URL, String.class, qq);
        //处理结果
        if (StringUtils.isNotBlank(res)) {
            byte[] bytes = res.getBytes(Charset.forName("ISO_8859_1"));
            String nickname = new String(bytes, Charset.forName("GB18030")).split(",")[6].replace("\"", "");
            if (StringUtils.isBlank(nickname)) {
                return "null";
            }
            return nickname;
        }
        return "null";
    }

    /**
     * 获取头像
     */
    public static ImageResource getQQAvatar(String qq) {
        if (!isQQNumber(qq)) {
            throw new RuntimeException("非标准格式QQ");
        }
        //发送请求
        String url = String.format(QQ_AVATAR_URL, qq);
        ResponseEntity<byte[]> res = restTemplate.getForEntity(url, byte[].class);
        //处理响应
        if (res.getStatusCode().equals(HttpStatus.OK)) {
            byte[] data = res.getBody();
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            return new ImageResource(is, res.getHeaders().getContentType().getSubtype(), (long) data.length);
        }
        throw new RuntimeException("获取头像失败");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ImageResource {
        private InputStream inputStream;
        private String imgType;
        private Long imgSize;
    }

}
