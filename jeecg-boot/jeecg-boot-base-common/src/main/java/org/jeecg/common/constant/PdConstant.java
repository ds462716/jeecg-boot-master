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
}
