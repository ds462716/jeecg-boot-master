package org.jeecg.modules.external.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.external.entity.ExHisChargeCodeGzzl;
import org.jeecg.modules.external.entity.ExHisZyChargeInfGzzl;
import org.jeecg.modules.external.entity.ExHisMzChargeInfGzzl;
import org.jeecg.modules.external.mapper.ExHisChargeInfGzzlMapper;
import org.jeecg.modules.external.service.IExHisChargeInfGzzlService;
import org.jeecg.modules.pd.entity.HisChargeInf;
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
@DS("multi-datasource1")
public class ExHisChargeInfGzzlServiceImpl extends ServiceImpl<ExHisChargeInfGzzlMapper, ExHisZyChargeInfGzzl> implements IExHisChargeInfGzzlService {
    @Autowired
    private ExHisChargeInfGzzlMapper exHisChargeInfGzzlMapper;
    /**
     * 赣州肿瘤医院查询病人信息
     * @param exHisZyChargeInfGzzl
     * @return
     */
    @Override
    @DS("multi-datasource1")
    public Page<ExHisZyChargeInfGzzl>  queryZyPatientInfoList(Page<ExHisZyChargeInfGzzl> page,ExHisZyChargeInfGzzl exHisZyChargeInfGzzl) {
        return exHisChargeInfGzzlMapper.queryZyPatientInfoList(page,exHisZyChargeInfGzzl);
    }

    /**
     * 赣州肿瘤医院查询门诊病人信息
     * @param exHisMzChargeInfGzzl
     * @return
     */
    @Override
    @DS("multi-datasource1")
    public Page<ExHisMzChargeInfGzzl> queryMzPatientInfoList(Page<ExHisMzChargeInfGzzl> page,ExHisMzChargeInfGzzl exHisMzChargeInfGzzl) {
        return exHisChargeInfGzzlMapper.queryMzPatientInfoList(page,exHisMzChargeInfGzzl);
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
