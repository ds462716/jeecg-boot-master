TRUNCATE TABLE pd_apply_order;                       -- 领用信息表
TRUNCATE TABLE pd_apply_detail;                      -- 领用明细表
TRUNCATE TABLE pd_allocation_record;                -- 调拨信息表
TRUNCATE TABLE pd_allocation_detail;                 -- 调拨明细表
TRUNCATE TABLE pd_product_stock_check;             -- 盘点信息表
TRUNCATE TABLE pd_product_stock_check_child;         -- 盘点明细表
TRUNCATE TABLE pd_product_stock_total;              -- 库存信息表
TRUNCATE TABLE pd_product_stock;                    -- 库存明细表
TRUNCATE TABLE pd_purchase_order;                   -- 采购信息表
TRUNCATE TABLE pd_purchase_detail;                  -- 采购明细表
TRUNCATE TABLE pd_purchase_order_merge;             -- 采购合并信息表
TRUNCATE TABLE pd_purchase_order_merge_detail;      -- 采购合并明细表
TRUNCATE TABLE pd_bottle_inf;                        -- 开瓶闭瓶记录表
TRUNCATE TABLE pd_auto_order_inf;                    -- 自动补货表
TRUNCATE TABLE pd_use_package;                       -- 检验项目用量主表
TRUNCATE TABLE pd_use_package_detail;                -- 检验项目用量明细表
TRUNCATE TABLE ex_inspection_items;                  -- 检验项目明细表
TRUNCATE TABLE ex_inspection_items_use;              -- 检验用量手动扣减主表
TRUNCATE TABLE ex_inspection_items_use_detail;       -- 检验用量手动扣减明细表
TRUNCATE TABLE ex_his_charge_inf;                    -- HIS收费项目信息表
TRUNCATE TABLE ex_his_user_inf;                      -- HIS系统用户表
TRUNCATE TABLE ex_his_depart_inf;                    -- HIS系统部门表


TRUNCATE TABLE pd_stock_record;        -- 出入库记录
TRUNCATE TABLE pd_stock_record_detail; -- 出入库记录明细
TRUNCATE TABLE pd_stock_log;           -- 库存日志
TRUNCATE TABLE pd_package;             -- 定数包
TRUNCATE TABLE pd_package_detail;      -- 定数包明细
TRUNCATE TABLE pd_package_record;      -- 定数包打包记录
TRUNCATE TABLE pd_package_record_detail;-- 定数包打包记录明细表
TRUNCATE TABLE pd_goods_allocation;    -- 货区货位
TRUNCATE TABLE pd_rejected;            -- 退货
TRUNCATE TABLE pd_rejected_detail;     -- 退货明细
TRUNCATE TABLE pd_storeroom_patrol;     -- 巡查表
TRUNCATE TABLE pd_storeroom_patrol_detail;   -- 巡查明细表


TRUNCATE TABLE pd_product;-- 产品表
TRUNCATE TABLE pd_product_rule;-- 产品关联编码规则表
TRUNCATE TABLE pd_depart_config;-- 部门配置表
TRUNCATE TABLE pd_category;-- 产品分类
TRUNCATE TABLE pd_unit;-- 产品单位表
TRUNCATE TABLE pd_group;-- 产品组别表
TRUNCATE TABLE pd_supplier;-- 产品供应商表
TRUNCATE TABLE pd_vender;-- 产品生产厂家表
TRUNCATE TABLE pd_encoding_rule;-- 产品编码规则表
TRUNCATE TABLE pd_encoding_rule_detail;-- 产品编码规则详情表
TRUNCATE TABLE pd_encoding_identifier;-- 应用标识符表
TRUNCATE TABLE pd_dosage;-- 器械使用表
TRUNCATE TABLE pd_dosage_detail;-- 器械使用详情表
TRUNCATE TABLE pd_product_stock_unique_code;-- 条码表

-- 不用清理的表
-- pd_on_off 开关表
-- 作废
-- ex_deductuin_dosage
-- pd_spec_log



