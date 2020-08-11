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
public class PdNumericalInfHcExlce {

    /**统计月份*/
    @Excel(name = "统计月份", width = 15)
    private String month;
    /**统计科室*/
    @Excel(name = "统计科室", width = 15)
    private String departName;
    /**入库数量*/
    @Excel(name = "入库数量", width = 15)
    private Double inRecordNum;
    /**入库金额*/
    @Excel(name = "入库金额", width = 15)
    private BigDecimal inRecordPrice;
    /**出库数量*/
    @Excel(name = "出库数量", width = 15)
    private Double callOutNum;
    /**出库金额*/
    @Excel(name = "出库金额", width = 15)
    private BigDecimal callOutPrice;
    /**使用数量*/
    @Excel(name = "使用数量", width = 15)
    private Double purchaseNum;
    /**使用金额*/
    @Excel(name = "使用金额", width = 15)
    private BigDecimal purchasePrice;
    /**退货数量*/
    @Excel(name = "退货数量", width = 15)
    private Double rejectedNum;
    /**退货金额*/
    @Excel(name = "退货金额", width = 15)
    private BigDecimal rejectedPrice;
    /**调入数量*/
    @Excel(name = "调入数量", width = 15)
    private Double callInNum;
    /**调入金额*/
    @Excel(name = "调入金额", width = 15)
    private BigDecimal callInPrice;
    /**库存数量*/
    @Excel(name = "库存数量", width = 15)
    private Double stockNum;
    /**库存金额*/
    @Excel(name = "库存金额", width = 15)
    private BigDecimal stockPrice;
    /**差异数量*/
    @Excel(name = "差异数量", width = 15)
    private Double disNum;
    /**差异金额*/
    @Excel(name = "差异金额", width = 15)
    private BigDecimal disPrice;

}
