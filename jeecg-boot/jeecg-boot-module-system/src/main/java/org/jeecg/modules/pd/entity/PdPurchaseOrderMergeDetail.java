package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 合并申购明细表
 * @Author: jiangxz
 * @Date:   2020-03-20
 * @Version: V1.0
 */
@Data
@TableName("pd_purchase_order_merge_detail")
public class PdPurchaseOrderMergeDetail extends BaseEntity {
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
	@ApiModelProperty(value = "更新人")
	private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	private String sysOrgCode;
	/**合并申购编号*/
	private String mergeOrderNo;
	/**产品id*/
	private String productId;
	/**申购数量*/
	@Excel(name = "申购数量", width = 15)
	private Double orderNum;
	/**申购编号（会有多个）*/
	@Excel(name = "申购编号", width = 15)
	private String orderNo;
	/**到货数量*/
	@Excel(name = "到货数量", width = 15)
	private Double arrivalNum;
	/**供应商ID*/
	private String supplierId;
	/**备注*/
	@Excel(name = "备注", width = 15)
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
	@TableField(exist = false)
	@Excel(name = "供应商名称", width = 15)
	private String supplierName;//供应商名称
	@TableField(exist = false)
	@Excel(name = "生产厂家名称", width = 15)
	private String venderName;//生产厂家名称
}
