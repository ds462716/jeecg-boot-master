package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.ExHisChargeCodeGzzl;
import org.jeecg.modules.external.entity.ExHisZyChargeInfGzzl;
import org.jeecg.modules.external.entity.ExHisMzChargeInfGzzl;
import org.jeecg.modules.pd.entity.HisChargeInf;

import java.util.List;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
public interface IExHisChargeInfGzzlService extends IService<ExHisZyChargeInfGzzl> {

    Page<ExHisZyChargeInfGzzl> queryZyPatientInfoList(Page<ExHisZyChargeInfGzzl> pageList,ExHisZyChargeInfGzzl exHisZyChargeInfGzzl);

    Page<ExHisMzChargeInfGzzl> queryMzPatientInfoList(Page<ExHisMzChargeInfGzzl> pageList, ExHisMzChargeInfGzzl exHisMzChargeInfGzzl);

    Page<ExHisChargeCodeGzzl>  queryHisChargeCode(Page<ExHisChargeCodeGzzl> pageList,ExHisChargeCodeGzzl exHisChargeCodeGzzl);

}
