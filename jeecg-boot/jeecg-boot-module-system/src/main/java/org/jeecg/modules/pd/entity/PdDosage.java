package org.jeecg.modules.pd.entity;

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
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
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
    private BigDecimal totalPrice;
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
    /**病人类型  0:住院  1:门诊 */
    @Excel(name = "病人类型", width = 15)
    @ApiModelProperty(value = "病人类型")
    private String patientType;
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

    /**病床号*/
    @Excel(name = "病床号", width = 15)
    @ApiModelProperty(value = "病床号")
    private String bedNumber;

    /**病例号*/
    @Excel(name = "病例号", width = 15)
    @ApiModelProperty(value = "病例号")
    private String medicalRecordNo;

    /**住院次数*/
    @Excel(name = "住院次数", width = 15)
    @ApiModelProperty(value = "住院次数")
    private String hospitalizationsNum;

    /**手术名称*/
    @Excel(name = "手术名称", width = 15)
    @ApiModelProperty(value = "手术名称")
    private String operationName;

    /**
     * 扩展字段1
     * 在丰城人民医院用作：手术或检查项目科室(仅做记录) add by jiangxz 2020年6月18日16:28:59
     */
    private String  extension1;//扩展字段1
    /**
     * 扩展字段2
     * 在丰城人民医院用作：门诊收费id(仅做记录) add by jiangxz 2020年6月18日16:28:59
     */
    private String  extension2;//扩展字段2


    private String visitNo;//就诊流水号（丰城中医院）

    @TableField(exist = false)
    private String oprDate;// 登记日期

    /**性别*/
    @TableField(exist = false)
    private String fsfXb;

    /**出生日期*/
    @TableField(exist = false)
    private Date fsfCsrq;

    /**手术时间*/
    @TableField(exist = false)
    private Date operationTime;

    /**入院日期*/
    @TableField(exist = false)
    private Date admissionDate;

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


    @TableField(exist = false)
    private BigDecimal jfTotalPrice;//计费总金额
    //冗余
    /*不是明细表字段*/
    @TableField(exist = false)
    private String productName;//产品名称
    @TableField(exist = false)
    private String number;//产品编号
    @TableField(exist = false)
    private String productNumber;//产品编号
    @TableField(exist = false)
    private String spec;//产品规格
    @TableField(exist = false)
    private String version;//产品型号
    @TableField(exist = false)
    private String unitName;//单位名称
    @TableField(exist = false)
    private String venderId;//生产厂家ID
    @TableField(exist = false)
    private String venderName;//生产厂家名称
    @TableField(exist = false)
    private String supplierId;//供应商ID
    @TableField(exist = false)
    private String supplierName;//供应商名称
    @TableField(exist = false)
    private String registration;//注册证号
    @TableField(exist = false)
    private String outHuoweiName;//出库货位
    @TableField(exist = false)
    private String batchNo;
    @TableField(exist = false)
    private String productBarCode;
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expDate;
    @TableField(exist = false)
    private String dosageCount;
    @TableField(exist = false)
    private String chargeCode;
    @TableField(exist = false)
    private String dhyCharged;
    @TableField(exist = false)
    private String pdDosageDetailId;
    /** 查询日期起始 **/
    @TableField(exist = false)
    private String queryDateStart;
    /** 查询日期结束 **/
    @TableField(exist = false)
    private String queryDateEnd;
    /**
     * 查询病人信息类别
     */
    @TableField(exist = false)
    private String prjType;

    private String dosageType;//使用类型，0唯一码使用和1普通码使用
    @TableField(exist = false)
    private String hospitalCode;//医院标识
    @TableField(exist = false)
    private String type;//住院标识（1：是  2：否）
    @TableField(exist = false)
    private String token;// 用于HIS收费防重复提交
}
