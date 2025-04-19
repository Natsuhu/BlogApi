SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for annex
-- ----------------------------
DROP TABLE IF EXISTS `annex`;
CREATE TABLE `annex`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `reflect_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '映射ID，存储后的文件名',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件全名',
  `suffix` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件后缀名',
  `size` bigint UNSIGNED NOT NULL COMMENT '文件大小，单位byte',
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型，用于分类',
  `is_published` bit(1) NOT NULL COMMENT '是否公开，主要用于区分前台和后台文件下载',
  `storage_type` int NULL DEFAULT NULL COMMENT '存储类型。1.文件系统',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存储全路径',
  `extra` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '额外信息，用JSON存储，例如mp3的专辑信息等',
  `download_count` int NULL DEFAULT NULL COMMENT '下载次数，统计在文件管理页面，点击下载按钮的次数',
  `create_time` datetime NOT NULL COMMENT '创建时间，对应文件上传时间',
  `update_time` datetime NOT NULL COMMENT '更新时间。',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '文章标题',
  `thumbnail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '略缩图地址',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '文章内容',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '描述',
  `is_published` bit(1) NOT NULL COMMENT '是否公开',
  `is_recommend` bit(1) NOT NULL COMMENT '是否推荐',
  `is_appreciation` bit(1) NOT NULL COMMENT '是否开启赞赏',
  `is_comment_enabled` bit(1) NOT NULL COMMENT '是否允许评论',
  `is_top` bit(1) NOT NULL COMMENT '是否置顶',
  `create_time` datetime NOT NULL COMMENT '发表时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `edit_time` datetime NOT NULL COMMENT '文章内容最后编辑时间',
  `views` int NOT NULL COMMENT '查看数',
  `words` int NULL DEFAULT NULL COMMENT '字数',
  `read_time` int NULL DEFAULT NULL COMMENT '阅读时长',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `author_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '作者名字',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `category_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '评论时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `is_published` bit(1) NULL DEFAULT NULL COMMENT '公开或隐藏',
  `is_admin_comment` bit(1) NULL DEFAULT NULL COMMENT '是否管理员评论',
  `page` int NOT NULL COMMENT '1文章2友链3关于我',
  `reply_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '回复的人昵称',
  `article_id` bigint NULL DEFAULT NULL COMMENT '评论的文章ID',
  `origin_id` varchar(32) NULL DEFAULT NULL COMMENT '归属ID',
  `parent_comment_id` bigint NOT NULL COMMENT '父论ID',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '个人主页',
  `qq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'QQ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for friend
-- ----------------------------
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '昵称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '描述',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '网站地址',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '头像地址',
  `is_published` bit(1) NOT NULL COMMENT '公开',
  `click` int NOT NULL COMMENT '点击次数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for moment
-- ----------------------------
DROP TABLE IF EXISTS `moment`;
CREATE TABLE `moment`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `likes` int NULL DEFAULT NULL COMMENT '点赞数',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发表时间',
  `edit_time` datetime NOT NULL COMMENT '最后编辑时间',
  `is_published` bit(1) NOT NULL COMMENT '是否公开',
  `is_comment_enabled` bit(1) NOT NULL COMMENT '评论开关',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '作者名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法',
  `param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `type` int NULL DEFAULT NULL COMMENT '操作类型',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作描述',
  `status` int NULL DEFAULT NULL COMMENT '状态 0失败 1成功',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP归属地',
  `os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  `times` int NULL DEFAULT NULL COMMENT '请求耗时',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `user_agent` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for setting
