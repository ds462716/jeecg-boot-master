package org.jeecg.modules.pd.entity;

import java.io.Serializable;
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
 * @Description: pd_storeroom_patrol
 * @Author: jiangxz
 * @Date:   2020-04-07
 * @Version: V1.0
 */
@ApiModel(value="pd_storeroom_patrol对象", description="pd_storeroom_patrol")
@Data
@TableName("pd_storeroom_patrol")
public class PdStoreroomPatrol extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**巡查单号*/
	@Excel(name = "巡查单号", width = 15)
    @ApiModelProperty(value = "巡查单号")
    private String patrolNo;
    @ApiModelProperty(value = "巡查部门ID")
	private String patrolDepartId;
	/**巡查产品量*/
	@Excel(name = "巡查产品量", width = 15)
    @ApiModelProperty(value = "巡查产品量")
    private Double patrolCount;
	/**合格产品量*/
	@Excel(name = "合格产品量", width = 15)
    @ApiModelProperty(value = "合格产品量")
    private Double passCount;
	/**不合格产品量*/
	@Excel(name = "不合格产品量", width = 15)
    @ApiModelProperty(value = "不合格产品量")
    private Double failCount;
	/**温度*/
	@Excel(name = "温度", width = 15)
    @ApiModelProperty(value = "温度")
    private String temperature;
	/**湿度*/
	@Excel(name = "湿度", width = 15)
    @ApiModelProperty(value = "湿度")
    private String wetness;
	/**巡查员*/
	@Excel(name = "巡查员", width = 15)
    @ApiModelProperty(value = "巡查员")
    private String patrolMan;
	/**巡查日期*/
	@Excel(name = "巡查日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "巡查日期")
    private Date patrolDate;
	/**备注信息*/
	@Excel(name = "备注信息", width = 15)
    @ApiModelProperty(value = "备注信息")
    private String remarks;
	/**删除标记 : 删除标记（0：正常；1：删除）*/
	@Excel(name = "删除标记 : 删除标记（0：正常；1：删除）", width = 15)
    @ApiModelProperty(value = "删除标记 : 删除标记（0：正常；1：删除）")
    private String delFlag;
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
    private List<PdStoreroomPatrolDetail> pdStoreroomPatrolDetailList;
    /** 查询日期起始 **/
    @TableField(exist = false)
    private String queryDateStart;
    /** 查询日期结束 **/
    @TableField(exist = false)
    private String queryDateEnd;
    @TableField(exist = false)
    private String productName;
    @TableField(exist = false)
    private String number;
    @TableField(exist = false)
    private String batchNo;
    @TableField(exist = false)
    private String patrolDepartName;
}
