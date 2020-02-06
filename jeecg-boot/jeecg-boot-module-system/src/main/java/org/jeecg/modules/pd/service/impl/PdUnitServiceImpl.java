package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdUnit;
import org.jeecg.modules.pd.mapper.PdUnitMapper;
import org.jeecg.modules.pd.service.IPdUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 产品单位表
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
public class PdUnitServiceImpl extends ServiceImpl<PdUnitMapper, PdUnit> implements IPdUnitService {
    @Autowired
    private PdUnitMapper pdUnitMapper;

    @Override
    public List<PdUnit> queryList(PdUnit pdUnit) {
        return pdUnitMapper.queryList(pdUnit);
    }

    @Override
    public Page<PdUnit> queryList(Page<PdUnit> pageList, PdUnit pdUnit) {
        return pageList.setRecords(pdUnitMapper.queryList(pdUnit));
    }
}
