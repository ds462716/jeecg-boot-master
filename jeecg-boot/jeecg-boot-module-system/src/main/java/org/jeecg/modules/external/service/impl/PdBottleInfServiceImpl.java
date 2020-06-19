package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.external.entity.PdBottleInf;
import org.jeecg.modules.external.mapper.PdBottleInfMapper;
import org.jeecg.modules.external.service.IPdBottleInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 开闭瓶记录表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Service
public class PdBottleInfServiceImpl extends ServiceImpl<PdBottleInfMapper, PdBottleInf> implements IPdBottleInfService {
    @Autowired
    private PdBottleInfMapper pdBottleInfMapper;


    @Override
    public Page<PdBottleInf> selectList(Page<PdBottleInf> page, PdBottleInf pdBottleInf) {
        return pdBottleInfMapper.selectList(page,pdBottleInf);
    }

    @Override
    public List<PdBottleInf> selectList(PdBottleInf pdBottleInf) {
        return pdBottleInfMapper.selectList(pdBottleInf);
    }

    @Override
    public Page<PdBottleInf> bottleInfReportQuery(Page<PdBottleInf> page, PdBottleInf pdBottleInf) {
        return pdBottleInfMapper.bottleInfReportQuery(page,pdBottleInf);
    }

    @Override
    public List<HashMap> queryRecordView(PdBottleInf pdBottleInf) {
        return pdBottleInfMapper.queryRecordView(pdBottleInf);
    }
}


