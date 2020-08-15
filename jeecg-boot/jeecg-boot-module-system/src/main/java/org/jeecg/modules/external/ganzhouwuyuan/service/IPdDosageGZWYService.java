package org.jeecg.modules.external.ganzhouwuyuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;

import java.util.List;

/**
 * @author jiangxz
 * @description 赣州五院计费
 * @date 2020-5-26
 */
public interface IPdDosageGZWYService extends IService<PdDosage> {

    /**
     * 初始化modal
     * @param pdDosage
     * @return
     */
    PdDosage initModal(PdDosage pdDosage);

    /*查询his系统病人信息*/
    List<PdDosage> queryPatientInfoList(PdDosage pdDosage);
    /*提交收费*/
    List<PdDosageDetail> saveMain(PdDosage pdDosage, String displayFlag);
    /*用量退回*/
    void dosageReturned(PdDosage pdDosage);
    /*补计费*/
    void dosageFee(PdDosage pdDosage);

}
