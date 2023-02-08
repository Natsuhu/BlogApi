package com.natsu.blog.utils.upload;

import com.natsu.blog.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class UploadUtils {

    @Autowired
    private LocalChannel localChannel;

    /**
     * 通过指定方式存储图片
     * */
    public String upload(ImageResource image) throws Exception {
        return localChannel.upload(image);
    }

    /**
     * 从网络获取图片数据
     * */
    public ImageResource getImageByRequest(String url) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
        if (Constants.IMAGE.equals(responseEntity.getHeaders().getContentType().getType())) {
            return new ImageResource(responseEntity.getBody(), responseEntity.getHeaders().getContentType().getSubtype());
        }
        throw new Exception("response contentType unlike image");
    }
}
