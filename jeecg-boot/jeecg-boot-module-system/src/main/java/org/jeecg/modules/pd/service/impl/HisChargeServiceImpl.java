package org.jeecg.modules.pd.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.entity.ExLabInstrInf;
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
		return hisChargeMapper.selectListByPage(page,hisChargeInf);
		//return page.setRecords(hisChargeMapper.selectList(hisChargeInf));
	}

	@Override
	public List<HisChargeInf> selectListForFCRMYY(HisChargeInf hisChargeInf) {
		return hisChargeMapper.selectListForFCRMYY(hisChargeInf);
	}

	@Override
	public Page<HisChargeInf> selectListForFCRMYY(Page<HisChargeInf> pageList, HisChargeInf hisChargeInf) {
		return hisChargeMapper.selectListByPageForFCRMYY(pageList,hisChargeInf);
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
			//hisChargeMapper.deleteChargeInf();
			 for(HisChargeInf inf:list){
			 	 String fsfXmBh=inf.getFsfXmbh();//项目编号
				 HisChargeInf chargeInf=hisChargeMapper.selectByHisChargeInf(fsfXmBh);
				if(ObjectUtils.isNotEmpty(chargeInf)){
					chargeInf.setFsfXmmc(inf.getFsfXmmc());
					chargeInf.setFsfSpec(inf.getFsfSpec());
					chargeInf.setFsfJe(inf.getFsfJe());
					chargeInf.setFsfKs(inf.getFsfKs());
					chargeInf.setFsfKsbh(inf.getFsfKsbh());
					chargeInf.setFsfXmlb(inf.getFsfXmlb());
					chargeInf.setFsfXmgg(inf.getFsfXmgg());
					hisChargeMapper.updateById(chargeInf);
				}else {
					hisChargeMapper.insert(inf);
				}
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
	//查询检验项目明细信息   his系统
	@Override
	@DS("multi-datasource1")
	public List<ExInspectionItems> selectExjianYanHis(ExInspectionItems exInspectionItems){
		List<ExInspectionItems> list=hisChargeMapper.selectExjianYanHis(exInspectionItems);
		return list;
	}

	//查询检验项目信息   lis系统
	@Override
	@DS("multi-datasource2")
	public List<ExInspectionItems> selectExjianYanLis(ExInspectionItems exInspectionItems){
		List<ExInspectionItems> list=hisChargeMapper.selectExjianYanLis(exInspectionItems);
		return list;
	}

   //查詢HIS系統病人信息
	@Override
	@DS("multi-datasource1")
	public List<PdDosage> queryPatientInfoList(PdDosage   pdDosage) {
		List<PdDosage> list=new ArrayList<PdDosage>();
		String patientType=pdDosage.getPatientType();
		String prjType=pdDosage.getPrjType();
		if("1".equals(prjType)){  //门诊
			    list=hisChargeMapper.queryPatientInfoMZ(pdDosage);
		}else{  //住院
				list=hisChargeMapper.queryPatientInfoSS(pdDosage);
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

	//查询HIS系统用戶信息
	@Override
	@DS("multi-datasource2")
	public List<ExLabInstrInf> selectExLabInstrInf() {
		return hisChargeMapper.selectExLabInstrInf();
	}

	@Override
	public boolean deleteByDepartParentId(String departParentId) {
		return hisChargeMapper.deleteByDepartParentId(departParentId);
	}
}
