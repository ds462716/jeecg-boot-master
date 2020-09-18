-- add by zxh 2020年7月20日08:38:27 盘点优化start

ALTER TABLE `pd_product_stock_check_child`
ADD COLUMN `product_id`  varchar(64) NULL AFTER `check_no`,
ADD COLUMN `product_bar_code`  varchar(64) NULL COMMENT '产品条码' AFTER `product_id`,
ADD COLUMN `batch_no`  varchar(64) NULL COMMENT '批次' AFTER `product_bar_code`,
ADD COLUMN `exp_date`  datetime NULL COMMENT '有效期' AFTER `batch_no`,
ADD COLUMN `out_huowei_code`  varchar(255) NULL COMMENT '出库货位编号' AFTER `exp_date`;
-- add by zxh 2020年7月10日10:57:40 盘点优化
ALTER TABLE `pd_product_stock_check`
ADD COLUMN `audit_by`  varchar(64) NULL COMMENT '审核人' AFTER `check_status`,
ADD COLUMN `audit_date`  datetime NULL COMMENT '审核时间' AFTER `audit_by`,
ADD COLUMN `audit_status`  varchar(1) NULL COMMENT '审核状态 1-待审核； 2-审核通过； 3-已拒绝' AFTER `audit_date`,
ADD COLUMN `refuse_reason`  longtext NULL COMMENT '驳回原因' AFTER `audit_status`;
-- add by zxh 2020年7月15日08:55:29 盘点管理锁定状态
ALTER TABLE `pd_product_stock_check`
ADD COLUMN `locking_state`  varchar(1) NULL DEFAULT '0' COMMENT '锁定状态' AFTER `check_status`;
-- add by zxh 2020年7月15日08:55:29 加入操作的目标部门
ALTER TABLE `pd_product_stock_check`
ADD COLUMN `target_depart_id`  varchar(64) NULL COMMENT '盘点目标id' AFTER `check_no`;
INSERT INTO `sys_permission` VALUES ('1282613133895380993', '1218803434842820609', '入库审核按钮', null, null, null, null, '2', '0', 'stock:form:inRecordExamine', '2', '1.00', '0', null, '1', '1', '0', '0', null, 'admin', '2020-07-13 17:49:36', 'admin', '2020-07-13 18:52:42', '0', '0', '1', '0');
INSERT INTO `sys_permission` VALUES ('1282631475762888705', '1218803853975425025', '出库审核按钮', null, null, null, null, '2', '0', 'stock:form:outRecordExamine', '2', '1.00', '0', null, '1', '1', '0', '0', null, 'admin', '2020-07-13 19:02:29', null, '2020-07-13 19:02:29', '0', '0', '1', '0');
update `sys_permission` a set is_leaf = '0' where id = '1218803853975425025';
update `sys_permission` a set is_leaf = '0' where id = '1218803434842820609';

