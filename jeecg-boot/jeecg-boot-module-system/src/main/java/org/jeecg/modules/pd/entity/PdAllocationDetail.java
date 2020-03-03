package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 调拨明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Data
@TableName("pd_allocation_detail")
public class PdAllocationDetail extends BaseEntity {
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
	/**调拨单编号*/
	private String allocationNo;
	/**产品ID*/
	@Excel(name = "产品ID", width = 15)
	private String productId;
	/**产品批次号*/
	@Excel(name = "产品批次号", width = 15)
	private String batchNo;
	/**产品条码*/
	@Excel(name = "产品条码", width = 15)
	private String productBarCode;
	/**调拨数量*/
	@Excel(name = "调拨数量", width = 15)
	private Double allocationNum;
	/**调拨时库存数量*/
	@Excel(name = "调拨时库存数量", width = 15)
	private Double stockNum;
	/**产品属性：1、产品 2、定数包*/
	@Excel(name = "产品属性：1、产品 2、定数包", width = 15)
	private String productAttr;



	/*不是明细表字段*/
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
	@TableField(exist = false)
	private String packageName;//定数包名称
	@TableField(exist = false)
	private String packageCode;//定数包编号
}
