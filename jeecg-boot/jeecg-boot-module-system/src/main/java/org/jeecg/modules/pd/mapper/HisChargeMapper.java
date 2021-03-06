package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.entity.ExLabInstrInf;
import org.jeecg.modules.external.entity.ExLabPurpose;
import org.jeecg.modules.pd.entity.HisChargeInf;
import org.jeecg.modules.pd.entity.HisDepartInf;
import org.jeecg.modules.pd.entity.HisUserInf;
import org.jeecg.modules.pd.entity.PdDosage;

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

	List<HisChargeInf> selectListForFCRMYY(HisChargeInf hisChargeInf);

	Page<HisChargeInf> selectListByPageForFCRMYY(Page<HisChargeInf> page, @Param("entity") HisChargeInf entity);


	List<HisChargeInf> selectByHisCharge();

	HisChargeInf  selectByHisChargeInf(String fsfXmbh);

	void deleteChargeInf();

	public boolean deleteByDepartParentId(@Param("departParentId") String departParentId);

	List<HisDepartInf> selectHisDepart();

	List<HisUserInf> selectHisUser(HisUserInf info);

	List<PdDosage> queryPatientInfoSS(PdDosage pdDosage);

	List<PdDosage> queryPatientInfoJC(PdDosage pdDosage);

	 Map<String,Object>   queryMztfList(String  id);

	List<PdDosage> queryPatientInfoMZ(PdDosage pdDosage);

	List<ExInspectionItems> selectExjianYanHis(ExInspectionItems exInspectionItems);

	List<ExInspectionItems> selectExjianYanLis(ExInspectionItems exInspectionItems);

	List<ExLabInstrInf> selectExLabInstrInf();

    List<ExInspectionItems> selectExjianYanLisFc(ExInspectionItems exInspectionItems);

    List<ExLabPurpose> selectExLabPurpose(ExLabPurpose exLabPurpose);

}
