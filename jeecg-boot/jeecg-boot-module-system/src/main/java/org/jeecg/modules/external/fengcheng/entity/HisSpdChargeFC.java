package org.jeecg.modules.external.fengcheng.entity;

import org.jeecg.modules.pd.entity.BaseEntity;

/**
 * @author jiangxz
 * @description HIS门诊收费 中间表
 * @date 2020-6-17
 */
public class HisSpdChargeFC extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private String aplyNo; //申请编号
    private String operNo; //手术编号（如果没有手术，可为空）
    private String blngDptmNo; //所属病区编码
    private String blngDptmName; //所属病区
    private String blngNo; //所属科室编码
    private String blngBame; //所属科室
    private String type; //住院标识（1：是  2：否）
    private String projectName; //手术名称
    private String departName; //手术或检查项目科室（如果没有手术，则是检查项目科室）
    private String createDate; //登记日期
    private String doctorCode; //手术医生编码
    private String doctorName; //手术医生姓名
    private String sex; //男或女
    private String patientName; //病人姓名
    private String outpatCode; //门诊号
    private String hitaionNo; //住院号（如果不是住院，可为空）
    private String bedCode; //床位号（如果没有，可为空）
    private String dosageDate; //计费日期（yyyy-MM-dd hh:mm:ss）
    private String totalSum; //耗材总数量
    private String totalPrice; //计费总金额

}
