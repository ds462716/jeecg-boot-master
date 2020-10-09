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
public class RpSupplierUseReportPage {

    private String id;
    @Excel(name = "月份", width = 15)
    private String auditDate;
    private String supplierId;//供应商Id
    @Excel(name = "供应商", width = 15)
    private String supplierName;//供应商名称
    @Excel(name = "入库产品数量", width = 15)
    private Double inProductNum;// 入库产品数量
    @Excel(name = "入库产品金额", width = 15)
    private BigDecimal inTotalPrice;// 入库产品金额
    @Excel(name = "产品使用数量", width = 15)
    private Double dosageNum;// 产品使用数量
    @Excel(name = "产品使用金额", width = 15)
    private BigDecimal dosagePrice;// 产品使用金额
    @Excel(name = "产品退货数量", width = 15)
    private Double rejectedNum;// 产品退货数量
    @Excel(name = "产品退货金额", width = 15)
    private BigDecimal rejectedPrice;// 产品退货金额

    // 查询条件
    private String auditStatus;

    private String yearMonth;       // 年月
    private String queryDateStart;  // 查询日期
    private String queryDateEnd;

    private String querySupplierId;//查询参数
    //所属医院
    private String departParentId;

}
