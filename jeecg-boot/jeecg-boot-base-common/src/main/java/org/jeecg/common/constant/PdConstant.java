package org.jeecg.common.constant;

/**
 * @author: zxh
 * @date: 2020年1月3日14:54:40
 * @description:业务常量
 */
public interface PdConstant {

	public static final String ADMIN_DEPART_CODE = "admin";

	/** 失败标志 add by jiangxz 20200310 */
	public static final String FAIL_500 = "500";
	/** 成功标志 add by jiangxz 20200310 */
	public static final String SUCCESS_200 = "200";

	public static final String SUCCESS_0 = "0"; //成功标识
	public static final String FAIL_1 = "-1";    //失败标识

	/** 编码规则  start */
	//应用标识符类型 固定长度
	public static final String  IDENTIFIER_TYPE_1= "1";
	//应用标识符类型 可变长度
	public static final String  IDENTIFIER_TYPE_2= "2";
	/** * 编码规则  end */

	/** 审核状态 */
	public static final String  AUDIT_STATE_1= "1";//待审核
	public static final String  AUDIT_STATE_2= "2";//审核通过
	public static final String  AUDIT_STATE_3= "3";//已拒绝

	/** 提交状态 */
	public static final String  SUBMIT_STATE_1= "1";//待提交
	public static final String  SUBMIT_STATE_2= "2";//已提交
	public static final String  SUBMIT_STATE_3= "3";//已撤回

	/** * 删除状态 */
	public static final String  DEL_FLAG_0= "0";//有效
	public static final String  DEL_FLAG_1= "1";//删除

	// true or false add by jiangxz 20200228
	public final static String TRUE = "true";
	public final static String FALSE = "false";

	//货区货位标识，用于页面中与机构部门标识区分 add by jiangxz 20200212
	public static final String GOODS_ALLCATION_FLAG_1 = "huoqu"; //货区货位
	public static final String GOODS_ALLCATION_FLAG_2 = "huowei"; //货区货位

	/**货区货位数据字典  add by jiangxz 20200213*/
	public static final String GOODS_ALLCATION_AREA_TYPE_1 = "1"; //货区
	public static final String GOODS_ALLCATION_AREA_TYPE_2 = "2"; //货位

	/** 出入库类型  add by jiangxz 20200219 **/
	public static final String RECODE_TYPE_1 = "1"; //入库
	public static final String RECODE_TYPE_2 = "2"; //出库

	/**入库类型 add by jiangxz 20200214*/
	public static final String IN_TYPE_1 = "1";//正常入库
	public static final String IN_TYPE_2 = "2";//退货入库
	public static final String IN_TYPE_3 = "3";//调拨入库

	/**入库类型 add by jiangxz 20200302*/
	public static final String OUT_TYPE_1 = "1";//正常出库
	public static final String OUT_TYPE_2 = "2";//科室出库
	public static final String OUT_TYPE_3 = "3";//调拨出库

	/***编码规则*/
	//各种类型单号首字母
	public final static String ORDER_NO_FIRST_LETTER_RK = "RK";//入库单
	public final static String ORDER_NO_FIRST_LETTER_CK = "CK";//出库单
	public final static String ORDER_NO_FIRST_LETTER_TH = "TH";//退货单
	public final static String ORDER_NO_FIRST_LETTER_YL = "YL";//用量单
	public final static String ORDER_NO_FIRST_LETTER_TYL = "TYL";//用量退回单
	public final static String ORDER_NO_FIRST_LETTER_DB = "DB";//调拨单
	public final static String ORDER_NO_FIRST_LETTER_SL = "SL";//申领单
	public final static String ORDER_NO_FIRST_LETTER_SG = "SG";//申购单
	public final static String ORDER_NO_FIRST_LETTER_XC = "XC";//巡查单
	public final static String ORDER_NO_FIRST_LETTER_PD = "PD";//盘点单
	public final static String ORDER_NO_FIRST_LETTER_DSB = "DSB";//定数包

