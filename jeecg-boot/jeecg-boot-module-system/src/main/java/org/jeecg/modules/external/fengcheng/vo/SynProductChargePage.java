package org.jeecg.modules.external.fengcheng.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @author jiangxz
 * @description 产品价格对照表
 * @date 2020-7-7
 */
@Data
public class SynProductChargePage {

    @Excel(name = "产品名称", width = 15)
    private java.lang.String name;
    @Excel(name = "产品编号", width = 15)
    private java.lang.String number;
    @Excel(name = "规格", width = 15)
    private java.lang.String spec;
    @Excel(name = "单位", width = 15)
    private java.lang.String unitName;
    @Excel(name = "生产厂家", width = 15)
    private java.lang.String venderName;
    @Excel(name = "是否计费", width = 15)
    private java.lang.String isCharge;
    @Excel(name = "进价", width = 15)
    private java.math.BigDecimal purchasePrice;
    @Excel(name = "出价", width = 15)
    private java.math.BigDecimal sellingPrice;
    @Excel(name = "HIS价格", width = 15)
    private BigDecimal fsfJe;//HIS价格
    @Excel(name = "价格差", width = 15)
    private BigDecimal priceDifference;//价格差
    @Excel(name = "HIS项目名称", width = 15)
    private String fsfXmmc;//HIS项目名称
}
