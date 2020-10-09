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
public class RpDepartStockReportPage {

    private String id;
    @Excel(name = "科室", width = 15)
    private String departName;//科室名称
    @Excel(name = "当前库存数量", width = 15)
    private Double stockNum;// 当前库存数量
    @Excel(name = "出库金额", width = 15)
    private BigDecimal sellingAmount;// 出库金额
    @Excel(name = "入库金额", width = 15)
    private BigDecimal purchaseAmount;// 入库金额

    // 查询条件
    private String yearMonth;       // 年月
    private String queryDateStart;  // 查询日期
    private String queryDateEnd;
    private String departId;        // 科室id
    private String departParentId;  // 机构id
    private String departIds;
    private List<String> departIdList;

}
