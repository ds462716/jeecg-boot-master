package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jiangxz
 * @description 出入库统计报表
 * @date 2020-8-5
 */
@Data
public class RpDepartUseReportPage {

    private String id;
    @Excel(name = "月份", width = 15)
    private String auditDate;
    @Excel(name = "科室", width = 15)
    private String departName;//科室名称
    @Excel(name = "用量数量", width = 15)
    private Double dosageNum;// 用量数量
    @Excel(name = "用量金额", width = 15)
    private BigDecimal dosagePrice;// 用量金额
    @Excel(name = "收费产品数量", width = 15)
    private Double chargeDosageNum;// 收费产品数量
    @Excel(name = "收费产品金额", width = 15)
    private BigDecimal chargeDosagePrice;// 收费产品金额
    @Excel(name = "未收费产品数量", width = 15)
    private Double noChargeDosageNum;// 未收费产品数量
    @Excel(name = "未收费产品金额", width = 15)
    private BigDecimal noChargeDosagePrice;// 未收费产品金额

    // 查询条件
    private String yearMonth;       // 年月
    private String queryDateStart;  // 查询日期
    private String queryDateEnd;
    private String departId;        // 科室id
    private String departParentId;  // 机构id
    private String departIds;
    private List<String> departIdList;

}
