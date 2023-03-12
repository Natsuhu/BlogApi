package com.natsu.blog.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Result {

    private long totalPage;

    private long total;

    private boolean success;

    private int code;

    private String msg;

    private Object data;

    public static Result success(long totalPage, long total, Object data) {
        return new Result(totalPage, total, true, 200, "success", data);
    }

    public static Result success(Object data) {
        return new Result(0, 0, true, 200, "success", data);
    }

    public static Result fail(String msg) {
        return new Result(0, 0, false, 500, msg, null);
    }

    public static Result fail(int code, String msg) {
        return new Result(0, 0, false, code, msg, null);
    }
}
