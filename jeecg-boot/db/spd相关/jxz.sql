-- 2020年7月16日 09:52:59  初始化库存相关菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1283577818444173314', '1218784892172963842', '库存初始化', '/pd/PdProductStockInit', 'pd/PdProductStockInit', NULL, NULL, 1, '0', NULL, '1', 10.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2020-07-16 09:42:54', 'admin', '2020-07-16 09:43:13', 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1283578038078902273', '1283577818444173314', '初始化库存', '/pd/PdProductStockInitList', 'pd/PdProductStockInitList', NULL, NULL, 1, '0', NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-07-16 09:43:47', NULL, '2020-07-16 09:43:47', 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1283578138930941953', '1283577818444173314', '初始化库存审核', '/pd/PdProductStockInitExamineList', 'pd/PdProductStockInitExamineList', NULL, NULL, 1, '0', NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-07-16 09:44:11', 'admin', '2020-07-16 09:49:15', 0, 0, '1', 0);

-- 2020年7月16日 10:03:21  数据字典新增初始化库存
update sys_dict set description = '1：入库;2：出库;3：初始化库存' where id = '1229267323532144642';
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1283581530965250050', '1229267323532144642', '初始化库存', '3', '', 3, 1, 'admin', '2020-07-16 09:57:39', NULL, '2020-07-16 09:57:39');

-- 2020年7月16日 13:57:59  日志数据字典 增加初始化库存
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1283641307074359298', '1233244236134825986', '初始化库存', '13', '', 13, 1, 'admin', '2020-07-16 13:55:11', NULL, '2020-07-16 13:55:11');

-- 2020年7月17日 10:56:47 库存初始化菜单修改
update sys_permission set url='/pdProductStockInit',component='layouts/RouteView' where id = '1283577818444173314';

