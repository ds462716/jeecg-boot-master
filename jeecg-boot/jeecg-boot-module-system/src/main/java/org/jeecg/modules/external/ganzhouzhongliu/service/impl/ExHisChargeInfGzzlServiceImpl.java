package org.jeecg.modules.external.ganzhouzhongliu.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.external.ganzhouzhongliu.entity.ExHisChargeCodeGzzl;
import org.jeecg.modules.external.ganzhouzhongliu.entity.ExHisZyChargeInfGzzl;
import org.jeecg.modules.external.ganzhouzhongliu.entity.ExHisMzChargeInfGzzl;
import org.jeecg.modules.external.ganzhouzhongliu.mapper.ExHisChargeInfGzzlMapper;
import org.jeecg.modules.external.ganzhouzhongliu.service.IExHisChargeInfGzzlService;
import org.jeecg.modules.pd.entity.PdDosage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
@Service
@DS("multi-datasourceGZZL")
public class ExHisChargeInfGzzlServiceImpl extends ServiceImpl<ExHisChargeInfGzzlMapper, ExHisZyChargeInfGzzl> implements IExHisChargeInfGzzlService {
    @Autowired
    private ExHisChargeInfGzzlMapper exHisChargeInfGzzlMapper;
    /**
     * 赣州肿瘤医院查询病人信息
     * @param pdDosage
     * @return
     */
    @Override
    @DS("multi-datasource1")
    public Page<PdDosage>  queryZyPatientInfoList(Page<PdDosage> page, PdDosage pdDosage) {
        return exHisChargeInfGzzlMapper.queryZyPatientInfoList(page,pdDosage);
    }

    /**
     * 赣州肿瘤医院查询病人信息
     * @param exHisZyChargeInfGzzl
     * @return
     */
    @Override
    @DS("multi-datasource1")
    public List<ExHisZyChargeInfGzzl> queryZyPatientInfoList(ExHisZyChargeInfGzzl exHisZyChargeInfGzzl) {
        return exHisChargeInfGzzlMapper.queryZyPatientInfoList(exHisZyChargeInfGzzl);
    }

    /**
     * 赣州肿瘤医院查询门诊病人信息
     * @param pdDosage
     * @return
     */
    @Override
    @DS("multi-datasource1")
    public Page<PdDosage> queryMzPatientInfoList(Page<PdDosage> page,PdDosage pdDosage) {
        return exHisChargeInfGzzlMapper.queryMzPatientInfoList(page,pdDosage);
    }

    /**
     * 赣州肿瘤医院查询收费代码
     * @param exHisChargeCodeGzzl
     * @return
     */
    @Override
    @DS("multi-datasource1")
    public Page<ExHisChargeCodeGzzl> queryHisChargeCode(Page<ExHisChargeCodeGzzl> page,ExHisChargeCodeGzzl exHisChargeCodeGzzl) {
        return exHisChargeInfGzzlMapper.queryHisChargeCode(page,exHisChargeCodeGzzl);
    }


}
