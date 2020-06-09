package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Data
@TableName("ex_inspection_items")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ex_inspection_items对象", description="检查项目表")
public class ExInspectionItems{
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "lis系统主键")
	private String jyId;
	/**患者姓名*/
	@Excel(name = "患者姓名", width = 15)
    @ApiModelProperty(value = "患者姓名")
    private String patientName;
	/**患者性别*/
	@Excel(name = "患者性别", width = 15)
    @ApiModelProperty(value = "患者性别")
    private String patientSex;
	/**患者年龄*/
	@Excel(name = "患者年龄", width = 15)
    @ApiModelProperty(value = "患者年龄")
    private String patientAge;
	/**就诊卡号*/
	@Excel(name = "就诊卡号", width = 15)
    @ApiModelProperty(value = "就诊卡号")
    private String cardId;
	/**条形码*/
	@Excel(name = "条形码", width = 15)
    @ApiModelProperty(value = "条形码")
    private String barCode;
	/**申请医生ID*/
	@Excel(name = "申请医生ID", width = 15)
    @ApiModelProperty(value = "申请医生ID")
    private String applyDoctor;
    /**申请医生姓名*/
    @TableField(exist = false)
    private String applyDoctorName;
	/**申请科室ID*/
	@Excel(name = "申请科室ID", width = 15)
    @ApiModelProperty(value = "申请科室ID")
    private String applyDepartment;
    /**申请科室名称*/
    @TableField(exist = false)
    private String applyDepartmentName;
	/**检验医生*/
	@Excel(name = "检验医生", width = 15)
    @ApiModelProperty(value = "检验医生")
    private String testDoctor;
	/**检验科室*/
	@Excel(name = "检验科室", width = 15)
    @ApiModelProperty(value = "检验科室")
    private String testDepartment;
	/**患者类型*/
	@Excel(name = "患者类型", width = 15)
    @ApiModelProperty(value = "患者类型")
    private String patientType;
	/**工作组*/
	@Excel(name = "工作组", width = 15)
    @ApiModelProperty(value = "工作组")
    private String groupBy;
    /**接收日期*/
    @Excel(name = "接收日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "接收日期")
    private Date receiveDate;
	/**检验日期*/
    @Excel(name = "检验日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检验日期")
    private Date testDate;
	/**样本类型*/
	@Excel(name = "样本类型", width = 15)
    @ApiModelProperty(value = "样本类型")
    private String specimenType;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String state;
    @Excel(name = "项目组合名称", width = 15)
    @ApiModelProperty(value = "项目组合名称")
    private String combinationName;
    @Excel(name = "项目组合代码", width = 15)
    @ApiModelProperty(value = "项目组合代码")
    private String combinationCode;
    @Excel(name = "检查项目名称", width = 15)
    @ApiModelProperty(value = "检查项目名称")
    private String testItemName;
    @Excel(name = "检查项目代码", width = 15)
    @ApiModelProperty(value = "检查项目代码")
    private String testItemCode;
    @Excel(name = "检查项目费用", width = 15)
    @ApiModelProperty(value = "检查项目费用")
    private Double testItemCost;
    @Excel(name = "读取状态", width = 15)
    @ApiModelProperty(value = "读取状态")
    private String acceptStatus;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**所属仪器代号*/
    @Excel(name = "所属仪器代号", width = 15)
    @ApiModelProperty(value = "所属仪器代号")
    private String instrCode;
    /**所属仪器名称*/
    @Excel(name = "所属仪器名称", width = 15)
    @ApiModelProperty(value = "所属仪器名称")
    private String instrName;
    /**检验项目ID*/
    private String packageId;
    /**备注*/
    @Excel(name = "备注", width = 15)
    private String remarks;
    /**
     * 所属部门
     */
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    private String departId;

    /**
     * 所属顶级部门（医院id）
     */
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    private String departParentId;

    /** 查询日期起始 **/
    @TableField(exist = false)
    private String queryDateStart;
    /** 查询日期结束 **/
    @TableField(exist = false)
    private String queryDateEnd;


}
