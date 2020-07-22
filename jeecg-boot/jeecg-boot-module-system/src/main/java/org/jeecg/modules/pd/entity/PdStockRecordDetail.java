package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 出入库明细表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Data
@TableName("pd_stock_record_detail")
public class PdStockRecordDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**出入库记录ID*/
	private String recordId;
	/**产品ID*/
	private String productId;
	/**产品条码*/
	private String productBarCode;
	private String refBarCode;// REF条码(在出入库明细中已作废)
	/**采购订单号*/
	private String orderNo;
	/**合并采购订单号*/
	private String mergeOrderNo;
	/**货位ID*/
//	private String huoquId;
	/**货位ID*/
	private String inHuoweiCode;
	private String outHuoweiCode;
	/**产品批号*/
	private String batchNo;
	/**产品数量（出入库数量）*/
	private Double productNum;
	/**出库单价*/
	private java.math.BigDecimal sellingPrice;
	/**入库单价*/
	private java.math.BigDecimal purchasePrice;
	/**生产日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date produceDate;
	/**有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date expDate;

	private String registration;//注册证号
	/**定数包 打包记录ID*/
	private String packageRecordId;
	/**高低值耗材标志 1-高值；2-低值*/
	private String highLowSupplies;
	/**导入单号*/
	private String importNo;
	private String supplierId;//供应商ID
	private String distributorId;//配送商ID
	private String specUnitId;//规格单位
	private Double specQuantity;//规格数量
	/**备注*/
	private String remarks;
	/**删除标记（0：正常；1：删除）*/
//	private String delFlag;
	/**扩展1*/
	private String extend1;  // 本字段用于存 出库入库的 出库单号， 用于退货出库时 按出库单号查询库存
	/**扩展2*/
	private String extend2;
	/**扩展3*/
	private String extend3;
	/**创建人*/
	private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**所属部门*/
	private String sysOrgCode;
	/**所属部门*/
	private String departId;
	private String departParentId;
	/**产品库存ID*/
	private String productStockId;
	/**条码类型**/
	private String barCodeType;

    /*不是明细表字段*/
	@TableField(exist = false)
	private String productName;//产品名称
	@TableField(exist = false)
	private String number;//产品编号
	@TableField(exist = false)
	private String productNumber;//产品编号
	@TableField(exist = false)
	private String spec;//产品规格
	@TableField(exist = false)
	private String version;//产品型号
	@TableField(exist = false)
	private String chargeCode;//收费代号
	@TableField(exist = false)
	private String unitName;//单位名称
	@TableField(exist = false)
	private String bidingNumber;//中标号
	@TableField(exist = false)
	private Double autoNum;//自动补货数量
	@TableField(exist = false)
	private String venderId;//生产厂家ID
	@TableField(exist = false)
	private String venderName;//生产厂家名称
	@TableField(exist = false)
	private String supplierName;//供应商名称
	@TableField(exist = false)
	private String distributorName;//配送商名称
	@TableField(exist = false)
	private String inDepartId;//入库科室Id
	@TableField(exist = false)
	private String inDepartName;//入库科室名称
	@TableField(exist = false)
	private String outDepartName;//出库科室名称
	@TableField(exist = false)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date submitDate;//出入库时间
	@TableField(exist = false)
	private String recordType;//出入库类型
	@TableField(exist = false)
	private BigDecimal outTotalPrice;//出库金额
	@TableField(exist = false)
	private BigDecimal inTotalPrice;// 入库金额
	@TableField(exist = false)
	private String submitStatus;//记录提交状态
	@TableField(exist = false)
	private String auditStatus;//记录审核状态
	@TableField(exist = false)
	private String deptId;//科室ID
	@TableField(exist = false)
	private String recordNo;//出入库单号
	@TableField(exist = false)
	private String realname;//操作人名称
	@TableField(exist = false)
	private String outType;//出库类型
	@TableField(exist = false)
	private String inType;//入库类型
	@TableField(exist = false)
	private Double stockNum;// 库存数量

	@TableField(exist = false)
	private String inHuoweiName;// 入库货位
	@TableField(exist = false)
	private String outHuoweiName;//出库货位
	@TableField(exist = false)
	private String oldInHuoweiCode;
	@TableField(exist = false)
	private String oldOutHuoweiCode;

	@TableField(exist = false)
	private String queryExpDateStart;
	@TableField(exist = false)
	private String queryExpDateEnd;

	@TableField(exist = false)
	private String queryInDateStart;
	@TableField(exist = false)
	private String queryInDateEnd;

	@TableField(exist = false)
	private String queryOutDateStart;
	@TableField(exist = false)
	private String queryOutDateEnd;

	@TableField(exist = false)
	private String queryDateStart;
	@TableField(exist = false)
	private String queryDateEnd;

	@TableField(exist = false)
	private List<String> departIdList;
	@TableField(exist = false)
	private String inDepartIds; //批量查询用
	@TableField(exist = false)
	private List<String> inDepartIdList;
	@TableField(exist = false)
	private String outDepartIds; //批量查询用
	@TableField(exist = false)
	private List<String> outDepartIdList;
	@TableField(exist = false)
	private List<String> productStockIdList;

	@TableField(exist = false)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date auditDate;

	@TableField(exist = false)
	private String jdeCode;// JDE编号
	@TableField(exist = false)
	private String venderJdeCode; // 厂家JDEcode
	@TableField(exist = false)
	private String supplierJdeCode;//供应商JDEcode
	@TableField(exist = false)
	private String isUrgent;  // 是否是紧急产品   0是1不是
	@TableField(exist = false)
	private Double upQuantity; // 紧急产品需要采购数量
	@TableField(exist = false)
	private Double purchasedQuantity; //紧急产品已采购数量
	@TableField(exist = false)
	private String productRegistration; //产品表中的注册证

	@TableField(exist = false)
	private String invoiceNo; // 发票号
	@TableField(exist = false)
	private String invoiceCode; //发票代码
	@TableField(exist = false)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date invoiceData; //发票日期
	/**试剂产品or普通产品，0产品，1试剂*/
	@TableField(exist = false)
	private String productFlag;
	@TableField(exist = false)
	private String productFlagName;
	@TableField(exist = false)
	private String nestatStatus;//库存占用状态 0：使用中  1：未使用 2:已用完
}
