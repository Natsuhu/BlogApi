package com.natsu.blog.utils;

import cn.hutool.crypto.digest.DigestUtil;

public class MD5Utils {

    public static Boolean checkContentChange(String contentA, String contentB) {
        String aSha256 = DigestUtil.sha256Hex(contentA);
        String bSha256 = DigestUtil.sha256Hex(contentB);
        return aSha256.equals(bSha256);
    }

}
