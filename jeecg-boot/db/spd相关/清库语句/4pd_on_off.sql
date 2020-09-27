-- ----------------------------
-- Table structure for pd_on_off
-- ----------------------------
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
INSERT INTO `pd_on_off` VALUES ('094951ddc9394585a8fdebd8096b50a1', '是否允许入库证照过期的供应商', 'on_off_stock_in_exp_supplier', '目前只做了提醒，没有限制死必须不能入库', '1-允许；0-不允许', 0, 'jxz', '2020-08-13 11:10:35', NULL, '2020-08-13 11:10:35', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('1136251ebd2a4b57b079a25042af03a6', '是否需要入库审批', 'on_off_stock_in_audit', NULL, '1-是；0-否', 0, 'jxz', '2020-08-13 00:00:00', 'jxz', '2020-09-25 09:27:31', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('1293364869915095041', '是否根据规格数量扣减库存开关', 'on_off_spec_num', NULL, '1:根据规格数量    0:不根据规格数量', 0, 'mcb', '2020-08-12 09:53:09', NULL, '2020-08-12 09:53:09', 'dea5919617234ef9854c5806d4b44efa', 'c6d7cb4deeac411cb3384b1b31278596', '0', 'A01A01');
INSERT INTO `pd_on_off` VALUES ('26b5767dd5774c02b728f401d665a434', '是否显示入库单抬头', 'on_off_stock_in_text', '国药集团江西医疗器械有限公司——', '1-显示；0-不显示', 0, 'jxz', '2020-08-13 11:10:35', NULL, '2020-08-13 11:10:35', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('6675c61c700d4acd9d12fa311db61908', '是否允许入库非订单产品', 'on_off_allow_not_order_product', '', '1-允许；0-不允许', 1, 'jxz', '2020-08-13 11:10:35', NULL, '2020-08-13 11:10:35', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('70ac7c7159d247058caa810c0b570382', '是否显示出库单抬头', 'on_off_stock_out_text', '国药集团江西医疗器械有限公司——', '1-显示；0-不显示', 0, 'jxz', '2020-08-13 11:10:35', NULL, '2020-08-13 11:10:35', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('8b4a1cb5dd674612b0a57273b86ab42a', '是否需要出库审批', 'on_off_stock_out_audit', NULL, '1-是；0-否', 0, 'jxz', '2020-08-13 00:00:00', 'jxz', '2020-09-27 09:17:12', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('a272ea107f934c649e5c043031fd3a12', '是否允许入库证照过期的产品', 'on_off_stock_in_exp_product', '目前只做了提醒，没有限制死必须不能入库', '1-允许；0-不允许', 0, 'jxz', '2020-08-13 11:10:35', NULL, '2020-08-13 11:10:35', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('a2d6174557d942d7a1ac01a4319d00f8', '是否显示二级条码框（入库、出库、退货）', 'on_off_show_s_barcode', '用于只扫产品编号，不扫二级条码的情况', '1-显示；0-不显示', 0, 'jxz', '2020-08-13 00:00:00', 'jxz', '2020-08-13 11:10:54', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('aa28814c04ae43b1aca38960d1063e23', '是否允许入库非本供应商产品', 'on_off_allow_supplier', '', '1-允许；0-不允许', 1, 'jxz', '2020-08-13 11:10:35', NULL, '2020-08-13 11:10:35', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('af2b18e5ca5546a7a6a0be66582429b1', '是否允许入库量大于订单量', 'on_off_allow_in_more_order', '', '1-允许；0-不允许', 1, 'jxz', '2020-08-13 11:10:35', NULL, '2020-08-13 11:10:35', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');
INSERT INTO `pd_on_off` VALUES ('b04911fd8c3c49f09332d117083ab9e6', '是否允许出入库时可修改进价和出价', 'on_off_allow_edit_price', '', '1-允许；0-不允许', 0, 'jxz', '2020-08-13 11:10:35', NULL, '2020-08-13 11:10:35', 'f3c6b01fe1704ef6b62657c6b2c3b9f8', 'a7f5be42ad714bbe8f6d6fd0ad89b65f', '0', 'A02A01');

-- *******************************************************
-- 注意：部署后 需要点一次“系统管理-开关配置”菜单，来初始化菜单
-- *******************************************************