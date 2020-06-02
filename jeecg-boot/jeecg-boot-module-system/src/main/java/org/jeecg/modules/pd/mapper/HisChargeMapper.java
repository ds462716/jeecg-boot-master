package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.pd.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: HIs接口相关
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface HisChargeMapper extends BaseMapper<HisChargeInf> {

	List<HisChargeInf> selectList(HisChargeInf hisChargeInf);

	Page<HisChargeInf> selectListByPage(Page<HisChargeInf> page, @Param("entity") HisChargeInf entity);

	List<HisChargeInf> selectByHisCharge();

	HisChargeInf  selectByHisChargeInf(String fsfXmbh);

	void deleteChargeInf();

	List<HisDepartInf> selectHisDepart();

	List<HisUserInf> selectHisUser();

	List<PdDosage> queryPatientInfoSS(PdDosage pdDosage);

	List<PdDosage> queryPatientInfoJC(PdDosage pdDosage);

	 Map<String,Object>   queryMztfList(String  id);

	List<PdDosage> queryPatientInfoMZ(PdDosage pdDosage);

	List<ExInspectionItems> selectExjianYan(ExInspectionItems exInspectionItems);
}