-- 2020年7月17日 11:23:25 配送商表
CREATE TABLE `pd_distributor`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `py` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼音简码',
  `wb` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '五笔简码',
  `zdy` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义码',
  `licence_name0` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称12',
  `licence_num0` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码12',
  `licence_date0` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期12',
  `licence_site0` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址12',
  `licence_validity0` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name1` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称1',
  `licence_num1` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码1',
  `licence_date1` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期1',
  `licence_site1` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址1',
  `licence_validity1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name2` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称2',
  `licence_num2` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码2',
  `licence_date2` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期2',
  `licence_site2` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址2',
  `licence_validity2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name3` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称3',
  `licence_num3` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码3',
  `licence_date3` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期3',
  `licence_site3` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址3',
  `licence_validity3` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name4` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称4',
  `licence_num4` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码4',
  `licence_date4` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期4',
  `licence_site4` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址4',
  `licence_validity4` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name5` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称5',
  `licence_num5` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码5',
  `licence_date5` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期5',
  `licence_site5` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址5',
  `licence_validity5` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name6` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称6',
  `licence_num6` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码6',
  `licence_date6` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期6',
  `licence_site6` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址6',
  `licence_validity6` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name7` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称7',
  `licence_num7` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码7',
  `licence_date7` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期7',
  `licence_site7` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址7',
  `licence_validity7` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name8` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称8',
  `licence_num8` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码8',
  `licence_date8` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期8',
  `licence_site8` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址8',
  `licence_validity8` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 是否过期标识，0未过期，1已过期，2近效期',
  `licence_name9` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称9',
  `licence_num9` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码9',
  `licence_date9` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期9',
  `licence_site9` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址9',
  `licence_validity9` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name10` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称10',
  `licence_num10` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码10',
  `licence_date10` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期10',
  `licence_site10` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址10',
  `licence_validity10` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `licence_name11` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照名称11',
  `licence_num11` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照号码11',
  `licence_date11` datetime(0) NULL DEFAULT NULL COMMENT '证照有效期11',
  `licence_site11` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证照地址11',
  `licence_validity11` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `validity_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否过期标识，0未过期，1已过期，2近效期',
  `msg_send_state` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '消息发送状态，近效期1，已过期2，初始值为0',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'remarks',
  `depart_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门的顶级部门',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '停用启用状态0启用1停用',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '配送商表-jiangxz' ROW_FORMAT = Compact;

-- 2020年7月17日 16:23:23 配送商菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1284039247483863041', '1210107255254798338', '配送商管理', '/pd/PdDistributorList', 'pd/PdDistributorList', NULL, NULL, 1, '0', NULL, '1', 9.50, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-07-17 16:16:28', 'admin', '2020-07-17 16:18:52', 0, 0, '1', 0);

-- 2020年7月20日 18:16:24 增加“初始化库存”供应商，用于初始化库存，以及方便后期查询
INSERT INTO `pd_supplier`(`id`, `name`, `py`, `wb`, `zdy`, `jde_code`, `licence_name0`, `licence_num0`, `licence_date0`, `licence_site0`, `licence_validity0`, `licence_name1`, `licence_num1`, `licence_date1`, `licence_site1`, `licence_validity1`, `licence_name2`, `licence_num2`, `licence_date2`, `licence_site2`, `licence_validity2`, `licence_name3`, `licence_num3`, `licence_date3`, `licence_site3`, `licence_validity3`, `licence_name4`, `licence_num4`, `licence_date4`, `licence_site4`, `licence_validity4`, `licence_name5`, `licence_num5`, `licence_date5`, `licence_site5`, `licence_validity5`, `licence_name6`, `licence_num6`, `licence_date6`, `licence_site6`, `licence_validity6`, `licence_name7`, `licence_num7`, `licence_date7`, `licence_site7`, `licence_validity7`, `licence_name8`, `licence_num8`, `licence_date8`, `licence_site8`, `licence_validity8`, `licence_name9`, `licence_num9`, `licence_date9`, `licence_site9`, `licence_validity9`, `licence_name10`, `licence_num10`, `licence_date10`, `licence_site10`, `licence_validity10`, `licence_name11`, `licence_num11`, `licence_date11`, `licence_site11`, `licence_validity11`, `create_by`, `create_time`, `update_by`, `update_time`, `sys_org_code`, `validity_flag`, `msg_send_state`, `remarks`, `depart_id`, `depart_parent_id`, `status`, `del_flag`, `supplier_type`) VALUES ('1285060963299377153', '初始化库存', 'CSHKC', 'PVWYD', '', '', '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, '', '', NULL, '', NULL, 'jxz', NULL, NULL, NULL, 'A01A01', '0', '0', '', '729a9f639aa94208829733015158080a', 'c6d7cb4deeac411cb3384b1b31278596', '0', '0', '5');

-- 2020年7月20日 19:47:18 出入库以及库存表 增加配送商id字段
ALTER TABLE `pd_stock_record`
ADD COLUMN `distributor_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配送商ID' AFTER `supplier_id`;
ALTER TABLE `pd_stock_record_detail`
ADD COLUMN `distributor_id` varchar(64) NULL COMMENT '配送商ID' AFTER `supplier_id`;
ALTER TABLE `pd_product_stock`
ADD COLUMN `distributor_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配送商ID' AFTER `supplier_id`;
-- ***************德兴医院部署截止2020年7月26日09:52:24

-- 2020年7月30日 14:34:25 增加盘点出入库
ALTER TABLE `pd_stock_record`
MODIFY COLUMN `record_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出入库类型：1-入库；2-出库；3-初始化库存；4-盘点' AFTER `record_no`,
MODIFY COLUMN `out_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库类型 : 1-申领出库; 2-科室出库; 3-调拨出库; 4-退货出库；5-盘亏出库' AFTER `record_type`,
MODIFY COLUMN `in_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库类型 : 1-正常入库，2-退货入库，3-调拨入库；4-盘盈入库' AFTER `out_type`;
ALTER TABLE `pd_stock_record`
MODIFY COLUMN `extend1` varchar(320) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展1，自动生成入库单时存对应的出库单号' AFTER `apply_by`,
MODIFY COLUMN `extend2` varchar(320) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展2，自动生成盘点出入库单时存对应的存盘点单号' AFTER `extend1`;
ALTER TABLE `pd_stock_record_detail`
MODIFY COLUMN `extend1` varchar(320) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展1，自动生成入库单时存对应的出库单号' AFTER `del_flag`,
MODIFY COLUMN `extend2` varchar(320) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展2，自动生成盘点出入库单时存对应的存盘点单号' AFTER `extend1`;