-- ----------------------------
DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `setting_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设置key',
  `setting_value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '设置key',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `page` int NOT NULL COMMENT '0首页，1文章，2友链，3关于我',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签名称',
  `color` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签颜色',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tag_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键jobkey',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `type` int NULL DEFAULT NULL COMMENT '任务类型',
  `cron` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron',
  `param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '输入参数',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行目标',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  `current_count` int NULL DEFAULT NULL COMMENT '当前执行次数',
  `max_count` int NULL DEFAULT NULL COMMENT '最大执行次数',
  `next_time` datetime NULL DEFAULT NULL COMMENT '下次执行时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '到期时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for visit_log
-- ----------------------------
DROP TABLE IF EXISTS `visit_log`;
CREATE TABLE `visit_log`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客标识码',
  `visitor_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访客ID',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求接口',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `behavior` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问行为',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问内容',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  `times` int NOT NULL COMMENT '请求耗时（毫秒）',
  `create_time` datetime NOT NULL COMMENT '访问时间',
  `user_agent` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user-agent用户代理',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for visitor
-- ----------------------------
DROP TABLE IF EXISTS `visitor`;
CREATE TABLE `visitor`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客标识码',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `province` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  `pv` int NULL DEFAULT NULL COMMENT '访问页数统计',
  `create_time` datetime NOT NULL COMMENT '首次访问时间',
  `last_time` datetime NOT NULL COMMENT '最后访问时间',
  `user_agent` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user-agent用户代理',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_uuid`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Insert init data
-- ----------------------------
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (1, 'blogName', 'xxBlog', '博客名称', 0, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (2, 'webTitleSuffix', ' - xxBlog', '网页标题后缀', 0, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (3, 'cardAvatar', null, '资料卡头像', 5, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (4, 'cardName', 'xx', '资料卡名字', 5, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (5, 'cardSignature', '什么都没有~~', '资料卡个性签名', 5, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (6, 'friendContent', '欢迎交换链接', '文案', 2, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (7, 'friendIsComment', 'true', '是否允许评论', 2, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (8, 'aboutMusicId', null , '网易云音乐ID', 3, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (9, 'aboutContent', '关于我……', '文案', 3, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (10, 'aboutIsComment', 'true', '是否允许评论', 3, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (11, 'aboutMusicServer', 'netease', '关于我页面音乐服务器选择', 3, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (12, 'aboutTitle', '关于本站', '标题', 3, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (13, 'headerTitle', '本地调试' , '首图中的标题', 0, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (14, 'headerImage', null , '首图地址', 0, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (15, 'bodyImage', null , '博客整体背景图', 0, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (16, 'github', null , '资料卡Github', 5, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (17, 'qq', null , '资料卡QQ', 5, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (18, 'bilibili', null , '资料卡Bilibili', 5, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (19, 'netease', null , '资料卡网易云', 5, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (20, 'email', null , '资料卡邮箱', 5, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (21, 'adminCommentLabel', '博主', '博主评论标识', 0, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (22, 'frontLoginImage', null, '博客前台登录页背景图', 4, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (23, 'backLoginImage', null , '博客后台登录页背景图', 4, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (24, 'copyright', null , '页脚版权信息', 0, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (25, 'icpInfo', null , '页脚备案信息', 0, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (26, 'badgeList', null , '页脚徽标', 0, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (27, 'cardCustom', null , '资料卡自定义', 5, now(), now());
INSERT INTO `setting` (`id`, `setting_key`, `setting_value`, `remark`, `page`, `create_time`, `update_time`) VALUES (28, 'isDeleteCommentInDeleteArticle', 'false', '删除文章时，是否顺带删除文章下的评论', 99, now(), now());
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `avatar`, `email`, `role`, `create_time`, `update_time`) VALUES (1, 'admin', '$2a$10$VNkYnWM5DPVUDWthcO.Pz.k5k8j/7hQEPrUhwaU7uhNkjqLA3j7pi', '管理员', '1', 'xxx@xxx.com', 'admin', now(), now());
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `avatar`, `email`, `role`, `create_time`, `update_time`) VALUES (2, 'Visitor', '$2a$10$VNkYnWM5DPVUDWthcO.Pz.k5k8j/7hQEPrUhwaU7uhNkjqLA3j7pi', '访客', '1', 'xxx@xxx.com', 'visitor', now(), now());
