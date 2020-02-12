package org.jeecg.modules.pd.constants;


/**
 * 全局变量(与字典对应)
 * @author changjundong
 *
 */
public class MinaGlobalConstants {

	public final static String OFFICE_CODE_WSY_GY = "WSYGY"; //卫生院共用机构代码（用于公共产品、供应商、厂家等）

	//用户类型
	public final static String SYS_USER_TYPE = "sys_user_type";
	public final static String SYS_USER_TYPE_1 = "1";		//系统管理员
	public final static String SYS_USER_TYPE_3 = "3";		//普通用户
	public final static String SYS_USER_TYPE_4 = "4";		//供应商
	
	//出入库类型
	public final static String STOCK_RECORD_TYPE = "stock_record_type";
	public final static String STOCK_RECORD_TYPE_IN = "0";		//入库
	public final static String STOCK_RECORD_TYPE_OUT = "1";		//出库
	public final static String STOCK_RECORD_TYPE_IN_NAME = "入库";		//入库
	public final static String STOCK_RECORD_TYPE_OUT_NAME = "出库";		//出库
	
	//出库类型
	public final static String STOCK_OUT_TYPE = "stock_out_type";
	public final static String STOCK_OUT_TYPE_0 = "0";		//正常出库
	public final static String STOCK_OUT_TYPE_1 = "1";		//退货出库
	public final static String STOCK_OUT_TYPE_2 = "2";		//调拨出库
	
	//入库类型 
	public final static String STOCK_IN_TYPE = "stock_in_type";
	public final static String STOCK_IN_TYPE_0 = "0";		//正常入库
	public final static String STOCK_IN_TYPE_1 = "1";		//退货入库
	public final static String STOCK_IN_TYPE_2 = "2";		//调拨入库
	
	//出入库记录状态
	public final static String STOCK_RECORD_STATE = "stock_record_state";
	public final static String STOCK_RECORD_STATE_0 = "0";	//待审核
	public final static String STOCK_RECORD_STATE_1 = "1";	//已通过
	public final static String STOCK_RECORD_STATE_2 = "2";	//已拒绝
	
	//出入库导入方式
	public final static String STOCK_IMP_TYPE = "stock_imp_type";
	public final static String STOCK_IMP_TYPE_0 = "0";	//从采购单导入
	public final static String STOCK_IMP_TYPE_1 = "1";	//从申购单导入
	public final static String STOCK_IMP_TYPE_2 = "2";	//从调拨单导入
	public final static String STOCK_IMP_TYPE_3 = "3";	//从退货单导入
	
	//产品类型
	public final static String PRODUCT_TYPE = "product_type";
	public final static String PRODUCT_TYPE_0 = "0";		//低值耗材
	public final static String PRODUCT_TYPE_1 = "1";		//高值耗材
	
	//采购单状态
	public final static String PURCHASE_ORDER_STATUS = "order_status";
	public final static String PURCHASE_ORDER_STATUS_SENDED = "0"; //待审核
	public final static String PURCHASE_ORDER_STATUS_PASSED = "1"; //已审核
	public final static String PURCHASE_ORDER_STATUS_REFUSED = "2";//已拒绝
	public final static String PURCHASE_ORDER_STATUS_STOCKED = "3";//已入库
	
	//供应商受理状态
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_0 = "4";//待上传
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_1 = "0";//待接收
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_2 = "1";//已接收
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_3 = "2";//已拒绝
	public final static String PURCHASE_ORDER_SUPPLIER_STATUS_4 = "3";//已收货
	
	//申请领用单状态  //调拨单状态
	public final static String APPLY_ORDER_STATUS = "apply_status";
	public final static String APPLY_ORDER_STATUS_SENDED = "0"; //待审核
	public final static String APPLY_ORDER_STATUS_PASSED = "1"; //已审核
	public final static String APPLY_ORDER_STATUS_REFUSED = "2";//已拒绝
	public final static String APPLY_ORDER_STATUS_IMPORT_OUT_STOCK = "3";//出库已导入
	public final static String APPLY_ORDER_STATUS_OUT_REJECT = "4";//出库被驳回
	public final static String APPLY_ORDER_STATUS_OUT_STOCK = "5";//已出库(一级出库)
	public final static String APPLY_ORDER_STATUS_IMPORT_IN_STOCK = "6";//入库已导入
	public final static String APPLY_ORDER_STATUS_IN_REJECT = "7";//入库被驳回
	public final static String APPLY_ORDER_STATUS_IN_STOCK = "8";//已完结
	
	//用量退回状态
	public final static String DOSAGERT_STATE="dosagert_state";
	public final static String DOSAGERT_STATE_1="1";//已导入
	public final static String DOSAGERT_STATE_2="2";//已出库
	
	//是否在退货中
	public final static String IS_IN_REFUND = "1";//退货中
	
	//器械用量管理是否收费标识
	public final static String IS_CHARGE = "1";//收费
	
	//库房类型
	public final static String STOREROOM_TYPE = "storeroom_type";
	public final static String STOREROOM_TYPE_0 = "0";		//一级库房
	public final static String STOREROOM_TYPE_1 = "1";		//二级库房
	
	//库房分类
	public final static String STOREROOM_CLASS = "storeroom_class";
	public final static String STOREROOM_CLASS_0 = "0";		//智能柜
	public final static String STOREROOM_CLASS_1 = "1";		//普通柜
	
