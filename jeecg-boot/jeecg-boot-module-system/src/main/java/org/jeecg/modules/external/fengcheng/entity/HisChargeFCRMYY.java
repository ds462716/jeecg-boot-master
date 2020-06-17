package org.jeecg.modules.external.fengcheng.entity;

import lombok.Data;
import org.jeecg.modules.pd.entity.BaseEntity;

/**
 * @Description: 收费项目基础信息
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
public class HisChargeFCRMYY extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String chargeCode; //收费项目代码
    private String proName; //收费项目名称
    private String price;   //价格
    private String spec;    //规格
}
