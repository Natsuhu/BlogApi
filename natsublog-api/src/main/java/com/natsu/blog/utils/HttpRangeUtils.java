package com.natsu.blog.utils;

import cn.hutool.http.HttpException;

public class HttpRangeUtils {

    private static final String RANGE_BYTES = "bytes=";

    public static Long getRangeStart(String range) {
        String cacheA = range.replaceAll(RANGE_BYTES, "");
        if (cacheA.contains(",") || !cacheA.contains("-")) {
            throw new HttpException("Range格式错误");
        }
        int cacheB = cacheA.indexOf("-");
        String start = cacheA.substring(0, cacheB);
        try {
            return Long.parseLong(start);
        } catch (Exception e) {
            throw new HttpException("Range格式错误");
        }
    }

    public static Long getRangeEnd(String range) {
        String cacheA = range.replaceAll(RANGE_BYTES, "");
        if (cacheA.contains(",") || !cacheA.contains("-")) {
            throw new HttpException("Range格式错误");
        }
        int cacheB = cacheA.indexOf("-");
        String end = cacheA.substring(cacheB + 1);
        try {
            return Long.parseLong(end);
        } catch (Exception e) {
            throw new HttpException("Range格式错误");
        }
    }

    public static Long getRangeLength(String range) {
        String cacheA = range.replaceAll(RANGE_BYTES, "");
        if (cacheA.contains(",") || !cacheA.contains("-")) {
            throw new HttpException("Range格式错误");
        }
        int cacheB = cacheA.indexOf("-");
        String start = cacheA.substring(0, cacheB);
        String end = cacheA.substring(cacheB + 1);
        try {
            long startLong = Long.parseLong(start);
            long endLong = Long.parseLong(end);
            return endLong - startLong + 1;
        } catch (Exception e) {
            throw new HttpException("Range格式错误");
        }
    }

}