-- 2020年8月3日 11:03:19 盘点明细菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1290118650616700929', '1218785597982052353', '盘点明细', '/pd/query/PdProductStockCheckQueryList', 'pd/query/PdProductStockCheckQueryList', NULL, NULL, 1, '0', NULL, '1', 13.00, 0, '', 1, 1, 0, 0, NULL, 'admin', '2020-08-03 10:53:50', 'admin', '2020-08-03 10:56:24', 0, 0, '1', 0);
-- 2020年8月3日 16:36:22 增加当前库存数量字段
ALTER TABLE `pd_product_stock_check_child`
ADD COLUMN `stock_num` double(10, 2) NULL COMMENT '当前库存数量' AFTER `stock_id`;

-- 2020年8月5日 09:16:09 修改统计报表菜单顺序
update sys_permission set business_type = '0', sort_no = '3.41' where id = 'f0675b52d89100ee88472b6800754a08';
update sys_permission set sort_no = '3.45' where id = '1275351703808368642';
-- 2020年8月5日 09:22:52 出入库统计报表菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1290819696255049729', 'f0675b52d89100ee88472b6800754a08', '出入库统计报表', '/pd/report/RpInAndOutReport', 'pd/report/RpInAndOutReport', NULL, NULL, 1, '0', NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-08-05 09:19:32', NULL, '2020-08-05 09:19:33', 0, 0, '1', 0);

-- 2020年8月11日 10:59:18 增加套包管理菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `business_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1293016386424840194', '', '套包管理', '/package', 'layouts/RouteView', NULL, NULL, 0, '0', NULL, '1', 3.35, 0, 'shopping', 1, 0, 0, 0, NULL, 'admin', '2020-08-11 10:48:24', 'admin', '2020-08-11 10:54:06', 0, 0, '1', 0);
update sys_permission set parent_id = '1293016386424840194',name = '套包管理',sort_no = '1' where id = '1223876921657380866';
update sys_permission set parent_id = '1293016386424840194',name = '打包管理',sort_no = '2' where id = '1254291714732748802';

-- 2020年8月12日 14:43:58 打包编号
ALTER TABLE `pd_package_record`
ADD COLUMN `record_no` varchar(64) NULL COMMENT '打包编号' AFTER `depart_parent_id`;
-- ***************赣州肿瘤医院部署截止 2020年8月26日16:22:29

-- 2020年8月20日 10:52:44 入库ID 打包记录id 打包记录明细id
ALTER TABLE `pd_product_stock`
ADD COLUMN `record_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库ID' AFTER `depart_parent_id`,
ADD COLUMN `package_record_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打包记录ID' AFTER `record_detail_id`,
ADD COLUMN `package_record_detail_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打包记录明细ID' AFTER `package_record_id`;

-- 2020年8月20日 16:02:13
ALTER TABLE `pd_product_stock`
MODIFY COLUMN `record_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库ID' AFTER `depart_parent_id`,
MODIFY COLUMN `record_detail_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库明细Id' AFTER `record_id`;

-- 2020年8月20日 14:16:19 打包记录明细id
ALTER TABLE `pd_stock_record_detail`
ADD COLUMN `package_record_detail_id` varchar(36) NULL COMMENT '打包记录明细ID' AFTER `package_record_id`;

-- 2020年8月20日 15:23:29  出库报表增加入库id字段
ALTER TABLE `pd_stock_record_detail`
ADD COLUMN `in_record_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库ID（用于出库单）' AFTER `registration`,
ADD COLUMN `in_record_detail_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库明细Id（用于出库单）' AFTER `in_record_id`;

-- 2020年9月15日 15:37:47  入库明细增加供应商单据号
ALTER TABLE `pd_stock_record_detail`
ADD COLUMN `supplier_bill_no` varchar(64) NULL COMMENT '供应商单据号' AFTER `bar_code_type`;
-- 2020年9月16日 09:10:17  入库明细增加发票号字段
ALTER TABLE `pd_stock_record_detail`
ADD COLUMN `invoice_no` varchar(640) NULL COMMENT '发票号' AFTER `supplier_bill_no`;

-- 赣州肿瘤截止 2020年9月21日15:38:01