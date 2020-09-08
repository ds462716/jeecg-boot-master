package org.jeecg.modules.pd.vo;

import lombok.Data;

/**
 * @author mcb
 * @description 科室采购趋势图表
 * @date 2020-8-5
 */
@Data
public class RpPurchaseUseReportPage {

    private String DepartIds;// 科室名称
    private String yearMonth;//年月

    private String type;
    private String y;
    private String money;


}