	//调拨单状态
	/*public final static String ALLOCATION_STATE = "allocation_state";
	public final static String ALLOCATION_STATE_1 = "1";		//待审核
	public final static String ALLOCATION_STATE_2 = "2";		//已通过
	public final static String ALLOCATION_STATE_3 = "3";		//已拒绝
	public final static String ALLOCATION_STATE_4 = "4";		//已出库
	public final static String ALLOCATION_STATE_5 = "5";		//已入库
	public final static String ALLOCATION_STATE_6 = "6";		//已导入
*/	
	//是否过期
	public final static String PD_STATE="pd_state";
	public final static String PD_STATE_0="0";//即将过期
	public final static String PD_STATE_1="1";//过期
	
	//是否久存
	public final static String IS_LONG="";
	public final static String IS_LONG_0="0";//否
	public final static String IS_LONG_1="1";//是
	
	//登记不良品等级
	public final static String BAD_PRODUCT_REGISTER_LEVEL = "bad_prod_level";
	public final static String BAD_PRODUCT_REGISTER_LEVEL_COMMON = "0";//一般
	public final static String BAD_PRODUCT_REGISTER_LEVEL_LIGHT = "1";//严重
	public final static String BAD_PRODUCT_REGISTER_LEVEL_HEAVY = "2";//非常严重
	
	//院内物流操作类型
	public final static String PRODUCT_FLOW_TYPE = "product_flow_type";
	public final static String PRODUCT_FLOW_TYPE_1 = "1";//耗材入库
	public final static String PRODUCT_FLOW_TYPE_2 = "2";//耗材出库
	public final static String PRODUCT_FLOW_TYPE_3 = "3";//用量使用
	public final static String PRODUCT_FLOW_TYPE_4 = "4";//用量退回
	public final static String PRODUCT_FLOW_TYPE_5 = "5";//耗材退货
	public final static String PRODUCT_FLOW_TYPE_6 = "6";//执行收费
	public final static String PRODUCT_FLOW_TYPE_7 = "7";//耗材院外退货

	//库房巡查结果
	public final static String STOREROOM_PATROL_RESULT = "pd_storeroom_patrol_result";
	public final static String STOREROOM_PATROL_RESULT_PASSED = "0";//合格
	public final static String STOREROOM_PATROL_RESULT_FAILED = "1";//不合格
	public final static String STOREROOM_PATROL_RESULT_OTHERS = "2";//其他
	
	//产品分类类型
	public final static String CATEGORY_TYPE = "categroy_type";
	public final static String CATEGORY_TYPE_HIGH = "1";//高值耗材
	public final static String CATEGORY_TYPE_LOW = "0";//低值耗材
	public final static String CATEGORY_TYPE_NAME_HIGH = "高值耗材";//高值耗材
	public final static String CATEGORY_TYPE_NAME_LOW = "低值耗材";//低值耗材
	
	//紧急产品
	public final static String URGENT_TYPE = "urgent_type";
	public final static String URGENT_TYPE_YES = "a1";//紧急产品
	public final static String URGENT_TYPE_NO = "a0";//非紧急产品
	
	//产品权限
	public final static String PRODUCT_POWER = "product_power";
	public final static String PRODUCT_POWER_PUBLIC = "0";//公共产品
	public final static String PRODUCT_POWER_PRIVATE = "1";//自有产品
	public final static String PRODUCT_POWER_PUBLIC_NAME = "公共产品";//公共产品
	public final static String PRODUCT_POWER_PRIVATE_NAME = "自有产品";//自有产品
	
	
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
	
	//退货单状态
	public final static String RETURN_STATE_0="0";//退货单已出库
	public final static String RETURN_STATE_1="1";//退货单已入库
	
	//同步状态 0,提交失败,1提交成功
	public final static String SYNC_STATE_0 = "0";//同步失败
	public final static String SYNC_STATE_1 = "1";//同步成功
	
	//同步状态码
	public final static String SYNC_STATE_ERROR = "500";//同步失败
	public final static String SYNC_STATE_SUCCESS = "200";//同步成功
	
	
	//药品采购单状态
	public final static String DRUG_ORDER_STATUS = "drug_order_status";
	public final static String DRUG_ORDER_STATUS_WAITING_ACCEPT = "0";//待审核---(中心平台：待接收)
	public final static String DRUG_ORDER_STATUS_ALREADY_ACCEPT = "1";//已审核---(中心平台：已接收)
	public final static String DRUG_ORDER_STATUS_ALREADY_REFUSE = "2";//已拒绝---(中心平台：已拒绝)
	public final static String DRUG_ORDER_STATUS_ALREADY_INROOM = "3";//已入库---(中心平台：已入库)
			
	
	//耗材与供应商中间表type，0：药品，1：耗材
	public final static String IS_DRUG_OR_CONSUMABLE_0 = "0";//药品
	public final static String IS_DRUG_OR_CONSUMABLE_1 = "1";//耗材
	
	public final static String HIS_SYNC_DATA_TYPE_0 = "0";//药品目录
	public final static String HIS_SYNC_DATA_TYPE_1 = "1";//药品总库存
	public final static String HIS_SYNC_DATA_TYPE_2 = "2";//药品库存明细
	public final static String HIS_SYNC_DATA_TYPE_3 = "3";//药品入库单
	public final static String HIS_SYNC_DATA_TYPE_4 = "4";//药品入库明细
	public final static String HIS_SYNC_DATA_TYPE_5 = "5";//药品退货单
	public final static String HIS_SYNC_DATA_TYPE_6 = "6";//药品退货明细
	
	public final static String IDENTIFIER_TYPE_1="1";//编码规则固定长度
	public final static String IDENTIFIER_TYPE_2="2";//编码规则选定长度

	//同步成功标志
	public static final Integer SYN_SUCCESS = 1;
	//未同步标识
	public static final Integer SYN_NONE = 0;

	public static final String SUCCESS = "0"; //成功标识
	public static final String FAIL = "-1";    //失败标识
}
