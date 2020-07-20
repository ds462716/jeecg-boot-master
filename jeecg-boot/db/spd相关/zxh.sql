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