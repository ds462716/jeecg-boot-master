package org.jeecg.modules.external.fengcheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.fengcheng.entity.HisSpdChargeDetailFC;
import org.jeecg.modules.external.fengcheng.entity.HisSpdChargeFC;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;

import java.util.List;

/**
 * @author jiangxz
 * @description 丰城中医院、人民医院 门诊计费
 * @date 2020-5-26
 */
public interface IHisSpdChargeFCService extends IService<HisSpdChargeFC> {

    /**
     * 查询HIS门诊收退费列表
     * @param entity
     * @return
     */
    List<PdDosage> selectList(@Param("entity") PdDosage entity);

    /**
     * 查询HIS门诊收退费明细
     * @param entity
     * @return
     */
    List<PdDosageDetail> selectDetailList(@Param("entity") PdDosageDetail entity);

}
