package org.jeecg.modules.pd.vo;

import lombok.Data;

/**
 * @Description:  住院收费实体类
 * @Author: jeecg-boot
 * @Date:   2020-03-29
 * @Version: V1.0
 */
@Data
public class ExHisZyInfPage {
    private String fsfZyh;//病人住院号	取住院病人信息
    private String fsfZyhm;//病例号	取住院病人信息
    private String fsfCs;//次数	取住院病人信息
    private String fsfXmbh;//收费项目编号	具体产品收费项目代码
    private String FsfMc;//名称	产品名称
    private String FsbGg;//规格	产品规格
    private String FsbSl;//数量	产品数量
    private String FsbJe;//金额
    private String fsfKdKs;//开单科室	取住院病人信息申请科室
    private String FsfZxKs;//执行科室	取住院病人信息执行科室
    private String FsfRq;//计费日期
    private String FsbRy;//计费人员	与HIS操作人员中一致对照
    private String FsbZt; //HIS计费状态	计费为1，未计费为0
    private String FsbXh;//序号	自增长序号，计退费用
    private String FsbTfjlxh;//自增长序号，计退费用	取收费序号
    private String FsbBrbs;//HIS手术编号	有编号的会记录到手术费用表中
    private String HisJlxh;//HIS计费jlxh	HIS系统填写
}
