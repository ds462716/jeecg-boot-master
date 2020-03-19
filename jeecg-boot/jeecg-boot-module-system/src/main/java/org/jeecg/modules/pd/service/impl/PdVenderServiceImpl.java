package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdVender;
import org.jeecg.modules.pd.mapper.PdVenderMapper;
import org.jeecg.modules.pd.service.IPdVenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 生产厂家
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
@Service
public class PdVenderServiceImpl extends ServiceImpl<PdVenderMapper, PdVender> implements IPdVenderService {

    @Autowired
    private PdVenderMapper pdVenderMapper;
    @Override
    public  List<PdVender> verify(PdVender pdVender) {
        return pdVenderMapper.verify(pdVender);
    }

    /**
     * 下拉选择
     * @param pdVender
     * @return
     */
    @Override
    public List<PdVender> selectList(PdVender pdVender) {
        return pdVenderMapper.selectList(pdVender);
    }

    @Override
    public List<PdVender> selectAllList(PdVender pdVender) {
        return pdVenderMapper.selectAllList(pdVender);
    }

    @Override
    public void updateValidityFlag(PdVender pdVender){
        pdVenderMapper.updateValidityFlag(pdVender);
    }

    @Override
    public Page<PdVender> selectList(Page<PdVender> page, PdVender pdVender) {
        return page.setRecords(pdVenderMapper.selectList(pdVender));
    }

    ;
}
