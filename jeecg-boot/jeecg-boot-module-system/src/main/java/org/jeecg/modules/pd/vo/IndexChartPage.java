package org.jeecg.modules.pd.vo;

import lombok.Data;


/**
 * @Description: 首页统计查询需用到的字段（不涉及表）
 * @Author: jeecg-boot
 * @Date:   2020-03-29
 * @Version: V1.0
 */
@Data
public class IndexChartPage {

    /** 查询数据的类型 **/
    private String type;

    /** 根据年/月/周/日查询 **/
    private String dayType;

}
