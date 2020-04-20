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