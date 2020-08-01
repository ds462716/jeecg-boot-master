package org.jeecg.modules.external.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: mcb
 * @Date:   2020-08-1
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PdBottleInfMonthExlce {

    @Excel(name = "统计月份", width = 15)
    private String month;
    @Excel(name = "试剂消耗金额", width = 15)
    private BigDecimal totalPrice;
    @Excel(name = "试剂消耗数量", width = 15)
    private Double num;
    @Excel(name = "检验收入金额", width = 15)
    private BigDecimal itemPrice;
    @Excel(name = "检验项目数量", width = 15)
    private Double itemNum;

}
