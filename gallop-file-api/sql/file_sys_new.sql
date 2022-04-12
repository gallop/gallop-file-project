/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : file_sys_new

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2022-04-12 08:58:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `id` bigint(20) NOT NULL,
  `username` varchar(63) NOT NULL DEFAULT '' COMMENT '管理员名称',
  `password` varchar(63) NOT NULL DEFAULT '' COMMENT '管理员密码',
  `birthday` date DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL COMMENT '性别',
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `nick_name` varchar(63) DEFAULT NULL,
  `last_login_ip` varchar(63) DEFAULT '' COMMENT '最近一次登录IP地址',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近一次登录时间',
  `avatar` varchar(255) DEFAULT '''' COMMENT '头像图片',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `role_ids` varchar(127) DEFAULT '[]' COMMENT '角色列表',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态（字典 0正常 1冻结 2删除）',
  `admin_type` tinyint(4) DEFAULT '0' COMMENT '管理员类型（1超级管理员 0非管理员）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` VALUES ('1', 'admin', '$2a$10$P22hjdX4kNPkbn6wKfujhuvBdgkPGenmpdOOAgk4yBRG5juLG9wDG', '2021-11-11', '2', '39100782@qq.com', '15959202117', 'gallop5', '127.0.0.1', '2021-10-04 12:04:59', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '2019-05-18 14:12:49', '2021-10-04 12:04:59', '0', '[1]', '0', '1');
INSERT INTO `admin_user` VALUES ('2', 'ch_user', '$2a$10$P22hjdX4kNPkbn6wKfujhuvBdgkPGenmpdOOAgk4yBRG5juLG9wDG', '1999-06-01', '2', null, '15959202898', 'gallop2', '127.0.0.1', '2021-10-04 10:41:25', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '2020-04-24 16:04:25', '2021-10-04 10:41:25', '0', '[2]', '0', '0');
INSERT INTO `admin_user` VALUES ('1280709549107552257', 'test06', '$2a$10$m9X6U05CiEt5KZfCZjCBIeAVJW8/H2R2I/Vrje9BSp9A98K8IJfMm', '2021-11-21', '1', null, '150', 'test', '', null, '\'', null, null, '0', '[]', '1', '0');
INSERT INTO `admin_user` VALUES ('1480100914764308482', 'gallop', '$2a$10$bEeaRVa9ydxBSX.G/xo5xetlW82Ik5msd9HfWYkH8NuuhNmiSpGUS', '2020-12-01', '1', null, '13859202001', 'gallop', '', null, 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '2022-01-09 16:55:32', null, '0', '[2]', '0', '0');

-- ----------------------------
-- Table structure for file_storage
-- ----------------------------
DROP TABLE IF EXISTS `file_storage`;
CREATE TABLE `file_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(63) NOT NULL COMMENT '文件的唯一索引',
  `name` varchar(255) NOT NULL COMMENT '文件名',
  `type` varchar(20) NOT NULL COMMENT '文件类型',
  `size` int(11) NOT NULL COMMENT '文件大小',
  `url` varchar(255) DEFAULT NULL COMMENT '文件访问链接',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COMMENT='文件存储表';

-- ----------------------------
-- Records of file_storage
-- ----------------------------
INSERT INTO `file_storage` VALUES ('20', 'whale_logo.png', 'whale_logo.png', 'image/png', '5045', null, '2021-08-11 16:35:41', '2021-08-11 16:35:41', '0');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `permission` varchar(63) DEFAULT NULL COMMENT '权限',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', '1', '*', '2019-01-01 00:00:00', '2019-01-01 00:00:00', '0');
INSERT INTO `permission` VALUES ('2', '2', 'admin:sysRole:list', '2022-01-08 18:53:21', '2022-01-08 18:53:21', '0');
INSERT INTO `permission` VALUES ('3', '2', 'admin:user:list', '2022-01-08 18:53:21', '2022-01-08 18:53:21', '0');
INSERT INTO `permission` VALUES ('4', '2', 'admin:user:detail', '2022-01-08 18:53:21', '2022-01-08 18:53:21', '0');
INSERT INTO `permission` VALUES ('5', '2', 'admin:user:ownRole', '2022-01-08 18:53:21', '2022-01-08 18:53:21', '0');
INSERT INTO `permission` VALUES ('6', '2', 'admin:user:resetPwd', '2022-01-08 18:53:21', '2022-01-08 18:53:21', '0');
INSERT INTO `permission` VALUES ('7', '2', 'admin:user:edit', '2022-01-08 18:53:21', '2022-01-08 18:53:21', '0');
INSERT INTO `permission` VALUES ('8', '2', '我的文档', '2022-01-08 19:18:05', '2022-01-08 19:18:05', '0');
INSERT INTO `permission` VALUES ('9', '2', 'admin:fileManager:create', '2022-01-08 19:18:05', '2022-01-08 19:18:05', '0');
INSERT INTO `permission` VALUES ('10', '2', 'admin:fileManager:list', '2022-01-08 19:18:05', '2022-01-08 19:18:05', '0');
INSERT INTO `permission` VALUES ('11', '2', 'admin:fileManager:operate', '2022-01-08 19:18:05', '2022-01-08 19:18:05', '0');
INSERT INTO `permission` VALUES ('12', '2', 'admin:fileManager:uploadMino', '2022-01-08 19:18:05', '2022-01-08 19:18:05', '0');
INSERT INTO `permission` VALUES ('13', '2', 'admin:fileManager:download', '2022-01-08 19:18:05', '2022-01-08 19:18:05', '0');
INSERT INTO `permission` VALUES ('14', '2', 'admin:fileManager:doPreview', '2022-01-08 19:18:05', '2022-01-08 19:18:05', '0');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(63) NOT NULL COMMENT '角色名称',
  `code` varchar(63) DEFAULT NULL,
  `remark` varchar(256) DEFAULT NULL COMMENT '角色描述',
  `sort` int(3) DEFAULT '1' COMMENT '排序',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（字典 0正常 1停用 2删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '超级管理员', 'super', '所有模块的权限', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00', '0');
INSERT INTO `role` VALUES ('2', '普通管理员', 'normal', '只有操作对象存储权限', '2', '2020-04-23 20:48:50', '2020-04-23 20:48:50', '0');
INSERT INTO `role` VALUES ('3', '文档浏览', 'fileView', '只查看文档。', '100', null, null, '0');

-- ----------------------------
-- Table structure for root_folder
-- ----------------------------
DROP TABLE IF EXISTS `root_folder`;
CREATE TABLE `root_folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `file_id` varchar(255) DEFAULT NULL COMMENT '文档id',
  `add_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(4) DEFAULT '0' COMMENT '状态（字典 0正常 1冻结 2删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='用于存储每个用户的根目录文件夹';

-- ----------------------------
-- Records of root_folder
-- ----------------------------
INSERT INTO `root_folder` VALUES ('1', '1', '61231c4b559d6043d620189e', '2022-01-03 18:19:05', '2022-01-03 18:19:08', '0');
INSERT INTO `root_folder` VALUES ('2', '1', '6123594965ef014f4b3f1284', '2022-01-03 18:19:27', '2022-01-03 18:19:30', '0');
INSERT INTO `root_folder` VALUES ('3', '2', '61d97cbf579a703aa6bd757d', '2022-01-08 20:00:01', '2022-01-08 20:00:01', '0');
INSERT INTO `root_folder` VALUES ('4', '1480100914764308482', '61daa3549d5ba727849b5bc8', '2022-01-09 16:56:53', '2022-01-09 16:56:53', '0');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type_id` bigint(20) NOT NULL COMMENT '字典类型id',
  `value` text NOT NULL COMMENT '值',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `sort` int(11) NOT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) NOT NULL COMMENT '状态（字典 0正常 1停用 2删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统字典值表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES ('1265216536659087357', '1265216211667636234', '男', '1', '100', '男性', '0', '2020-04-01 10:23:29', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216536659087358', '1265216211667636234', '女', '2', '100', '女性', '0', '2020-04-01 10:23:49', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216536659087359', '1265216211667636234', '未知', '3', '100', '未知性别', '0', '2020-04-01 10:24:01', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216536659087361', '1265216211667636235', '默认常量', 'DEFAULT', '100', '默认常量，都以SNOWY_开头的', '0', '2020-04-14 23:25:45', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216536659087363', '1265216211667636235', '阿里云短信', 'ALIYUN_SMS', '100', '阿里云短信配置', '0', '2020-04-14 23:25:45', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216536659087364', '1265216211667636235', '腾讯云短信', 'TENCENT_SMS', '100', '腾讯云短信', '0', '2020-04-14 23:25:45', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216536659087365', '1265216211667636235', '邮件配置', 'EMAIL', '100', '邮箱配置', '0', '2020-04-14 23:25:45', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216536659087366', '1265216211667636235', '文件上传路径', 'FILE_PATH', '100', '文件上传路径', '0', '2020-04-14 23:25:45', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216536659087367', '1265216211667636235', 'Oauth配置', 'OAUTH', '100', 'Oauth配置', '0', '2020-04-14 23:25:45', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216617500102656', '1265216211667636226', '正常', '0', '100', '正常', '0', '2020-05-26 17:41:44', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216617500102657', '1265216211667636226', '停用', '1', '100', '停用', '0', '2020-05-26 17:42:03', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265216938389524482', '1265216211667636226', '删除', '2', '100', '删除', '0', '2020-05-26 17:43:19', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265217669028892673', '1265217074079453185', '否', 'N', '100', '否', '0', '2020-05-26 17:46:14', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265217706584690689', '1265217074079453185', '是', 'Y', '100', '是', '0', '2020-05-26 17:46:23', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265220776437731330', '1265217846770913282', '登录', '1', '100', '登录', '0', '2020-05-26 17:58:34', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265220806070489090', '1265217846770913282', '登出', '2', '100', '登出', '0', '2020-05-26 17:58:41', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265221129564573697', '1265221049302372354', '目录', '0', '100', '目录', '0', '2020-05-26 17:59:59', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265221163119005697', '1265221049302372354', '菜单', '1', '100', '菜单', '0', '2020-05-26 18:00:07', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265221188091891713', '1265221049302372354', '按钮', '2', '100', '按钮', '0', '2020-05-26 18:00:13', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265466389204967426', '1265466149622128641', '未发送', '0', '100', '未发送', '0', '2020-05-27 10:14:33', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265466432670539778', '1265466149622128641', '发送成功', '1', '100', '发送成功', '0', '2020-05-27 10:14:43', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265466486097584130', '1265466149622128641', '发送失败', '2', '100', '发送失败', '0', '2020-05-27 10:14:56', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265466530477514754', '1265466149622128641', '失效', '3', '100', '失效', '0', '2020-05-27 10:15:07', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265466835009150978', '1265466752209395713', '无', '0', '100', '无', '0', '2020-05-27 10:16:19', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265466874758569986', '1265466752209395713', '组件', '1', '100', '组件', '0', '2020-05-27 10:16:29', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265466925476093953', '1265466752209395713', '内链', '2', '100', '内链', '0', '2020-05-27 10:16:41', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265466962209808385', '1265466752209395713', '外链', '3', '100', '外链', '0', '2020-05-27 10:16:50', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265467428423475202', '1265467337566461954', '系统权重', '1', '100', '系统权重', '0', '2020-05-27 10:18:41', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265467503090475009', '1265467337566461954', '业务权重', '2', '100', '业务权重', '0', '2020-05-27 10:18:59', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265468138431062018', '1265468028632571905', '全部数据', '1', '100', '全部数据', '0', '2020-05-27 10:21:30', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265468194928336897', '1265468028632571905', '本部门及以下数据', '2', '100', '本部门及以下数据', '0', '2020-05-27 10:21:44', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265468241992622082', '1265468028632571905', '本部门数据', '3', '100', '本部门数据', '0', '2020-05-27 10:21:55', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265468273634451457', '1265468028632571905', '仅本人数据', '4', '100', '仅本人数据', '0', '2020-05-27 10:22:02', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265468302046666753', '1265468028632571905', '自定义数据', '5', '100', '自定义数据', '0', '2020-05-27 10:22:09', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265468508100239362', '1265468437904367618', 'app', '1', '100', 'app', '0', '2020-05-27 10:22:58', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265468543433056258', '1265468437904367618', 'pc', '2', '100', 'pc', '0', '2020-05-27 10:23:07', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1265468576874242050', '1265468437904367618', '其他', '3', '100', '其他', '0', '2020-05-27 10:23:15', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617233011335170', '1275617093517172738', '其它', '0', '100', '其它', '0', '2020-06-24 10:30:23', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617295355469826', '1275617093517172738', '增加', '1', '100', '增加', '0', '2020-06-24 10:30:38', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617348610547714', '1275617093517172738', '删除', '2', '100', '删除', '0', '2020-06-24 10:30:50', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617395515449346', '1275617093517172738', '编辑', '3', '100', '编辑', '0', '2020-06-24 10:31:02', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617433612312577', '1275617093517172738', '更新', '4', '100', '更新', '0', '2020-06-24 10:31:11', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617472707420161', '1275617093517172738', '查询', '5', '100', '查询', '0', '2020-06-24 10:31:20', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617502973517826', '1275617093517172738', '详情', '6', '100', '详情', '0', '2020-06-24 10:31:27', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617536959963137', '1275617093517172738', '树', '7', '100', '树', '0', '2020-06-24 10:31:35', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617619524837377', '1275617093517172738', '导入', '8', '100', '导入', '0', '2020-06-24 10:31:55', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617651816783873', '1275617093517172738', '导出', '9', '100', '导出', '0', '2020-06-24 10:32:03', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617683475390465', '1275617093517172738', '授权', '10', '100', '授权', '0', '2020-06-24 10:32:10', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617709928865793', '1275617093517172738', '强退', '11', '100', '强退', '0', '2020-06-24 10:32:17', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617739091861505', '1275617093517172738', '清空', '12', '100', '清空', '0', '2020-06-24 10:32:23', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1275617788601425921', '1275617093517172738', '修改状态', '13', '100', '修改状态', '0', '2020-06-24 10:32:35', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1277774590944317441', '1277774529430654977', '阿里云', '1', '100', '阿里云', '0', '2020-06-30 09:22:57', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1277774666055913474', '1277774529430654977', '腾讯云', '2', '100', '腾讯云', '0', '2020-06-30 09:23:15', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1277774695168577538', '1277774529430654977', 'minio', '3', '100', 'minio', '0', '2020-06-30 09:23:22', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1277774726835572737', '1277774529430654977', '本地', '4', '100', '本地', '0', '2020-06-30 09:23:29', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1278607123583868929', '1278606951432855553', '运行', '1', '100', '运行', '0', '2020-07-02 16:31:08', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1278607162943217666', '1278606951432855553', '停止', '2', '100', '停止', '0', '2020-07-02 16:31:18', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1278939265862004738', '1278911800547147777', '通知', '1', '100', '通知', '0', '2020-07-03 14:30:57', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1278939319922388994', '1278911800547147777', '公告', '2', '100', '公告', '0', '2020-07-03 14:31:10', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1278939399001796609', '1278911952657776642', '草稿', '0', '100', '草稿', '0', '2020-07-03 14:31:29', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1278939432686252034', '1278911952657776642', '发布', '1', '100', '发布', '0', '2020-07-03 14:31:37', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1278939458804183041', '1278911952657776642', '撤回', '2', '100', '撤回', '0', '2020-07-03 14:31:43', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1278939485878415362', '1278911952657776642', '删除', '3', '100', '删除', '0', '2020-07-03 14:31:50', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1291390260160299009', '1291390159941599233', '是', 'true', '100', '是', '2', '2020-08-06 23:06:46', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1291390315437031426', '1291390159941599233', '否', 'false', '100', '否', '2', '2020-08-06 23:06:59', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1342446007168466945', '1342445962104864770', '下载压缩包', '1', '100', '下载压缩包', '0', '2020-12-25 20:24:04', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1342446035433881601', '1342445962104864770', '生成到本项目', '2', '100', '生成到本项目', '0', '2020-12-25 20:24:11', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358094655567454210', '1358094419419750401', '输入框', 'input', '100', '输入框', '0', '2021-02-07 00:46:13', '1265476890672672808', '2021-02-08 01:01:28', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358094740510498817', '1358094419419750401', '时间选择', 'datepicker', '100', '时间选择', '0', '2021-02-07 00:46:33', '1265476890672672808', '2021-02-08 01:04:07', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358094793149014017', '1358094419419750401', '下拉框', 'select', '100', '下拉框', '0', '2021-02-07 00:46:46', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358095496009506817', '1358094419419750401', '单选框', 'radio', '100', '单选框', '0', '2021-02-07 00:49:33', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358095673084633090', '1358094419419750401', '开关', 'switch', '100', '开关', '2', '2021-02-07 00:50:15', '1265476890672672808', '2021-02-11 19:07:18', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358458689433190402', '1358457818733428737', '等于', 'eq', '1', '等于', '0', '2021-02-08 00:52:45', '1265476890672672808', '2021-02-13 23:35:36', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358458785168179202', '1358457818733428737', '模糊', 'like', '2', '模糊', '0', '2021-02-08 00:53:08', '1265476890672672808', '2021-02-13 23:35:46', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358460475682406401', '1358094419419750401', '多选框', 'checkbox', '100', '多选框', '0', '2021-02-08 00:59:51', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358460819019743233', '1358094419419750401', '数字输入框', 'inputnumber', '100', '数字输入框', '0', '2021-02-08 01:01:13', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358470210267725826', '1358470065111252994', 'Long', 'Long', '100', 'Long', '0', '2021-02-08 01:38:32', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358470239351029762', '1358470065111252994', 'String', 'String', '100', 'String', '0', '2021-02-08 01:38:39', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358470265640927233', '1358470065111252994', 'Date', 'Date', '100', 'Date', '0', '2021-02-08 01:38:45', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358470300168437761', '1358470065111252994', 'Integer', 'Integer', '100', 'Integer', '0', '2021-02-08 01:38:53', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358470697377415169', '1358470065111252994', 'boolean', 'boolean', '100', 'boolean', '0', '2021-02-08 01:40:28', '1265476890672672808', '2021-02-08 01:40:47', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358471133434036226', '1358470065111252994', 'int', 'int', '100', 'int', '0', '2021-02-08 01:42:12', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358471188291338241', '1358470065111252994', 'double', 'double', '100', 'double', '0', '2021-02-08 01:42:25', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1358756511688761346', '1358457818733428737', '大于', 'gt', '3', '大于', '0', '2021-02-08 20:36:12', '1265476890672672808', '2021-02-13 23:45:24', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358756547159990274', '1358457818733428737', '小于', 'lt', '4', '大于', '0', '2021-02-08 20:36:20', '1265476890672672808', '2021-02-13 23:45:29', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358756609990664193', '1358457818733428737', '不等于', 'ne', '7', '不等于', '0', '2021-02-08 20:36:35', '1265476890672672808', '2021-02-13 23:45:46', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358756685030957057', '1358457818733428737', '大于等于', 'ge', '5', '大于等于', '0', '2021-02-08 20:36:53', '1265476890672672808', '2021-02-13 23:45:35', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1358756800525312001', '1358457818733428737', '小于等于', 'le', '6', '小于等于', '0', '2021-02-08 20:37:20', '1265476890672672808', '2021-02-13 23:45:40', '1265476890672672808');
INSERT INTO `sys_dict_data` VALUES ('1360529773814083586', '1358094419419750401', '文本域', 'textarea', '100', '文本域', '0', '2021-02-13 18:02:30', '1265476890672672808', null, null);
INSERT INTO `sys_dict_data` VALUES ('1360606105914732545', '1358457818733428737', '不为空', 'isNotNull', '8', '不为空', '0', '2021-02-13 23:05:49', '1265476890672672808', '2021-02-13 23:45:50', '1265476890672672808');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `sort` int(11) NOT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) NOT NULL COMMENT '状态（字典 0正常 1停用 2删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES ('1265216211667636226', '通用状态', 'common_status', '100', '通用状态', '0', '2020-05-26 17:40:26', '1265476890672672808', '2020-06-08 11:31:47', '1265476890672672808');
INSERT INTO `sys_dict_type` VALUES ('1265216211667636234', '性别', 'sex', '100', '性别字典', '0', '2020-04-01 10:12:30', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1265216211667636235', '常量的分类', 'consts_type', '100', '常量的分类，用于区别一组配置', '0', '2020-04-14 23:24:13', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1265217074079453185', '是否', 'yes_or_no', '100', '是否', '0', '2020-05-26 17:43:52', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1265217846770913282', '访问类型', 'vis_type', '100', '访问类型', '0', '2020-05-26 17:46:56', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1265221049302372354', '菜单类型', 'menu_type', '100', '菜单类型', '0', '2020-05-26 17:59:39', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1265466149622128641', '发送类型', 'send_type', '100', '发送类型', '0', '2020-05-27 10:13:36', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1265466752209395713', '打开方式', 'open_type', '100', '打开方式', '0', '2020-05-27 10:16:00', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1265467337566461954', '菜单权重', 'menu_weight', '100', '菜单权重', '0', '2020-05-27 10:18:19', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1265468028632571905', '数据范围类型', 'data_scope_type', '100', '数据范围类型', '0', '2020-05-27 10:21:04', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1265468437904367618', '短信发送来源', 'sms_send_source', '100', '短信发送来源', '0', '2020-05-27 10:22:42', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1275617093517172738', '操作类型', 'op_type', '100', '操作类型', '0', '2020-06-24 10:29:50', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1277774529430654977', '文件存储位置', 'file_storage_location', '100', '文件存储位置', '0', '2020-06-30 09:22:42', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1278606951432855553', '运行状态', 'run_status', '100', '定时任务运行状态', '0', '2020-07-02 16:30:27', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1278911800547147777', '通知公告类型', 'notice_type', '100', '通知公告类型', '0', '2020-07-03 12:41:49', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1278911952657776642', '通知公告状态', 'notice_status', '100', '通知公告状态', '0', '2020-07-03 12:42:25', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1291390159941599233', '是否boolean', 'yes_true_false', '100', '是否boolean', '2', '2020-08-06 23:06:22', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1342445962104864770', '代码生成方式', 'code_gen_create_type', '100', '代码生成方式', '0', '2020-12-25 20:23:53', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1358094419419750401', '代码生成作用类型', 'code_gen_effect_type', '100', '代码生成作用类型', '0', '2021-02-07 00:45:16', '1265476890672672808', '2021-02-08 00:47:48', '1265476890672672808');
INSERT INTO `sys_dict_type` VALUES ('1358457818733428737', '代码生成查询类型', 'code_gen_query_type', '100', '代码生成查询类型', '0', '2021-02-08 00:49:18', '1265476890672672808', null, null);
INSERT INTO `sys_dict_type` VALUES ('1358470065111252994', '代码生成java类型', 'code_gen_java_type', '100', '代码生成java类型', '0', '2021-02-08 01:37:57', '1265476890672672808', null, null);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin` varchar(45) DEFAULT NULL COMMENT '管理员',
  `ip` varchar(45) DEFAULT NULL COMMENT '登入ip',
  `type` int(11) DEFAULT NULL COMMENT '操作分类',
  `action` varchar(45) DEFAULT NULL COMMENT '操作动作',
  `status` tinyint(1) DEFAULT NULL COMMENT '操作状态',
  `result` varchar(127) DEFAULT NULL COMMENT '操作结果，或者成功消息，或者失败消息',
  `comment` varchar(255) DEFAULT NULL COMMENT '补充信息',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
