package org.jeecg.modules.external.ganzhouzhongliu.entity;

import lombok.Data;
import org.jeecg.modules.pd.entity.BaseEntity;

/**
 * @Description: 收费项目基础信息
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
public class ExHisMzChargeInfGzzl extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String gyMzh;//门诊号
    private String gySqbh;//申请编号
    private String gyLs;//申请科室名称
    private String gyKsdm;//申请科室代码
    private String gyXm;//姓名
    private String gyXb;//性别
    private String gyCsrq;//出生日期
    private String gyZjhm;//证件号码
    private String gyRyrq;//检查日期
    private String GyZd;//检查诊断
    private String gyZzys;//诊治医生
    private String gyZzysbh;//诊治医生编号
    private String gyLxdh;//联系电话
    private String gyDz;//联系地址
    private String gyBrbs;//检查项目编号
    private String gySsmc;//检查项目名称
    private String gySsks;//检查科室
}
