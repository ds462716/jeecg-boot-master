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
import java.util.Date;

/**
 * @Description: 申购模板详细表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Data
@TableName("pd_purchase_temp_detail")
public class PdPurchaseTempDetail extends BaseEntity {
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
	/**模板编号*/
	@Excel(name = "模板编号", width = 15)
	private String tempNo;
	/**产品ID*/
	private String productId;
	/**申购数量*/
	@Excel(name = "申购数量", width = 15)
	private Double orderNum;
	/**备注*/
	private String remarks;
	/**产品名称*/
	@TableField(exist = false)
	private String productName;
	/**产品编号*/
	@TableField(exist = false)
	private String number;
	/**产品规格*/
	@TableField(exist = false)
	private String spec;
	/**单位名称*/
	@TableField(exist = false)
	private String unitName;
	/**中标号*/
	@TableField(exist = false)
	private String bidingNumber;
	/**生产厂家*/
	@TableField(exist = false)
	private String venderName;
	/**供应商*/
	@TableField(exist = false)
	private String supplierName;
	/**产品出价*/
	@TableField(exist = false)
	private BigDecimal purchasePrice;
}


