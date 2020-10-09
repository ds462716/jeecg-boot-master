package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author mcb
 * @description 科室同比增长表
 * @date 2020-9-17
 */
@Data
public class RpYrUseReportExcel {
    @Excel(name = "科室", width = 15)
    private String departName;//科室
    @Excel(name = "月份", width = 15)
    private String theLastYearMonth;//上上个月
    @Excel(name ="采购金额", width = 15)
    private Double theLastPrice;//上上个月采购金额
    @Excel(name = "月份", width = 15)
    private String lastMonth;//上个月
    @Excel(name ="采购金额", width = 15)
    private Double lastPrice;//上个月采购金额
    @Excel(name ="同比涨幅率", width = 15)
    private String yrOnYr;//同比涨幅率
}
