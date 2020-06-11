package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: ex_lab_instr_inf
 * @Author: jiangxz
 * @Date:   2020-06-09
 * @Version: V1.0
 */
@Data
@TableName("ex_lab_instr_inf")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ExLabInstrInf  extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**设备代号*/
	@Excel(name = "设备代号", width = 15)
    @ApiModelProperty(value = "设备代号")
    private String instrCode;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String instrName;
	/**拼音简码*/
	@Excel(name = "拼音简码", width = 15)
    @ApiModelProperty(value = "拼音简码")
    private String py;
	/**五笔简码*/
	@Excel(name = "五笔简码", width = 15)
    @ApiModelProperty(value = "五笔简码")
    private String wb;
	/**自定义简码*/
	@Excel(name = "自定义简码", width = 15)
    @ApiModelProperty(value = "自定义简码")
    private String zdy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
	/**所属科室*/
	@Excel(name = "所属科室", width = 15)
    @ApiModelProperty(value = "所属科室")
    private String departId;
	/**所在检验室*/
	@Excel(name = "所在检验室", width = 15)
    @ApiModelProperty(value = "所在检验室")
    private String testDepartId;
	/**使用状态*/
	@Excel(name = "使用状态", width = 15)
    @ApiModelProperty(value = "使用状态")
    private String status;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd  HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd  HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd  HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**部门*/
	@Excel(name = "部门", width = 15)
    @ApiModelProperty(value = "部门")
    private String sysOrgCode;
	/**所属机构*/
	@Excel(name = "所属机构", width = 15)
    @ApiModelProperty(value = "所属机构")
    private String departParentId;
    /**所属科室名称*/
    @TableField(exist = false)
    private String departName;
    /**检验室名称*/
    @TableField(exist = false)
    private String testDepartName;
}
