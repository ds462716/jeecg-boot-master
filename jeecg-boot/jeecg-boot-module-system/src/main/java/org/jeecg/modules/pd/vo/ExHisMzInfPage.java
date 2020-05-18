package org.jeecg.modules.pd.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:  门诊收费实体类
 * @Author: jeecg-boot
 * @Date:   2020-03-29
 * @Version: V1.0
 */
@Data
public class ExHisMzInfPage {
    private String fsfBrId;   //病人门诊ID
    private String fsfYjxh;   //对应ms_yj01表yjxh
    private String fsfJzxh;//对应MS_YJ01中jzxh
    private String fsfXmbh;//收费项目编号
    private String fsfMc;  //	产品名称
    private Double fsbSl;//产品计费数量
    private BigDecimal fsbJe;//产品计费金额
    private String fsfKdKs;//开单科室	取门诊病人信息申请科室代码
    private String fsfZxKs;//执行科室	取门诊病人信息检查科室
    private Date fsfRq;//计费日期
    private String fsbRy;//计费人员	与HIS系统中一致对照
    private String fsbZt;//HIS计费状态	计费为1，未计费为0
    private Long fsbXh;//序号	自增长序号，计退费用
    private String  HisJlxh;//HIS计费jlxh	HIS系统填写

}
