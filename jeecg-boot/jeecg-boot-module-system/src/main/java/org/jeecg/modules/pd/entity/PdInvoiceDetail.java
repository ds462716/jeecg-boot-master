package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: pd_invoice_detail
 * @Author: jiangxz
 * @Date:   2020-06-24
 * @Version: V1.0
 */
@ApiModel(value="pd_invoice对象", description="pd_invoice")
@Data
@TableName("pd_invoice_detail")
public class PdInvoiceDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "id")
	private String id;
	/**发票id*/
	@ApiModelProperty(value = "发票id")
	private String invoiceId;
	/**单据号*/
	@Excel(name = "单据号", width = 15)
	@ApiModelProperty(value = "单据号")
	private String billNo;
	/**单据id*/
	@Excel(name = "单据id", width = 15)
	@ApiModelProperty(value = "单据id")
	private String billId;
	/**单据明细id*/
	@Excel(name = "单据明细id", width = 15)
	@ApiModelProperty(value = "单据明细id")
	private String billDetailId;
	/**回款日期*/
	@Excel(name = "业务日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "业务日期")
	private Date billDate;
	/**产品id*/
	@Excel(name = "产品id", width = 15)
	@ApiModelProperty(value = "产品id")
	private String productId;
	/**库存明细id*/
	@Excel(name = "库存明细id", width = 15)
	@ApiModelProperty(value = "库存明细id")
	private String productStockId;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private Double num;
	/**单价*/
	@Excel(name = "单价", width = 15)
	@ApiModelProperty(value = "单价")
	private BigDecimal price;
	/**金额*/
	@Excel(name = "金额", width = 15)
	@ApiModelProperty(value = "金额")
	private BigDecimal money;
	/**状态1-未完成；2-已完成*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remarks;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建日期")
	private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	@ApiModelProperty(value = "更新人")
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "更新日期")
	private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	@ApiModelProperty(value = "所属部门")
	private String sysOrgCode;
	/**所属父部门*/
	@Excel(name = "所属父部门", width = 15)
	@ApiModelProperty(value = "所属父部门")
	private String departParentId;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	@ApiModelProperty(value = "所属部门")
	private String departId;

	// 发票信息==========================
	@TableField(exist = false)
	private String invoiceRegNo;//发票登记号
	@TableField(exist = false)
	private String invoiceNo;//发票号
	@TableField(exist = false)
	private String invoiceCode;//发票代码
	@TableField(exist = false)
	private Date invoiceData;//发票日期
	@TableField(exist = false)
	private BigDecimal invoiceMoney;//发票金额
	@TableField(exist = false)
	private BigDecimal returnMoney;//回款金额
	@TableField(exist = false)
	private Date returnData;//回款日期
	@TableField(exist = false)
	private String invoiceType;//发票类型；1-入库单发票；2-出库单发票
	@TableField(exist = false)
	private String payStatus;//支付状态：1-已支付；2-未支付
	@TableField(exist = false)
	private String invoiceStatus;//发票状态：1-未完成；2-已完成
	@TableField(exist = false)
	private String billBy;//单据登记人（入库单创建人）

	// 库存产品信息==========================
	@TableField(exist = false)
	private String inDepartId;//入库科室Id
	@TableField(exist = false)
	private String inDepartName;//入库科室名称
	@TableField(exist = false)
	private String outDepartId;//出库科室Id
	@TableField(exist = false)
	private String outDepartName;//出库科室名称
	@TableField(exist = false)
	private String productName;//产品名称
//	@TableField(exist = false)
//	private String number;//产品编号
	@TableField(exist = false)
	private String productNumber;//产品编号
	@TableField(exist = false)
	private String spec;//产品规格
	@TableField(exist = false)
	private String version;//产品型号
	@TableField(exist = false)
	private String unitName;//单位名称
	@TableField(exist = false)
	private String venderId;//生产厂家ID
	@TableField(exist = false)
	private String venderName;//生产厂家名称
	@TableField(exist = false)
	private String supplierId;//供应商ID
	@TableField(exist = false)
	private String supplierName;//供应商名称
	@TableField(exist = false)
	private BigDecimal purchasePrice;//入库价
	@TableField(exist = false)
	private BigDecimal sellingPrice; // 出库价
	@TableField(exist = false)
	private BigDecimal inTotalPrice;//入库总金额
	@TableField(exist = false)
	private BigDecimal outTotalPrice;// 出库总金额
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@TableField(exist = false)
	private Date expDate;//有效期
	@TableField(exist = false)
	private String batchNo;//产品批号
	@TableField(exist = false)
	private Double stockNum;//库存数量
	@TableField(exist = false)
	private String auditStatus;//入库单审核状态
	@TableField(exist = false)
	private List<String> billDetailIdList;//单据明细idList

}
