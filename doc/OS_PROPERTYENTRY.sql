/*
 Navicat Premium Data Transfer

 Source Server         : MySQL阿里云香港量化正式环境
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 172.17.0.136:13306
 Source Schema         : qutmtrade

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 22/12/2021 09:32:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for OS_PROPERTYENTRY
-- ----------------------------
DROP TABLE IF EXISTS `OS_PROPERTYENTRY`;
CREATE TABLE `OS_PROPERTYENTRY` (
  `ID` bigint(60) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `GLOBAL_KEY` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT 'GLOBAL_KEY',
  `ITEM_KEY` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT 'ITEM_KEY',
  `ITEM_TYPE` int(11) DEFAULT NULL,
  `STRING_VALUE` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `DATE_VALUE` datetime DEFAULT NULL,
  `DATA_VALUE` varchar(900) COLLATE utf8mb4_bin DEFAULT NULL,
  `FLOAT_VALUE` decimal(24,6) DEFAULT NULL,
  `NUMBER_VALUE` decimal(24,6) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `GLOBAL_ITEM_KEY` (`GLOBAL_KEY`,`ITEM_KEY`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='属性集合';

SET FOREIGN_KEY_CHECKS = 1;
