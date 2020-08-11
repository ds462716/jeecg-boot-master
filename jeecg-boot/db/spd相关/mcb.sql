-- 2020年7月16日 17:52:59  增加补货类型静态字典
INSERT INTO `sys_dict` VALUES ('1283695545638080513', '补货类型', 'rep_type', '1:按库存上下限    2：按上月消耗量', 0, 'admin', '2020-07-16 17:30:43', NULL, '2020-07-16 17:30:43', 0);
INSERT INTO `sys_dict_item` VALUES ('1283695632250458114', '1283695545638080513', '按库存上下限', '1', '', 1, 1, 'admin', '2020-07-16 17:31:03', NULL, '2020-07-16 17:31:03');
INSERT INTO `sys_dict_item` VALUES ('1283695677897068545', '1283695545638080513', '按上月消耗量', '2', '', 2, 1, 'admin', '2020-07-16 17:31:14', NULL, '2020-07-16 17:31:14');
-- 2020年7月17日 17:52:59  增加补货类型字段
 ALTER TABLE `pd_purchase_order`
ADD COLUMN `rep_type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '补货类型' AFTER `purchase_type`;

-- 2020年7月17日 19:52:59  增加供应商类别
ALTER TABLE `pd_supplier`
ADD COLUMN `supplier_type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商分类：0:0类 1:I类 2:II类  3:III类  4:无证 5:未分配' AFTER `del_flag`;
-- 2020年7月17日 19:52:59  增加供应商类别静态字典
INSERT INTO `sys_dict` VALUES ('1284072131916816385', '供应商类别', 'supplier_type', '0:0类 1:I类 2:II类  3:III类  4:无证 5:未分配', 0, 'admin', '2020-07-17 18:27:08', NULL, '2020-07-17 18:27:08', 0);
INSERT INTO `sys_dict_item` VALUES ('1284072203886878722', '1284072131916816385', '0类', '0', '', 1, 1, 'admin', '2020-07-17 18:27:25', NULL, '2020-07-17 18:27:25');
INSERT INTO `sys_dict_item` VALUES ('1284072249055338497', '1284072131916816385', 'I类', '1', '', 2, 1, 'admin', '2020-07-17 18:27:36', NULL, '2020-07-17 18:27:36');
INSERT INTO `sys_dict_item` VALUES ('1284072293473017858', '1284072131916816385', 'II类', '2', '', 3, 1, 'admin', '2020-07-17 18:27:46', NULL, '2020-07-17 18:27:46');
INSERT INTO `sys_dict_item` VALUES ('1284072344844853250', '1284072131916816385', 'III类', '3', '', 4, 1, 'admin', '2020-07-17 18:27:59', NULL, '2020-07-17 18:27:59');
INSERT INTO `sys_dict_item`VALUES ('1284072389556133889', '1284072131916816385', '无证', '4', '', 5, 1, 'admin', '2020-07-17 18:28:09', NULL, '2020-07-17 18:28:09');
INSERT INTO `sys_dict_item` VALUES ('1284072432501612546', '1284072131916816385', '未分配', '5', '', 6, 1, 'admin', '2020-07-17 18:28:19', NULL, '2020-07-17 18:28:19');
-- 2020年7月18日 12:52:59  增加按钮权限
 INSERT INTO `sys_permission`VALUES ('1284313794308251650', '1218787201875513345', '自动生成采购计划按钮', NULL, NULL, NULL, NULL, 2, '0', 'purchase:form:autoAdd', '2', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-07-18 10:27:25', NULL, '2020-07-18 10:27:25', 0, 0, '1', 0);

-- 2020年7月19日 12:52:59  增加菜单
 INSERT INTO `sys_permission` VALUES ('1284699679533076481', '1218785597982052353', '供应商用量明细', '/pd/query/PdSupplierCountQuery', 'pd/query/PdSupplierCountQueryList', NULL, NULL, 1, '0', NULL, '1', 11.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-07-19 12:00:47', NULL, '2020-07-19 12:00:47', 0, 0, '1', 0);

-- 2020年7月21日 10:52:59  增加中标类型静态值
INSERT INTO `sys_dict` VALUES ('1285398280212221953', '中标类型', 'biding_type', '1：省标、2：市标 3：其他招标:4：备案', 0, 'admin', '2020-7-21 10:16:46', NULL, '2020-7-21 10:16:46', 0);
INSERT INTO `sys_dict_item` VALUES ('1285398363574013954', '1285398280212221953', '省标', '1', '', 1, 1, 'admin', '2020-7-21 10:17:06', NULL, '2020-7-21 10:17:06');
INSERT INTO `sys_dict_item` VALUES ('1285398403419901954', '1285398280212221953', '市标', '2', '', 2, 1, 'admin', '2020-7-21 10:17:16', NULL, '2020-7-21 10:17:16');
INSERT INTO `sys_dict_item` VALUES ('1285398448311537666', '1285398280212221953', '其他招标', '3', '', 3, 1, 'admin', '2020-7-21 10:17:26', NULL, '2020-7-21 10:17:26');
INSERT INTO `sys_dict_item` VALUES ('1285398487503114242', '1285398280212221953', '备案', '4', '', 4, 1, 'admin', '2020-7-21 10:17:36', NULL, '2020-7-21 10:17:36');
-- ***************德兴医院部署截止2020年7月26日09:52:24

-- 2020年7月31日 18:52:59  增加使用状态
ALTER TABLE  `pd_bottle_inf`
ADD COLUMN `status` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 0 COMMENT '使用状态' AFTER `instr_code`;
-- 2020年7月31日 18:52:59  修改字段类型
ALTER TABLE  `ex_inspection_items`
MODIFY COLUMN `test_item_cost` decimal(12, 4) NULL DEFAULT NULL COMMENT '检查项目费用' AFTER `test_item_code`;

INSERT INTO `sys_permission`  VALUES ('1289532174829174785', '1218785597982052353', '月消耗费用统计(市立)', '/pd/query/PdInspectionMonth', 'pd/query/PdInspectionMonthQuery', NULL, NULL, 1, '0', NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-08-01 20:03:23', 'admin', '2020-08-01 20:03:33', 0, 0, '1', 0);

-- 2020年8月4日 18:52:59  增加耗材/试剂使用统计报表
CREATE TABLE  `pd_numerical_inf`  (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `create_time` datetime(0) NULL COMMENT '创建时间',
  `create_by` varchar(50) NULL COMMENT '创建人',
  `update_time` datetime(0) NULL COMMENT '修改时间',
  `update_by` varchar(0) NULL COMMENT '修改人',
  `sys_org_code` varchar(64) NULL COMMENT '所属部门',
  `month` varchar(64) NULL COMMENT '统计月份',
  `depart_id` varchar(64) NULL COMMENT '所属科室',
  `sj_in_record_num` double(32, 2) NULL COMMENT '试剂入库数量',
  `sj_in_record_price` decimal(20, 2) NULL COMMENT '试剂入库金额',
  `sj_num` double(32, 2) NULL COMMENT '试剂使用数量',
  `sj_price` decimal(20, 2) NULL COMMENT '试剂使用金额',
  `sj_stock_num` double(32, 0) NULL COMMENT '试剂库存数量',
  `sj_stock_price` decimal(20, 2) NULL COMMENT '试剂库存金额',
  `hc_in_record_num` double(32, 0) NULL COMMENT '耗材入库数量',
  `hc_in_record_price` decimal(20, 2) NULL COMMENT '耗材入库金额',
  `hc_num` double(32, 0) NULL COMMENT '耗材使用数量',
  `hc_price` decimal(20, 2) NULL COMMENT '耗材使用金额',
  `hc_stock_num` double(32, 0) NULL COMMENT '耗材库存数量',
  `hc_stock_price` decimal(20, 2) NULL COMMENT '耗材库存金额',
  `item_price` decimal(20, 2) NULL COMMENT '检验项目收入金额',
  `item_num` double(32, 0) NULL COMMENT '检验项目总数量',
  `del_flag` varchar(4)  DEFAULT '0' COMMENT '删除标识，0-正常；1-删除',
  PRIMARY KEY (`id`)
);

INSERT INTO `sys_quartz_job` VALUES ('1290475878282895361', 'admin', '2020-08-04 10:33:20', 0, NULL, '2020-08-04 10:33:20', 'org.jeecg.modules.quartz.job.PdNumericalInfTaskJob', '* * * * * ? *', NULL, '根据月份定时更新试剂/耗材入库、消耗、库存数量及金额定时任务', -1);

-- 2020年8月5日 18:52:59  月統計費用表增加字段
ALTER TABLE `pd_numerical_inf`
ADD COLUMN `sj_jy_stock_num` double(32, 0) NULL COMMENT '上月结余试剂库存数量' AFTER `del_flag`,
ADD COLUMN `sj_jy_stock_price` decimal(20, 2) NULL COMMENT '上月结余试剂库存金额' AFTER `sj_jy_stock_num`,
ADD COLUMN `in_num` double(32, 0) NULL COMMENT '调入数量' AFTER `sj_jy_stock_price`,
ADD COLUMN `out_num` double(32, 0) NULL COMMENT '调出数量' AFTER `in_num`,
ADD COLUMN `hc_jy_stock_num` double(32, 0) NULL COMMENT '上月结余耗材库存数量' AFTER `out_num`,
ADD COLUMN `hc_jy_stock_price` decimal(20, 2) NULL COMMENT '上月结余耗材库存金额' AFTER `hc_jy_stock_num`,
ADD COLUMN `rejected_num` double(32, 0) NULL COMMENT '退货数量' AFTER `hc_jy_stock_price`,
ADD COLUMN `rejected_price` decimal(20, 2) NULL COMMENT '退货金额' AFTER `rejected_num`;


-- 2020年8月6日 18:52:59   統計費用表字段调整及增加
ALTER TABLE  `pd_numerical_inf`
CHANGE COLUMN `sj_in_record_num` `in_record_num` double(32, 2) NULL DEFAULT NULL COMMENT '入库数量' AFTER `depart_id`,
CHANGE COLUMN `sj_in_record_price` `in_record_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '入库金额' AFTER `in_record_num`,
CHANGE COLUMN `sj_num` `purchase_num` double(32, 2) NULL DEFAULT NULL COMMENT '使用数量' AFTER `in_record_price`,
CHANGE COLUMN `sj_price` `purchase_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '使用金额' AFTER `purchase_num`,
CHANGE COLUMN `sj_stock_num` `stock_num` double(32, 0) NULL DEFAULT NULL COMMENT '库存数量' AFTER `purchase_price`,
CHANGE COLUMN `sj_stock_price` `stock_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '库存金额' AFTER `stock_num`,
CHANGE COLUMN `hc_in_record_num` `call_in_num` double(32, 0) NULL DEFAULT NULL COMMENT '调入数量' AFTER `stock_price`,
CHANGE COLUMN `hc_in_record_price` `call_in_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '调入金额' AFTER `call_in_num`,
CHANGE COLUMN `hc_num` `call_out_num` double(32, 0) NULL DEFAULT NULL COMMENT '调出数量' AFTER `call_in_price`,
CHANGE COLUMN `hc_price` `call_out_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '调出金额' AFTER `call_out_num`,
CHANGE COLUMN `hc_stock_num` `dis_num` double(32, 0) NULL DEFAULT NULL COMMENT '差异数量' AFTER `call_out_price`,
CHANGE COLUMN `hc_stock_price` `dis_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '差异金额' AFTER `dis_num`,
CHANGE COLUMN `sj_jy_stock_num` `the_stock_num` double(32, 0) NULL DEFAULT NULL COMMENT '期初库存数量' AFTER `del_flag`,
CHANGE COLUMN `sj_jy_stock_price` `the_stock_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '期初库存金额' AFTER `the_stock_num`,
CHANGE COLUMN `in_num` `spec_quantity_num` double(32, 0) NULL DEFAULT NULL COMMENT '使用理论规格数量' AFTER `the_stock_price`,
CHANGE COLUMN `out_num` `spec_reality_num` double(32, 0) NULL DEFAULT NULL COMMENT '实际使用规格数量' AFTER `spec_quantity_num`,
CHANGE COLUMN `hc_jy_stock_num` `dis_spec_num` double(32, 0) NULL DEFAULT NULL COMMENT '差异规格数量' AFTER `spec_reality_num`,
CHANGE COLUMN `hc_jy_stock_price` `tj_type` varchar(4) NULL DEFAULT NULL COMMENT '统计类型  1：试剂   0：耗材' AFTER `dis_spec_num`;


INSERT INTO  `sys_permission` VALUES ('1291276845681188866', 'f0675b52d89100ee88472b6800754a08', '试剂月统计报表', '/pd/query/PdCallMonth', 'pd/query/PdCallMonthQuery', NULL, NULL, 1, '0', NULL, '1', 1.10, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-08-06 15:36:05', NULL, '2020-08-06 15:36:05', 0, 0, '1', 0);

ALTER TABLE `jeecg-boot`.`pd_numerical_inf`
DROP COLUMN `sj_jy_stock_num`,
DROP COLUMN `sj_jy_stock_price`,
DROP COLUMN `hc_jy_stock_num`,
DROP COLUMN `hc_jy_stock_price`;


-- 2020年8月11日 18:52:59   字段注释调整
ALTER TABLE  `pd_product_stock`
MODIFY COLUMN `spec_num` double(32, 2) NULL DEFAULT NULL COMMENT '库存规格数量' AFTER `record_detail_id`;