/*
 Navicat Premium Data Transfer

 Source Server         : 本机_123456
 Source Server Type    : MySQL
 Source Server Version : 50739
 Source Host           : localhost:3306
 Source Schema         : blog_ten

 Target Server Type    : MySQL
 Target Server Version : 50739
 File Encoding         : 65001

 Date: 19/07/2023 18:21:45
*/

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
  `size` bigint(20) UNSIGNED NOT NULL COMMENT '文件大小，单位byte',
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型，用于分类',
  `storage_type` int(20) NULL DEFAULT NULL COMMENT '存储类型。1.文件系统',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存储全路径',
  `extra` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '额外信息，用JSON存储，例如mp3的专辑信息等',
  `download_count` int(255) NULL DEFAULT NULL COMMENT '下载次数，统计在文件管理页面，点击下载按钮的次数',
  `create_time` datetime NOT NULL COMMENT '创建时间，对应文件上传时间',
  `update_time` datetime NOT NULL COMMENT '更新时间。',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
