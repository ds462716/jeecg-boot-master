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
    private String id;
    @Excel(name = "科室名称", width = 15)
    private String departName;// 科室名称
    @Excel(name = "01月采购金额", width = 15)
    private BigDecimal janPurchaseMoney;
    @Excel(name = "01月收费金额", width = 15)
    private BigDecimal janDosageMoney;
    @Excel(name = "01月不收费金额", width = 15)
    private BigDecimal janNoDosageMoney;
    @Excel(name = "02月采购金额", width = 15)
    private BigDecimal febPurchaseMoney;
    @Excel(name = "02月收费金额", width = 15)
    private BigDecimal febDosageMoney;
    @Excel(name = "02月不收费金额", width = 15)
    private BigDecimal febNoDosageMoney;
    @Excel(name = "03月采购金额", width = 15)
    private BigDecimal marPurchaseMoney;
    @Excel(name = "03月收费金额", width = 15)
    private BigDecimal marDosageMoney;
    @Excel(name = "03月不收费金额", width = 15)
    private BigDecimal marNoDosageMoney;
    @Excel(name = "04月采购金额", width = 15)
    private BigDecimal aprPurchaseMoney;
    @Excel(name = "04月收费金额", width = 15)
    private BigDecimal aprDosageMoney;
    @Excel(name = "04月不收费金额", width = 15)
    private BigDecimal aprNoDosageMoney;
    @Excel(name = "05月采购金额", width = 15)
    private BigDecimal mayPurchaseMoney;
    @Excel(name = "05月收费金额", width = 15)
    private BigDecimal mayDosageMoney;
    @Excel(name = "05月不收费金额", width = 15)
    private BigDecimal mayNoDosageMoney;
    @Excel(name = "06月采购金额", width = 15)
    private BigDecimal junPurchaseMoney;
    @Excel(name = "06月收费金额", width = 15)
    private BigDecimal junDosageMoney;
    @Excel(name = "06月不收费金额", width = 15)
    private BigDecimal junNoDosageMoney;
    @Excel(name = "07月采购金额", width = 15)
    private BigDecimal julPurchaseMoney;
    @Excel(name = "07月收费金额", width = 15)
    private BigDecimal julDosageMoney;
    @Excel(name = "07月不收费金额", width = 15)
    private BigDecimal julNoDosageMoney;
    @Excel(name = "08月采购金额", width = 15)
    private BigDecimal augPurchaseMoney;
    @Excel(name = "08月收费金额", width = 15)
    private BigDecimal augDosageMoney;
    @Excel(name = "08月不收费金额", width = 15)
    private BigDecimal augNoDosageMoney;
    @Excel(name = "09月采购金额", width = 15)
    private BigDecimal septPurchaseMoney;
    @Excel(name = "09月收费金额", width = 15)
    private BigDecimal septDosageMoney;
    @Excel(name = "09月不收费金额", width = 15)
    private BigDecimal septNoDosageMoney;
    @Excel(name = "10月采购金额", width = 15)
    private BigDecimal octPurchaseMoney;
    @Excel(name = "10月收费金额", width = 15)
    private BigDecimal octDosageMoney;
    @Excel(name = "10月不收费金额", width = 15)
    private BigDecimal octNoDosageMoney;
    @Excel(name = "11月采购金额", width = 15)
    private BigDecimal novPurchaseMoney;
    @Excel(name = "11月收费金额", width = 15)
    private BigDecimal novDosageMoney;
    @Excel(name = "11月不收费金额", width = 15)
    private BigDecimal novNoDosageMoney;
    @Excel(name = "12月采购金额", width = 15)
    private BigDecimal decPurchaseMoney;
    @Excel(name = "12月收费金额", width = 15)
    private BigDecimal decDosageMoney;
    @Excel(name = "12月不收费金额", width = 15)
    private BigDecimal decNoDosageMoney;
    @Excel(name = "合计采购金额", width = 15)
    private BigDecimal countPurchaseMoney;
    @Excel(name = "合计收费金额", width = 15)
    private BigDecimal countDosageMoney;
    @Excel(name = "合计不收费金额", width = 15)
    private BigDecimal countNoDosageMoney;


    private String productFlag;
    private String departIds;
    private String year;// 年份
    private List<String> departIdList; /*多个部门集合*/
}
