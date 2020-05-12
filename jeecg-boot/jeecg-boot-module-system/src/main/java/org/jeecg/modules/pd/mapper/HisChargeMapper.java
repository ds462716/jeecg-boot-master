package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.HisChargeInf;
import org.jeecg.modules.pd.entity.HisDepartInf;
import org.jeecg.modules.pd.entity.HisUserInf;
import org.jeecg.modules.pd.entity.NewPdDosage;

import java.util.List;

/**
 * @Description: HIs接口相关
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface HisChargeMapper extends BaseMapper<HisChargeInf> {

	List<HisChargeInf> selectList(HisChargeInf hisChargeInf);

	List<HisChargeInf> selectByHisCharge();

	void deleteChargeInf();

	List<HisDepartInf> selectHisDepart();

	List<HisUserInf> selectHisUser();

	List<HisUserInf> queryPatientInfoSS(NewPdDosage newPdDosage);

	List<HisUserInf> queryPatientInfoJC(NewPdDosage newPdDosage);

	List<HisUserInf> queryPatientInfoJY(NewPdDosage newPdDosage);

	List<HisUserInf> queryPatientInfoMZ(NewPdDosage newPdDosage);
}
