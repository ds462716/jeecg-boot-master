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
-- TRUNCATE TABLE ex_his_charge_inf;                    -- HIS收费项目信息表
-- TRUNCATE TABLE ex_his_user_inf;                      -- HIS系统用户表
-- TRUNCATE TABLE ex_his_depart_inf;                    -- HIS系统部门表


TRUNCATE TABLE pd_stock_record;        -- 出入库记录
TRUNCATE TABLE pd_stock_record_detail; -- 出入库记录明细
TRUNCATE TABLE pd_stock_log;           -- 库存日志
-- TRUNCATE TABLE pd_package;             -- 定数包
-- TRUNCATE TABLE pd_package_detail;      -- 定数包明细
TRUNCATE TABLE pd_package_record;      -- 定数包打包记录
TRUNCATE TABLE pd_package_record_detail;-- 定数包打包记录明细表
-- TRUNCATE TABLE pd_goods_allocation;    -- 货区货位
TRUNCATE TABLE pd_rejected;            -- 退货
TRUNCATE TABLE pd_rejected_detail;     -- 退货明细
TRUNCATE TABLE pd_storeroom_patrol;    -- 巡查表
TRUNCATE TABLE pd_storeroom_patrol_detail;-- 巡查明细表


-- TRUNCATE TABLE pd_product;-- 产品表
-- TRUNCATE TABLE pd_product_rule;-- 产品关联编码规则表
-- TRUNCATE TABLE pd_depart_config;-- 部门配置表
-- TRUNCATE TABLE pd_category;-- 产品分类
-- TRUNCATE TABLE pd_unit;-- 产品单位表
-- TRUNCATE TABLE pd_group;-- 产品组别表
-- TRUNCATE TABLE pd_supplier;-- 产品供应商表
-- TRUNCATE TABLE pd_vender;-- 产品生产厂家表
-- TRUNCATE TABLE pd_encoding_rule;-- 产品编码规则表
-- TRUNCATE TABLE pd_encoding_rule_detail;-- 产品编码规则详情表
-- TRUNCATE TABLE pd_encoding_identifier;-- 应用标识符表
TRUNCATE TABLE pd_dosage;-- 器械使用表
TRUNCATE TABLE pd_dosage_detail;-- 器械使用详情表
TRUNCATE TABLE pd_product_stock_unique_code;-- 条码表