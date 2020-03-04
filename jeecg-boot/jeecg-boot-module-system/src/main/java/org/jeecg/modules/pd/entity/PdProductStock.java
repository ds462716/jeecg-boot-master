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
	/**删除标识*/
	private String delFlag;
	/**是否永存*/
	@Excel(name = "是否永存", width = 15)
	private String isLong;
	/**是否永存*/
	@Excel(name = "货位编号", width = 15)
	private String huoweiCode;

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
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
	@TableField(exist = false)
	private String supplierName;
	/**科室名称*/
	@Excel(name = "科室名称", width = 15)
	@TableField(exist = false)
	private String deptName;
}