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
