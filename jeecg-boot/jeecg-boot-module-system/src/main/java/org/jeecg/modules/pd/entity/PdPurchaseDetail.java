package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 申购单详细表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Data
@TableName("pd_purchase_detail")
public class PdPurchaseDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
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
	/**申购单编号*/
	@Excel(name = "申购单编号", width = 15)
	private String orderNo;
	/**产品ID*/
	private String productId;
	/**产品单价*/
	@Excel(name = "产品单价", width = 15)
	private BigDecimal purchasePrice;
	/**申购数量*/
	@Excel(name = "申购数量", width = 15)
	private Double orderNum;
	/**申购总金额*/
	@Excel(name = "申购总金额", width = 15)
	private BigDecimal orderMoney;
	/**申购时总库存数量*/
	@Excel(name = "申购时总库存数量", width = 15)
	private Double stockNum;
	/**到货数量*/
	@Excel(name = "到货数量", width = 15)
	private Double arrivalNum;
	/**备注*/
	private String remarks;
	@TableField(exist = false)
	@Excel(name = "产品名称", width = 15)
	private String productName;//产品名称
	@TableField(exist = false)
	@Excel(name = "产品编号", width = 15)
	private String number;//产品编号
	@TableField(exist = false)
	@Excel(name = "产品规格", width = 15)
	private String spec;//产品规格
	@TableField(exist = false)
	@Excel(name = "产品型号", width = 15)
	private String version;//产品型号
	@TableField(exist = false)
	@Excel(name = "单位名称", width = 15)
	private String unitName;//单位名称
	/**供应商id*/
	private String supplierId;
	@TableField(exist = false)
	@Excel(name = "供应商名称", width = 15)
	private String supplierName;//供应商名称
	@TableField(exist = false)
	@Excel(name = "生产厂家名称", width = 15)
	private String venderName;//生产厂家名称
	/**申购编号（会有多个）*/
	@TableField(exist = false)
	@Excel(name = "申购编号", width = 15)
	private List<String> orderNos;
	/**合并申购编号*/
	@TableField(exist = false)
	@Excel(name = "合并申购编号", width = 15)
	private String mergeOrderNo;
}


