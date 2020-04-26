-- add by zxh 20200420 产品加入试剂
ALTER TABLE `pd_product`
ADD COLUMN `product_flag`  varchar(1) NOT NULL COMMENT '0产品，1试剂' AFTER `jde_code`;

-- add by jiangxz 20200420 入库记录表增加业态字段
ALTER TABLE `pd_stock_record` ADD COLUMN `format` varchar(32) NULL COMMENT '业态' AFTER `remarks`;
-- add by jiangxz 20200420 业态数据字典
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1252054086213980161', '业态', 'format', '', 0, 'admin', '2020-04-20 09:58:51', NULL, '2020-04-20 09:58:51', 0);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1252054161417850881', '1252054086213980161', '直销', '1', '', 1, 1, 'admin', '2020-04-20 09:59:09', NULL, '2020-04-20 09:59:09');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1252054211766276097', '1252054086213980161', '集中配送', '2', '', 2, 1, 'admin', '2020-04-20 09:59:21', NULL, '2020-04-20 09:59:21');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1252054299817299969', '1252054086213980161', '配送', '3', '', 3, 1, 'admin', '2020-04-20 09:59:42', NULL, '2020-04-20 09:59:42');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1252054348651581441', '1252054086213980161', '分销', '4', '', 4, 1, 'admin', '2020-04-20 09:59:53', NULL, '2020-04-20 09:59:53');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1252054427324141570', '1252054086213980161', '物流平台', '5', '', 5, 1, 'admin', '2020-04-20 10:00:12', NULL, '2020-04-20 10:00:12');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1252054472857505794', '1252054086213980161', '零售', '6', '', 6, 1, 'admin', '2020-04-20 10:00:23', 'admin', '2020-04-20 10:00:35');

-- add by zxh 20200420 产品加入试剂
INSERT INTO `sys_permission` VALUES ('1252044092885708801', '1210107255254798338', '试剂管理', '/pd/PdProductReagents', 'pd/PdProductReagentsList', null, null, '1', '0', null, '1', '0.20', '0', null, '1', '1', '0', '0', null, 'admin', '2020-04-20 09:19:08', 'admin', '2020-04-20 09:19:42', '0', '0', '1', '0');

-- add by zxh 20200420 产品加入试剂
ALTER TABLE `pd_product`
ADD COLUMN `spec_unit_id`  varchar(64) NULL COMMENT '试剂规格单位' AFTER `zdy`,
ADD COLUMN `spec_quantity`  double(11,4) NULL COMMENT '试剂规格数量' AFTER `spec_unit_id`;

-- add by zxh 20200420 产品加入单位类型
ALTER TABLE `pd_unit`
ADD COLUMN `unit_type`  varchar(1) NULL COMMENT '单位类型' AFTER `name`;

-- add by zxh 2020年4月20日11:26:18 产品加入单位类型
INSERT INTO `sys_dict` VALUES ('1252073695738155010', '单位类型', 'unit_type', '', '0', 'admin', '2020-04-20 11:16:46', null, '2020-04-20 11:16:46', '0');
INSERT INTO `sys_dict_item` VALUES ('1252073800750944258', '1252073695738155010', '包装单位', '0', '', '1', '1', 'admin', '2020-04-20 11:17:11', null, '2020-04-20 11:17:11');
INSERT INTO `sys_dict_item` VALUES ('1252073837744705538', '1252073695738155010', '规格单位', '1', '', '1', '1', 'admin', '2020-04-20 11:17:20', null, '2020-04-20 11:17:20');


-- add by mcb 20200420 库存明细表增加规格库存数量及库存占用状态
ALTER TABLE `pd_product_stock`
ADD COLUMN `spec_num`  double(32,2) DEFAULT NULL COMMENT '试剂库存规格数量',
ADD COLUMN `nestat_status` varchar(4) DEFAULT '1' COMMENT '库存占用状态 0：使用中  1：未使用 2:已用完',
ADD COLUMN `spec_unit_id`  varchar(64) DEFAULT NULL COMMENT '试剂规格单位',
ADD COLUMN `spec_quantity` double(11, 4) DEFAULT NULL COMMENT '试剂规格数量';

