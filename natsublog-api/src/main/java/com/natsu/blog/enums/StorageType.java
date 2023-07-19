package com.natsu.blog.enums;

/**
 * 附件存储类型枚举
 *
 * @author NatsuKaze
 * @date 2023/7/19
 */
public enum StorageType {

    LOCAL(1, "本地存储"),
    MINIO(2, "MinIO存储");

    private final Integer type;

    private final String description;

    StorageType(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
