
DROP TABLE IF EXISTS `pd_on_off`;
CREATE TABLE `pd_on_off`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开关名称',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开关编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `value` int(1) NULL DEFAULT NULL COMMENT '值',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `depart_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `depart_parent_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门的顶级部门',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '删除标识',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of pd_on_off
-- ----------------------------
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('9', '是否允许入库证照过期的产品', 'on_off_stock_in_exp_product', '目前只做了提醒，没有限制死必须不能入库', '1-允许；0-不允许', 0, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('8', '是否需要出库审批', 'on_off_stock_out_audit', NULL, '1-是；0-否', 1, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('7', '是否需要入库审批', 'on_off_stock_in_audit', NULL, '1-是；0-否', 1, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('6', '是否显示出库单抬头', 'on_off_stock_out_text', '国药集团江西医疗器械有限公司——', '1-显示；0-不显示', 0, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('5', '是否显示入库单抬头', 'on_off_stock_in_text', '国药集团江西医疗器械有限公司——', '1-显示；0-不显示', 0, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('4', '是否允许出入库时可修改进价和出价', 'on_off_allow_edit_price', '', '1-允许；0-不允许', 0, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('3', '是否允许入库非本供应商产品', 'on_off_allow_supplier', '', '1-允许；0-不允许', 1, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('2', '是否允许入库量大于订单量', 'on_off_allow_in_more_order', '', '1-允许；0-不允许', 1, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('10', '是否允许入库证照过期的供应商', 'on_off_stock_in_exp_supplier', '目前只做了提醒，没有限制死必须不能入库', '1-允许；0-不允许', 0, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
INSERT INTO `pd_on_off` (`id`, `name`, `code`, `description`, `remarks`, `value`, `create_by`, `create_time`, `update_by`, `update_time`, `depart_id`, `depart_parent_id`, `del_flag`, `sys_org_code`) VALUES ('1', '是否允许入库非订单产品', 'on_off_allow_not_order_product', '', '1-允许；0-不允许', 1, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL);
