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
public class ExHisZyChargeInfGzzl extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String gyZyh;//住院号
    private String gyZyhm;//病历号
    private String gyCs;//住院次数
    private String gyKs;//申请科室名称
    private String gyKsdm;//申请科室代码
    private String gyBq;//申请病区
    private String gyBqdh;//申请病区代码
    private String gyBrch;//病床号
    private String gyXm;//姓名
    private String gyXb;//性别
    private String gyCsrq;//出生日期
    private String gyZjhm;//证件号码
    private String gyRyrq;//入院日期
    private String gyZd;//入院诊断
    private String gyZzys;//诊治医生
    private String gyZzysbh;//诊治医生编号
    private String gyLxdh;//联系电话
    private String gyDz;//联系地址
    private String gyBrbs   ;//手术编号
    private String gySsmc   ;//手术名称
    private String gySssj   ;//手术时间
    private String gySsks   ;//手术科室
}
