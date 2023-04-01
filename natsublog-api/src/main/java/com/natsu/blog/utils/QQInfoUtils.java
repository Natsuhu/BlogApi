package com.natsu.blog.utils;

import com.natsu.blog.utils.upload.ImageResource;
import com.natsu.blog.utils.upload.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@Component
public class QQInfoUtils {

    @Autowired
    private UploadUtils uploadUtils;

    private final String QQ_NICKNAME_URL = "https://r.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins={1}";
    private final String QQ_AVATAR_URL = "https://q.qlogo.cn/g?b=qq&nk=%s&s=100";

    /**
     * 获取QQ昵称
     */
    public String getQQNickname(String qq) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        String res = restTemplate.getForObject(QQ_NICKNAME_URL, String.class, qq);
        byte[] bytes = res.getBytes("iso-8859-1");
        String nickname = new String(bytes, "gb18030").split(",")[6].replace("\"", "");
        if ("".equals(nickname)) {
            return "nickname";
        }
        return nickname;
    }

    /**
     * 获取头像
     */
    private ImageResource getImageResourceByQQ(String qq) throws Exception {
        return uploadUtils.getImageByRequest(String.format(QQ_AVATAR_URL, qq));
    }

    /**
     * 上传头像，并获取头像URL
     */
    public String getQQAvatarUrl(String qq) throws Exception {
        return uploadUtils.upload(getImageResourceByQQ(qq));
    }

    /**
     * 判断是否为QQ
     */
    public boolean isQQNumber(String number) {
        return number.matches("^[1-9][0-9]{4,10}$");
    }
}
