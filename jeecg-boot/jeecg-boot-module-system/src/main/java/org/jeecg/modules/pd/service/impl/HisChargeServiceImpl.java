package org.jeecg.modules.pd.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.HisChargeMapper;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
@Service
public class HisChargeServiceImpl extends ServiceImpl<HisChargeMapper, HisChargeInf> implements IHisChargeService {
	@Autowired
	private HisChargeMapper hisChargeMapper;

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
	//查询检验项目明细信息
	@Override
	@DS("multi-datasource1")
	public List<ExInspectionItems> selectExjianYan(ExInspectionItems exInspectionItems){
		List<ExInspectionItems> list=hisChargeMapper.selectExjianYan(exInspectionItems);
		return list;
	}

   //查詢HIS系統病人信息
	@Override
	@DS("multi-datasource1")
	public List<PdDosage> queryPatientInfoList(PdDosage   pdDosage) {
		List<PdDosage> list=new ArrayList<PdDosage>();
		String patientType=pdDosage.getPatientType();
		String prjType=pdDosage.getPrjType();
		String inHospitalNo=pdDosage.getInHospitalNo();//住院号
		String outpatientNumber=pdDosage.getOutpatientNumber();//门诊号
		if(StringUtils.isEmpty(inHospitalNo) && StringUtils.isNotEmpty(outpatientNumber)){  //门诊
			    list=hisChargeMapper.queryPatientInfoMZ(pdDosage);
		}else if(StringUtils.isNotEmpty(inHospitalNo)){  //住院
			//if("0".equals(prjType)){  //手术项目
				list=hisChargeMapper.queryPatientInfoSS(pdDosage);
			//}else if("1".equals(prjType)){  //检查项目
				//list = hisChargeMapper.queryPatientInfoJC(newPdDosage);
			//}else{//检验项目
				//list = hisChargeMapper.queryPatientInfoJY(newPdDosage);
			//}
		}
		return list;
	}

	//查询his系统门诊病人退费信息
	@Override
	@DS("multi-datasource1")
	public String  queryMztfList(PdDosage pdDosage) {
		String prodNames=null;
		List<PdDosageDetail> list=pdDosage.getPdDosageDetails();
		if(list !=null && list.size()>0){
			for(PdDosageDetail detail: list){
		     Map params=hisChargeMapper.queryMztfList(detail.getId()+"");
                      if(MapUtils.isNotEmpty(params)){
						  prodNames+=MapUtils.getString(params,"fsf_mc")+",";
					  }
			   }
		}
		return prodNames;

	}
}
