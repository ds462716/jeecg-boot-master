package org.jeecg.modules.pd.entity;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用量表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Data
@TableName("pd_dosage")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pd_dosage对象", description="用量表")
public class PdDosage extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**用量单号*/
	@Excel(name = "用量单号", width = 15)
    @ApiModelProperty(value = "用量单号")
    private String dosageNo;
	/**用量日期*/
    @Excel(name = "用量日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "用量日期")
    private Date dosageDate;
	/**用量总数*/
	@Excel(name = "用量总数", width = 15)
    @ApiModelProperty(value = "用量总数")
    private Double totalSum;
	/**用量总金额*/
	@Excel(name = "用量总金额", width = 15)
    @ApiModelProperty(value = "用量总金额")
    private java.math.BigDecimal totalPrice;
	/**病人信息*/
	@Excel(name = "病人信息", width = 15)
    @ApiModelProperty(value = "病人信息")
    private String patientInfo;
	/**病人详细信息*/
	@Excel(name = "病人详细信息", width = 15)
    @ApiModelProperty(value = "病人详细信息")
    private String patientDetailInfo;
	/**执行科室id*/
	@Excel(name = "执行科室id", width = 15)
    @ApiModelProperty(value = "执行科室id")
    private String exeDeptId;
	/**执行科室名称*/
	@Excel(name = "执行科室名称", width = 15)
    @ApiModelProperty(value = "执行科室名称")
    private String exeDeptName;
	/**手术科室id*/
	@Excel(name = "手术科室id", width = 15)
    @ApiModelProperty(value = "手术科室id")
    private String oprDeptId;
	/**手术科室名称*/
	@Excel(name = "手术科室名称", width = 15)
    @ApiModelProperty(value = "手术科室名称")
    private String oprDeptName;
	/**手术医生id*/
	@Excel(name = "手术医生id", width = 15)
    @ApiModelProperty(value = "手术医生id")
    private String surgeonId;
	/**手术医生名称*/
	@Excel(name = "手术医生名称", width = 15)
    @ApiModelProperty(value = "手术医生名称")
    private String surgeonName;
	/**开方医生id*/
	@Excel(name = "开方医生id", width = 15)
    @ApiModelProperty(value = "开方医生id")
    private String sqrtDoctorId;
	/**开方医生名称*/
	@Excel(name = "开方医生名称", width = 15)
    @ApiModelProperty(value = "开方医生名称")
    private String sqrtDoctorName;
	/**住院号*/
	@Excel(name = "住院号", width = 15)
    @ApiModelProperty(value = "住院号")
    private String inHospitalNo;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
    private String dosageBy;
    @TableField(exist = false)
	private String dosageByName;
	/**所属病区id*/
	@Excel(name = "所属病区id", width = 15)
    @ApiModelProperty(value = "所属病区id")
    private String subordinateWardId;
	/**所属病区名称*/
	@Excel(name = "所属病区名称", width = 15)
    @ApiModelProperty(value = "所属病区名称")
    private String subordinateWardName;
	/**门诊号*/
	@Excel(name = "门诊号", width = 15)
    @ApiModelProperty(value = "门诊号")
    private String outpatientNumber;
	/**手术编号*/
	@Excel(name = "手术编号", width = 15)
    @ApiModelProperty(value = "手术编号")
    private String operativeNumber;
	/**是否有his接口标识符0没有，1有*/
	@Excel(name = "是否有his接口标识符0没有，1有", width = 15)
    @ApiModelProperty(value = "是否有his接口标识符0没有，1有")
    private String displayFlag;
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
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String departId;
    @TableField(exist = false)
    private String departName;
	/**所属医院*/
	@Excel(name = "所属医院", width = 15)
    @ApiModelProperty(value = "所属医院")
    private String departParentId;

    /**
     * 用量明细
     */
    @TableField(exist = false)
	private List<PdDosageDetail> pdDosageDetails;

    /**
     * 货区货位下拉
     */
    @TableField(exist = false)
    private List<PdGoodsAllocationPage> goodsAllocationList;

}
