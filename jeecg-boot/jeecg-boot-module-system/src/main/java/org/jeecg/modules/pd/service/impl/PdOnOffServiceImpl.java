package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdOnOff;
import org.jeecg.modules.pd.mapper.PdOnOffMapper;
import org.jeecg.modules.pd.service.IPdOnOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: pd_on_off
 * @Author: jiangxz
 * @Date:   2020-04-01
 * @Version: V1.0
 */
@Service
public class PdOnOffServiceImpl extends ServiceImpl<PdOnOffMapper, PdOnOff> implements IPdOnOffService {

    @Autowired
    private PdOnOffMapper pdOnOffMapper;

    @Override
    public PdOnOff getOne(PdOnOff pdOnOff) {
        return pdOnOffMapper.getOne(pdOnOff);
    }
}