-- add by mcb 2020年4月20日15:26:18  静态字典增加占用状态
INSERT INTO `sys_dict` VALUES ('1252139529844842498', '占用状态', 'nestat_status', '0：使用中  1：未使用', 0, 'admin', '2020-4-20 15:38:21', NULL, '2020-4-20 15:38:21', 0);
INSERT INTO `sys_dict_item` VALUES ('1252139596743991298', '1252139529844842498', '使用中', '0', '', 1, 1, 'admin', '2020-4-20 15:38:38', NULL, '2020-4-20 15:38:38');
INSERT INTO `sys_dict_item` VALUES ('1252139634337538049', '1252139529844842498', '未使用', '1', '', 2, 1, 'admin', '2020-4-20 15:38:47', NULL, '2020-4-20 15:38:47');
INSERT INTO `sys_dict_item` VALUES ('1252896597530947586', '1252139529844842498', '已用完', '2', '', 3, 1, 'admin', '2020-4-22 17:46:41', NULL, '2020-4-22 17:46:41');
-- add by 2020年4月20日16:44:05 入库明细表加 规格单位 规格数量
ALTER TABLE `pd_stock_record_detail`
ADD COLUMN `spec_unit_id` varchar(64) NULL COMMENT '试剂规格单位' AFTER `supplier_id`,
ADD COLUMN `spec_quantity` double(11, 4) NULL COMMENT '试剂规格数量' AFTER `spec_unit_id`;


-- add by jiangxz 2020年4月21日09:39:10 定数包产品数量 改为double
ALTER TABLE `pd_package`
MODIFY COLUMN `sum` double(11, 2) NULL DEFAULT NULL COMMENT '产品总数' AFTER `name`;
ALTER TABLE `pd_package_detail`
MODIFY COLUMN `count` double(11, 2) NULL DEFAULT 0 COMMENT '产品数量' AFTER `product_id`;

-- add by jiangxz 2020年4月21日09:48:06 定数包打包记录表、打包记录明细表
DROP TABLE IF EXISTS `pd_package_record`;
CREATE TABLE `pd_package_record`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `package_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定数包ID',
  `depart_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科室ID',
  `depart_parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属父部门',
  `package_bar_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定数包流水码',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库状态：0-已出库；1-未出库',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '删除标识，0-正常；1-删除',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
DROP TABLE IF EXISTS `pd_package_record_detail`;
CREATE TABLE `pd_package_record_detail`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `record_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '打包记录ID',
  `product_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品ID',
  `product_stock_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库存明细ID',
  `batch_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批号',
  `exp_date` datetime(0) NULL DEFAULT NULL COMMENT '有效期',
  `package_num` double(11, 2) NULL DEFAULT NULL COMMENT '打包数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- add by 2020年4月21日09:23:29 检验项目管理
