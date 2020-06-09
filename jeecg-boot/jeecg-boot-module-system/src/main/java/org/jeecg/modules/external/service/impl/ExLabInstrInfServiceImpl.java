package org.jeecg.modules.external.service.impl;

import org.jeecg.modules.external.entity.ExLabInstrInf;
import org.jeecg.modules.external.mapper.ExLabInstrInfMapper;
import org.jeecg.modules.external.service.IExLabInstrInfService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: ex_lab_instr_inf
 * @Author: jiangxz
 * @Date:   2020-06-09
 * @Version: V1.0
 */
@Service
public class ExLabInstrInfServiceImpl extends ServiceImpl<ExLabInstrInfMapper, ExLabInstrInf> implements IExLabInstrInfService {

    @Override
    public List<ExLabInstrInf> getExLabInstrInf(ExLabInstrInf exLabInstrInf) {
        return baseMapper.getExLabInstrInf(exLabInstrInf);
    }
}
