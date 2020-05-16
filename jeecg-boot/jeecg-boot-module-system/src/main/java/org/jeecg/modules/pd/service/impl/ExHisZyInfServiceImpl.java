package org.jeecg.modules.pd.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.mapper.ExHisZyInfMapper;
import org.jeecg.modules.pd.service.IExHisZyInfService;
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
@DS("multi-datasource1")
public class ExHisZyInfServiceImpl extends ServiceImpl<ExHisZyInfMapper, ExHisZyInfPage> implements IExHisZyInfService {
	@Autowired
	private ExHisZyInfMapper exHisZyInfMapper;




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
		return  exHisZyInfMapper.saveExHisZyInf(list);
	}

	//计费信息插入HIS中间表（门诊）
	@Override
	@Transactional
	@DS("multi-datasource1")
	public int saveExHisMzInf(PdDosage pdDosage,List<PdDosageDetail> chargeArray) {
		List<ExHisMzInfPage> list=new ArrayList<ExHisMzInfPage>();
		for(PdDosageDetail dosageDetail :chargeArray){
			ExHisMzInfPage  hisMzInfPage=new ExHisMzInfPage();
			hisMzInfPage.setFsfBrId("");//病人门诊ID
			hisMzInfPage.setFsfYjxh("");//对应ms_yj01表yjxh
			hisMzInfPage.setFsfJzxh("");//对应MS_YJ01中jzxh
			hisMzInfPage.setFsbSl(dosageDetail.getDosageCount()+"");//数量
			hisMzInfPage.setFsfXmbh(dosageDetail.getChargeCode());//收费项目编号
			hisMzInfPage.setFsfMc(dosageDetail.getProductName());//收费项目名称
			hisMzInfPage.setFsbJe("445");//金额
			hisMzInfPage.setFsfKdKs("66");//开单科室
			hisMzInfPage.setFsfZxKs("执行");//执行科室
			hisMzInfPage.setFsfRq("");//计费日期
			hisMzInfPage.setFsbRy("5");//计费人员
			hisMzInfPage.setFsbZt("0");//计费状态
			hisMzInfPage.setFsbXh(dosageDetail.getId());//序号
			list.add(hisMzInfPage);
		}
		return  exHisZyInfMapper.saveExHisMzInf(list);
	}
}
