package org.jeecg.modules.external.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class ExInspectionItems extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
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
	/**就诊卡号3*/
	@Excel(name = "就诊卡号3", width = 15)
    @ApiModelProperty(value = "就诊卡号3")
    private String cardId;
	/**条形码*/
	@Excel(name = "条形码", width = 15)
    @ApiModelProperty(value = "条形码")
    private String barCode;
	/**申请医生*/
	@Excel(name = "申请医生", width = 15)
    @ApiModelProperty(value = "申请医生")
    private String applyDoctor;
	/**申请科室*/
	@Excel(name = "申请科室", width = 15)
    @ApiModelProperty(value = "申请科室")
    private String applyDepartment;
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
	@Excel(name = "接收日期", width = 15)
    @ApiModelProperty(value = "接收日期")
    private String receiveDate;
	/**检验日期*/
	@Excel(name = "检验日期", width = 15)
    @ApiModelProperty(value = "检验日期")
    private String testDate;
	/**样本类型*/
	@Excel(name = "样本类型", width = 15)
    @ApiModelProperty(value = "样本类型")
    private String specimenType;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String state;
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

    /**
     * 检验项目代码集合
     */
    private String data;
    /**
     * 检验项目代码集合
     */
    @TableField(exist = false)
    private List<ExInspectionItemsDetail> exInspectionItemsDetails;
}
