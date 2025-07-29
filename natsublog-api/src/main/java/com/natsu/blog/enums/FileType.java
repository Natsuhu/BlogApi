package com.natsu.blog.enums;

public enum FileType {

    SELF_UPLOAD("自主上传", "1"),
    QQ_AVATAR("QQ头像", "2"),
    ARTICLE_THUMBNAIL("文章封面", "3"),
    TEXT_EDITOR("富文本编辑器上传", "4"),
    MUSIC_FILE("音乐文件：音频，封面，歌词", "5");

    public String fileTypeName;

    public String fileTypeCode;

    FileType(String fileTypeName, String fileTypeCode) {
        this.fileTypeName = fileTypeName;
        this.fileTypeCode = fileTypeCode;
    }
}
