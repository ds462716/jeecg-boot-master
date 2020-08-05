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
public class RpInAndOutReportPage {

    private String id;
    @Excel(name = "科室", width = 15)
    private String departName;//科室名称
    @Excel(name = "入库产品数量", width = 15)
    private Double inProductNum;// 入库产品数量
    @Excel(name = "入库产品金额", width = 15)
    private BigDecimal inTotalPrice;// 入库产品金额
    @Excel(name = "出库产品数量", width = 15)
    private Double outProductNum;// 出库产品数量
    @Excel(name = "出库产品金额", width = 15)
    private BigDecimal outTotalPrice;// 出库产品金额

    // 查询条件
    private String auditStatus;
    private String departId;        // 科室id
    private String parentId;        // 科室父id
    private String departParentId;  // 机构id
    private String departIds;
    private List<String> departIdList;

    private String yearMonth;       // 年月
    private String queryDateStart;  // 查询日期
    private String queryDateEnd;
}