DROP TABLE IF EXISTS `pd_product_stock_check_permission`;
CREATE TABLE `pd_product_stock_check_permission` (
  `id` varchar(36) NOT NULL,
  `target_depart_id` varchar(64) DEFAULT NULL COMMENT '被操作的部门',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(64) DEFAULT NULL COMMENT '权限id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `sys_dict` VALUES ('1282958924639916034', '盘点锁定状态', 'locking_state', '', '0', 'admin', '2020-07-14 16:43:39', null, '2020-07-14 16:43:39', '0');
INSERT INTO `sys_dict_item` VALUES ('1282958991593590786', '1282958924639916034', '已锁定', '1', '', '1', '1', 'admin', '2020-07-14 16:43:54', null, '2020-07-14 16:43:54');
INSERT INTO `sys_dict_item` VALUES ('1282959020370710530', '1282958924639916034', '正常', '0', '', '1', '1', 'admin', '2020-07-14 16:44:01', null, '2020-07-14 16:44:01');
INSERT INTO `sys_permission` VALUES ('1282492599924731905', '1218784892172963842', '盘点审核', '/pd/PdProductStockCheckExamine', 'pd/PdProductStockCheckExamineList', null, null, '1', '0', null, '1', '3.10', '0', null, '1', '1', '0', '0', null, 'admin', '2020-07-13 09:50:38', 'admin', '2020-07-13 09:51:21', '0', '0', '1', '0');
INSERT INTO `sys_permission` VALUES ('1283567494771359746', '1239453470183882753', '退货审核按钮权限', null, null, null, null, '2', '0', 'stock:rejectedSave', '2', '1.00', '0', null, '1', '1', '0', '0', null, 'admin', '2020-07-16 09:01:53', 'admin', '2020-07-16 09:49:07', '0', '0', '1', '0');
-- add by zxh 2020年7月20日08:38:35 盘点优化end

INSERT INTO `sys_permission` VALUES ('1283650544821706754', '1210107255254798338', '供应商产品树', '/pd/pdSupplierRelevanceProduct', 'pd/query/PdSupplierRelevanceProduct', null, null, '1', '0', null, '1', '1.00', '0', null, '1', '1', '0', '0', null, 'admin', '2020-07-16 14:31:54', 'admin', '2020-07-16 15:36:44', '0', '0', '1', '1');
ALTER TABLE `pd_supplier`
ADD COLUMN `jde_code`  varchar(64) NULL COMMENT 'jde编号' AFTER `zdy`;
ALTER TABLE `pd_vender`
ADD COLUMN `jde_code`  varchar(64) NULL COMMENT 'jde编号' AFTER `zdy`;
ALTER TABLE `pd_product`
ADD COLUMN `biding_type`  varchar(1) NULL COMMENT '中标类型中标类型：（1：省标、2：市标 3：其他招标:4：备案）' AFTER `biding_number`,
ADD COLUMN `biding_price`  decimal(12,4) NULL COMMENT '中标价' AFTER `biding_type`;

-- add by zxh 2020年7月26日09:25:04 赣州加入财务报表
ALTER TABLE `pd_product`
ADD COLUMN `finance_classification`  varchar(1) NULL COMMENT '财务分类' AFTER `biding_price`;
INSERT INTO `sys_dict` VALUES ('1287184158196838401', '财务分类', 'finance_classification', 'I类 2:II类  3:III类', '0', 'admin', '2020-07-26 08:33:13', 'admin', '2020-07-26 08:33:39', '0');
INSERT INTO `sys_dict_item` VALUES ('1287184386887069698', '1287184158196838401', 'I类', '1', '', '1', '1', 'admin', '2020-07-26 08:34:07', 'admin', '2020-07-26 08:40:31');
INSERT INTO `sys_dict_item` VALUES ('1287184468852158465', '1287184158196838401', 'II类', '2', '', '1', '1', 'admin', '2020-07-26 08:34:27', 'admin', '2020-07-26 08:40:35');
INSERT INTO `sys_dict_item` VALUES ('1287184503417417729', '1287184158196838401', 'III类', '3', '', '1', '1', 'admin', '2020-07-26 08:34:35', 'admin', '2020-07-26 08:40:40');
-- ***************德兴医院部署截止2020年7月26日09:52:24

-- 物流追溯加入唯一码 2020年7月28日13:51:58
ALTER TABLE `pd_stock_log`
ADD COLUMN `ref_bar_code`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '唯一码' AFTER `product_bar_code`;

-- 医院编号2020年7月28日13:52:00
ALTER TABLE `sys_depart`
ADD COLUMN `hospital_code`  varchar(64) NULL COMMENT '医院编号' AFTER `depart_type`;
-- ***************德兴医院部署截止2020年7月29日10:46:32
-- 德兴医院部署截止2020年7月29日10:46:32

-- 九江医院加入省标码2020年8月3日11:24:10
ALTER TABLE `pd_product`
ADD COLUMN `dart_code`  varchar(64) NULL COMMENT '省标码' AFTER `jde_code`;

-- 赣州市立医院供应商用量统计查询
INSERT INTO `sys_permission` VALUES ('1293093425294729218', 'f0675b52d89100ee88472b6800754a08', '供应商用量使用统计报表', '/pd/report/RpSupplierUseReport', 'pd/report/RpSupplierUseReport', null, null, '1', '0', null, '1', '1.20', '0', null, '1', '1', '0', '0', null, 'admin', '2020-08-11 15:54:32', 'admin', '2020-08-11 15:54:44', '0', '0', '1', '0');

-- 库存统计报表
INSERT INTO `sys_permission` VALUES ('1295920023274373121', 'f0675b52d89100ee88472b6800754a08', '库存统计报表', '/pd/report/RpDepartStockReport', 'pd/report/RpDepartStockReport', null, null, '1', '0', null, '1', '1.00', '0', null, '1', '1', '0', '0', null, 'admin', '2020-08-19 11:06:25', null, '2020-08-19 11:06:25', '0', '0', '1', '0');

-- 科室用量统计
INSERT INTO `sys_permission` VALUES ('1295261514840997889', 'f0675b52d89100ee88472b6800754a08', '科室用量统计', '/pd/report/RpDepartUseReport', 'pd/report/RpDepartUseReport', null, null, '1', '0', null, '1', '1.30', '0', null, '1', '1', '0', '0', null, 'admin', '2020-08-17 15:29:45', 'admin', '2020-08-17 15:30:14', '0', '0', '1', '0');

-- 供应商试剂用量统计
INSERT INTO `sys_permission` VALUES ('1295987290456567810', 'f0675b52d89100ee88472b6800754a08', '供应商试剂用量使用统计报表', '/pd/report/RpSupplierReagentUseReport', 'pd/report/RpSupplierReagentUseReport', null, null, '1', '0', null, '1', '12.10', '0', null, '1', '1', '0', '0', null, 'admin', '2020-08-19 15:33:43', null, '2020-08-19 15:33:43', '0', '0', '1', '0');
-- 赣州肿瘤医院部署截止 2020年8月26日16:22:29

-- 赣州肿瘤医院产品价格对照
INSERT INTO `sys_permission` VALUES ('1302889584016056321', '1210107255254798338', '产品价格对照', '/external/ganzhouzhongliu/HisSynProductListGZZL', 'external/ganzhouzhongliu/HisSynProductListGZZL', null, null, '1', '0', null, '1', '1.51', '0', null, '1', '1', '0', '0', null, 'admin', '2020-09-07 16:40:58', 'admin', '2020-09-07 16:41:14', '0', '0', '1', '0');

-- 2020年9月18日11:44:07
INSERT INTO `sys_dict` VALUES ('1306795887557910530', '证照名称', 'license_name', '', '0', 'admin', '2020-09-18 11:23:13', null, '2020-09-18 11:23:13', '0');
INSERT INTO `sys_dict_item` VALUES ('1306795977416679425', '1306795887557910530', '营业执照', '营业执照', '', '1', '1', 'admin', '2020-09-18 11:23:35', null, '2020-09-18 11:23:35');
INSERT INTO `sys_dict_item` VALUES ('1306796040612257794', '1306795887557910530', '医疗器械经营许可证', '医疗器械经营许可证', '', '2', '1', 'admin', '2020-09-18 11:23:50', 'admin', '2020-09-18 11:28:52');
INSERT INTO `sys_dict_item` VALUES ('1306796090742579201', '1306795887557910530', '医疗器械生产许可证', '医疗器械生产许可证', '', '3', '1', 'admin', '2020-09-18 11:24:02', 'admin', '2020-09-18 11:28:57');
INSERT INTO `sys_dict_item` VALUES ('1306796118823444482', '1306795887557910530', '一类备案凭证', '一类备案凭证', '', '4', '1', 'admin', '2020-09-18 11:24:08', 'admin', '2020-09-18 11:29:05');
INSERT INTO `sys_dict_item` VALUES ('1306796203573551105', '1306795887557910530', '二类备案凭证', '二类备案凭证', '', '4', '1', 'admin', '2020-09-18 11:24:29', 'admin', '2020-09-18 11:29:10');
INSERT INTO `sys_dict_item` VALUES ('1306796225455235073', '1306795887557910530', '生产产品登记表', '生产产品登记表', '', '6', '1', 'admin', '2020-09-18 11:24:34', 'admin', '2020-09-18 11:29:29');
INSERT INTO `sys_dict_item` VALUES ('1306796245021663233', '1306795887557910530', '法人授权书', '法人授权书', '', '7', '1', 'admin', '2020-09-18 11:24:39', 'admin', '2020-09-18 11:29:35');
INSERT INTO `sys_dict_item` VALUES ('1306796266521665537', '1306795887557910530', '厂家授权书', '厂家授权书', '', '8', '1', 'admin', '2020-09-18 11:24:44', 'admin', '2020-09-18 11:29:39');
INSERT INTO `sys_dict_item` VALUES ('1306796284989186050', '1306795887557910530', '经销商授权书', '经销商授权书', '', '9', '1', 'admin', '2020-09-18 11:24:48', 'admin', '2020-09-18 11:29:52');
INSERT INTO `sys_dict_item` VALUES ('1306796302055809026', '1306795887557910530', '产品注册证', '产品注册证', '', '10', '1', 'admin', '2020-09-18 11:24:52', 'admin', '2020-09-18 11:29:56');
INSERT INTO `sys_dict_item` VALUES ('1306796324042350594', '1306795887557910530', '备案凭证', '备案凭证', '', '11', '1', 'admin', '2020-09-18 11:24:57', 'admin', '2020-09-18 11:30:01');
INSERT INTO `sys_dict_item` VALUES ('1306796362923548674', '1306795887557910530', '说明', '说明', '', '12', '1', 'admin', '2020-09-18 11:25:07', 'admin', '2020-09-18 11:30:17');
INSERT INTO `sys_dict_item` VALUES ('1306796385648287745', '1306795887557910530', '其他', '其他', '', '13', '1', 'admin', '2020-09-18 11:25:12', 'admin', '2020-09-18 11:30:20');