package org.jeecg.modules.pd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:赣州市立医院用量明细导出
 * @Author: mzb
 * @Date:   2020-08-13
 * @Version: V1.0
 */
@Data
public class PdDosagePageExcel{
	/**用量单号*/
	@Excel(name = "用量单号", width = 15)
    private String dosageNo;
    @Excel(name = "使用科室", width = 15)
    private String departName;
    @Excel(name = "产品名称", width = 15)
    private String productName;//产品名称
    @Excel(name = "使用数量", width = 15)
    private Double dosageCount;
    @Excel(name = "单位", width = 15)
    private String unitName;//单位名称
    @Excel(name = "使用金额", width = 15)
    private BigDecimal amountMoney;
    @Excel(name = "产品规格", width = 15)
    private String spec;//产品规格
    @Excel(name = "批号", width = 15)
    private String batchNo;
    @Excel(name = "有效期", width = 15)
    private String expDate;
    @Excel(name = "住院号", width = 15)
    private String inHospitalNo;
    @Excel(name = "病人姓名", width = 15)
    private String patientInfo;
    @Excel(name = "使用日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dosageDate;
    @Excel(name = "唯一码", width = 15)
    private String refBarCode;
    @Excel(name = "生产厂家", width = 15)
    private String venderName;//生产厂家名称
    @Excel(name = "供应商", width = 15)
    private String supplierName;//供应商名称
    @Excel(name = "中标号", width = 15)
    private String bidingNumber;//中标号











}
