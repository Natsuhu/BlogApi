package com.natsu.blog.utils.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageResource {

    /**
     * 图片字节流数据
     * */
    byte[] data;

    /**
     * 图片类型
     * */
    String type;
}
