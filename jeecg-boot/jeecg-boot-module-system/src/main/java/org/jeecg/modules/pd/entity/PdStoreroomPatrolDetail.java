package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: pd_storeroom_patrol_detail
 * @Author: jiangxz
 * @Date:   2020-04-07
 * @Version: V1.0
 */
@ApiModel(value="pd_storeroom_patrol对象", description="pd_storeroom_patrol")
@Data
@TableName("pd_storeroom_patrol_detail")
public class PdStoreroomPatrolDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "主键")
	private String id;
	/**巡查ID*/
	@ApiModelProperty(value = "巡查ID")
	private String storeroomPatrolId;
	/**产品主键*/
	@Excel(name = "产品主键", width = 15)
	@ApiModelProperty(value = "产品主键")
	private String productId;
	/**记录时库存*/
	@Excel(name = "记录时库存", width = 15)
	@ApiModelProperty(value = "记录时库存")
	private Double stockNum;
	/**巡查结果 : 合格，不合格，其他*/
	@Excel(name = "巡查结果 : 合格，不合格，其他", width = 15)
	@ApiModelProperty(value = "巡查结果 : 合格，不合格，其他")
	private String patrolResult;
	/**批次号*/
	@Excel(name = "批次号", width = 15)
	@ApiModelProperty(value = "批次号")
	private String batchNo;
	/**产品条码*/
	@Excel(name = "产品条码", width = 15)
	@ApiModelProperty(value = "产品条码")
	private String productBarCode;
	/**备注信息 : 备注信息*/
	@Excel(name = "备注信息 : 备注信息", width = 15)
	@ApiModelProperty(value = "备注信息 : 备注信息")
	private String remarks;
	/**删除标记 : 删除标记（0：正常；1：删除）*/
	@Excel(name = "删除标记 : 删除标记（0：正常；1：删除）", width = 15)
	@ApiModelProperty(value = "删除标记 : 删除标记（0：正常；1：删除）")
	private String delFlag;
	/**extend1*/
	@Excel(name = "extend1", width = 15)
	@ApiModelProperty(value = "extend1")
	private String extend1;
	/**extend2*/
	@Excel(name = "extend2", width = 15)
	@ApiModelProperty(value = "extend2")
	private String extend2;
	/**extend3*/
	@Excel(name = "extend3", width = 15)
	@ApiModelProperty(value = "extend3")
	private String extend3;
	/**部门ID*/
	@Excel(name = "部门ID", width = 15)
	@ApiModelProperty(value = "部门ID")
	private String departId;
	/**所属父部门*/
	@Excel(name = "所属父部门", width = 15)
	@ApiModelProperty(value = "所属父部门")
	private String departParentId;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	@ApiModelProperty(value = "所属部门")
	private String sysOrgCode;
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

	@TableField(exist = false)
	private String departName;
	@TableField(exist = false)
	private String productName;
	@TableField(exist = false)
	private String number;
	@TableField(exist = false)
	private String spec;
	@TableField(exist = false)
	private String version;
	@TableField(exist = false)
	private String unitName;
	@TableField(exist = false)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date expDate;
	@TableField(exist = false)
	private String registration;
	@TableField(exist = false)
	private String venderName;
	@TableField(exist = false)
	private String isExp; // 是否过期
}