	//是否过期
	public final static String PD_STATE="pd_state";
	public final static String PD_STATE_0="0";//未过期
	public final static String PD_STATE_1="1";//即将过期
	public final static String PD_STATE_2="2";//过期

	//是否久存
	public final static String IS_LONG="";
	public final static String IS_LONG_0="0";//否
	public final static String IS_LONG_1="1";//是

	/**开关-是否允许入库量大于订单量 add by jiangxz 20200222 **/
	public final static String ON_OFF_ALLOW_IN_MORE_ORDER = "on_off_allow_in_more_order";
	/**开关-是否允许入库非订单产品 add by jiangxz 20200222 **/
	public final static String ON_OFF_ALLOW_NOT_ORDER_PRODUCT = "on_off_allow_not_order_product";
	/**开关-是否允许入库非本供应商产品 add by jiangxz 20200222 **/
	public final static String ON_OFF_ALLOW_SUPPLIER = "on_off_allow_supplier";
	/**开关-是否允许出入库时可修改进价和出价 add by jiangxz 20200222 **/
	public final static String ON_OFF_ALLOW_EDIT_PRICE = "on_off_allow_edit_price";
	public final static Integer ON_OFF_ALLOW_EDIT_PRICE_0 = 0; // 1-是；0-否
	public final static Integer ON_OFF_ALLOW_EDIT_PRICE_1 = 1; // 1-是；0-否
	/**开关-是否显示入库单抬头**/
	public final static String ON_OFF_STOCK_IN_TEXT = "on_off_stock_in_text";
	public final static Integer ON_OFF_STOCK_IN_TEXT_0 = 0; // 1-显示；0-不显示
	public final static Integer ON_OFF_STOCK_IN_TEXT_1 = 1; // 1-显示；0-不显示
	/**开关-是否显示出库单抬头**/
	public final static String ON_OFF_STOCK_OUT_TEXT = "on_off_stock_out_text";
	public final static Integer ON_OFF_STOCK_OUT_TEXT_0 = 0; // 1-显示；0-不显示
	public final static Integer ON_OFF_STOCK_OUT_TEXT_1 = 1; // 1-显示；0-不显示
	/**开关-是否需要入库审批**/
	public final static String ON_OFF_STOCK_IN_AUDIT = "on_off_stock_in_audit";
	public final static Integer ON_OFF_STOCK_IN_AUDIT_0 = 0; // 1-是；0-否
	public final static Integer ON_OFF_STOCK_IN_AUDIT_1 = 1; // 1-是；0-否
	/**开关-是否需要出库审批**/
	public final static String ON_OFF_STOCK_OUT_AUDIT = "on_off_stock_out_audit";
	public final static Integer ON_OFF_STOCK_OUT_AUDIT_0 = 0; // 1-是；0-否
	public final static Integer ON_OFF_STOCK_OUT_AUDIT_1 = 1; // 1-是；0-否
	/**开关-是否允许入库证照过期的产品**/
	public final static String ON_OFF_STOCK_IN_EXP_PRODUCT = "on_off_stock_in_exp_product";
	/**开关-是否允许入库证照过期的供应商**/
	public final static String ON_OFF_STOCK_IN_EXP_SUPPLIER = "on_off_stock_in_exp_supplier";


	//系统消息提醒模板CODE
	public final static String PURCHASE_SUBMIT_MSG = "purchase_submitMsg";		//采购提交提醒
	public final static String APPLY_SUBMIT_MSG = "apply_submitMsg";		    //领用提交审核提醒
	public final static String ALLOCATION_MSG = "allocation_msg";		    //调拨提交审核提醒
	public final static String STOCK_RECORD_IN_SUBMIT_MSG = "stock_record_in_submit_msg"; // 入库单审核提醒
	public final static String STOCK_RECORD_OUT_SUBMIT_MSG = "stock_record_out_submit_msg"; // 出库单审核提醒


	//扫码近效期提醒时间
	public final static int REMINDER_TIME = 90;

