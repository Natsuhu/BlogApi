package com.natsu.blog.enums;

public enum OperationTypeEnum {

    LOGIN("登录", 1),
    INSERT("新增", 2),
    UPDATE("更新", 3),
    DELETE("删除", 4),
    QUERY("查询", 5),
    UPLOAD("上传", 6),
    DOWNLOAD("下载", 7),
    EXEC("执行", 8);

    private final String operationTypeName;

    private final Integer operationTypeCode;

    OperationTypeEnum(String operationTypeName, Integer operationTypeCode) {
        this.operationTypeName = operationTypeName;
        this.operationTypeCode = operationTypeCode;
    }

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public Integer getOperationTypeCode() {
        return operationTypeCode;
    }
}
