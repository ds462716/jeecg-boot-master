package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdVender;
import org.jeecg.modules.pd.mapper.PdVenderMapper;
import org.jeecg.modules.pd.service.IPdVenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
    public PdVender verify(PdVender pdVender) {
        return pdVenderMapper.verify(pdVender);
    }
}