	//当前登录的部门属性
	public final static String  CURRENT_DEPARTID = "departId";
	public final static String  DEPART_PARENT_ID = "departParentId";
	//部门类型 0医院1一级库房2二级库房
	public final static String DEPART_TYPE_0 = "0";
	public final static String DEPART_TYPE_1 = "1";
	public final static String DEPART_TYPE_2 = "2";

	//菜单是否是业务菜单 0是1否
	public final static String PERMISSION_BUSINESS_TYPE_0 = "0";
	public final static String PERMISSION_BUSINESS_TYPE_1 = "1";

	/**
	 * 盘点完成状态
	 */
	public static final String  CHECK_STATE_0= "0";//未完成
	public static final String  CHECK_STATE_1= "1";//已完成

	//院内物流操作类型  add by jiangxz 20200228
	public final static String STOCK_LOG_TYPE = "stock_log_type";
	public final static String STOCK_LOG_TYPE_1 = "1";//耗材入库
	public final static String STOCK_LOG_TYPE_2 = "2";//耗材出库
	public final static String STOCK_LOG_TYPE_3 = "3";//用量使用
	public final static String STOCK_LOG_TYPE_4 = "4";//用量退回
	public final static String STOCK_LOG_TYPE_5 = "5";//耗材退货
	public final static String STOCK_LOG_TYPE_6 = "6";//执行收费
	public final static String STOCK_LOG_TYPE_7 = "7";//耗材院外退货
	public final static String STOCK_LOG_TYPE_8 = "8";//定数包打包
	public final static String STOCK_LOG_TYPE_9 = "9";//定数包拆包
	public final static String STOCK_LOG_TYPE_10 = "10";//执行退费


	//生产厂家及供应商证照到期消息提醒发送状态
 	public final static String MSG_SEND_STATUS_0="0";//初始值，未发送
	public final static String MSG_SEND_STATUS_1="1";//已发送近效期消息提醒
	public final static String MSG_SEND_STATUS_2="2";//已发送过期消息提醒

	//部门个性化配置是否默认
	public final static String IS_DEFAULT_0 ="0";//是默认的
	public final static String IS_DEFAULT_1 ="1";//不是默认的
	public final static String REMINDER_TYPE_1 = "venderNEP";//生产厂家近效期提醒时间
	public final static String REMINDER_TYPE_2 = "supplierNEP";//供应商近效期提醒时间
	public final static String REMINDER_TYPE_3 = "stockNEP";//库存近效期提醒时间
	public final static String REMINDER_TYPE_4 = "existRemind";//久存提醒时间
	public final static String REMINDER_TYPE_5 = "productNEP";//产品证照近效期提醒时间


	public final static String REMINDER_DETE_1 = "60";//生产厂家近效期提醒时间默认值
	public final static String REMINDER_DETE_2 = "60";//供应商近效期提醒时间默认值
	public final static String REMINDER_DETE_3 = "60";//库存近效期提醒时间默认值
	public final static String REMINDER_DETE_4 = "60";//久存提醒时间默认值
	public final static String REMINDER_DETE_5 = "60";//产品近效期提醒时间默认值

	/**
	 * 菜单名称变了需要改名字
	 */
	public final static String AUDIT_MENU_1="入库审核";
	public final static String AUDIT_MENU_2="出库审核";
	public final static String AUDIT_MENU_3="采购审核";
	public final static String AUDIT_MENU_4="库存管理";
	public final static String AUDIT_MENU_5="供应商管理";
	public final static String AUDIT_MENU_6="生产厂家管理";
	public final static String AUDIT_MENU_7="领用审核";
	public final static String AUDIT_MENU_8="调拨审核";
	public final static String AUDIT_MENU_9="产品管理";



	//同步状态码
	public final static String SYNC_STATE_ERROR = "500";//同步失败
	public final static String SYNC_STATE_SUCCESS = "200";//同步成功


	//供应商受理状态
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_0 = "4";//待上传
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_1 = "0";//待接收
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_2 = "1";//已接收
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_3 = "2";//已拒绝
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_4 = "3";//已收货

