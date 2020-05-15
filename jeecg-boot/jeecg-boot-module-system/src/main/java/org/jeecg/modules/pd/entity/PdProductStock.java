package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 库存明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("pd_product_stock")
public class PdProductStock extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**主键集合*/
	@TableField(exist = false)
	private String ids;
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
	@Excel(name = "所属部门", width = 15)
	private String sysOrgCode;
	/**科室ID*/
	private String departId;
	/** 所属医院Id*/
	@TableField(strategy = FieldStrategy.NOT_EMPTY)
	private String departParentId;
	/**产品id*/
	@Excel(name = "产品id", width = 15)
	private String productId;
	/**产品条码*/
	@Excel(name = "产品条码", width = 15)
	private String productBarCode;
	/**批次号*/
	@Excel(name = "批次号", width = 15)
	private String batchNo;
	/**数量*/
	@Excel(name = "数量", width = 15)
	private Double stockNum;
	/**库存规格数量*/
	@Excel(name = "库存规格数量", width = 15)
	private Double specNum;
	/**库存占用状态  0:使用中   1:未使用 2:已用完*/
	@Excel(name = "库存占用状态", width = 15)
	private String nestatStatus;
	private String specUnitId;//规格单位
	@TableField(exist = false)
	private String specUnitName;//规格单位

	private Double specQuantity;//规格数量
	/**生产日期*/
	@Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date produceDate;
	/**有效期*/
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date expDate;
	/**过期状态*/
	@Excel(name = "过期状态", width = 15)
	private String expStatus;
	/**供应商ID*/
	private String supplierId;
	/**备注*/
	private String remarks;
	/** 消息发送状态，近效期1，已过期2，初始值为0 */
	private String msgSendState;
	/**是否永存*/
	@Excel(name = "是否永存", width = 15)
	private String isLong;
	/**货位编号*/
	@Excel(name = "货位编号", width = 15)
	private String huoweiCode;
	/**产品id*/
	@Excel(name = "入库明细Id", width = 15)
	private String recordDetailId;
	/**规格库存数量清零原因*/
	private String reason;
	/**货位名称**/
	@Excel(name = "货位名称", width = 15)
	@TableField(exist = false)
	private String huoweiName;

	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
	@TableField(exist = false)
	private String unitName;
	/**产品名称*/
	@Excel(name = "产品名称", width = 15)
	@TableField(exist = false)
	private String productName;
	/**产品编号*/
	@Excel(name = "产品编号", width = 15)
	@TableField(exist = false)
	private String number;
	/**规格*/
	@Excel(name = "规格", width = 15)
	@TableField(exist = false)
	private String spec;
	/**型号*/
	@Excel(name = "型号", width = 15)
	@TableField(exist = false)
	private String version;
	/**注册证号*/
	@Excel(name = "注册证号", width = 15)
	@TableField(exist = false)
	private String registration;
	/**厂家名称*/
	@Excel(name = "厂家名称", width = 15)
	@TableField(exist = false)
	private String venderName;
	/**厂家Id*/
	@TableField(exist = false)
	private String venderId;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
	@TableField(exist = false)
	private String supplierName;
	/**科室名称*/
	@Excel(name = "科室名称", width = 15)
	@TableField(exist = false)
	private String deptName;
	/**进价*/
	@TableField(exist = false)
	private BigDecimal purchasePrice;
	//入库时产品单价
	@TableField(exist = false)
	private String inPurchasePrice;
	/**出价*/
	@TableField(exist = false)
	private BigDecimal sellingPrice;
	/**JDE编码*/
	@TableField(exist = false)
	private String jdeCode;

	/** 查询日期起始 **/
	@TableField(exist = false)
	private String queryDateStart;

	/** 查询日期结束 **/
	@TableField(exist = false)
	private String queryDateEnd;

	/** 申领单号 **/
	@TableField(exist = false)
	private String applyNo;

	/** 调拨单号 **/
	@TableField(exist = false)
	private String allocationNo;

	@TableField(exist = false)
	private String oldHuoweiCode;

	/** 产品收费代码 **/
	@TableField(exist = false)
	private String chargeCode;

	/** 是否计费 **/
	@TableField(exist = false)
	private String isCharge;
	/** 移库数量 **/
	@TableField(exist = false)
	private Double ykStockNum;

	/*多个部门集合*/
	@TableField(exist = false)
	private List<String> departIdList;

	@TableField(exist = false)
	private String inHospitalNo;

	@TableField(exist = false)
	private String departIds; //批量查询用

	@TableField(exist = false)
	private String productFlag; // 试剂产品or普通产品，0产品，1试剂

	@TableField(exist = false) //试剂产品数量
	private Double productNum;

	@TableField(exist = false) //试剂定数包id
	private String packageId;

    @TableField(exist = false)
    private List<String> productIdList;

    @TableField(exist = false)
    private String productIds;
}