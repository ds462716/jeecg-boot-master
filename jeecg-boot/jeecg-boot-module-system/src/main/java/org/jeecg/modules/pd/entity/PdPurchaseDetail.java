package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;

/**
 * @Description: 申购单详细表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Data
@TableName("pd_purchase_detail")
public class PdPurchaseDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	private String sysOrgCode;
	/**申购单编号*/
	private String orderNo;
	/**产品ID*/
	@Excel(name = "产品ID", width = 15)
	private String productId;
	/**产品编号*/
	@Excel(name = "产品编号", width = 15)
	private String productNo;
	/*产品批次号
	@Excel(name = "产品批次号", width = 15)
	private String batchNo;
	/**产品有效期*/
	/*@Excel(name = "产品有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date expireDate;*/
	/**产品单价*/
	@Excel(name = "产品单价", width = 15)
	private java.math.BigDecimal inPrice;
	/**申购数量*/
	@Excel(name = "申购数量", width = 15)
	private Integer applyCount;
	/**申购总金额*/
	@Excel(name = "申购总金额", width = 15)
	private java.math.BigDecimal amountMoney;
	/**申购时总库存数量*/
	@Excel(name = "申购时总库存数量", width = 15)
	private Integer stockNum;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private String remarks;
	@TableField(exist = false)
	private String productName;//产品名称
	@TableField(exist = false)
	private String spec;//产品规格
	@TableField(exist = false)
	private String version;//产品型号
	@TableField(exist = false)
	private String sellingPrice;//产品单价
	@TableField(exist = false)
	private String unitName;//单位名称
	@TableField(exist = false)
	private String supplierName;//供应商名称
	@TableField(exist = false)
	private String venderName;//生产厂家名称
}
