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

    private String departIds;// 科室名称
    private String prpductFlag;// 是否试剂
    private String yearMonth;//年月
    private List<String> departIdList; /*多个部门集合*/

    private String type;
    private Double y;
    private Double x;
    private String money;
    private Double count;
    private String item;
    private Double 采购金额;
    private Double 收费金额;
}
