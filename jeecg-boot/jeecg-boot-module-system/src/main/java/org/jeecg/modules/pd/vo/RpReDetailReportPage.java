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
public class RpReDetailReportPage {

    private String id;//出入库明细id
    /**退货日期*/
    @Excel(name = "退货日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "退货日期")
    private Date rejectedDate;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;//供应商名称
    @Excel(name = "退货单号", width = 15)
    @ApiModelProperty(value = "退货单号")
    private String rejectedNo;
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
    @Excel(name = "退货数量", width = 15)
    @ApiModelProperty(value = "退货数量")
    private Double rejectedCount;
    @Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称")
    private String unitName;//单位名称
    /**退货单价*/
    @Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal purchasePrice;
    /**退货金额*/
    @Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    private java.math.BigDecimal rejectedPrice;
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


    private String supplierId;//供应商ID
    private String departParentId;
    private String yearMonth;
    private String queryExpDateStart;
    private String queryExpDateEnd;
    private String queryDateStart;
    private String queryDateEnd;
    private String registration;

}
