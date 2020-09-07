package org.jeecg.modules.pd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
public class PdProductStockExcel{
    /**科室名称*/
    @Excel(name = "科室名称", width = 15)
    private String deptName;
    /**产品名称*/
    @Excel(name = "产品名称", width = 15)
    private String productName;
    /**产品编号*/
    @Excel(name = "产品编号", width = 15)
    private String number;
    @Excel(name = "产品类型", width = 15)
    private String productFlagName;
    /**产品条码*/
    @Excel(name = "产品条码", width = 15)
    private String productBarCode;
    /**规格*/
    @Excel(name = "规格", width = 15)
    private String spec;
    /**批次号*/
    @Excel(name = "批次号", width = 15)
    private String batchNo;
    /**生产日期*/
    @Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date produceDate;
    /**有效期*/
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expDate;
    /**库存数量*/
    /**单位名称*/
    @Excel(name = "单位名称", width = 15)
    private String unitName;
    @Excel(name = "数量", width = 15)
    private Double stockNum;
    @Excel(name = "产品单价", width = 15)
    private BigDecimal purchasePrice;
    @Excel(name = "规格单位", width = 15)
    private String specUnitName;//规格单位
    @Excel(name = "规格数量", width = 15)
    private Double specQuantity;//规格数量
    /**库存占用状态  0:使用中   1:未使用 2:已用完*/
    @Excel(name = "库存占用状态 0:使用中   1:未使用", width = 15)
    private String nestatStatus;
    /**过期状态*/
    @Excel(name = "过期状态", width = 15)
    private String expStatus;
    /**是否永存*/
    @Excel(name = "是否永存", width = 15)
    private String isLong;
    /**货位编号*/
    @Excel(name = "货位编号", width = 15)
    private String huoweiCode;
    /**注册证号*/
    @Excel(name = "注册证号", width = 15)
    private String registration;
    /**厂家名称*/
    @Excel(name = "厂家名称", width = 15)
    private String venderName;
    @Excel(name = "厂家JDE编号", width = 15)
    private String venderJdeCode; // 厂家JDEcode
    /**供应商名称*/
    @Excel(name = "供应商名称", width = 15)
    private String supplierName;
    @Excel(name = "供应商JDE编号", width = 15)
    private String supplierJdeCode;//供应商JDEcode
    @Excel(name = "配送商", width = 15)
    private String distributorName;//配送商名称
    /**中标号*/
    @Excel(name = "中标号", width = 15)
    private String bidingNumber;
    /**收费代码*/
    @Excel(name = "收费代码", width = 15)
    private String chargeCode;
    /**条码集合*/
    @Excel(name = "唯一码", width = 15)
    private String refBarCodes;
    @Excel(name = "产品JDE编号", width = 15)
    private String jdeCode;


}


