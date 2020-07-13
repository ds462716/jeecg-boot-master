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
  `card_id` varchar(64) DEFAULT NULL COMMENT '就诊卡号',
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
-- add by jiangxz 2020年4月26日14:14:30 定数包打包记录状态数据字典
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1254304620731985922', '定数包打包记录状态', 'package_record_status', '', 0, 'admin', '2020-04-26 15:01:40', NULL, '2020-04-26 15:01:40', 0);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1254304707726045186', '1254304620731985922', '已出库', '0', '', 1, 1, 'admin', '2020-04-26 15:02:01', NULL, '2020-04-26 15:02:01');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1254304737581101058', '1254304620731985922', '未出库', '1', '', 1, 1, 'admin', '2020-04-26 15:02:08', NULL, '2020-04-26 15:02:08');
-- add by jiangxz 2020年4月26日14:14:30 定数包字段名称修改
ALTER TABLE `pd_package`
CHANGE COLUMN `code` `package_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定数包编号' AFTER `id`,
CHANGE COLUMN `name` `package_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定数包名称' AFTER `package_code`,
CHANGE COLUMN `sum` `package_sum` double(11, 2) NULL DEFAULT NULL COMMENT '产品总数' AFTER `package_name`;

-- add by mcb 2020年4月26日19:14:30 增加打包记录id
ALTER TABLE `pd_apply_detail`
ADD COLUMN `package_record_id` varchar(64) NULL COMMENT '打包记录id';
ALTER TABLE `pd_allocation_detail`
ADD COLUMN `package_record_id` varchar(64) NULL COMMENT '打包记录id';

