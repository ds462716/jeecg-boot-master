package org.jeecg.modules.pd.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class PdDosagePage extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**用量单号*/
	@Excel(name = "用量单号", width = 15)
    @ApiModelProperty(value = "用量单号")
    private String dosageNo;
    @Excel(name = "用量库房", width = 15)
    @ApiModelProperty(value = "用量库房")
    @TableField(exist = false)
    private String departName;
    @Excel(name = "货位", width = 15)
    @ApiModelProperty(value = "货位")
    @TableField(exist = false)
    private String outHuoweiName;//出库货位
	/**用量日期*/
    @Excel(name = "用量日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "用量日期")
    private Date dosageDate;
    @Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称")
    @TableField(exist = false)
    private String productName;//产品名称
    @Excel(name = "产品条码", width = 15)
    @ApiModelProperty(value = "产品条码")
    @TableField(exist = false)
    private String productBarCode;
    @Excel(name = "产品规格", width = 15)
    @ApiModelProperty(value = "产品规格")
    @TableField(exist = false)
    private String spec;//产品规格
    @Excel(name = "产品型号", width = 15)
    @ApiModelProperty(value = "产品型号")
    @TableField(exist = false)
    private String version;//产品型号
    @Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号")
    @TableField(exist = false)
    private String batchNo;
    @Excel(name = "有效期", width = 15)
    @ApiModelProperty(value = "有效期")
    @TableField(exist = false)
    private String expDate;
    /**用量总数*/
    @Excel(name = "使用单数量", width = 15)
    @ApiModelProperty(value = "使用单数量")
    private Double totalSum;
    @Excel(name = "实际使用数量", width = 15)
    @ApiModelProperty(value = "实际使用数量")
    private Double leftRefundNum;
	/**用量总金额*/
	@Excel(name = "实际使用总金额", width = 15)
    @ApiModelProperty(value = "实际使用总金额")
    private BigDecimal totalPrice;
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    @TableField(exist = false)
    private String unitName;//单位名称
    @Excel(name = "生产厂家", width = 15)
    @ApiModelProperty(value = "生产厂家")
    @TableField(exist = false)
    private String venderName;//生产厂家名称
	/**病人信息*/
	@Excel(name = "病人信息", width = 15)
    @ApiModelProperty(value = "病人信息")
    private String patientInfo;
	/**病人详细信息*/
    private String patientDetailInfo;
	/**执行科室id*/
    private String exeDeptId;
	/**执行科室名称*/
	@Excel(name = "执行科室名称", width = 15)
    @ApiModelProperty(value = "执行科室名称")
    private String exeDeptName;
	/**手术科室id*/
    private String oprDeptId;
	/**手术科室名称*/
	@Excel(name = "手术科室名称", width = 15)
    @ApiModelProperty(value = "手术科室名称")
    private String oprDeptName;
	/**手术医生id*/
    private String surgeonId;
	/**手术医生名称*/
	@Excel(name = "手术医生名称", width = 15)
    @ApiModelProperty(value = "手术医生名称")
    private String surgeonName;
	/**开方医生id*/
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
    private String dosageBy;
    @Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
    @TableField(exist = false)
	private String dosageByName;
	/**所属病区id*/
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
    private String displayFlag;
	/**创建人*/
    private String createBy;
	/**创建日期*/
    private Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
    private Date updateTime;
	/**所属部门*/
    private String sysOrgCode;
	/**备注*/
    private String remarks;
	/**所属部门*/
    private String departId;
	/**所属医院*/
    private String departParentId;
    /**
     * 是否收费标识
     */
	private String hyCharged;

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
    /*多个部门集合*/
    @TableField(exist = false)
    private List<String> departIdList;


    /**
     * 使用报表查询条件部门集合
     */
    @TableField(exist = false)
    private String departIds;

    //冗余
    /*不是明细表字段*/

    @TableField(exist = false)
    private String number;//产品编号
    @TableField(exist = false)
    private String productNumber;//产品编号



    @TableField(exist = false)
    private String venderId;//生产厂家ID
    @TableField(exist = false)
	private String supplierId;//供应商ID
    @TableField(exist = false)
    private String supplierName;//供应商名称
    @TableField(exist = false)
    private String registration;//注册证号
    @TableField(exist = false)
    private String inHuoweiName;// 入库货位
    @TableField(exist = false)
    private String oldInHuoweiCode;
    @TableField(exist = false)
    private String oldOutHuoweiCode;
    @TableField(exist = false)
    private String dosageCount;
    @TableField(exist = false)
    private String chargeCode;
    @TableField(exist = false)
    private String dhyCharged;
    @TableField(exist = false)
    private String pdDosageDetailId;
    @TableField(exist = false)
    private String deptName;

}
