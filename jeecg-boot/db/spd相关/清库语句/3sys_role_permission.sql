/*
Navicat MySQL Data Transfer

Source Server         : 阿里云
Source Server Version : 50628
Source Host           : 119.23.56.26:3366
Source Database       : jeecg-boot

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2020-03-31 14:57:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_group_role_per_id` (`role_id`,`permission_id`) USING BTREE,
  KEY `index_group_role_id` (`role_id`) USING BTREE,
  KEY `index_group_per_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='角色权限表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('00b82058779cca5106fbb84783534c9b', 'f6817f48af4fb3af11b9e8bf182f618b', '4148ec82b6acd69f470bea75fe41c357', null);
INSERT INTO `sys_role_permission` VALUES ('0254c0b25694ad5479e6d6935bbc176e', 'f6817f48af4fb3af11b9e8bf182f618b', '944abf0a8fc22fe1f1154a389a574154', null);
INSERT INTO `sys_role_permission` VALUES ('09bd4fc30ffe88c4a44ed3868f442719', 'f6817f48af4fb3af11b9e8bf182f618b', 'e6bfd1fcabfd7942fdd05f076d1dad38', null);
INSERT INTO `sys_role_permission` VALUES ('0c2d2db76ee3aa81a4fe0925b0f31365', 'f6817f48af4fb3af11b9e8bf182f618b', '024f1fd1283dc632458976463d8984e1', null);
INSERT INTO `sys_role_permission` VALUES ('0c6b8facbb1cc874964c87a8cf01e4b1', 'f6817f48af4fb3af11b9e8bf182f618b', '841057b8a1bef8f6b4b20f9a618a7fa6', null);
INSERT INTO `sys_role_permission` VALUES ('0c6e1075e422972083c3e854d9af7851', 'f6817f48af4fb3af11b9e8bf182f618b', '08e6b9dc3c04489c8e1ff2ce6f105aa4', null);
INSERT INTO `sys_role_permission` VALUES ('0e1469997af2d3b97fff56a59ee29eeb', 'f6817f48af4fb3af11b9e8bf182f618b', 'e41b69c57a941a3bbcce45032fe57605', null);
INSERT INTO `sys_role_permission` VALUES ('0f861cb988fdc639bb1ab943471f3a72', 'f6817f48af4fb3af11b9e8bf182f618b', '97c8629abc7848eccdb6d77c24bb3ebb', null);
INSERT INTO `sys_role_permission` VALUES ('1185039870491439105', 'f6817f48af4fb3af11b9e8bf182f618b', '1174506953255182338', null);
INSERT INTO `sys_role_permission` VALUES ('1185039870529187841', 'f6817f48af4fb3af11b9e8bf182f618b', '1174590283938041857', null);
INSERT INTO `sys_role_permission` VALUES ('1185039870537576450', 'f6817f48af4fb3af11b9e8bf182f618b', '1166535831146504193', null);
INSERT INTO `sys_role_permission` VALUES ('1197431682208206850', 'f6817f48af4fb3af11b9e8bf182f618b', '1192318987661234177', null);
INSERT INTO `sys_role_permission` VALUES ('1197795315916271617', 'f6817f48af4fb3af11b9e8bf182f618b', '109c78a583d4693ce2f16551b7786786', null);
INSERT INTO `sys_role_permission` VALUES ('1197795316268593154', 'f6817f48af4fb3af11b9e8bf182f618b', '9fe26464838de2ea5e90f2367e35efa0', null);
INSERT INTO `sys_role_permission` VALUES ('1209423530518761473', 'f6817f48af4fb3af11b9e8bf182f618b', '1205097455226462210', null);
INSERT INTO `sys_role_permission` VALUES ('1209423530594258945', 'f6817f48af4fb3af11b9e8bf182f618b', '1205098241075453953', null);
INSERT INTO `sys_role_permission` VALUES ('1209423530606841858', 'f6817f48af4fb3af11b9e8bf182f618b', '1205306106780364802', null);
INSERT INTO `sys_role_permission` VALUES ('1209423580355481602', 'f6817f48af4fb3af11b9e8bf182f618b', '190c2b43bec6a5f7a4194a85db67d96a', null);
INSERT INTO `sys_role_permission` VALUES ('1209654501558046722', 'f6817f48af4fb3af11b9e8bf182f618b', 'f2849d3814fc97993bfc519ae6bbf049', null);
INSERT INTO `sys_role_permission` VALUES ('1210107554648412161', 'f6817f48af4fb3af11b9e8bf182f618b', '1210107255254798338', null);
INSERT INTO `sys_role_permission` VALUES ('1210107554673577986', 'f6817f48af4fb3af11b9e8bf182f618b', '1210107416018276354', null);
INSERT INTO `sys_role_permission` VALUES ('1210107554681966593', 'f6817f48af4fb3af11b9e8bf182f618b', '1210107503263993857', null);
INSERT INTO `sys_role_permission` VALUES ('1214742940209131522', 'f6817f48af4fb3af11b9e8bf182f618b', '1214742568912564225', null);
INSERT INTO `sys_role_permission` VALUES ('1215076474601947137', 'f6817f48af4fb3af11b9e8bf182f618b', '1215076277134114817', null);
INSERT INTO `sys_role_permission` VALUES ('1216982914098798593', 'f6817f48af4fb3af11b9e8bf182f618b', '1216982732514795522', null);
INSERT INTO `sys_role_permission` VALUES ('1217012395375190017', 'f6817f48af4fb3af11b9e8bf182f618b', '1217011811901313026', null);
INSERT INTO `sys_role_permission` VALUES ('1217254560604999682', 'f6817f48af4fb3af11b9e8bf182f618b', '1217254447035830273', null);
INSERT INTO `sys_role_permission` VALUES ('1217375645782577153', 'f6817f48af4fb3af11b9e8bf182f618b', '1216997965862551554', null);
INSERT INTO `sys_role_permission` VALUES ('1223100121045053441', 'f6817f48af4fb3af11b9e8bf182f618b', '1218787201875513345', null);
INSERT INTO `sys_role_permission` VALUES ('1223100121061830658', 'f6817f48af4fb3af11b9e8bf182f618b', '1218787512421781505', null);
INSERT INTO `sys_role_permission` VALUES ('1223100121061830659', 'f6817f48af4fb3af11b9e8bf182f618b', '1218784310368473089', null);
INSERT INTO `sys_role_permission` VALUES ('1223878397968388097', 'f6817f48af4fb3af11b9e8bf182f618b', '1223876921657380866', null);
INSERT INTO `sys_role_permission` VALUES ('1225971623813722113', 'f6817f48af4fb3af11b9e8bf182f618b', '277bfabef7d76e89b33062b16a9a5020', null);
INSERT INTO `sys_role_permission` VALUES ('1225971623830499330', 'f6817f48af4fb3af11b9e8bf182f618b', '1170592628746878978', null);
INSERT INTO `sys_role_permission` VALUES ('1225971623830499331', 'f6817f48af4fb3af11b9e8bf182f618b', 'e8af452d8948ea49d37c934f5100ae6a', null);
INSERT INTO `sys_role_permission` VALUES ('1225971623830499332', 'f6817f48af4fb3af11b9e8bf182f618b', '1218728743440543746', null);
INSERT INTO `sys_role_permission` VALUES ('1225971623838887938', 'f6817f48af4fb3af11b9e8bf182f618b', '1218785597982052353', null);
INSERT INTO `sys_role_permission` VALUES ('1226792659727753218', 'f6817f48af4fb3af11b9e8bf182f618b', '1218784892172963842', null);
INSERT INTO `sys_role_permission` VALUES ('1226792659861970946', 'f6817f48af4fb3af11b9e8bf182f618b', '1218804086247591938', null);
INSERT INTO `sys_role_permission` VALUES ('1226792659870359553', 'f6817f48af4fb3af11b9e8bf182f618b', '1218804153767497730', null);
INSERT INTO `sys_role_permission` VALUES ('1227069123773624322', 'f6817f48af4fb3af11b9e8bf182f618b', '1218804573101428738', null);
INSERT INTO `sys_role_permission` VALUES ('1227074431324790785', 'f6817f48af4fb3af11b9e8bf182f618b', '1227074338542592001', null);
INSERT INTO `sys_role_permission` VALUES ('1227096974791950338', 'f6817f48af4fb3af11b9e8bf182f618b', '1227095215092666369', null);
INSERT INTO `sys_role_permission` VALUES ('1227116800771190785', 'f6817f48af4fb3af11b9e8bf182f618b', '1227116578942840834', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255293579265', 'f6817f48af4fb3af11b9e8bf182f618b', '1227835511816474626', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255306162178', 'f6817f48af4fb3af11b9e8bf182f618b', '1218788335595884546', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255306162179', 'f6817f48af4fb3af11b9e8bf182f618b', '1218803434842820609', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255306162180', 'f6817f48af4fb3af11b9e8bf182f618b', '1218803535120240641', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255306162181', 'f6817f48af4fb3af11b9e8bf182f618b', '1218803853975425025', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255306162182', 'f6817f48af4fb3af11b9e8bf182f618b', '1218803953644670977', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255306162183', 'f6817f48af4fb3af11b9e8bf182f618b', '1218804015787479041', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255314550785', 'f6817f48af4fb3af11b9e8bf182f618b', '1218804229155917826', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255314550786', 'f6817f48af4fb3af11b9e8bf182f618b', '1218804449637896193', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255314550787', 'f6817f48af4fb3af11b9e8bf182f618b', '1218804657369190401', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255314550788', 'f6817f48af4fb3af11b9e8bf182f618b', '1218804727791554562', null);
INSERT INTO `sys_role_permission` VALUES ('1228542255314550789', 'f6817f48af4fb3af11b9e8bf182f618b', '1218804802164953090', null);
INSERT INTO `sys_role_permission` VALUES ('1228545310445584386', 'f6817f48af4fb3af11b9e8bf182f618b', '1228543840199426050', null);
INSERT INTO `sys_role_permission` VALUES ('1228545310445584387', 'f6817f48af4fb3af11b9e8bf182f618b', '1228544416329023489', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603104116738', 'f6817f48af4fb3af11b9e8bf182f618b', '1229596888779501569', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603154448386', 'f6817f48af4fb3af11b9e8bf182f618b', '1229597152689303553', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603154448387', 'f6817f48af4fb3af11b9e8bf182f618b', '1229597308742578178', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603154448388', 'f6817f48af4fb3af11b9e8bf182f618b', '1229597419832913921', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603162836994', 'f6817f48af4fb3af11b9e8bf182f618b', '1229597544651206657', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603171225602', 'f6817f48af4fb3af11b9e8bf182f618b', '1229598028015382530', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603171225603', 'f6817f48af4fb3af11b9e8bf182f618b', '1229598148647759873', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603179614210', 'f6817f48af4fb3af11b9e8bf182f618b', '1229598256474927106', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603179614211', 'f6817f48af4fb3af11b9e8bf182f618b', '1229578873631465473', null);
INSERT INTO `sys_role_permission` VALUES ('1229600603179614212', 'f6817f48af4fb3af11b9e8bf182f618b', '1229598995830059010', null);
INSERT INTO `sys_role_permission` VALUES ('1234698907435429890', 'f6817f48af4fb3af11b9e8bf182f618b', '1232828781594730498', null);
INSERT INTO `sys_role_permission` VALUES ('1234726885214396418', '1234726827530133506', '1232828781594730498', null);
INSERT INTO `sys_role_permission` VALUES ('1235085845729689602', '1234726827530133506', '1235083941888966658', null);
INSERT INTO `sys_role_permission` VALUES ('1237634555395792898', 'f6817f48af4fb3af11b9e8bf182f618b', '1235083941888966658', null);
INSERT INTO `sys_role_permission` VALUES ('1237634555446124546', 'f6817f48af4fb3af11b9e8bf182f618b', '1235100965553881090', null);
INSERT INTO `sys_role_permission` VALUES ('1237634555446124547', 'f6817f48af4fb3af11b9e8bf182f618b', '1235103274027499522', null);
INSERT INTO `sys_role_permission` VALUES ('1237634555446124548', 'f6817f48af4fb3af11b9e8bf182f618b', 'b6bcee2ccc854052d3cc3e9c96d90197', null);
INSERT INTO `sys_role_permission` VALUES ('1237634555446124549', 'f6817f48af4fb3af11b9e8bf182f618b', '1224641973866467330', null);
INSERT INTO `sys_role_permission` VALUES ('1237634555454513153', 'f6817f48af4fb3af11b9e8bf182f618b', '1209731624921534465', null);
INSERT INTO `sys_role_permission` VALUES ('1237634555454513154', 'f6817f48af4fb3af11b9e8bf182f618b', '1237623063769853953', null);
INSERT INTO `sys_role_permission` VALUES ('1237634555454513155', 'f6817f48af4fb3af11b9e8bf182f618b', '1229674163694841857', null);
INSERT INTO `sys_role_permission` VALUES ('1244503730621865985', 'f6817f48af4fb3af11b9e8bf182f618b', '1239452985569804289', null);
INSERT INTO `sys_role_permission` VALUES ('1244503730634448898', 'f6817f48af4fb3af11b9e8bf182f618b', '1239453470183882753', null);
INSERT INTO `sys_role_permission` VALUES ('126ea9faebeec2b914d6d9bef957afb6', 'f6817f48af4fb3af11b9e8bf182f618b', 'f1cb187abf927c88b89470d08615f5ac', null);
INSERT INTO `sys_role_permission` VALUES ('154edd0599bd1dc2c7de220b489cd1e2', 'f6817f48af4fb3af11b9e8bf182f618b', '7ac9eb9ccbde2f7a033cd4944272bf1e', null);
INSERT INTO `sys_role_permission` VALUES ('165acd6046a0eaf975099f46a3c898ea', 'f6817f48af4fb3af11b9e8bf182f618b', '4f66409ef3bbd69c1d80469d6e2a885e', null);
INSERT INTO `sys_role_permission` VALUES ('1664b92dff13e1575e3a929caa2fa14d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd2bbf9ebca5a8fa2e227af97d2da7548', null);
INSERT INTO `sys_role_permission` VALUES ('1c1dbba68ef1817e7fb19c822d2854e8', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb367426764077dcf94640c843733985', null);
INSERT INTO `sys_role_permission` VALUES ('20e53c87a785688bdc0a5bb6de394ef1', 'f6817f48af4fb3af11b9e8bf182f618b', '540a2936940846cb98114ffb0d145cb8', null);
INSERT INTO `sys_role_permission` VALUES ('25491ecbd5a9b34f09c8bc447a10ede1', 'f6817f48af4fb3af11b9e8bf182f618b', 'd07a2c87a451434c99ab06296727ec4f', null);
INSERT INTO `sys_role_permission` VALUES ('29fb6b0ad59a7e911c8d27e0bdc42d23', 'f6817f48af4fb3af11b9e8bf182f618b', '9a90363f216a6a08f32eecb3f0bf12a3', null);
INSERT INTO `sys_role_permission` VALUES ('2ad37346c1b83ddeebc008f6987b2227', 'f6817f48af4fb3af11b9e8bf182f618b', '8d1ebd663688965f1fd86a2f0ead3416', null);
INSERT INTO `sys_role_permission` VALUES ('38a2e55db0960262800576e34b3af44c', 'f6817f48af4fb3af11b9e8bf182f618b', '5c2f42277948043026b7a14692456828', null);
INSERT INTO `sys_role_permission` VALUES ('3b1886f727ac503c93fecdd06dcb9622', 'f6817f48af4fb3af11b9e8bf182f618b', 'c431130c0bc0ec71b0a5be37747bb36a', null);
INSERT INTO `sys_role_permission` VALUES ('3e4e38f748b8d87178dd62082e5b7b60', 'f6817f48af4fb3af11b9e8bf182f618b', '7960961b0063228937da5fa8dd73d371', null);
INSERT INTO `sys_role_permission` VALUES ('3f1d04075e3c3254666a4138106a4e51', 'f6817f48af4fb3af11b9e8bf182f618b', '3fac0d3c9cd40fa53ab70d4c583821f8', null);
INSERT INTO `sys_role_permission` VALUES ('4204f91fb61911ba8ce40afa7c02369f', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d', null);
INSERT INTO `sys_role_permission` VALUES ('439568ff7db6f329bf6dd45b3dfc9456', 'f6817f48af4fb3af11b9e8bf182f618b', '7593c9e3523a17bca83b8d7fe8a34e58', null);
INSERT INTO `sys_role_permission` VALUES ('444126230885d5d38b8fa6072c9f43f8', 'f6817f48af4fb3af11b9e8bf182f618b', 'f780d0d3083d849ccbdb1b1baee4911d', null);
INSERT INTO `sys_role_permission` VALUES ('445656dd187bd8a71605f4bbab1938a3', 'f6817f48af4fb3af11b9e8bf182f618b', '020b06793e4de2eee0007f603000c769', null);
INSERT INTO `sys_role_permission` VALUES ('455cdb482457f529b79b479a2ff74427', 'f6817f48af4fb3af11b9e8bf182f618b', 'e1979bb53e9ea51cecc74d86fd9d2f64', null);
INSERT INTO `sys_role_permission` VALUES ('45a358bb738782d1a0edbf7485e81459', 'f6817f48af4fb3af11b9e8bf182f618b', '0ac2ad938963b6c6d1af25477d5b8b51', null);
INSERT INTO `sys_role_permission` VALUES ('4e0a37ed49524df5f08fc6593aee875c', 'f6817f48af4fb3af11b9e8bf182f618b', 'f23d9bfff4d9aa6b68569ba2cff38415', null);
INSERT INTO `sys_role_permission` VALUES ('4ea403fc1d19feb871c8bdd9f94a4ecc', 'f6817f48af4fb3af11b9e8bf182f618b', '2e42e3835c2b44ec9f7bc26c146ee531', null);
INSERT INTO `sys_role_permission` VALUES ('4f2fd4a190db856e21476de2704bbd99', 'f6817f48af4fb3af11b9e8bf182f618b', '1a0811914300741f4e11838ff37a1d3a', null);
INSERT INTO `sys_role_permission` VALUES ('520b5989e6fe4a302a573d4fee12a40a', 'f6817f48af4fb3af11b9e8bf182f618b', '6531cf3421b1265aeeeabaab5e176e6d', null);
INSERT INTO `sys_role_permission` VALUES ('54fdf85e52807bdb32ce450814abc256', 'f6817f48af4fb3af11b9e8bf182f618b', 'cc50656cf9ca528e6f2150eba4714ad2', null);
INSERT INTO `sys_role_permission` VALUES ('5d230e6cd2935c4117f6cb9a7a749e39', 'f6817f48af4fb3af11b9e8bf182f618b', 'fc810a2267dd183e4ef7c71cc60f4670', null);
INSERT INTO `sys_role_permission` VALUES ('5e4015a9a641cbf3fb5d28d9f885d81a', 'f6817f48af4fb3af11b9e8bf182f618b', '2dbbafa22cda07fa5d169d741b81fe12', null);
INSERT INTO `sys_role_permission` VALUES ('60eda4b4db138bdb47edbe8e10e71675', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb07ca05a3e13674dbf6d3245956da2e', null);
INSERT INTO `sys_role_permission` VALUES ('61835e48f3e675f7d3f5c9dd3a10dcf3', 'f6817f48af4fb3af11b9e8bf182f618b', 'f0675b52d89100ee88472b6800754a08', null);
INSERT INTO `sys_role_permission` VALUES ('660fbc40bcb1044738f7cabdf1708c28', 'f6817f48af4fb3af11b9e8bf182f618b', 'b3c824fc22bd953e2eb16ae6914ac8f9', null);
INSERT INTO `sys_role_permission` VALUES ('66b202f8f84fe766176b3f51071836ef', 'f6817f48af4fb3af11b9e8bf182f618b', '1367a93f2c410b169faa7abcbad2f77c', null);
INSERT INTO `sys_role_permission` VALUES ('6b605c261ffbc8ac8a98ae33579c8c78', 'f6817f48af4fb3af11b9e8bf182f618b', 'fba41089766888023411a978d13c0aa4', null);
INSERT INTO `sys_role_permission` VALUES ('6c74518eb6bb9a353f6a6c459c77e64b', 'f6817f48af4fb3af11b9e8bf182f618b', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', null);
INSERT INTO `sys_role_permission` VALUES ('6fb4c2142498dd6d5b6c014ef985cb66', 'f6817f48af4fb3af11b9e8bf182f618b', '6e73eb3c26099c191bf03852ee1310a1', null);
INSERT INTO `sys_role_permission` VALUES ('76a54a8cc609754360bf9f57e7dbb2db', 'f6817f48af4fb3af11b9e8bf182f618b', 'c65321e57b7949b7a975313220de0422', null);
INSERT INTO `sys_role_permission` VALUES ('7a6bca9276c128309c80d21e795c66c6', 'f6817f48af4fb3af11b9e8bf182f618b', '54097c6a3cf50fad0793a34beff1efdf', null);
INSERT INTO `sys_role_permission` VALUES ('7ca833caa5eac837b7200d8b6de8b2e3', 'f6817f48af4fb3af11b9e8bf182f618b', 'fedfbf4420536cacc0218557d263dfea', null);
INSERT INTO `sys_role_permission` VALUES ('884f147c20e003cc80ed5b7efa598cbe', 'f6817f48af4fb3af11b9e8bf182f618b', 'e5973686ed495c379d829ea8b2881fc6', null);
INSERT INTO `sys_role_permission` VALUES ('8b09925bdc194ab7f3559cd3a7ea0507', 'f6817f48af4fb3af11b9e8bf182f618b', 'ebb9d82ea16ad864071158e0c449d186', null);
INSERT INTO `sys_role_permission` VALUES ('8d154c2382a8ae5c8d1b84bd38df2a93', 'f6817f48af4fb3af11b9e8bf182f618b', 'd86f58e7ab516d3bc6bfb1fe10585f97', null);
INSERT INTO `sys_role_permission` VALUES ('8dd64f65a1014196078d0882f767cd85', 'f6817f48af4fb3af11b9e8bf182f618b', 'e3c13679c73a4f829bcff2aba8fd68b1', null);
INSERT INTO `sys_role_permission` VALUES ('8e3dc1671abad4f3c83883b194d2e05a', 'f6817f48af4fb3af11b9e8bf182f618b', 'b1cb0a3fedf7ed0e4653cb5a229837ee', null);
INSERT INTO `sys_role_permission` VALUES ('905bf419332ebcb83863603b3ebe30f0', 'f6817f48af4fb3af11b9e8bf182f618b', '8fb8172747a78756c11916216b8b8066', null);
INSERT INTO `sys_role_permission` VALUES ('9380121ca9cfee4b372194630fce150e', 'f6817f48af4fb3af11b9e8bf182f618b', '65a8f489f25a345836b7f44b1181197a', null);
INSERT INTO `sys_role_permission` VALUES ('94911fef73a590f6824105ebf9b6cab3', 'f6817f48af4fb3af11b9e8bf182f618b', '8b3bff2eee6f1939147f5c68292a1642', null);
INSERT INTO `sys_role_permission` VALUES ('9700d20dbc1ae3cbf7de1c810b521fe6', 'f6817f48af4fb3af11b9e8bf182f618b', 'ec8d607d0156e198b11853760319c646', null);
INSERT INTO `sys_role_permission` VALUES ('980171fda43adfe24840959b1d048d4d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', null);
INSERT INTO `sys_role_permission` VALUES ('987c23b70873bd1d6dca52f30aafd8c2', 'f6817f48af4fb3af11b9e8bf182f618b', '00a2a0ae65cdca5e93209cdbde97cbe6', null);
INSERT INTO `sys_role_permission` VALUES ('9b2ad767f9861e64a20b097538feafd3', 'f6817f48af4fb3af11b9e8bf182f618b', '73678f9daa45ed17a3674131b03432fb', null);
INSERT INTO `sys_role_permission` VALUES ('9d980ec0489040e631a9c24a6af42934', 'f6817f48af4fb3af11b9e8bf182f618b', '05b3c82ddb2536a4a5ee1a4c46b5abef', null);
INSERT INTO `sys_role_permission` VALUES ('a307a9349ad64a2eff8ab69582fa9be4', 'f6817f48af4fb3af11b9e8bf182f618b', '0620e402857b8c5b605e1ad9f4b89350', null);
INSERT INTO `sys_role_permission` VALUES ('a5d25fdb3c62904a8474182706ce11a0', 'f6817f48af4fb3af11b9e8bf182f618b', '418964ba087b90a84897b62474496b93', null);
INSERT INTO `sys_role_permission` VALUES ('ae1852fb349d8513eb3fdc173da3ee56', 'f6817f48af4fb3af11b9e8bf182f618b', '8d4683aacaa997ab86b966b464360338', null);
INSERT INTO `sys_role_permission` VALUES ('af60ac8fafd807ed6b6b354613b9ccbc', 'f6817f48af4fb3af11b9e8bf182f618b', '58857ff846e61794c69208e9d3a85466', null);
INSERT INTO `sys_role_permission` VALUES ('b0c8a20800b8bf1ebdd7be473bceb44f', 'f6817f48af4fb3af11b9e8bf182f618b', '58b9204feaf07e47284ddb36cd2d8468', null);
INSERT INTO `sys_role_permission` VALUES ('b128ebe78fa5abb54a3a82c6689bdca3', 'f6817f48af4fb3af11b9e8bf182f618b', 'aedbf679b5773c1f25e9f7b10111da73', null);
INSERT INTO `sys_role_permission` VALUES ('b21b07951bb547b09cc85624a841aea0', 'f6817f48af4fb3af11b9e8bf182f618b', '4356a1a67b564f0988a484f5531fd4d9', null);
INSERT INTO `sys_role_permission` VALUES ('b64c4ab9cd9a2ea8ac1e9db5fb7cf522', 'f6817f48af4fb3af11b9e8bf182f618b', '2aeddae571695cd6380f6d6d334d6e7d', null);
INSERT INTO `sys_role_permission` VALUES ('bbec16ad016efec9ea2def38f4d3d9dc', 'f6817f48af4fb3af11b9e8bf182f618b', '13212d3416eb690c2e1d5033166ff47a', null);
INSERT INTO `sys_role_permission` VALUES ('be8e5a9080569e59863f20c4c57a8e22', 'f6817f48af4fb3af11b9e8bf182f618b', '22d6a3d39a59dd7ea9a30acfa6bfb0a5', null);
INSERT INTO `sys_role_permission` VALUES ('bea2986432079d89203da888d99b3f16', 'f6817f48af4fb3af11b9e8bf182f618b', '54dd5457a3190740005c1bfec55b1c34', null);
INSERT INTO `sys_role_permission` VALUES ('c09373ebfc73fb5740db5ff02cba4f91', 'f6817f48af4fb3af11b9e8bf182f618b', '339329ed54cf255e1f9392e84f136901', null);
INSERT INTO `sys_role_permission` VALUES ('c56fb1658ee5f7476380786bf5905399', 'f6817f48af4fb3af11b9e8bf182f618b', 'de13e0f6328c069748de7399fcc1dbbd', null);
INSERT INTO `sys_role_permission` VALUES ('c6fee38d293b9d0596436a0cbd205070', 'f6817f48af4fb3af11b9e8bf182f618b', '4f84f9400e5e92c95f05b554724c2b58', null);
INSERT INTO `sys_role_permission` VALUES ('c90b0b01c7ca454d2a1cb7408563e696', 'f6817f48af4fb3af11b9e8bf182f618b', '882a73768cfd7f78f3a37584f7299656', null);
INSERT INTO `sys_role_permission` VALUES ('cf2ef620217673e4042f695743294f01', 'f6817f48af4fb3af11b9e8bf182f618b', '717f6bee46f44a3897eca9abd6e2ec44', null);
INSERT INTO `sys_role_permission` VALUES ('cf43895aef7fc684669483ab00ef2257', 'f6817f48af4fb3af11b9e8bf182f618b', '700b7f95165c46cc7a78bf227aa8fed3', null);
INSERT INTO `sys_role_permission` VALUES ('d281a95b8f293d0fa2a136f46c4e0b10', 'f6817f48af4fb3af11b9e8bf182f618b', '5c8042bd6c601270b2bbd9b20bccc68b', null);
INSERT INTO `sys_role_permission` VALUES ('d37ad568e26f46ed0feca227aa9c2ffa', 'f6817f48af4fb3af11b9e8bf182f618b', '9502685863ab87f0ad1134142788a385', null);
INSERT INTO `sys_role_permission` VALUES ('d3ddcacee1acdfaa0810618b74e38ef2', 'f6817f48af4fb3af11b9e8bf182f618b', 'c6cf95444d80435eb37b2f9db3971ae6', null);
INSERT INTO `sys_role_permission` VALUES ('d83282192a69514cfe6161b3087ff962', 'f6817f48af4fb3af11b9e8bf182f618b', '53a9230444d33de28aa11cc108fb1dba', null);
INSERT INTO `sys_role_permission` VALUES ('d8a5c9079df12090e108e21be94b4fd7', 'f6817f48af4fb3af11b9e8bf182f618b', '078f9558cdeab239aecb2bda1a8ed0d1', null);
INSERT INTO `sys_role_permission` VALUES ('de8f43229e351d34af3c95b1b9f0a15d', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160ae432231af', null);
INSERT INTO `sys_role_permission` VALUES ('e7467726ee72235baaeb47df04a35e73', 'f6817f48af4fb3af11b9e8bf182f618b', 'e08cb190ef230d5d4f03824198773950', null);
INSERT INTO `sys_role_permission` VALUES ('eaef4486f1c9b0408580bbfa2037eb66', 'f6817f48af4fb3af11b9e8bf182f618b', '2a470fc0c3954d9dbb61de6d80846549', null);
INSERT INTO `sys_role_permission` VALUES ('ec4bc97829ab56afd83f428b6dc37ff6', 'f6817f48af4fb3af11b9e8bf182f618b', '200006f0edf145a2b50eacca07585451', null);
INSERT INTO `sys_role_permission` VALUES ('ec93bb06f5be4c1f19522ca78180e2ef', 'f6817f48af4fb3af11b9e8bf182f618b', '265de841c58907954b8877fb85212622', null);
INSERT INTO `sys_role_permission` VALUES ('ecdd72fe694e6bba9c1d9fc925ee79de', 'f6817f48af4fb3af11b9e8bf182f618b', '45c966826eeff4c99b8f8ebfe74511fc', null);
INSERT INTO `sys_role_permission` VALUES ('edefd8d468f5727db465cf1b860af474', 'f6817f48af4fb3af11b9e8bf182f618b', '6ad53fd1b220989a8b71ff482d683a5a', null);
INSERT INTO `sys_role_permission` VALUES ('ef8bdd20d29447681ec91d3603e80c7b', 'f6817f48af4fb3af11b9e8bf182f618b', 'ae4fed059f67086fd52a73d913cf473d', null);
INSERT INTO `sys_role_permission` VALUES ('f99f99cc3bc27220cdd4f5aced33b7d7', 'f6817f48af4fb3af11b9e8bf182f618b', '655563cd64b75dcf52ef7bcdd4836953', null);
INSERT INTO `sys_role_permission` VALUES ('fafe73c4448b977fe42880a6750c3ee8', 'f6817f48af4fb3af11b9e8bf182f618b', '9cb91b8851db0cf7b19d7ecc2a8193dd', null);
INSERT INTO `sys_role_permission` VALUES ('fced905c7598973b970d42d833f73474', 'f6817f48af4fb3af11b9e8bf182f618b', '4875ebe289344e14844d8e3ea1edd73f', null);
INSERT INTO `sys_role_permission` VALUES ('fd97963dc5f144d3aecfc7045a883427', 'f6817f48af4fb3af11b9e8bf182f618b', '043780fa095ff1b2bec4dc406d76f023', null);
