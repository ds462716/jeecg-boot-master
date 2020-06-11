package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.external.entity.ExLabInstrInf;
import org.jeecg.modules.external.mapper.ExLabInstrInfMapper;
import org.jeecg.modules.external.service.IExLabInstrInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Description: ex_lab_instr_inf
 * @Author: jiangxz
 * @Date:   2020-06-09
 * @Version: V1.0
 */
@Service
public class ExLabInstrInfServiceImpl extends ServiceImpl<ExLabInstrInfMapper, ExLabInstrInf> implements IExLabInstrInfService {
    @Autowired
    private ExLabInstrInfMapper exLabInstrInfMapper;

    @Override
    public Page<ExLabInstrInf> selectList(Page<ExLabInstrInf> page, ExLabInstrInf exLabInstrInf) {
        return page.setRecords(exLabInstrInfMapper.selectList(exLabInstrInf));
    }

    @Override
    public List<ExLabInstrInf> getExLabInstrInf(ExLabInstrInf exLabInstrInf) {
        return baseMapper.getExLabInstrInf(exLabInstrInf);
    }


    @Override
    @Transactional
    public void synUpdateInstrInf(List<ExLabInstrInf> labInstrInfList) {
        if (labInstrInfList != null && labInstrInfList.size() > 0) {
            for(ExLabInstrInf inf: labInstrInfList){
                ExLabInstrInf exLabInstrInf=exLabInstrInfMapper.getOne(inf);
                if(exLabInstrInf==null){
                    exLabInstrInfMapper.insert(inf);
                }
            }
        }
    }
}
