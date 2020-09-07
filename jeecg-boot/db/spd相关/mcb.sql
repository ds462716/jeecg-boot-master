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

-- ALTER TABLE `jeecg-boot`.`pd_numerical_inf`
-- DROP COLUMN `sj_jy_stock_num`,
-- DROP COLUMN `sj_jy_stock_price`,
-- DROP COLUMN `hc_jy_stock_num`,
-- DROP COLUMN `hc_jy_stock_price`;


-- 2020年8月11日 18:52:59   字段注释调整
ALTER TABLE  `pd_product_stock`
MODIFY COLUMN `spec_num` double(32, 2) NULL DEFAULT NULL COMMENT '库存规格数量' AFTER `record_detail_id`;

-- 2020年8月12日 18:52:59   增加开关
INSERT INTO  `pd_on_off` VALUES ('1293364869915095041', '是否根据规格数量扣减库存开关', 'on_off_spec_num', NULL, '1:根据规格数量    0:不根据规格数量', 0, 'mcb', '2020-08-12 09:53:09', NULL, '2020-08-12 09:53:09', 'dea5919617234ef9854c5806d4b44efa', 'c6d7cb4deeac411cb3384b1b31278596', '0', 'A01A01');
-- 2020年8月12日 18:52:59   用量明细表增加库存规格数量
ALTER TABLE  `pd_dosage_detail`
ADD COLUMN `spec_quantity` double(11, 2) NULL COMMENT '库存规格数量' AFTER `his_package_flag`;

-- 2020年8月13日 18:52:59   用量明细表库存规格数量字段调整
 ALTER TABLE  `pd_dosage_detail`
CHANGE COLUMN `spec_quantity` `spec_num` double(11, 2) NULL DEFAULT NULL COMMENT '库存规格数量' AFTER `his_package_flag`;


-- 2020年8月14日 18:52:59   新建申购模板表及模板明细表
CREATE TABLE `pd_purchase_temp` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `temp_no` varchar(64) DEFAULT NULL COMMENT '模板编号',
  `temp_name` varchar(100) DEFAULT NULL COMMENT '模板名称',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注信息',
  `del_flag` varchar(4) DEFAULT NULL COMMENT '删除状态',
  `total_num` double(32,2) DEFAULT NULL COMMENT '模板产品总数量',
  `depart_id` varchar(64) DEFAULT NULL COMMENT '模板科室编号',
  `depart_parent_id` varchar(64) DEFAULT NULL COMMENT '所属医院ID',
  `time_type` varchar(4) DEFAULT NULL COMMENT '模板类型',
  `user_id` varchar(64) DEFAULT NULL COMMENT '模板创建人编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='申购模板主表';

 CREATE TABLE `pd_purchase_temp_detail` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `temp_no` varchar(64) DEFAULT NULL COMMENT '模板编号',
  `product_id` varchar(64) DEFAULT NULL COMMENT '产品ID',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `del_flag` varchar(4) DEFAULT '0' COMMENT '删除状态',
  `order_num` double(32,2) DEFAULT NULL COMMENT '申购数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='申购模板明细表';


-- 2020年8月14日 18:52:59   增加采购模板菜单
 INSERT INTO  `sys_permission`  VALUES ('1294088254068539393', '1218784310368473089', '采购模板', '/pd/PdPurchaseTemp', 'pd/PdPurchaseTempList', NULL, NULL, 1, '0', NULL, '1', 4.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-08-14 09:47:37', 'admin', '2020-08-14 09:47:51', 0, 0, '1', 0);
-- 2020年8月14日 18:52:59   增加申购模板静态字段
INSERT INTO  `sys_dict` VALUES ('1294109481097166849', '申购模板类型', 'temp_type', '1：私有   2：公用', 0, 'admin', '2020-08-14 11:11:58', 'admin', '2020-08-14 11:12:49', 0);
INSERT INTO  `sys_dict_item`  VALUES ('1294109619622445057', '1294109481097166849', '公用', '2', '', 2, 1, 'admin', '2020-08-14 11:12:31', 'admin', '2020-08-14 11:12:38');
INSERT INTO  `sys_dict_item` VALUES ('1294109566577082369', '1294109481097166849', '私有', '1', '', 1, 1, 'admin', '2020-08-14 11:12:19', 'admin', '2020-08-14 11:12:42');
-- 2020年8月15日 18:52:59   增加器械使用（赣州五院）菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1294445368496369666', '1244532359552057345', '器械使用（赣州五院）', '/pd/PdDosageGZWY', 'pd/PdDosageList', NULL, NULL, 1, '0', NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-08-15 09:26:40', NULL, '2020-08-15 09:26:40', 0, 0, '1', 0);

