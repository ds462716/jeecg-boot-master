package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mcb
 * @description 科室领用统计报表
 * @date 2020-9-25
 */
@Data
public class RpDepartApplyPage {

    private String id;
    @Excel(name = "科室", width = 15)
    private String departName;
    @Excel(name = "01月金额", width = 15)
    private BigDecimal janPrice;
    @Excel(name = "02月金额", width = 15)
    private BigDecimal febPrice;
    @Excel(name = "03月金额", width = 15)
    private BigDecimal marPrice;
    @Excel(name = "04月金额", width = 15)
    private BigDecimal aprPrice;
    @Excel(name = "05月金额", width = 15)
    private BigDecimal mayPrice;
    @Excel(name = "06月金额", width = 15)
    private BigDecimal junPrice;
    @Excel(name = "07月金额", width = 15)
    private BigDecimal julPrice;
    @Excel(name = "08月金额", width = 15)
    private BigDecimal augPrice;
    @Excel(name = "09月金额", width = 15)
    private BigDecimal septPrice;
    @Excel(name = "10月金额", width = 15)
    private BigDecimal octPrice;
    @Excel(name = "11月金额", width = 15)
    private BigDecimal novPrice;
    @Excel(name = "12月金额", width = 15)
    private BigDecimal decPrice;
    @Excel(name = "合计金额", width = 15)
    private BigDecimal countPrice;

    // 查询条件
    private String year;//年
    private String yearMonth;       // 年月
    private String departId;        // 科室id
    private String departParentId;  // 机构id
    private String departIds;
    private List<String> departIdList;
    private String productFlag;

}
