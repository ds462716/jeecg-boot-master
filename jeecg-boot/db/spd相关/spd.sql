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
ADD COLUMN `spec_quantity`  double(11,2) NULL COMMENT '试剂规格数量' AFTER `spec_unit_id`;

-- add by zxh 20200420 产品加入单位类型
ALTER TABLE `pd_unit`
ADD COLUMN `unit_type`  varchar(1) NULL COMMENT '单位类型' AFTER `name`;

-- add by zxh 2020年4月20日11:26:18 产品加入单位类型
INSERT INTO `sys_dict` VALUES ('1252073695738155010', '单位类型', 'unit_type', '', '0', 'admin', '2020-04-20 11:16:46', null, '2020-04-20 11:16:46', '0');
INSERT INTO `sys_dict_item` VALUES ('1252073800750944258', '1252073695738155010', '包装单位', '0', '', '1', '1', 'admin', '2020-04-20 11:17:11', null, '2020-04-20 11:17:11');
INSERT INTO `sys_dict_item` VALUES ('1252073837744705538', '1252073695738155010', '使用单位', '1', '', '1', '1', 'admin', '2020-04-20 11:17:20', null, '2020-04-20 11:17:20');


-- add by mcb 20200420 库存明细表增加规格库存数量及库存占用状态
ALTER TABLE `pd_product_stock`
ADD COLUMN `spec_num`  double(32,2) DEFAULT NULL COMMENT '试剂库存规格数量',
ADD COLUMN `nestat_status` varchar(4) DEFAULT '0' COMMENT '库存占用状态 0：使用中  1：未使用';

-- add by mcb 2020年4月20日15:26:18  静态字典增加占用状态
INSERT INTO `sys_dict` VALUES ('1252139529844842498', '占用状态', 'nestat_status', '0：使用中  1：未使用', 0, 'admin', '2020-4-20 15:38:21', NULL, '2020-4-20 15:38:21', 0);
INSERT INTO `sys_dict_item` VALUES ('1252139596743991298', '1252139529844842498', '使用中', '0', '', 1, 1, 'admin', '2020-4-20 15:38:38', NULL, '2020-4-20 15:38:38');
INSERT INTO `sys_dict_item` VALUES ('1252139634337538049', '1252139529844842498', '未使用', '1', '', 2, 1, 'admin', '2020-4-20 15:38:47', NULL, '2020-4-20 15:38:47');

-- add by 2020年4月20日16:44:05 入库明细表加 规格单位 规格数量
ALTER TABLE `pd_stock_record_detail`
ADD COLUMN `spec_unit_id` varchar(64) NULL COMMENT '试剂规格单位' AFTER `supplier_id`,
ADD COLUMN `spec_quantity` double(11, 4) NULL COMMENT '试剂规格数量' AFTER `spec_unit_id`;