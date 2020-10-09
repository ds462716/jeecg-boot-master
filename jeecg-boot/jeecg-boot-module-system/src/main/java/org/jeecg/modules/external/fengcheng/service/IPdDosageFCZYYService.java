package org.jeecg.modules.external.fengcheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;

import java.util.List;

/**
 * @author jiangxz
 * @description 丰城中医院计费
 * @date 2020-5-26
 */
public interface IPdDosageFCZYYService extends IService<PdDosage> {

    /**
     * 初始化modal
     * @param pdDosage
     * @return
     */
    PdDosage initModal(PdDosage pdDosage);

    /*查询his系统病人信息*/
    List<PdDosage> queryPatientInfoList(PdDosage pdDosage);

    List<PdDosageDetail> saveMain(PdDosage pdDosage, String displayFlag);

    void dosageReturned(PdDosage pdDosage);

    void dosageFee(PdDosage pdDosage);

}
