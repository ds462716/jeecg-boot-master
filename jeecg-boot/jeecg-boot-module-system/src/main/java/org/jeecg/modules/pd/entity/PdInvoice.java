package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: pd_invoice
 * @Author: jiangxz
 * @Date:   2020-06-24
 * @Version: V1.0
 */
@ApiModel(value="pd_invoice对象", description="pd_invoice")
@Data
@TableName("pd_invoice")
public class PdInvoice extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**发票登记号*/
	@Excel(name = "发票登记号", width = 15)
    @ApiModelProperty(value = "发票登记号")
    private String invoiceRegNo;
	/**发票号*/
	@Excel(name = "发票号", width = 15)
    @ApiModelProperty(value = "发票号")
    private String invoiceNo;
	/**发票代码*/
	@Excel(name = "发票代码", width = 15)
    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;
	/**发票日期*/
	@Excel(name = "发票日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发票日期")
    private Date invoiceData;
	/**发票金额*/
	@Excel(name = "发票金额", width = 15)
    @ApiModelProperty(value = "发票金额")
    private BigDecimal invoiceMoney;
	/**回款金额*/
	@Excel(name = "回款金额", width = 15)
    @ApiModelProperty(value = "回款金额")
    private BigDecimal returnMoney;
	/**回款日期*/
	@Excel(name = "回款日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "回款日期")
    private Date returnData;
	/**发票类型；1-入库单发票；2-出库单发票*/
	@Excel(name = "发票类型；1-入库单发票；2-出库单发票", width = 15)
    @ApiModelProperty(value = "发票类型；1-入库单发票；2-出库单发票")
    private String invoiceType;
	/**支付状态：1-已支付；2-未支付*/
	@Excel(name = "支付状态：1-已支付；2-未支付", width = 15)
    @ApiModelProperty(value = "支付状态：1-已支付；2-未支付")
    private String payStatus;
	/**发票状态：1-未完成；2-已完成*/
	@Excel(name = "发票状态：1-未完成；2-已完成", width = 15)
    @ApiModelProperty(value = "发票状态：1-未完成；2-已完成")
    private String invoiceStatus;
	/**单据登记人（入库单创建人）*/
	@Excel(name = "单据登记人（入库单创建人）", width = 15)
    @ApiModelProperty(value = "单据登记人（入库单创建人）")
    private String billBy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
//	/**删除标识，0-正常；1-删除*/
//	@Excel(name = "删除标识，0-正常；1-删除", width = 15)
//    @ApiModelProperty(value = "删除标识，0-正常；1-删除")
////    private String delFlag;
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

    @TableField(exist = false)
    private List<PdInvoiceDetail> pdInvoiceDetailList;

    @TableField(exist = false)
    private String invoiceId;//发票id
    @TableField(exist = false)
    private String billId;//单据id
    @TableField(exist = false)
    private List<String> billDetailIdList;//单据明细idList

    @TableField(exist = false)
    private String hospitalCode;//医院代码


}
