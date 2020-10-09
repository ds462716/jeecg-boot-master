/*
Navicat MySQL Data Transfer

Source Server         : 阿里云
Source Server Version : 50628
Source Host           : 119.23.56.26:3366
Source Database       : jeecg-boot

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2020-04-03 14:42:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `pd_depart_config`
-- ----------------------------

-- ----------------------------
-- Records of pd_depart_config
-- ----------------------------
INSERT INTO `pd_depart_config` VALUES ('1218732534235729922', '久存提醒时间', 'JCTXSJ', 'QDRSJU', '1', 'existRemind', '60', 'admin', '2020-01-19 11:10:54', 'zxh', '2020-03-10 09:51:55', 'A01', '以天为单位', null, null, '0', '0');
INSERT INTO `pd_depart_config` VALUES ('1218732877631787010', '库存近效期提醒时间', 'KCJXQTXSJ', 'YDRUARSJU', '2', 'stockNEP', '60', 'admin', '2020-01-19 11:12:16', 'admin', '2020-01-19 11:14:01', 'A01', '以天为单位', null, null, '0', '0');
INSERT INTO `pd_depart_config` VALUES ('1218733107349622785', '供应商近效期提醒时间', 'GYSJXQTXSJ', 'WYURUARSJU', '3', 'supplierNEP', '60', 'admin', '2020-01-19 11:13:10', 'admin', '2020-01-19 11:14:25', 'A01', '以天为单位', null, null, '0', '0');
INSERT INTO `pd_depart_config` VALUES ('1218733497470226434', '生产厂家近效期提醒时间', 'SCCJJXQTXSJ', 'TUDPRUARSJU', '4', 'venderNEP', '60', 'admin', '2020-01-19 11:14:43', 'admin', '2020-01-19 11:15:28', 'A01', '以天为单位', null, null, '0', '0');
