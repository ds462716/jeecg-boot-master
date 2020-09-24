package org.jeecg.modules.pd.vo;

import lombok.Data;

import java.util.List;

/**
 * @author mcb
 * @description 科室采购趋势图表
 * @date 2020-8-5
 */
@Data
public class RpPurchaseUseReportPage {

    private String departIds;// 科室ID集合
    private String productFlag;// 是否试剂
    private String yearMonth;//年月
    private List<String> departIdList; /*多个部门集合*/

    private String type;
    private Double y;//采购金额
    private Double x;//收费金额
    private Double s;//不可收费金额
    private String money;
    private Double count;
    private String item;
    private Double 采购金额;
    private Double 收费金额;
    private Double 检验收入金额;
    private String departName;// 科室名称
    private String amount;
    private String selectType;//0：环比   1：同比
    private String departId;// 科室Id
    private String parentId;// 上级科室ID
    private String departType;// 科室类型
    private String lastMonth;//上个月
    private Double lastPrice;//上个月采购金额
    private String theLastYearMonth;//上上个月
    private Double theLastPrice;//上上个月采购金额
    private String moOnMo;//环比涨幅率
    private String yrOnYr;//同比涨幅率

    private Double value;
    private String name;
}
