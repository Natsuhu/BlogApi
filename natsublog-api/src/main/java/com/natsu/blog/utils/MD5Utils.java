package com.natsu.blog.utils;

import cn.hutool.crypto.digest.DigestUtil;

public class MD5Utils {

    /**
     * 对比SHA256值，判断内容是否变化
     *
     * @param contentA 文本A
     * @param contentB 文本B
     * @return Boolean
     */
    public static Boolean checkContentChange(String contentA, String contentB) {
        String aSha256 = DigestUtil.sha256Hex(contentA);
        String bSha256 = DigestUtil.sha256Hex(contentB);
        return aSha256.equals(bSha256);
    }

}
