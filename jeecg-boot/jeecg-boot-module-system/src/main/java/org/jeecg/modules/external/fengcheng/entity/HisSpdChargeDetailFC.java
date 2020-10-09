package org.jeecg.modules.external.fengcheng.entity;

import org.jeecg.modules.pd.entity.BaseEntity;

/**
 * @author jiangxz
 * @description HIS门诊收费明细 中间表
 * @date 2020-6-17
 */
public class HisSpdChargeDetailFC extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private String charge_id;//主表id
    private String chargeCode;//主表id
    private String prodNum;//主表id
    private String price;//主表id
    private String chargeType;//主表id
}
