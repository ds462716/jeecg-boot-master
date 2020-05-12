package org.jeecg.modules.pd.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.HisChargeInf;
import org.jeecg.modules.pd.entity.HisDepartInf;
import org.jeecg.modules.pd.entity.HisUserInf;
import org.jeecg.modules.pd.entity.NewPdDosage;
import org.jeecg.modules.pd.mapper.HisChargeMapper;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
@Service
public class HisChargeServiceImpl extends ServiceImpl<HisChargeMapper, HisChargeInf> implements IHisChargeService {
	@Autowired
	HisChargeMapper hisChargeMapper;

	/**
	 * 查询列表
	 * @param page
	 * @param hisChargeInf
	 * @return
	 */
	@Override
	public Page<HisChargeInf> selectList(Page<HisChargeInf> page, HisChargeInf hisChargeInf) {
		return page.setRecords(hisChargeMapper.selectList(hisChargeInf));
	}


	@Override
	@DS("multi-datasource1")
	public List<HisChargeInf> selectByHisCharge() {
		List list = hisChargeMapper.selectByHisCharge();
		return list;
	}


	@Override
	@Transactional
	public void saveMain(List<HisChargeInf> list) {
		if (list != null && list.size() > 0) {
			hisChargeMapper.deleteChargeInf();
			 for(HisChargeInf inf:list){
				 hisChargeMapper.insert(inf);
			 }
		}
	}


	//查询HIS系统科室信息
	@Override
	@DS("multi-datasource1")
	public List<HisDepartInf> selectHisDepart() {
		List list = hisChargeMapper.selectHisDepart();
		return list;
	}

	//查询HIS系统用戶信息
	@Override
	@DS("multi-datasource1")
	public List<HisUserInf> selectHisUser() {
		List list = hisChargeMapper.selectHisUser();
		return list;
	}

   //查詢HIS系統病人信息
	@Override
	@DS("multi-datasource1")
	public List<NewPdDosage> queryPatientInfoList(NewPdDosage   newPdDosage) {
		List list=new ArrayList<>();
		String patientType=newPdDosage.getPatientType();
		String prjType=newPdDosage.getPrjType();
		if("2".equals(patientType)){//门诊
			    list=hisChargeMapper.queryPatientInfoMZ(newPdDosage);
		}else{  //住院
			if("0".equals(prjType)){  //手术项目
				list=hisChargeMapper.queryPatientInfoSS(newPdDosage);
			}else if("1".equals(prjType)){  //检查项目
				list = hisChargeMapper.queryPatientInfoJC(newPdDosage);
			}else{//检验项目
				list = hisChargeMapper.queryPatientInfoJY(newPdDosage);
			}
		}
		return list;
	}
}
