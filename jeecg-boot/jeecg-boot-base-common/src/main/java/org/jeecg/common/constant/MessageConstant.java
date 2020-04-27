package org.jeecg.common.constant;

/**
 * @author: zxh
 * @date: 2020年1月3日14:54:40
 * @description:业务常量
 */
public interface MessageConstant {

	/**
	 * 扫码提示语
	 */
	public final static String CODE_MESSAGE_1 = "解析失败，请记录条码并联系管理员";
	public final static String CODE_MESSAGE_2 = "扫码成功";
	public final static String CODE_MESSAGE_3 = "解析失败，扫描的编码与绑定的规则长度不一致";
	public final static String CODE_MESSAGE_4 = "没有找到该条码对应的产品";
	public final static String CODE_MESSAGE_5 = "解析失败，参数不正确";
	public final static String CODE_MESSAGE_6 = "该产品已过期，禁止入库";
	public final static String CODE_MESSAGE_7 = "扫描成功.但请注意该产品的有效期!";
	public final static String CODE_MESSAGE_8 = "系统异常，扫码规则出错";


	/**
	 * 定数包扫码提示
	 */
	public final static String PACKAGE_CODE_MESSAGE_1 = "解析失败，请记录条码并联系管理员";
	public final static String PACKAGE_CODE_MESSAGE_2 = "扫码成功";
	public final static String PACKAGE_CODE_MESSAGE_3 = "解析失败，该条码下有多条打包记录，请联系管理员";
	public final static String PACKAGE_CODE_MESSAGE_4 = "没有找到该条码对应的打包记录";
	public final static String PACKAGE_CODE_MESSAGE_5 = "解析失败，参数不正确";
	public final static String PACKAGE_CODE_MESSAGE_6 = "打包记录有过期产品，禁止入库";
	public final static String PACKAGE_CODE_MESSAGE_7 = "扫描成功.但请注意打包记录有近效期产品!";
	public final static String PACKAGE_CODE_MESSAGE_8 = "系统异常，扫码规则出错";


	/**
	 * 扫码状态
	 */
	public final static String CODE_STATE_200 = "200";//成功
	public final static int ICODE_STATE_200 = 200;//成功
	public final static String CODE_STATE_500 = "500";//失败
	public final static int ICODE_STATE_500 = 500;//失败
	public final static String CODE_STATE_201 = "201";//已过期
	public final static int ICODE_STATE_201 = 201;//已过期
	public final static String CODE_STATE_203 = "203";//近效期
	public final static int ICODE_STATE_203 = 203;//近效期
}
