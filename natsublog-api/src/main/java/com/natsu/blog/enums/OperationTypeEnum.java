package com.natsu.blog.enums;

/**
 * 操作类型枚举
 *
 * @author NatsuKaze
 * @since 2025/01/20
 */
public enum OperationTypeEnum {

    /**
     * 登录
     */
    LOGIN("登录", 1),
    /**
     * 新增
     */
    INSERT("新增", 2),
    /**
     * 更新
     */
    UPDATE("更新", 3),
    /**
     * 删除
     */
    DELETE("删除", 4),
    /**
     * 查询
     */
    QUERY("查询", 5),
    /**
     * 上传
     */
    UPLOAD("上传", 6),
    /**
     * 下载
     */
    DOWNLOAD("下载", 7),
    /**
     * 执行
     */
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
