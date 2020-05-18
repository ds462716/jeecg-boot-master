package org.jeecg.modules.pd.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.HisChargeMapper;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.jeecg.modules.pd.vo.ExHisMzInfPage;
import org.jeecg.modules.pd.vo.ExHisZyInfPage;
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



	//计费信息插入HIS中间表（门诊）
	@Override
	@Transactional
	@DS("multi-datasource1")
	public int saveExHisMzInf(ExHisMzInfPage exHisMzInf) {
		return  hisChargeMapper.saveExHisMzInf(exHisMzInf);
 	}


	//计费信息插入HIS中间表（住院）
	@Override
	@Transactional
	@DS("multi-datasource1")
	public int saveExHisZyInf(PdDosage pdDosage,List<PdDosageDetail> chargeArray) {
 		List<ExHisZyInfPage> list=new ArrayList<ExHisZyInfPage>();
 		for(PdDosageDetail dosageDetail :chargeArray){
		ExHisZyInfPage  hisZyInfPage=new ExHisZyInfPage();
		hisZyInfPage.setFsfZyh(pdDosage.getInHospitalNo());//住院号
		hisZyInfPage.setFsfZyhm(pdDosage.getMedicalRecordNo());//病历号
		hisZyInfPage.setFsfCs("3");//住院次数
		hisZyInfPage.setFsbSl(dosageDetail.getDosageCount()+"");//数量
		hisZyInfPage.setFsfXmbh(dosageDetail.getChargeCode());//收费项目编号
		hisZyInfPage.setFsfMc(dosageDetail.getProductName());//收费项目名称
		hisZyInfPage.setFsbGg("33");//规格
		hisZyInfPage.setFsbJe("445");//金额
		hisZyInfPage.setFsfKdKs("66");//开单科室
		hisZyInfPage.setFsfZxKs("执行");//执行科室
		hisZyInfPage.setFsfRq("");//计费日期
		hisZyInfPage.setFsbRy("5");//计费人员
		hisZyInfPage.setFsbZt("0");//计费状态
		hisZyInfPage.setFsbXh(dosageDetail.getId());//序号
		hisZyInfPage.setFsbTfjlxh("123");//自增长序号
		hisZyInfPage.setFsbBrbs("123");//手术编号
		list.add(hisZyInfPage);
		}
		return  hisChargeMapper.saveExHisZyInf(list);
	}
}
