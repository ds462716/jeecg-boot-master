package org.jeecg.modules.external.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PdBottleInfExlce{

    @Excel(name = "产品名称", width = 15)
    private String productName;
    @Excel(name = "产品编号", width = 15)
    private String number;//产品编号
    @Excel(name = "规格数量", width = 15)
    private Double specQuantity;
    @Excel(name = "规格单位", width = 15)
    private String specUnitName;
    @Excel(name = "消耗总数量", width = 15)
    private Double num;
    @Excel(name = "单位", width = 15)
    private String unItName;
    @Excel(name = "消耗总金额", width = 15)
    private BigDecimal totalPrice;
    @Excel(name = "生产厂家", width = 15)
    private String venderName;
}
