/*
Navicat MySQL Data Transfer
Source Server         : MySQL
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : test
Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001
Date: 2018-11-30 19:35:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
/**
CREATE TABLE `account` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `name` varchar(20) NOT NULL,
                           `money` double DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

*/

CREATE TABLE `account` (
                         `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                         `name` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
                         `money` int(26) NOT NULL DEFAULT '0' COMMENT '钱',
                         `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                         `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`),
                         KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=551 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', 'aaa', '1000');
INSERT INTO `account` VALUES ('2', 'bbb', '1000');
INSERT INTO `account` VALUES ('3', 'ccc', '1000');
--