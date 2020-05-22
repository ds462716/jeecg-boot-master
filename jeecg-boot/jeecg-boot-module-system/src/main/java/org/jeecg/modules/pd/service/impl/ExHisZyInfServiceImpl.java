package org.jeecg.modules.pd.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.mapper.ExHisZyInfMapper;
import org.jeecg.modules.pd.service.IExHisZyInfService;
import org.jeecg.modules.pd.vo.ExHisMzInfPage;
import org.jeecg.modules.pd.vo.ExHisZyInfPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
@Service
@DS("multi-datasource1")
public class ExHisZyInfServiceImpl extends ServiceImpl<ExHisZyInfMapper, ExHisZyInfPage> implements IExHisZyInfService {
	@Autowired
	private ExHisZyInfMapper exHisZyInfMapper;




	//计费信息插入HIS中间表（住院）
	@Override
	@Transactional
	@DS("multi-datasource1")
	public int saveExHisZyInf(PdDosage pdDosage,List<PdDosageDetail> chargeArray,String chargeType) {
 		List<ExHisZyInfPage> list=new ArrayList<ExHisZyInfPage>();
 		for(PdDosageDetail dosageDetail :chargeArray){
		ExHisZyInfPage  hisZyInfPage=new ExHisZyInfPage();
		hisZyInfPage.setFsfZyh(pdDosage.getInHospitalNo());//住院号
		hisZyInfPage.setFsfZyhm(pdDosage.getMedicalRecordNo());//病历号
		hisZyInfPage.setFsfCs(pdDosage.getHospitalizationsNum());//住院次数

		if(PdConstant.IS_CHARGE_TYPE_0.equals(chargeType)){ //如果是退费操作
		hisZyInfPage.setFsbSl(Double.valueOf("-"+dosageDetail.getDosageCount()));//数量
		hisZyInfPage.setFsbJe(new BigDecimal("-"+dosageDetail.getAmountMoney()));//金额
		hisZyInfPage.setFsbTfjlxh(Long.valueOf(dosageDetail.getId()));
		}else{
		hisZyInfPage.setFsbSl(dosageDetail.getDosageCount());//数量
		hisZyInfPage.setFsbJe(dosageDetail.getAmountMoney());//金额
		hisZyInfPage.setFsbTfjlxh(null);
		}
		hisZyInfPage.setFsfXmbh(dosageDetail.getChargeCode());//收费项目编号
		hisZyInfPage.setFsfMc(dosageDetail.getProductName());//收费项目名称
		hisZyInfPage.setFsbGg(dosageDetail.getSpec());//规格

		hisZyInfPage.setFsfKdKs(pdDosage.getSqrtDoctorId());//开单科室
		hisZyInfPage.setFsfZxKs(pdDosage.getOprDeptId());//执行科室
		hisZyInfPage.setFsfRq(new Date());//计费日期
		hisZyInfPage.setFsbRy(pdDosage.getCreateBy());//计费人员
		hisZyInfPage.setFsbZt("0");//计费状态  0：未计费
		hisZyInfPage.setFsbXh(Long.valueOf(dosageDetail.getId()));//序号
		hisZyInfPage.setFsbBrbs(pdDosage.getOperativeNumber());//手术编号
		list.add(hisZyInfPage);
		}
		return  exHisZyInfMapper.saveExHisZyInf(list);
	}

	//计费信息插入HIS中间表（门诊）
	@Override
	@Transactional
	@DS("multi-datasource1")
	public int saveExHisMzInf(PdDosage pdDosage,List<PdDosageDetail> chargeArray,String chargeType) {
		List<ExHisMzInfPage> list=new ArrayList<ExHisMzInfPage>();
		for(PdDosageDetail dosageDetail :chargeArray){
			ExHisMzInfPage  hisMzInfPage=new ExHisMzInfPage();
			hisMzInfPage.setFsfBrId(pdDosage.getMedicalRecordNo());//病人ID
			hisMzInfPage.setFsfYjxh(pdDosage.getExtension1());//对应ms_yj01表yjxh
			hisMzInfPage.setFsfJzxh(pdDosage.getExtension2());//对应MS_YJ01中jzxh
			//if(PdConstant.IS_CHARGE_TYPE_0.equals(chargeType)){ //如果是退费操作
			//hisMzInfPage.setFsbSl(Double.valueOf("-"+dosageDetail.getDosageCount()));//数量
			//hisMzInfPage.setFsbJe(new BigDecimal("-"+dosageDetail.getAmountMoney()));//金额
			//}else{
			hisMzInfPage.setFsbSl(dosageDetail.getDosageCount());//数量
			hisMzInfPage.setFsbJe(dosageDetail.getAmountMoney());//金额
			//}
			hisMzInfPage.setFsfXmbh(dosageDetail.getChargeCode());//收费项目编号
			hisMzInfPage.setFsfMc(dosageDetail.getProductName());//收费项目名称
			hisMzInfPage.setFsfKdKs(pdDosage.getOprDeptId());//开单科室
			hisMzInfPage.setFsfZxKs(pdDosage.getOprDeptId());//执行科室
			hisMzInfPage.setFsfRq(new Date());//计费日期
			hisMzInfPage.setFsbRy(pdDosage.getCreateBy());//计费人员
			hisMzInfPage.setFsbZt("0");//计费状态
			hisMzInfPage.setFsbXh(Long.valueOf(dosageDetail.getId()));//序号
			list.add(hisMzInfPage);
		}
		return  exHisZyInfMapper.saveExHisMzInf(list);
	}
}
