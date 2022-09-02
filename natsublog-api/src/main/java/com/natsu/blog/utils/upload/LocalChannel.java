package com.natsu.blog.utils.upload;

import com.natsu.blog.config.properties.BlogProperties;
import com.natsu.blog.config.properties.UploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;


@Lazy
@Component
/*本地存储方式*/
public class LocalChannel{

    @Autowired
    private BlogProperties blogProperties;

    @Autowired
    private UploadProperties uploadProperties;

    /*将图片保存到本地，并返回访问本地图片的URL*/
    public String upload(ImageResource image) throws Exception {
        File folder = new File(uploadProperties.getPath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = UUID.randomUUID() + "." + image.getType();
        FileOutputStream fileOutputStream = new FileOutputStream(uploadProperties.getPath() + fileName);
        fileOutputStream.write(image.getData());
        fileOutputStream.close();
        return blogProperties.getApi() + "/image/" + fileName;
    }
}