TRUNCATE TABLE sys_log;-- 系统日志表
TRUNCATE TABLE sys_data_log;-- 系统数据库操作日志表
TRUNCATE TABLE sys_announcement;-- 消息通知表
TRUNCATE TABLE sys_announcement_send;-- 消息通知详情表
TRUNCATE TABLE sys_user;-- 用户表
TRUNCATE TABLE sys_role;-- 角色表
TRUNCATE TABLE sys_user_agent;-- 用户代理人设置表
TRUNCATE TABLE sys_user_role;-- 用户角色关联表
TRUNCATE TABLE sys_depart;-- 部门表
TRUNCATE TABLE sys_user_depart;-- 用户部门关联表
TRUNCATE TABLE sys_depart_permission;-- 用户部门关联表
TRUNCATE TABLE sys_depart_role;-- 用户部门关联表
TRUNCATE TABLE sys_depart_role_permission;-- 用户部门关联表
TRUNCATE TABLE sys_depart_role_user;-- 用户部门关联表

-- 插入admin用户
INSERT INTO `sys_user` (`id`, `username`, `realname`, `password`, `salt`, `avatar`, `birthday`, `sex`, `email`, `phone`, `org_code`, `status`, `del_flag`, `activiti_sync`, `work_no`, `post`, `telephone`, `create_by`, `create_time`, `update_by`, `update_time`, `identity`, `depart_parent_id`, `depart_ids`) VALUES ('e9ca23d68d884d4ebb19d07889727dae', 'admin', '管理员', 'cb362cfeefbf3d8d', 'RCGTeGiH', 'files/20200115/1578539185_1579061977923.png', '2018-12-5 00:00:00', 1, '11@qq.com', '18566666661', 'A01', 1, 0, 1, '111', 'devleader', NULL, NULL, '2038-6-21 17:54:10', 'admin', '2020-3-3 14:15:25', 2, 'c6d7cb4deeac411cb3384b1b31278596', 'c6d7cb4deeac411cb3384b1b31278596');
-- 插入admin角色
INSERT INTO `sys_role` VALUES ('1234726827530133506', '临时角色', 'LSJS', null, 'admin', '2020-03-03 14:26:30', null, null);
INSERT INTO `sys_role` VALUES ('f6817f48af4fb3af11b9e8bf182f618b', '管理员', 'admin', '管理员', null, '2018-12-21 18:03:39', 'admin', '2019-05-20 11:40:26');
-- 插入admin角色关联
INSERT INTO `sys_user_role` VALUES ('1244503901942407169', '1244503261900005377', 'f6817f48af4fb3af11b9e8bf182f618b');
INSERT INTO `sys_user_role` VALUES ('1234724028754960386', 'e9ca23d68d884d4ebb19d07889727dae', 'f6817f48af4fb3af11b9e8bf182f618b');
-- 插入admin部门
INSERT INTO `sys_depart` (`id`, `parent_id`, `depart_name`, `depart_name_en`, `depart_name_abbr`, `depart_order`, `description`, `org_category`, `org_type`, `org_code`, `mobile`, `fax`, `address`, `memo`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `py`, `wb`, `zdy`, `depart_id`, `depart_parent_id`, `depart_type`) VALUES ('c6d7cb4deeac411cb3384b1b31278596', '', '吉水妇幼保健院', NULL, NULL, 0, NULL, '1', '1', 'A01', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-2-11 14:21:51', 'admin', '2020-1-8 13:50:20', NULL, NULL, NULL, 'c6d7cb4deeac411cb3384b1b31278596', 'c6d7cb4deeac411cb3384b1b31278596', '0');
-- 插入admin部门关联表
INSERT INTO `sys_user_depart` (`ID`, `user_id`, `dep_id`) VALUES ('1234724038464774146', 'e9ca23d68d884d4ebb19d07889727dae', 'c6d7cb4deeac411cb3384b1b31278596');









