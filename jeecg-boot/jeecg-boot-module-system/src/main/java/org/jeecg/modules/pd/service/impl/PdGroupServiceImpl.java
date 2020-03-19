package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdGroup;
import org.jeecg.modules.pd.mapper.PdGroupMapper;
import org.jeecg.modules.pd.service.IPdGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 产品组别
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
@Service
public class PdGroupServiceImpl extends ServiceImpl<PdGroupMapper, PdGroup> implements IPdGroupService {

    @Autowired
    private PdGroupMapper pdGroupMapper;

    @Override
    public List<PdGroup> selectList(PdGroup pdGroup) {
        return pdGroupMapper.selectList(pdGroup);
    }

    @Override
    public Page<PdGroup> selectList(Page<PdGroup> page, PdGroup pdGroup) {
        return page.setRecords(pdGroupMapper.selectList(pdGroup));
    }

    @Override
    public List<PdGroup> verify(PdGroup pdGroup) {
        return pdGroupMapper.verify(pdGroup);
    }
}