CREATE TABLE `pd_use_package` (
  `id` varchar(36) NOT NULL,
  `code` varchar(32) DEFAULT NULL COMMENT '检验项目编号',
  `name` varchar(200) DEFAULT NULL COMMENT '检验项目名称',
  `sum` double(11,4) DEFAULT NULL COMMENT '产品总数',
  `py` varchar(32) DEFAULT NULL COMMENT '拼音简码',
  `wb` varchar(32) DEFAULT NULL COMMENT '五笔简码',
  `zdy` varchar(32) DEFAULT NULL COMMENT '自定义码',
  `del_flag` varchar(1) NOT NULL COMMENT '删除标识，0-正常；1-删除',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(64) NOT NULL COMMENT '所属父部门',
  `depart_id` varchar(64) NOT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

CREATE TABLE `pd_use_package_detail` (
  `id` varchar(36) NOT NULL,
  `package_id` varchar(36) DEFAULT NULL COMMENT '检验项目id',
  `product_id` varchar(36) DEFAULT NULL COMMENT '产品id',
  `count` double(11,2) DEFAULT '0.00' COMMENT '产品数量',
  `remarks` varchar(320) DEFAULT NULL COMMENT '备注',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '删除标识，0-正常；1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(64) NOT NULL COMMENT '所属父部门',
  `depart_id` varchar(64) NOT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- add by 2020年4月22日15:49:16 器械分类
ALTER TABLE `pd_product`
ADD COLUMN `device_classification`  varchar(1) NULL COMMENT '器械分类0类，1类，2类，3类' AFTER `jde_code`;
INSERT INTO `sys_dict` VALUES ('1252882340809883649', '器械分类', 'device_classification', '', '0', 'admin', '2020-04-22 16:50:02', null, '2020-04-22 16:50:02', '0');
INSERT INTO `sys_dict_item` VALUES ('1252882417976688641', '1252882340809883649', '0类', '0', '', '1', '1', 'admin', '2020-04-22 16:50:21', null, '2020-04-22 16:50:21');
INSERT INTO `sys_dict_item` VALUES ('1252882453347254273', '1252882340809883649', 'Ⅰ类', '1', '', '1', '1', 'admin', '2020-04-22 16:50:29', null, '2020-04-22 16:50:29');
INSERT INTO `sys_dict_item` VALUES ('1252882477808435201', '1252882340809883649', 'Ⅱ类', '2', '', '1', '1', 'admin', '2020-04-22 16:50:35', null, '2020-04-22 16:50:35');
INSERT INTO `sys_dict_item` VALUES ('1252882496439533569', '1252882340809883649', 'Ⅲ类', '3', '', '1', '1', 'admin', '2020-04-22 16:50:39', 'admin', '2020-04-22 16:54:50');

-- add by 2020年4月23日16:59:06 检验包管理
CREATE TABLE `ex_inspection_items` (
  `id` varchar(36) NOT NULL,
  `patient_name` varchar(64) DEFAULT NULL COMMENT '患者姓名',
  `patient_sex` varchar(64) DEFAULT NULL COMMENT '患者性别',
  `patient_age` varchar(64) DEFAULT NULL COMMENT '患者年龄',
  `card_id` varchar(64) DEFAULT NULL COMMENT '就诊卡号3',
  `bar_code` varchar(64) DEFAULT NULL COMMENT '条形码',
  `apply_doctor` varchar(64) DEFAULT NULL COMMENT '申请医生',
  `apply_department` varchar(64) DEFAULT NULL COMMENT '申请科室',
  `test_doctor` varchar(64) DEFAULT NULL COMMENT '检验医生',
  `test_department` varchar(64) DEFAULT NULL COMMENT '检验科室',
  `patient_type` varchar(64) DEFAULT NULL COMMENT '患者类型',
  `group_by` varchar(64) DEFAULT NULL COMMENT '工作组',
  `receive_date` varchar(64) DEFAULT NULL COMMENT '接收日期',
  `test_date` varchar(64) DEFAULT NULL COMMENT '检验日期',
  `specimen_type` varchar(64) DEFAULT NULL COMMENT '样本类型',
  `state` varchar(1) DEFAULT NULL COMMENT '状态',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `ex_inspection_items_detail` (
  `id` varchar(36) NOT NULL,
  `ref_id` varchar(64) DEFAULT NULL COMMENT '检查项目Id',
  `test_combination_name` varchar(64) DEFAULT NULL COMMENT '组合名称',
  `testitem_name` varchar(64) DEFAULT NULL COMMENT '检查项目名称',
  `testitem_code` varchar(64) DEFAULT NULL COMMENT '检查项目代码',
  `testitem_cost` varchar(32) DEFAULT NULL COMMENT '检查项目费用',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- add by jiangxz 2020年4月24日10:51:59 出库记录明细 定数包ID改为打包记录ID
ALTER TABLE `pd_stock_record_detail`
CHANGE COLUMN `package_id` `package_record_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打包记录ID' AFTER `exp_date`;

-- add by mcb 2020年4月25日 库存明细增加规格库存数量清零原因字段
ALTER TABLE `pd_product_stock`
ADD COLUMN `reason` varchar(2000) NULL COMMENT '规格库存数量清零原因';

-- add by jiangxz 2020年4月26日14:14:30 定数包打包记录 菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1254291714732748802', '1218784892172963842', '定数包打包记录', '/pd/PdPackageRecordList', 'pd/PdPackageRecordList', NULL, NULL, 1, '0', NULL, '1', 1.10, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-04-26 14:10:23', NULL, '2020-04-26 14:10:23', 0, 0, '1', 0);