-- add by mcb 2020年4月29日19:14:30 增加HIS系统收费项目基础信息表
CREATE TABLE `ex_his_charge_inf` (
  `id` varchar(36) NOT NULL,
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `fsf_xmbh` varchar(32) DEFAULT NULL COMMENT '收费项目代码',
  `fsf_xmmc` varchar(64) DEFAULT NULL COMMENT '收费项目名称',
  `fsf_xmgg` varchar(64) DEFAULT NULL COMMENT '收费项目规格',
  `fsf_spec` varchar(64) DEFAULT NULL COMMENT '规格',
  `fsf_version` varchar(64) DEFAULT NULL COMMENT '型号',
  `fsf_unitname` varchar(32) DEFAULT NULL COMMENT '单位名称',
  `fsf_vender` varchar(64) DEFAULT NULL COMMENT '生产厂家',
  `fsf_supplier` varchar(64) DEFAULT NULL COMMENT '供应商',
  `fsf_ksbh` varchar(32) DEFAULT NULL COMMENT '收费科室代码',
  `fsf_ks` varchar(64) DEFAULT NULL COMMENT '收费科室名称',
  `fsf_xmlb` varchar(32) DEFAULT NULL COMMENT '收费类别标识',
  `del_flag` varchar(4) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--- add by mcb 2020年5月6日9:14:30 增加试剂管理主菜单
INSERT INTO `sys_permission` VALUES ('1255456003879661569', '1255448486688649218', '收费项目管理', '/pd/PdChargeList', 'pd/PdChargeList', NULL, NULL, 1, '0', NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-4-29 19:16:51', 'admin', '2020-4-29 19:17:12', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1255448486688649218', '', '试剂管理', '/pdProductReagent', 'layouts/RouteView', NULL, NULL, 0, '0', NULL, '1', 0.80, 0, 'rocket', 1, 0, 0, 0, NULL, 'admin', '2020-4-29 18:46:59', 'admin', '2020-4-29 18:48:45', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1252411354562027522', '1255448486688649218', '检验配置管理', '/pd/PdUsePackageList', 'pd/PdUsePackageList', NULL, NULL, 1, '0', NULL, '1', 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-4-21 09:38:30', 'admin', '2020-4-29 19:17:19', 0, 0, '1', 0);
DELETE FROM `sys_permission` WHERE id ='1252044092885708801';
INSERT INTO `sys_permission` VALUES ('1252044092885708801', '1255448486688649218', '试剂管理', '/pd/PdProductReagents', 'pd/PdProductReagentsList', NULL, NULL, 1, '0', NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-4-20 09:19:08', 'admin', '2020-4-29 19:17:04', 0, 0, '1', 0);
--- add by mcb 2020年5月6日11:14:30 增加HIS系统用户管理菜单
INSERT INTO `sys_permission` VALUES ('1257863184923021314', '1210107255254798338', 'HIS用户管理', '/pd/PdHisUser', 'pd/PdHisUserList', NULL, NULL, 1, '0', NULL, '1', 13.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-5-6 10:42:08', NULL, '2020-5-6 10:42:08', 0, 0, '1', 0);
--- add by mcb 2020年5月6日11:14:30 增加HIS系统用户信息表
CREATE TABLE `ex_his_user_inf` (
  `id` varchar(36) NOT NULL,
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `fsf_yhm` varchar(32) DEFAULT NULL COMMENT '人员内部工号',
  `fsf_yhid` varchar(32) DEFAULT NULL COMMENT '登录ID号',
  `fsf_yhxm` varchar(32) DEFAULT NULL COMMENT '姓名',
  `fsf_yhks` varchar(32) DEFAULT NULL COMMENT '所属科室代码',
  `del_flag` varchar(4) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--- add by mcb 2020年5月6日11:14:30 增加HIS系统科室信息表

CREATE TABLE `ex_his_depart_inf` (
  `id` varchar(36) NOT NULL,
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `fsf_ksbh` varchar(32) DEFAULT NULL COMMENT '科室编号',
  `fsf_ksmc` varchar(32) DEFAULT NULL COMMENT '科室名称',
  `fsf_ksjm` varchar(32) DEFAULT NULL COMMENT '科室拼音简码',
  `spd_depart_id` varchar(64) DEFAULT NULL COMMENT '关联SPD系统科室ID',
  `del_flag` varchar(4) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--- add by mcb 2020年5月7日11:14:30 增加HIS系统科室管理菜单
INSERT INTO `sys_permission` VALUES ('1258213960258211841', '1210107255254798338', 'HIS科室管理', '/pd/PdHisDepart', 'pd/PdHisDepartList', NULL, NULL, 1, '0', NULL, '1', 14.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-5-7 09:55:59', NULL, '2020-5-7 09:55:59', 0, 0, '1', 0);

-- add by zxh 2020年5月6日15:02:49 lis对接表字段优化
ALTER TABLE `ex_inspection_items`
ADD COLUMN `combination_name`  varchar(64) NULL COMMENT '项目组合名称' AFTER `state`,
ADD COLUMN `combination_code`  varchar(64) NULL COMMENT '项目组合代码' AFTER `combination_name`,
ADD COLUMN `testItem_name`  varchar(64) NULL COMMENT '检查项目名称' AFTER `combination_code`,
ADD COLUMN `testItem_code`  varchar(64) NULL COMMENT '检查项目代码' AFTER `testItem_name`,
ADD COLUMN `testItem_cost`  double(11,4) NULL COMMENT '检查项目费用' AFTER `testItem_code`,
ADD COLUMN `accept_status`  varchar(1) NULL COMMENT '读取状态' AFTER `testItem_cost`;
ALTER TABLE `ex_inspection_items`
ADD COLUMN `jy_id`  varchar(64) NULL COMMENT 'lis系统主键' AFTER `id`
DROP TABLE `ex_inspection_items_detail`;

ALTER TABLE `ex_inspection_items`
MODIFY COLUMN `card_id`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就诊卡号' AFTER `patient_age`;

ALTER TABLE `ex_inspection_items`
CHANGE COLUMN `testItem_name` `test_item_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检查项目名称' AFTER `combination_code`,
CHANGE COLUMN `testItem_code` `test_item_code`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检查项目代码' AFTER `test_item_name`,
CHANGE COLUMN `testItem_cost` `test_item_cost`  double(11,4) NULL DEFAULT NULL COMMENT '检查项目费用' AFTER `test_item_code`;

-- add by zxh 2020年5月8日14:54:22 器械使用详情加入单价
ALTER TABLE `pd_dosage_detail`
ADD COLUMN `selling_price`  decimal(11,4) NULL COMMENT '出库单价' AFTER `left_refund_num`;

-- add by zxh 2020年5月8日14:54:22 手动执行lis发过来的项目包
INSERT INTO `sys_permission` VALUES ('1258590018295095297', '1255448486688649218', '检验项目使用', '/external/exInspectionItems', 'external/ExInspectionItemsList', null, null, '1', '0', null, '1', '1.00', '0', null, '1', '1', '0', '0', null, 'admin', '2020-05-08 10:50:18', null, '2020-05-08 10:50:18', '0', '0', '1', '0');

-- add by jiangxz 2020年5月9日14:11:55 进销存报表
update sys_permission set url = '/pd/query/PdPurchaseSaleStockReportList',component = 'pd/query/PdPurchaseSaleStockReportList' where name = '进销存报表'

-- add by zxh 2020年5月11日10:54:56 检验项目同步加入部门
ALTER TABLE `ex_inspection_items`
ADD COLUMN `depart_id`  varchar(64) NULL COMMENT '所属部门' AFTER `sys_org_code`,
ADD COLUMN `depart_parent_id`  varchar(64) NULL COMMENT '所属父部门' AFTER `depart_id`;
INSERT INTO `sys_dict` VALUES ('1259668780394835970', '检验项目类型', 'inspection_item_type', '', '0', 'admin', '2020-05-11 10:16:55', 'admin', '2020-05-11 10:58:01', '0');
INSERT INTO `sys_dict_item` VALUES ('1259668832395816961', '1259668780394835970', '复检', '1', '', '1', '1', 'admin', '2020-05-11 10:17:08', null, '2020-05-11 10:17:08');
INSERT INTO `sys_dict_item` VALUES ('1259668854369775617', '1259668780394835970', '质控', '2', '', '1', '1', 'admin', '2020-05-11 10:17:13', null, '2020-05-11 10:17:13');
INSERT INTO `sys_dict_item` VALUES ('1259668878419914753', '1259668780394835970', '测试', '3', '', '1', '1', 'admin', '2020-05-11 10:17:19', null, '2020-05-11 10:17:19');
INSERT INTO `sys_dict_item` VALUES ('1259668901975126018', '1259668780394835970', '空白阴阳对照', '4', '', '1', '1', 'admin', '2020-05-11 10:17:24', null, '2020-05-11 10:17:24');
INSERT INTO `sys_dict_item` VALUES ('1259668924741808129', '1259668780394835970', '其他', '5', '', '1', '1', 'admin', '2020-05-11 10:17:30', null, '2020-05-11 10:17:30');
INSERT INTO `sys_permission` VALUES ('1259662438426750977', '1255448486688649218', '检验项目手动使用', '/external/exInspectionItemsUse', 'external/ExInspectionItemsUseList', null, null, '1', '0', null, '1', '3.00', '0', null, '1', '1', '0', '0', null, 'admin', '2020-05-11 09:51:43', null, '2020-05-11 09:51:43', '0', '0', '1', '0');

-- add by mcb 2020年5月11日14:11:55 增加自动补货记录表
CREATE TABLE `pd_auto_order_inf` (
  `id` varchar(36) NOT NULL,
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `auto_date` datetime DEFAULT NULL COMMENT '补货申请日期',
  `auto_num` double DEFAULT NULL COMMENT '自动补货数量',
  `limit_down` double DEFAULT NULL COMMENT '库存下限数量',
  `stock_num` double DEFAULT NULL COMMENT '补货时库存数量',
  `product_id` varchar(32) DEFAULT NULL COMMENT '补货产品id',
  `depart_id` varchar(32) DEFAULT NULL COMMENT '补货科室id',
  `depart_parent_id` varchar(32) DEFAULT NULL COMMENT '机构ID',
  `order_type` varchar(4) DEFAULT NULL COMMENT '生成订单类型 0:申领单  1：采购单',
  `order_no` varchar(32) DEFAULT NULL COMMENT '对应订单编号',
  `del_flag` varchar(4) DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- add by mcb 2020年5月11日14:11:55 库存总表增加自动补货数量字段
ALTER TABLE `pd_product_stock_total`
ADD COLUMN `auto_num` double(32,2) DEFAULT NULL COMMENT '自动补货数量';

-- add by mcb 2020年5月11日14:11:55 增加自动补货定时任务
INSERT INTO `sys_quartz_job` VALUES ('1248489519106277378', 'admin', '2020-4-10 13:54:31', 0, 'admin', '2020-4-27 15:30:27', 'org.jeecg.modules.quartz.job.AutoOrderTaskJob', '3/5 * * * * ? *', NULL, '自动补货生成订单定时任务', -1);

-- add by mcb 2020年5月12日14:11:55 增加收费金额字段
ALTER TABLE `ex_his_charge_inf`
ADD COLUMN `fsf_je` decimal(20,2) DEFAULT NULL COMMENT '收费金额';


-- add by mcb 2020年5月14日14:11:55 增加病人信息相关字段
ALTER TABLE `pd_dosage`
ADD COLUMN `patient_type`  varchar(64) NULL COMMENT '病人类型' AFTER `del_flag`,
ADD COLUMN `medical_record_no`  varchar(64) NULL COMMENT '病历号' AFTER `patient_type`,
ADD COLUMN `hospitalizations_num`  int(11) NULL COMMENT '住院次数' AFTER `medical_record_no`,
ADD COLUMN `operation_name`  varchar(64) NULL AFTER `hospitalizations_num`,
ADD COLUMN `operation_time`  datetime NULL COMMENT '手术时间' AFTER `operation_name`,
ADD COLUMN `bed_number`  varchar(64) NULL COMMENT '病床号' AFTER `operation_time`,
ADD COLUMN `admission_date`  datetime NULL COMMENT '入院日期' AFTER `bed_number`;

-- add by mcb 2020年5月16日10:11:55 字段数据类型修改并增加备注字段
ALTER TABLE `ex_inspection_items`
MODIFY COLUMN `receive_date`  datetime NULL DEFAULT NULL COMMENT '接收日期' AFTER `group_by`,
MODIFY COLUMN `test_date`  datetime NULL DEFAULT NULL COMMENT '检验日期' AFTER `receive_date`,
ADD COLUMN `remarks`  varchar(1000) NULL DEFAULT NULL COMMENT '备注' AFTER `depart_parent_id`;


-- add by mcb 2020年5月16日10:11:55 增加获取检验项目扣减库存用量定时任务
INSERT INTO `sys_quartz_job` VALUES ('1248489519106277379', 'admin', '2020-5-16 11:23:31', 0, 'admin', '2020-5-16 11:23:31', 'org.jeecg.modules.quartz.job.LisInspectionItemsTaskJob', '3/5 * * * * ? *', NULL, '获取检验项目扣减库存用量定时任务', -1);

-- add by mcb 2020年5月18日10:11:55 增加扣减状态静态字典
INSERT INTO `SYS_DICT` VALUES ('1262211106076762114', '试剂扣减状态', 'accept_status', '0:已扣减  1：未配置检验用量  2：未扣减', 0, 'admin', '2020-5-18 10:39:13', NULL, '2020-5-18 10:39:13', 0);
INSERT INTO `SYS_DICT_ITEM` VALUES ('1262211216206602242', '1262211106076762114', '已扣减', '0', '', 1, 1, 'admin', '2020-5-18 10:39:39', 'admin', '2020-5-18 10:40:18');
INSERT INTO `SYS_DICT_ITEM` VALUES ('1262211261735772161', '1262211106076762114', '未配置检验用量', '1', '', 2, 1, 'admin', '2020-5-18 10:39:50', 'admin', '2020-5-18 10:40:27');
INSERT INTO `SYS_DICT_ITEM` VALUES ('1262211294640087042', '1262211106076762114', '未扣减', '2', '', 3, 1, 'admin', '2020-5-18 10:39:58', 'admin', '2020-5-18 10:40:36');

-- add by jiangxz 2020年5月18日11:56:22 出入库明细加注册证号
ALTER TABLE `pd_stock_record_detail`  ADD COLUMN `registration` varchar(255) NULL COMMENT '注册证' AFTER `exp_date`;

-- add by jiangxz 2020年5月18日17:56:22 用量主表增加扩展字段
ALTER TABLE `pd_dosage`  ADD COLUMN `extension1` varchar(64) NULL COMMENT '扩展字段1';
ALTER TABLE `pd_dosage`  ADD COLUMN `extension2` varchar(64) NULL COMMENT '扩展字段2';

-- add by zxh 2020年5月19日09:06:32  检验项目手动使用优化字段
CREATE TABLE `ex_inspection_items_use` (
  `id` varchar(36) NOT NULL,
  `ref_id` varchar(64) DEFAULT NULL COMMENT '关联病人信息id',
  `item_type` varchar(1) DEFAULT NULL COMMENT '检验项目类型1复检，2质控，3测试，4空白阴阳对照，5其他',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `remarks` varchar(244) DEFAULT NULL COMMENT '备注',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `depart_id` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(64) DEFAULT NULL COMMENT '所属父部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ex_inspection_items_use_detail` (
  `id` varchar(36) NOT NULL,
  `ref_id` varchar(64) DEFAULT NULL COMMENT '检验项目使用id',
  `product_id` varchar(64) DEFAULT NULL COMMENT '产品ID',
  `package_id` varchar(64) DEFAULT NULL COMMENT '检验包的id',
  `product_bar_code` varchar(64) DEFAULT NULL COMMENT '产品条码',
  `batch_no` varchar(64) DEFAULT NULL COMMENT '产品批号',
  `exp_date` datetime DEFAULT NULL COMMENT '有效期',
  `product_stock_id` varchar(64) DEFAULT NULL COMMENT '产品库存明细id-用于出库时记录出库库存id',
  `out_huowei_code` varchar(64) DEFAULT NULL COMMENT '出库货位编号',
  `product_num` double(11,2) DEFAULT NULL COMMENT '产品数量（出入库数量）',
  `selling_price` decimal(11,4) DEFAULT NULL COMMENT '出库单价',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `depart_id` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(64) DEFAULT NULL COMMENT '所属父部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- add by jiangxz 2020年5月20日14:58:03 定数包打包相关日志数据字典
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1262999977709240322', '1233244236134825986', '定数包打包', '8', '', 8, 1, 'admin', '2020-05-20 14:53:55', NULL, '2020-05-20 14:53:55');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1263000120286216194', '1233244236134825986', '定数包拆包', '9', '', 9, 1, 'admin', '2020-05-20 14:54:29', NULL, '2020-05-20 14:54:29');


-- add by mcb 2020年5月20日09:06:32  收费项目管理菜单移到基础信息模块
update sys_permission set parent_id='1210107255254798338'   where id='1255456003879661569';

-- add by mcb 2020年5月20日09:06:32  字段长度变更
ALTER TABLE `ex_inspection_items`
MODIFY COLUMN `combination_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目组合名称' AFTER `state`;

-- add by mcb 2020年5月20日09:06:32  字段数据类型变更
delete from `pd_dosage_detail`;
ALTER TABLE `pd_dosage_detail`
MODIFY COLUMN `id`  int(64) NOT NULL AUTO_INCREMENT FIRST ;


-- add by mcb 2020年5月20日14:58:03 器械使用耗材退费日志数据字典
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1263023784303230977', '1233244236134825986', '耗材退费', '10', '', 10, 1, 'admin', '2020-05-20 14:54:29', NULL, '2020-05-20 14:54:29');
-- add by mcb 2020年5月20日17:58:03 试剂类菜单名称更改
update sys_permission set name='检验明细管理'  where  id='1258590018295095297';
update sys_permission set name='检验用量扣减'  where  id='1259662438426750977';

-- add by mcb 2020年5月20日18:58:03 增加规格数量清零日志菜单及清零按钮权限
INSERT INTO `sys_permission` VALUES ('1263053062075985922', '1255448486688649218', '库存清零日志', '/pd/PdSpecLog', 'pd/PdSpecLogList', NULL, NULL, 1, '0', NULL, '1', 5.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-5-20 18:24:51', NULL, '2020-5-20 18:24:51', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1263062064688214018', '1218804573101428738', '规格数量清零按钮权限', NULL, NULL, NULL, NULL, 2, '0', 'stock:form:specRemove', '2', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-5-20 19:00:37', NULL, '2020-5-20 19:00:37', 0, 0, '1', 0);


-- add by mcb 2020年5月21日18:58:03 增加病人姓名
ALTER TABLE `ex_inspection_items_use`
ADD COLUMN `ref_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '病人姓名' AFTER `depart_parent_id`,
ADD COLUMN `in_hospital_no`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '住院号' AFTER `ref_name`,
ADD COLUMN `outpatient_number`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '门诊号' AFTER `in_hospital_no`;


-- add by mcb 2020年5月22日10:58:03 增加规格数量清零日志表
CREATE TABLE `pd_spec_log` (
  `id` varchar(36) NOT NULL,
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `person_name` varchar(32) DEFAULT NULL COMMENT '操作人姓名',
  `person_id` varchar(32) DEFAULT NULL COMMENT '操作人ID',
  `spec_num` double DEFAULT NULL COMMENT '清零前剩余规格数量',
  `stock_id` varchar(64) DEFAULT NULL COMMENT '对应的库存明细ID',
  `reason` varchar(200) DEFAULT NULL COMMENT '清零原因',
  `product_id` varchar(32) DEFAULT NULL COMMENT '产品ID',
  `depart_id` varchar(32) DEFAULT NULL COMMENT '所属部门ID',
  `depart_parent_id` varchar(32) DEFAULT NULL COMMENT '所属机构ID',
  `del_flag` varchar(4) DEFAULT NULL,
  `spec_unit_id` varchar(32) DEFAULT NULL COMMENT '产品规格单位',
  `spec_quantity` double DEFAULT NULL COMMENT '产品规格数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- add by mcb 2020年5月22日16:00:03 库存明细表增加ref条形码字段
ALTER TABLE `pd_product_stock`
ADD COLUMN `ref_bar_code`  varchar(64) NULL COMMENT 'REF条形码' AFTER `reason`;
-- add by zxh 2020年5月22日16:30:23 条码打印
INSERT INTO `sys_permission` VALUES ('1263006502161293314', '1218784892172963842', '条码打印', '/pd/print/PdProductStockQueryModal', 'pd/print/PdProductStockQueryModal', null, null, '1', '0', null, '1', '1.01', '0', null, '1', '1', '0', '0', null, 'admin', '2020-05-20 15:19:50', 'admin', '2020-05-20 15:20:35', '0', '0', '1', '0');

-- add by jxz 2020年5月22日17:00:03 出入库明细表增加ref条形码字段
ALTER TABLE `pd_stock_record_detail`
ADD COLUMN `ref_bar_code` varchar(64) NULL COMMENT 'REF条码' AFTER `product_bar_code`;

-- add by mcb 2020年5月22日16:00:03 增加试剂用量扣减明细表
CREATE TABLE `ex_deductuin_dosage` (
  `id` varchar(36) NOT NULL,
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `depart_id` varchar(32) DEFAULT NULL COMMENT '科室ID',
  `depart_parent_id` varchar(32) DEFAULT NULL COMMENT '机构ID',
  `product_id` varchar(32) DEFAULT NULL COMMENT '产品ID',
  `exp_date` datetime DEFAULT NULL COMMENT '产品有效期',
  `batch_no` varchar(32) DEFAULT NULL COMMENT '产品批次号',
  `product_bar_code` varchar(64) DEFAULT NULL COMMENT '产品条码',
  `patient_name` varchar(32) DEFAULT NULL COMMENT '病人姓名',
  `in_hospital_no` varchar(32) DEFAULT NULL COMMENT '住院号',
  `outpatient_number` varchar(32) DEFAULT NULL COMMENT '门诊号',
  `deductuin_type` varchar(32) DEFAULT NULL COMMENT '扣减来源',
  `spec_unit_id` varchar(32) DEFAULT NULL COMMENT '规格单位',
  `spec_quantity` double DEFAULT NULL COMMENT '规格数量',
  `spec_num` double DEFAULT NULL COMMENT '扣减规格数量',
  `spec_date` datetime DEFAULT NULL COMMENT '扣减日期',
  `stock_id` varchar(32) DEFAULT NULL COMMENT '对应库存明细ID',
  `person_name` varchar(32) DEFAULT NULL COMMENT '操作人',
  `del_flag` varchar(4) DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- add by mcb 2020年5月23日16:30:23 试剂用量查询
INSERT INTO `sys_permission` VALUES ('1263760305687826434', '1218785597982052353', '试剂用量查询', '/pd/ExDeductuinDosage', 'pd/query/ExDeductuinDosageQueryList', NULL, NULL, 1, '0', NULL, '1', 6.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-5-22 17:15:11', 'admin', '2020-5-22 17:15:29', 0, 0, '1', 0);

-- add by mcb 2020年5月23日16:30:23 合并采购订单明细增加字段
ALTER TABLE `pd_purchase_order_merge_detail`
ADD COLUMN `purchase_price`  decimal(20,4)   NULL  COMMENT '申购单价' AFTER `order_no`,
ADD COLUMN `price`  decimal(20,4)   NULL COMMENT '价格' AFTER `purchase_price`;

-- add by mcb 2020年5月23日16:30:23 增加扣減类型
INSERT INTO `sys_dict` VALUES ('1264129484446199809', '扣減类型', 'deductuin_type', '0:自动扣减   1:人工扣减', 0, 'admin', '2020-5-23 17:42:10', NULL, '2020-5-23 17:42:10', 0);
INSERT INTO `sys_dict_item` VALUES ('1264129617623740417', '1264129484446199809', '自动扣减', '0', '', 1, 1, 'admin', '2020-5-23 17:42:42', NULL, '2020-5-23 17:42:42');
INSERT INTO `sys_dict_item` VALUES ('1264129658933440514', '1264129484446199809', '人工扣减', '1', '', 2, 1, 'admin', '2020-5-23 17:42:52', NULL, '2020-5-23 17:42:52');

-- add by mcb 2020年5月23日16:30:23 增加检验项目ID
ALTER TABLE `ex_inspection_items`
ADD COLUMN `package_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '检验项目id' AFTER `remarks`;

-- add by mcb 2020年5月24日16:30:23 增加索引
CREATE INDEX item_doctor ON ex_inspection_items (apply_doctor,apply_department);
CREATE INDEX fsf_yhm ON ex_his_user_inf (fsf_yhm);
CREATE INDEX fsf_ksbh ON ex_his_depart_inf (fsf_ksbh);

-- add by jiangxz 2020年5月26日15:30:11 就诊流水号
ALTER TABLE `pd_dosage`
ADD COLUMN `visit_no` varchar(64) NULL DEFAULT NULL COMMENT '就诊流水号（丰城中医院）' AFTER `del_flag`;

-- add by jiangxz 2020年5月26日15:30:11 His计费ID
ALTER TABLE `pd_dosage_detail`
ADD COLUMN `his_charge_id` varchar(64) NULL DEFAULT NULL COMMENT 'His计费ID（丰城中医院）' AFTER `del_flag`,
ADD COLUMN `his_charge_item_id` varchar(64) NULL DEFAULT NULL COMMENT 'His计费明细ID（丰城中医院）' AFTER `his_charge_id`;



-- add by mcb 2020年5月26日16:30:23 增加库房类型
INSERT INTO `sys_dict_item`  VALUES ('1265113802844839938', '1243463467576647681', '三級库房', '3', '', 3, 1, 'admin', '2020-05-26 10:53:30', NULL, '2020-05-26 10:53:30');

-- add by mcb 2020年5月26日16:30:23 增加检验科室ID及检验室ID
ALTER TABLE `pd_use_package`
ADD COLUMN `test_depart_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '检验科室ID' AFTER `depart_id`;

-- add by mcb 2020年5月26日16:30:23 增加开闭瓶菜单
 INSERT INTO `sys_permission` VALUES ('1265210177987076098', '1255448486688649218', '开闭瓶管理', '/external/PdBottle', 'external/PdBottleList', NULL, NULL, 1, '0', NULL, '1', 6.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-05-26 17:16:28', 'admin', '2020-05-26 17:16:38', 0, 0, '1', 0);

-- add by mcb 2020年5月26日16:30:23 增加开闭瓶信息表
CREATE TABLE `pd_bottle_inf` (
  `id` varchar(36) NOT NULL,
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `boottle_by` varchar(32) DEFAULT NULL COMMENT '开瓶操作人',
  `boottle_date` datetime DEFAULT NULL COMMENT '开瓶时间',
  `ref_bar_code` varchar(64) DEFAULT NULL COMMENT '试剂对应条码',
  `stock_id` varchar(64) DEFAULT NULL COMMENT '对应库存明细ID',
  `close_date` datetime DEFAULT NULL COMMENT '闭瓶时间',
  `close_by` varchar(32) DEFAULT NULL COMMENT '闭瓶操作人',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `depart_id` varchar(32) DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(32) DEFAULT NULL COMMENT '所属机构',
   `del_flag` varchar(4) DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- add by zxh 2020年5月27日12:09:28 库存对照条码表
CREATE TABLE `pd_product_stock_unique_code` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `product_stock_id` varchar(64) DEFAULT NULL COMMENT '库存明细表id',
  `unique_code_order` int(11) DEFAULT NULL COMMENT '条码序号',
  `print_type` varchar(1) DEFAULT NULL COMMENT '打印类型0唯一码打印，1批量打印',
  `code_state` varchar(1) DEFAULT NULL COMMENT '条码状态0正常，1已退货，2已用完',
  `depart_id` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(64) DEFAULT NULL COMMENT '所属医院',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `del_flag` varchar(1) NOT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 -- add by mcb 2020年5月28日10:30:23 增加使用规格数量
 ALTER TABLE `pd_bottle_inf`
ADD COLUMN `spec_num` double(32,4) DEFAULT NULL COMMENT '使用规格数量';

-- add by mcb 2020年5月28日10:30:23 增加扣减类型
ALTER TABLE `pd_use_package`
ADD COLUMN `deductuin_type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '扣减类型' AFTER `test_depart_id`;

-- add by zxh 2020年5月28日09:06:43 退货新增唯一码出库
ALTER TABLE `pd_rejected`
ADD COLUMN `rejected_type`  varchar(1) NULL COMMENT '退货类型' AFTER `rejected_no`;

-- add by zxh 2020年5月28日09:06:43 退货新增唯一码出库
ALTER TABLE `pd_rejected_detail`
ADD COLUMN `ref_bar_code`  varchar(64) NULL COMMENT 'REF唯一码' AFTER `product_bar_code`;
INSERT INTO `sys_dict` VALUES ('1265848731045318657', '退货类型', 'rejected_type', '', '0', 'admin', '2020-05-28 11:33:50', null, '2020-05-28 11:33:50', '0');
INSERT INTO `sys_dict_item` VALUES ('1265848860359905282', '1265848731045318657', '唯一码退货', '0', '', '1', '1', 'admin', '2020-05-28 11:34:21', null, '2020-05-28 11:34:21');
INSERT INTO `sys_dict_item` VALUES ('1265848887836790785', '1265848731045318657', '普通码退货', '1', '', '1', '1', 'admin', '2020-05-28 11:34:28', null, '2020-05-28 11:34:28');

-- add by jiangxz 2020年5月27日21:06:24 丰城中医院收费代码
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1265628850267561985', '1210107255254798338', '丰城中医院收费代码', '/external/fengcheng/HisChargeListFCZYY', 'external/fengcheng/HisChargeListFCZYY', NULL, NULL, 1, '0', NULL, '1', 10.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-05-27 21:00:07', 'admin', '2020-05-27 21:07:37', 0, 0, '1', 0);

-- add by zxh 2020年5月28日09:06:43 退货新增唯一码出库
ALTER TABLE `pd_rejected`
ADD COLUMN `total_sum`  double(11,4) NULL COMMENT '总数量' AFTER `rejected_date`;


-- add by mcb 2020年5月29日09:06:43 库存明细表增加条码类型字段
 ALTER TABLE `pd_product_stock`
ADD COLUMN `bar_code_type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 0 COMMENT '条码类型  0：普通条码   1：唯一码' AFTER `ref_bar_code`;

-- add by jiangxz 2020年5月29日17:27:39 出库增加领用人
ALTER TABLE `pd_stock_record`
ADD COLUMN `apply_by` varchar(64) NULL COMMENT '领用人' AFTER `return_status`;
-- add by jiangxz 2020年5月30日13:48:53 出库增加唯一码
ALTER TABLE `pd_stock_record_detail`
ADD COLUMN `bar_code_type` varchar(4) NULL COMMENT '条码类型  0：普通条码   1：唯一码' AFTER `product_stock_id`;
ALTER TABLE `pd_stock_record_detail`
MODIFY COLUMN `ref_bar_code` varchar(6400) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'REF条码' AFTER `product_bar_code`;
ALTER TABLE `pd_stock_record`
ADD COLUMN `bar_code_type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '条码类型  0：普通条码   1：唯一码' AFTER `depart_id`;

-- add by zxh 2020年5月31日12:39:35 器械使用加入唯一码
ALTER TABLE `pd_dosage`
ADD COLUMN `dosage_type`  varchar(1) NULL COMMENT '使用类型，0唯一码使用和1普通码使用' AFTER `admission_date`;
ALTER TABLE `pd_dosage_detail`
ADD COLUMN `ref_bar_code`  varchar(64) NULL COMMENT '唯一码' AFTER `del_flag`;

-- add by zxh 2020年6月1日09:58:09 加入字典
INSERT INTO `sys_dict` VALUES ('1267269964379004929', '条码类型', 'bar_code_type', '', '0', 'admin', '2020-06-01 09:41:19', null, '2020-06-01 09:41:19', '0');
INSERT INTO `sys_dict` VALUES ('1267270214661513218', '条码状态', 'bar_code_state', '', '0', 'admin', '2020-06-01 09:42:19', null, '2020-06-01 09:42:19', '0');

INSERT INTO `sys_dict_item` VALUES ('1267270074752114689', '1267269964379004929', '唯一码', '1', '', '1', '1', 'admin', '2020-06-01 09:41:45', 'admin', '2020-06-01 09:49:49');
INSERT INTO `sys_dict_item` VALUES ('1267270125310255106', '1267269964379004929', '非唯一码', '0', '', '1', '1', 'admin', '2020-06-01 09:41:57', 'admin', '2020-06-01 09:49:53');
INSERT INTO `sys_dict_item` VALUES ('1267270272794566658', '1267270214661513218', '正常', '0', '', '1', '1', 'admin', '2020-06-01 09:42:32', null, '2020-06-01 09:42:32');
INSERT INTO `sys_dict_item` VALUES ('1267270302767063041', '1267270214661513218', '已退货', '1', '', '1', '1', 'admin', '2020-06-01 09:42:40', null, '2020-06-01 09:42:40');
INSERT INTO `sys_dict_item` VALUES ('1267270329421864961', '1267270214661513218', '已用完', '2', '', '1', '1', 'admin', '2020-06-01 09:42:46', null, '2020-06-01 09:42:46');

-- add by mcb 2020年6月1日16:58:09 开闭瓶记录表增加字段
ALTER TABLE `pd_bottle_inf`
ADD COLUMN `close_remarks` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '闭瓶原因' AFTER `spec_num`;

-- add by mcb 2020年6月1日16:58:09 加入字典
 INSERT INTO `sys_dict` VALUES ('1267365735160807425', '闭瓶原因', 'close_remarks', '0:已用完    1:已过期', 0, 'admin', '2020-06-01 16:01:52', NULL, '2020-06-01 16:01:52', 0);

 INSERT INTO `sys_dict_item` VALUES ('1267365811555860482', '1267365735160807425', '已用完', '0', '', 1, 1, 'admin', '2020-06-01 16:02:11', NULL, '2020-06-01 16:02:11');
 INSERT INTO `sys_dict_item` VALUES ('1267365853700227074', '1267365735160807425', '已过期', '1', '', 2, 1, 'admin', '2020-06-01 16:02:21', NULL, '2020-06-01 16:02:21');

-- add by zxh 2020年6月1日17:20:19  用户加入简码
ALTER TABLE `sys_user`
ADD COLUMN `py`  varchar(64) NULL COMMENT '拼音简码' AFTER `realname`,
ADD COLUMN `wb`  varchar(64) NULL COMMENT '五笔简码' AFTER `py`,
ADD COLUMN `zdy`  varchar(64) NULL COMMENT '自定义码' AFTER `wb`;
-- add by zxh 2020年6月2日10:36:56  删除无用的表
DROP TABLE `pd_allocation_detail_copy`;

-- add by zxh 2020年6月2日18:09:25  细节优化
update sys_dict_item set item_text = '批次码' where id = '1267270125310255106';

UPDATE sys_dict_item SET item_text = '批次码退货' WHERE id='1265848887836790785';

 -- add by mcb 2020年6月3日14:09:25 静态值优化并增加静态值
 update sys_dict set dict_name='扣减类型',description='0:自动扣减   1:人工扣减  2:无需扣减'  where id='1264129484446199809';
 INSERT INTO `sys_dict_item` VALUES ('1268018852806234114', '1264129484446199809', '无需扣减', '2', '', 3, 1, 'admin', '2020-06-03 11:17:08', NULL, '2020-06-03 11:17:08');
 update sys_dict set description='0:已扣减  1：未配置检验项目  2：未扣减  3：未配置试剂用量'  where id='1262211106076762114';
 update sys_dict_item set item_text='未配置检验项目'  where id='1262211261735772161';
 INSERT INTO `sys_dict_item` VALUES ('1268019744573984769', '1262211106076762114', '未配置试剂用量', '3', '', 4, 1, 'admin', '2020-06-03 11:20:40', NULL, '2020-06-03 11:20:40');
 INSERT INTO `sys_dict`VALUES ('1268073662989438977', '试剂使用类型', 'use_type', '1:住院使用    2：门诊使用', 0, 'admin', '2020-06-03 14:54:56', NULL, '2020-06-03 14:54:56', 0);
 INSERT INTO `sys_dict_item` VALUES ('1268073778156638210', '1268073662989438977', '住院使用', '1', '', 1, 1, 'admin', '2020-06-03 14:55:23', NULL, '2020-06-03 14:55:23');
 INSERT INTO `sys_dict_item`VALUES ('1268073803850944514', '1268073662989438977', '门诊使用', '2', '', 1, 1, 'admin', '2020-06-03 14:55:29', NULL, '2020-06-03 14:55:29');

 -- add by mcb 2020年6月3日14:09:25 添加试剂使用类型
ALTER TABLE `pd_use_package_detail`
ADD COLUMN `use_type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '试剂使用类型  默认空，1：住院   2：门诊' AFTER `depart_id`;

-- add by zxh 防止业务部重复提交   添加试剂使用类型
ALTER TABLE `pd_rejected` DROP INDEX `number`;
alter table pd_purchase_order add unique(order_no);
alter table pd_stock_record add unique(record_no);
alter table pd_rejected add unique(rejected_no);
alter table pd_package add unique(package_code);
alter table pd_package_record add unique(package_bar_code);
alter table pd_apply_order add unique(apply_no);
alter table pd_allocation_record add unique(allocation_no);
alter table pd_product_stock_check add unique(check_no);
alter table pd_purchase_order_merge add unique(merge_order_no);
alter table pd_use_package add unique(code);
alter table pd_dosage add unique(dosage_no);

-- add 2020年6月4日14:09:25 by mcb 字段設置為自增
ALTER TABLE `pd_dosage_detail`
MODIFY COLUMN `id` int(64) NOT NULL AUTO_INCREMENT FIRST;
-- add 2020年6月4日14:09:25 by mcb 字段設置為默認0
ALTER TABLE `pd_bottle_inf`
MODIFY COLUMN `spec_num` double(20, 4) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '实际使用规格数量' AFTER `del_flag`;
-- add 2020年6月4日14:20:25 by mcb 增加试剂使用类型  通用
INSERT INTO `sys_dict_item` VALUES ('1268514157836136449', '1268073662989438977', '通用', '0', '', 1, 1, 'admin', '2020-06-04 20:05:18', NULL, '2020-06-04 20:05:18');

-- add 2020年6月9日10:20:25 by mcb 增加仪器代号及名称字段
 ALTER TABLE `ex_inspection_items`
ADD COLUMN `instr_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '所属仪器代号' AFTER `package_id`,
ADD COLUMN `instr_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '所属仪器名称' AFTER `instr_code`;
-- add 2020年6月9日10:20:25 by mcb 开瓶记录表增加仪器代号字段
 ALTER TABLE `pd_bottle_inf`
ADD COLUMN `instr_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '使用仪器设备代号' AFTER `close_remarks`;

-- add 2020年6月9日14:20:25 by mcb 定时任务 从lis系统获取检验数据
 INSERT INTO `sys_quartz_job` VALUES ('1270238046440267778', 'admin', '2020-06-09 14:15:25', 0, 'admin', '2020-06-09 14:16:19', 'org.jeecg.modules.quartz.job.LisInspectionItemsTaskLisJob', '59 1/10 * * * ? *', NULL, '获取检验项目扣减库存用量定时任务(lis系统获取数据)', -1);

-- add 2020年6月9日14:20:25 by mcb 增加检验仪器设备信息表
CREATE TABLE `ex_lab_instr_inf` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `instr_code` varchar(64) DEFAULT NULL COMMENT '设备代号',
  `instr_name` varchar(64) DEFAULT NULL COMMENT '设备名称',
  `py` varchar(64) DEFAULT NULL COMMENT '拼音简码',
  `wb` varchar(64) DEFAULT NULL COMMENT '五笔简码',
  `zdy` varchar(64) DEFAULT NULL COMMENT '自定义简码',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `depart_id` varchar(64) DEFAULT NULL COMMENT '所属科室',
  `test_depart_id` varchar(64) DEFAULT NULL COMMENT '所在检验室',
  `status` varchar(1) DEFAULT NULL COMMENT '使用状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '部门',
  `depart_parent_id` varchar(64) DEFAULT NULL COMMENT '所属机构',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='zxh-赣州-实验室设备表';

-- add 2020年6月9日15:20:25 by mcb 增加检验仪器管理菜单
 INSERT INTO `sys_permission` VALUES ('1270262138543468545', '1255448486688649218', '检验仪器管理', '/external/ExLabInstrInf', 'external/ExLabInstrInfList', NULL, NULL, 1, '0', NULL, '1', 7.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-06-09 15:51:09', NULL, '2020-06-09 15:51:09', 0, 0, '1', 0);


-- add 2020年6月11日10:04:43 by zxh 打印次数
ALTER TABLE `pd_product_stock_unique_code`
ADD COLUMN `print_num`  int(11) NULL DEFAULT 0 COMMENT '打印次数' AFTER `code_state`;

-- add 2020年6月12日14:34:37 by zxh 检验包库房多选
ALTER TABLE `pd_use_package`
MODIFY COLUMN `test_depart_id`  varchar(6400) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检验科室ID' AFTER `depart_id`;

-- add 2020年6月15日14:34:37 by mcb 增加静态字典
update sys_dict set description='0:已用完    1:已过期  2:试剂迁移'   where id='1267365735160807425';
INSERT INTO `sys_dict_item` VALUES ('1272348561698361346', '1267365735160807425', '试剂迁移', '2', '', 3, 1, 'admin', '2020-06-15 10:01:51', NULL, '2020-06-15 10:01:51');

-- add 2020年6月17日14:34:37 by mcb 增加未扣减用量记录表
CREATE TABLE `ex_inspection_inf` (
  `id` varchar(36) NOT NULL,
  `code` varchar(32) DEFAULT NULL COMMENT '检验项目编号',
  `jy_id` varchar(100) DEFAULT NULL COMMENT '检验序列ID',
 `product_id` varchar(64) DEFAULT NULL COMMENT '产品ID',
  `status` varchar(64) DEFAULT NULL COMMENT '扣减状态',
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


update sys_dict set description='0:已扣减  1：未配置检验项目  2：未扣减  3：未配置试剂用量  4：部分扣减'  where id='1262211106076762114';
INSERT INTO `sys_dict_item` VALUES ('1273218429156864002', '1262211106076762114', '部分扣减', '4', '', 4, 1, 'admin', '2020-06-17 19:38:23', NULL, '2020-06-17 19:38:23');


INSERT INTO `sys_dict` VALUES ('1273248327095300098', '扣减状态', 'inspection_status', '0:已扣减  1：未扣减', 0, 'admin', '2020-06-17 21:37:12', NULL, '2020-06-17 21:37:12', 0);

INSERT INTO `sys_dict_item` VALUES ('1273248420057853954', '1273248327095300098', '已扣减', '0', '', 1, 1, 'admin', '2020-06-17 21:37:34', 'admin', '2020-06-17 21:37:43');
INSERT INTO `sys_dict_item` VALUES ('1273248493483339778', '1273248327095300098', '未扣减', '1', '', 2, 1, 'admin', '2020-06-17 21:37:51', NULL, '2020-06-17 21:37:51');

-- add by jiangxz 2020年6月18日11:27:01
ALTER TABLE `jeecg-boot`.`pd_dosage`
ADD COLUMN `out_doctor_id` varchar(64) NULL COMMENT '门诊医生id' AFTER `surgeon_name`,
ADD COLUMN `out_doctor_name` varchar(64) NULL COMMENT '门诊医生姓名' AFTER `out_doctor_id`;
ALTER TABLE `jeecg-boot`.`pd_dosage`
ADD COLUMN `out_dept_id` varchar(64) NULL COMMENT '门诊科室id' AFTER `surgeon_name`,
ADD COLUMN `out_dept_name` varchar(64) NULL COMMENT '门诊科室名称' AFTER `out_dept_id`;

-- add 2020年6月18日14:34:37 by mcb 增加入库统计报表
INSERT INTO `sys_permission` VALUES ('1273431811606933506', '1218785597982052353', '入库统计报表', '/pd/query/PdStockRecordReport', 'pd/query/PdStockRecordReportQuery', NULL, NULL, 1, '0', NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-06-18 09:46:18', NULL, '2020-06-18 09:46:18', 0, 0, '1', 0);

-- 月份表
CREATE TABLE `xnumber` (
  `month` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `xnumber`(`month`) VALUES ('2020-06-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-01-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-02-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-03-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-04-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-05-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-07-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-08-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-09-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-10-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-11-01');
INSERT INTO `xnumber`(`month`) VALUES ('2020-12-01');
INSERT INTO `xnumber`(`month`) VALUES ('2021-01-01');

-- add 2020年6月19日10:34:37 by mcb 增加试剂消耗报表
INSERT INTO `sys_permission`  VALUES ('1273795402101075969', '1218785597982052353', '试剂消耗报表', '/pd/query/PdInspectionReport', 'pd/query/PdInspectionReportQuery', NULL, NULL, 1, '0', NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-06-19 09:51:04', NULL, '2020-06-19 09:51:04', 0, 0, '1', 0);

-- add 2020年6月20日10:34:37 by mcb 增加字段
ALTER TABLE `ex_inspection_inf`
MODIFY COLUMN `depart_parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '所属父部门' AFTER `sys_org_code`,
MODIFY COLUMN `depart_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '所属部门' AFTER `depart_parent_id`,
ADD COLUMN `stock_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '库存明细ID' AFTER `depart_id`,
ADD COLUMN `ref_bar_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '唯一码编号' AFTER `stock_id`;


-- add 2020年6月21日15:20:25 by mcb 增加索引
CREATE INDEX test_item_code ON ex_inspection_items (test_item_code);
CREATE INDEX code ON pd_use_package (code);
CREATE INDEX ref_bar_code ON ex_inspection_inf (ref_bar_code);

-- add by jiangxz 2020年6月22日09:18:53 his产品信息同步表增加拼音简码和五笔简码
ALTER TABLE `ex_his_charge_inf`
ADD COLUMN `py` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼音简码' AFTER `fsf_je`,
ADD COLUMN `wb` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '五笔简码' AFTER `py`;

-- add by jiangxz 2020年6月22日14:48:43 his产品信息同步表增加所属部门
ALTER TABLE `ex_his_charge_inf`
ADD COLUMN `depart_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '所属部门' AFTER `wb`,
ADD COLUMN `depart_parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '所属部门的顶级部门' AFTER `depart_id`;

-- add by mcb 2020年6月23日14:48:43 增加字段长度
ALTER TABLE `jeecg-boot`.`ex_lab_instr_inf`
MODIFY COLUMN `test_depart_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在检验室' AFTER `depart_id`;

-- add by jiangxz 2020年6月23日16:40:10 产品价格对照(丰城人民医院)
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1274893655943897090', '1210107255254798338', '产品价格对照(丰城人民医院)', '/external/fengcheng/HisSynProductListFC', 'external/fengcheng/HisSynProductListFC', NULL, NULL, 1, '0', NULL, '1', 1.50, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-06-22 10:35:09', 'admin', '2020-06-22 10:35:30', 0, 0, '1', 0);

-- add by jiangxz 2020年6月24日 14:03:03 发票菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1275352623019450370', '1275351703808368642', '发票维护', '/pd/PdInvoiceAddList', 'pd/PdInvoiceAddList', NULL, NULL, 1, '0', NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-06-23 16:58:55', NULL, '2020-06-23 16:58:55', 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1275351703808368642', '', '发票管理', '/invoice', 'layouts/RouteView', NULL, NULL, 0, '0', NULL, '1', 3.41, 0, 'money-collect', 1, 0, 0, 0, NULL, 'admin', '2020-06-23 16:55:16', 'admin', '2020-06-23 16:56:05', 0, 0, '1', 0);

-- add by zxh 2020年6月28日15:11:20 停用启用状态
ALTER TABLE `pd_vender`
ADD COLUMN `status`  varchar(1) NULL COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
update pd_vender set `status` = '0';
INSERT INTO `sys_dict` VALUES ('1277141705347649538', '停用启用状态', 'disable_enable_status', '', '0', 'admin', '2020-06-28 15:28:05', null, '2020-06-28 15:28:05', '0');
INSERT INTO `sys_dict_item` VALUES ('1277141750193147905', '1277141705347649538', '停用', '1', '', '1', '1', 'admin', '2020-06-28 15:28:16', null, '2020-06-28 15:28:16');
INSERT INTO `sys_dict_item` VALUES ('1277141766169251842', '1277141705347649538', '启用', '0', '', '1', '1', 'admin', '2020-06-28 15:28:20', null, '2020-06-28 15:28:20');
ALTER TABLE `pd_supplier`
ADD COLUMN `status`  varchar(1) NULL COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
update pd_supplier set `status` = '0';
ALTER TABLE `pd_group`
ADD COLUMN `status`  varchar(1) NULL COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
update pd_group set `status` = '0';
ALTER TABLE `pd_unit`
ADD COLUMN `status`  varchar(1) NULL COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
update pd_unit set `status` = '0';
ALTER TABLE `pd_category`
ADD COLUMN `status`  varchar(1) NULL COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
update pd_category set `status` = '0';
ALTER TABLE `pd_product`
ADD COLUMN `status`  varchar(1) NULL COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
update pd_product set `status` = '0';
ALTER TABLE `pd_vender`
MODIFY COLUMN `status`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
ALTER TABLE `pd_supplier`
MODIFY COLUMN `status`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
ALTER TABLE `pd_group`
MODIFY COLUMN `status`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
ALTER TABLE `pd_unit`
MODIFY COLUMN `status`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
ALTER TABLE `pd_category`
MODIFY COLUMN `status`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;
ALTER TABLE `pd_product`
MODIFY COLUMN `status`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '停用启用状态0启用1停用' AFTER `depart_parent_id`;

-- add by jiangxz 2020年6月29日 20:08:21  收费项目名称字段扩长
ALTER TABLE `ex_his_charge_inf`
MODIFY COLUMN `fsf_xmmc` varchar(640) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收费项目名称' AFTER `fsf_xmbh`;

-- add by jiangxz 2020年6月29日 20:08:56 增加丰城人民医院打包相关字段
ALTER TABLE `pd_dosage_detail`
ADD COLUMN `his_package_code` varchar(64) NULL COMMENT 'His打包编号（丰城人民医院）' AFTER `his_charge_item_id`,
ADD COLUMN `his_package_name` varchar(640) NULL COMMENT 'His打包名称（丰城人民医院）' AFTER `his_package_code`,
ADD COLUMN `his_package_index` varchar(4) NULL COMMENT 'His打包序号（丰城人民医院）' AFTER `his_package_name`,
ADD COLUMN `his_package_flag` varchar(4) NULL COMMENT 'His打包标志（丰城人民医院）' AFTER `his_package_index`;

-- add by zxh 2020年6月30日14:11:29 赣州市立医院产品列表加入中标号
ALTER TABLE `pd_product`
ADD COLUMN `biding_number`  varchar(64) NULL COMMENT '中标号' AFTER `depart_parent_id`;

-- add by jiangxz 2020年7月3日 15:38:48 发票表
DROP TABLE IF EXISTS `pd_invoice`;
CREATE TABLE `pd_invoice`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `invoice_reg_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票登记号',
  `invoice_no` varchar(640) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票号',
  `invoice_code` varchar(640) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票代码',
  `invoice_data` datetime(0) NULL DEFAULT NULL COMMENT '发票日期',
  `invoice_money` decimal(12, 4) NULL DEFAULT NULL COMMENT '发票金额',
  `return_money` decimal(12, 0) NULL DEFAULT NULL COMMENT '回款金额',
  `return_data` datetime(0) NULL DEFAULT NULL COMMENT '回款日期',
  `invoice_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票类型；1-入库单发票；2-出库单发票',
  `pay_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付状态：1-已支付；2-未支付',
  `invoice_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票状态：1-未完成；2-已完成',
  `bill_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据登记人（入库单创建人）',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标识，0-正常；1-删除',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属父部门',
  `depart_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `invoice_reg_no`(`invoice_reg_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- add by jiangxz 2020年7月3日 15:38:48 发票明细表
DROP TABLE IF EXISTS `pd_invoice_detail`;
CREATE TABLE `pd_invoice_detail`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `invoice_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票id',
  `bill_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据号',
  `bill_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据id',
  `bill_detail_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单据明细id',
  `bill_date` datetime(0) NULL DEFAULT NULL COMMENT '单据日期（业务日期）',
  `product_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品id',
  `product_stock_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库存明细id',
  `num` double(11, 2) NULL DEFAULT NULL COMMENT '数量',
  `price` decimal(12, 4) NULL DEFAULT NULL COMMENT '单价',
  `money` decimal(12, 4) NULL DEFAULT NULL COMMENT '金额',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '状态1-未完成；2-已完成',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标识，0-正常；1-删除',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属父部门',
  `depart_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- add by jiangxz 2020年7月3日 15:38:48 发票查询菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1278966303141355522', '1275351703808368642', '发票查询', '/pd/PdInvoiceList', 'pd/PdInvoiceList', NULL, NULL, 1, '0', NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-07-03 16:18:23', NULL, '2020-07-03 16:18:23', 0, 0, '1', 0);

-- add by zxh 2020年7月9日15:47:25 盘点优化
ALTER TABLE `pd_product_stock_check_child`
ADD COLUMN `product_id`  varchar(64) NULL AFTER `check_no`,
ADD COLUMN `product_bar_code`  varchar(64) NULL COMMENT '产品条码' AFTER `product_id`,
ADD COLUMN `batch_no`  varchar(64) NULL COMMENT '批次' AFTER `product_bar_code`,
ADD COLUMN `exp_date`  datetime NULL COMMENT '有效期' AFTER `batch_no`,
ADD COLUMN `out_huowei_code`  varchar(255) NULL COMMENT '出库货位编号' AFTER `exp_date`;


-- add by jiangxz 2020年7月9日 15:08:33 退货出库菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1281122507485487106', '1239452985569804289', '退货出库', '/pd/PdReturnList', 'pd/PdReturnList', NULL, NULL, 1, '0', NULL, '1', 0.50, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-07-09 15:06:23', NULL, '2020-07-09 15:06:23', 0, 0, '1', 0);

-- add by zxh 2020年7月10日10:57:40 盘点优化
ALTER TABLE `pd_product_stock_check`
ADD COLUMN `audit_by`  varchar(64) NULL COMMENT '审核人' AFTER `check_status`,
ADD COLUMN `audit_date`  datetime NULL COMMENT '审核时间' AFTER `audit_by`,
ADD COLUMN `audit_status`  varchar(1) NULL COMMENT '审核状态 1-待审核； 2-审核通过； 3-已拒绝' AFTER `audit_date`,
ADD COLUMN `refuse_reason`  longtext NULL COMMENT '驳回原因' AFTER `audit_status`;

-- add by jiangxz 2020年7月13日 08:49:43  修改退货出库菜单
update sys_permission set url = '/pd/PdStockRecordReturnList',component = 'pd/PdStockRecordReturnList' where id = '1281122507485487106'
