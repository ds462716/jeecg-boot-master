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
	public static final String  ORDER_STATE_0= "0";//待审核
	public static final String  ORDER_STATE_1= "1";//审核中
	public static final String  ORDER_STATE_2= "2";//审核通过
	public static final String  ORDER_STATE_3= "3";//已拒绝

	/**
	 * 提交状态
	 */
	public static final String  SUBMIT_STATE_1= "1";//未提交
	public static final String  SUBMIT_STATE_2= "2";//已提交

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
}
