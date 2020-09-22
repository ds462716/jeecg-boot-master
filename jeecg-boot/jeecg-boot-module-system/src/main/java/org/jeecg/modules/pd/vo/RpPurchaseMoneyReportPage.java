package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mcb
 * @description 科室采购趋势图表
 * @date 2020-8-5
 */
@Data
public class RpPurchaseMoneyReportPage {
    @Excel(name = "科室名称", width = 15)
    private String departName;// 科室名称
    @Excel(name = "月份", width = 15)
    private String month1;// 月份1
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney1;// 采购金额1
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney1;// 收费金额1
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney1;// 不可收费金额1
    @Excel(name = "月份", width = 15)
    private String month2;// 月份2
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney2;// 采购金额2
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney2;// 收费金额2
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney2;// 不可收费金额2
    @Excel(name = "月份", width = 15)
    private String month3;// 月份3
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney3;// 采购金额3
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney3;// 收费金额3
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney3;// 不可收费金额3
    @Excel(name = "月份", width = 15)
    private String month4;// 月份4
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney4;// 采购金额4
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney4;// 收费金额4
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney4;// 不可收费金额4
    @Excel(name = "月份", width = 15)
    private String month5;// 月份5
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney5;// 采购金额5
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney5;// 收费金额5
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney5;// 不可收费金额5
    @Excel(name = "月份", width = 15)
    private String month6;// 月份6
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney6;// 采购金额6
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney6;// 收费金额6
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney6;// 不可收费金额6
    @Excel(name = "月份", width = 15)
    private String month7;// 月份7
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney7;// 采购金额7
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney7;// 收费金额7
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney7;// 不可收费金额7
    @Excel(name = "月份", width = 15)
    private String month8;// 月份8
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney8;// 采购金额8
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney8;// 收费金额8
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney8;// 不可收费金额8
    @Excel(name = "月份", width = 15)
    private String month9;// 月份9
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney9;// 采购金额9
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney9;// 收费金额9
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney9;// 不可收费金额9
    @Excel(name = "月份", width = 15)
    private String month10;// 月份10
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney10;// 采购金额10
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney10;// 收费金额10
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney10;// 不可收费金额10
    @Excel(name = "月份", width = 15)
    private String month11;// 月份11
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney11;// 采购金额11
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney11;// 收费金额11
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney11;// 不可收费金额11
    @Excel(name = "月份", width = 15)
    private String month12;// 月份12
    @Excel(name = "采购金额", width = 15)
    private BigDecimal purchaseMoney12;// 采购金额12
    @Excel(name = "收费金额", width = 15)
    private BigDecimal dosageMoney12;// 收费金额12
    @Excel(name = "不可收费金额", width = 15)
    private BigDecimal noDosageMoney12;// 不可收费金额12


    private List<String> departIdList; /*多个部门集合*/
}