-- 2020年8月16日 10:22:59   增加索引
CREATE INDEX record_detail_id ON pd_product_stock (record_detail_id);
CREATE INDEX in_depart_id ON pd_stock_record (in_depart_id);
CREATE INDEX id ON pd_stock_record_detail (id);
CREATE INDEX distributor_id ON pd_stock_record_detail (distributor_id);
CREATE INDEX supplier_id ON pd_stock_record_detail (supplier_id);
CREATE INDEX depart_parent_id ON pd_stock_record_detail (depart_parent_id);
CREATE INDEX product_id ON pd_stock_record_detail (product_id);
CREATE INDEX record_id ON pd_stock_record_detail (record_id);


-- 2020年8月16日 10:22:59   试剂用量查询菜单设置逻辑删除
 update   sys_permission set del_flag='1'  where id='1263760305687826434' and name='试剂用量查询';

-- 2020年8月17日 10:22:59   菜单名称修改
UPDATE sys_permission SET name='供应商耗材用量统计报表' where id='1293093425294729218';
-- 2020年8月18日 10:22:59   增加静态字典
INSERT INTO  `sys_dict`  VALUES ('1295520195443605505', '是否执行收费标识', 'hy_charged', '0:已收费   1：未收费   2：已退回    3：已退费', 0, 'admin', '2020-08-18 08:37:39', NULL, '2020-08-18 08:37:39', 0);
INSERT INTO  `sys_dict_item`  VALUES ('1295520265144549378', '1295520195443605505', '已收费', '0', '', 1, 1, 'admin', '2020-08-18 08:37:55', NULL, '2020-08-18 08:37:55');
INSERT INTO  `sys_dict_item`  VALUES ('1295520302746484738', '1295520195443605505', '未收费', '1', '', 2, 1, 'admin', '2020-08-18 08:38:04', NULL, '2020-08-18 08:38:04');
INSERT INTO  `sys_dict_item`  VALUES ('1295520361621929985', '1295520195443605505', '已退回', '2', '', 3, 1, 'admin', '2020-08-18 08:38:18', NULL, '2020-08-18 08:38:18');
INSERT INTO  `sys_dict_item`  VALUES ('1295520410984693762', '1295520195443605505', '已退费', '3', '', 4, 1, 'admin', '2020-08-18 08:38:30', NULL, '2020-08-18 08:38:30');
-- 2020年8月18日 11:22:59   增加按钮权限
INSERT INTO  `sys_permission` VALUES ('1295550484245032962', '1229578873631465473', '耗材盘点导出按钮', NULL, NULL, NULL, NULL, 2, '0', 'stock:form:hcExportXls', '2', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-08-18 10:38:00', NULL, '2020-08-18 10:38:00', 0, 0, '1', 0);
INSERT INTO  `sys_permission` VALUES ('1295550197367222273', '1229578873631465473', '试剂盘点导出按钮', NULL, NULL, NULL, NULL, 2, '0', 'stock:form:sjExportXls', '2', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-08-18 10:36:52', NULL, '2020-08-18 10:36:52', 0, 0, '1', 0);

-- 2020年8月19日 11:22:59   增加字段
ALTER TABLE  `ex_inspection_items_use_detail`
ADD COLUMN `ref_bar_code` varchar(64) NULL COMMENT '唯一条码' AFTER `depart_parent_id`;
-- 2020年8月19日 14:50:59   增加金额字段
ALTER TABLE  `pd_bottle_inf`
ADD COLUMN `purchase_price` decimal(20, 4) NULL COMMENT '试剂金额' AFTER `status`;

-- 赣州肿瘤医院部署截止 2020年8月26日16:22:29

-- 2020年9月7日 10:22:59   增加产品单价字段
ALTER TABLE `pd_product_stock`
ADD COLUMN `purchase_price` decimal(20, 4) NULL COMMENT '产品单价' AFTER `stock_num`;








