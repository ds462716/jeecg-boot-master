package org.jeecg.modules.pd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author jiangxz
 * @description 出入库统计报表明细
 * @date 2020-8-5
 */
@Data
public class RpDepartUseDetailReportPage {

    private String id;//出入库明细id
    /**用量日期*/
    @Excel(name = "用量日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "用量日期")
    private Date dosageDate;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;//供应商名称
    @Excel(name = "用量单号", width = 15)
    @ApiModelProperty(value = "用量单号")
    private String dosageNo;
    @Excel(name = "产品编号", width = 15)
    @ApiModelProperty(value = "产品编号")
    private String productNumber;//产品编号
    @Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称")
    private String productName;//产品名称
    @Excel(name = "产品规格", width = 15)
    @ApiModelProperty(value = "产品规格")
    private String spec;//产品规格
    @Excel(name = "产品型号", width = 15)
    @ApiModelProperty(value = "产品型号")
    private String version;//产品型号
    @Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号")
    private String batchNo;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expDate;
    @Excel(name = "用量数量", width = 15)
    @ApiModelProperty(value = "用量数量")
    private Double leftRefundNum;
    @Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称")
    private String unitName;//单位名称
    /**用量单价*/
    @Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal sellingPrice;
    /**用量金额*/
    @Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    private java.math.BigDecimal dosagePrice;
    @Excel(name = "生产厂家", width = 15)
    @ApiModelProperty(value = "生产厂家")
    private String venderName;//生产厂家名称
    private String venderId;
    @Excel(name = "注册证号", width = 15)
    @ApiModelProperty(value = "注册证号")
    private String productRegistration;//注册证号
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**备注*/
    @Excel(name = "产品收费代码", width = 15)
    @ApiModelProperty(value = "产品收费代码")
    private String chargeCode;

    /**病人信息*/
    @Excel(name = "病人信息", width = 15)
    @ApiModelProperty(value = "病人信息")
    private String patientInfo;
    /**病人详细信息*/
    @Excel(name = "病人详细信息", width = 15)
    @ApiModelProperty(value = "病人详细信息")
    private String patientDetailInfo;
    /**执行科室名称*/
    @Excel(name = "执行科室名称", width = 15)
    @ApiModelProperty(value = "执行科室名称")
    private String exeDeptName;
    /**手术科室名称*/
    @Excel(name = "手术科室名称", width = 15)
    @ApiModelProperty(value = "手术科室名称")
    private String oprDeptName;
    /**手术医生名称*/
    @Excel(name = "手术医生名称", width = 15)
    @ApiModelProperty(value = "手术医生名称")
    private String surgeonName;
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
    private String dosageByName;
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
    private String supplierId;//供应商ID
    private String departId;
    private String departParentId;
    private String yearMonth;
    private String queryExpDateStart;
    private String queryExpDateEnd;
    private String queryDateStart;
    private String queryDateEnd;
    private String registration;

    private String flag ;//查询flag 传空查全部 传0查收费，传其他表示不收费
}