	//器械使用收费状态
	public final static String  CHARGE_FLAG_0 ="0";//已收费
	public final static String  CHARGE_FLAG_1 ="1";//未收费
	public final static String  CHARGE_FLAG_2 ="2";//已退回
	public final static String  CHARGE_FLAG_3 ="3";//已退费
	public final static String  IS_CHARGE_FLAG_0 ="0";//有接口
	public final static String  IS_CHARGE_FLAG_1 ="1";//没有接口

	public final static String  IS_CHARGE_TYPE_0 ="0";//退费操作
	public final static String  IS_CHARGE_TYPE_1 ="1";//收费操作

	//产品分类类型
	public final static String CATEGORY_TYPE_0 = "0";
	public final static String CATEGORY_TYPE_1 = "1";

	// 产品是否计费
	public final static String  IS_CHARGE_0 ="0";//计费
	public final static String  IS_CHARGE_1 ="1";//补计费


	//产品属性类型
	public final static String PROD_ATTR_1 = "1";//产品
	public final static String PROD_ATTR_2 = "2";//定数包

	// 巡查结果
	public final static String RESULT_QUALIFIED = "合格";
	public final static String RESULT_UNQUALIFIED = "不合格";
	public final static String RESULT_OTHER = "其他";

	// 紧急产品标志   0-是；1-不是
	public final static String IS_URGENT_0 = "0";

	//产品表试剂或者产品0产品1试剂
	public final static String PRODUCT_FLAG_0="0";
	public final static String PRODUCT_FLAG_1="1";

	//产品单位分类
	public final static String PRODUCT_UNIT_TYPE_0="0";//包装单位
	public final static String PRODUCT_UNIT_TYPE_1="1";//规格单位


    //库存占用状态
    public final static String STOCK_NESTAT_STATUS_0="0";//使用中
    public final static String STOCK_NESTAT_STATUS_1="1";//未使用
    public final static String STOCK_NESTAT_STATUS_2="2";//已用完

	//定数包打包记录出库状态
	public final static String PACKAGE_RECORD_STATUS_0="0";//出库状态：0-已出库；1-未出库
	public final static String PACKAGE_RECORD_STATUS_1="1";//出库状态：0-已出库；1-未出库


	//自动补货订单类别
	public final static String AUTO_ORDER_0="0";//自动申领单
	public final static String AUTO_ORDER_1="1";//自动采购单

	//试剂扣减状态
	public final static String ACCEPT_STATUS_0="0";//已扣减
	public final static String ACCEPT_STATUS_1="1";//未配置检验项目
	public final static String ACCEPT_STATUS_2="2";//未扣减
	public final static String ACCEPT_STATUS_3="3";//未配置试剂用量

	//试剂扣减类型
	public final static String DEDUCTUIN_TYPE_0="0";//自动扣减
	public final static String DEDUCTUIN_TYPE_1="1";//人工扣减
	public final static String DEDUCTUIN_TYPE_2="2";//无需扣减

	//生成条码的类型
	public final static String CODE_PRINT_TYPE_0 = "0";//批量打印的码
	public final static String CODE_PRINT_TYPE_1 = "1";//唯一码

	//生成条码的状态
	public final static String CODE_PRINT_STATE_0 = "0";//正常状态
	public final static String CODE_PRINT_STATE_1 = "1";//已退货
	public final static String CODE_PRINT_STATE_2 = "2";//已用完

	//退货类型
	public final static String REJECTED_TYPE_0 = "0";//唯一码退货
	public final static String REJECTED_TYPE_1 = "1";//普通码退货

	//使用类型
	public final static String DOSAGE_TYPE_0 = "0";//唯一码使用
	public final static String DOSAGE_TYPE_1 = "1";//普通码使用

	//试剂使用类型
	public final static String USE_TYPE_1="1";//住院使用
	public final static String USE_TYPE_2="2";//门诊使用

	//病人类型
	public final static String PATIENT_TYPE_1="住院";//住院病人
	public final static String PATIENT_TYPE_2="门诊";//门诊病人

}
