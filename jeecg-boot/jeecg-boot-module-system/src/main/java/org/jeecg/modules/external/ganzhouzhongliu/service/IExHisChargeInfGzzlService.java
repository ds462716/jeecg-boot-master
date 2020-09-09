package org.jeecg.modules.external.ganzhouzhongliu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.ganzhouzhongliu.entity.ExHisChargeCodeGzzl;
import org.jeecg.modules.external.ganzhouzhongliu.entity.ExHisZyChargeInfGzzl;
import org.jeecg.modules.external.ganzhouzhongliu.entity.ExHisMzChargeInfGzzl;
import org.jeecg.modules.pd.entity.PdDosage;

import java.util.List;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface IExHisChargeInfGzzlService extends IService<ExHisZyChargeInfGzzl> {

    Page<PdDosage> queryZyPatientInfoList(Page<PdDosage> pageList, PdDosage pdDosage);

    Page<PdDosage> queryMzPatientInfoList(Page<PdDosage> pageList, PdDosage pdDosage);

    Page<ExHisChargeCodeGzzl>  queryHisChargeCode(Page<ExHisChargeCodeGzzl> pageList,ExHisChargeCodeGzzl exHisChargeCodeGzzl);

    List<ExHisZyChargeInfGzzl> queryZyPatientInfoList(ExHisZyChargeInfGzzl exHisZyChargeInfGzzl);
}
