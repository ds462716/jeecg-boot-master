package org.jeecg.common.constant;

/**
 * @author: zxh
 * @date: 2020年1月3日14:54:40
 * @description:业务常量
 */
public interface PdConstant {

	public static final String ADMIN_DEPART_CODE = "admin";

	/**
	 * 编码规则  start
	 */

	//应用标识符类型 固定长度
	public static final String  IDENTIFIER_TYPE_1= "1";
	//应用标识符类型 可变长度
	public static final String  IDENTIFIER_TYPE_2= "2";

	/**
	 * 编码规则  end
	 */

	/**
	 * 采购订单状态
	 */
	public static final String  ORDER_STATE_1= "1";//待审核
	public static final String  ORDER_STATE_2= "2";//审核通过
	public static final String  ORDER_STATE_3= "3";//已拒绝

	/**
	 * 提交状态
	 */
	public static final String  SUBMIT_STATE_1= "1";//待提交
	public static final String  SUBMIT_STATE_2= "2";//已提交
	public static final String  SUBMIT_STATE_3= "3";//已撤回
	/**
	 * 删除状态
	 */
	public static final String  DEL_FLAG_0= "0";//有效
	public static final String  DEL_FLAG_1= "1";//删除

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

	/** 出入库流程状态 add by jiangxz 20200219 **/
	public static final String RECODE_STATE_0 = "0"; //待提交
	public static final String RECODE_STATE_1 = "1"; //待审核
	public static final String RECODE_STATE_2 = "2"; //已通过
	public static final String RECODE_STATE_3 = "3"; //已拒绝

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


	//是否过期
	public final static String PD_STATE="pd_state";
	public final static String PD_STATE_0="0";//未过期
	public final static String PD_STATE_1="1";//即将过期
	public final static String PD_STATE_2="2";//过期

	//是否久存
	public final static String IS_LONG="";
	public final static String IS_LONG_0="0";//否
	public final static String IS_LONG_1="1";//是


	//出入库类型
	public final static String STOCK_RECORD_TYPE = "stock_record_type";
	public final static String STOCK_RECORD_TYPE_IN = "0";		//入库
	public final static String STOCK_RECORD_TYPE_OUT = "1";		//出库
}
