package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.ExLabInstrInf;

import java.util.List;

/**
 * @Description: ex_lab_instr_inf
 * @Author: jiangxz
 * @Date:   2020-06-09
 * @Version: V1.0
 */
public interface IExLabInstrInfService extends IService<ExLabInstrInf> {

    public Page<ExLabInstrInf> selectList(Page<ExLabInstrInf> page, ExLabInstrInf exLabInstrInf);

    List<ExLabInstrInf> getExLabInstrInf(ExLabInstrInf exLabInstrInf);

    public void synUpdateInstrInf(List<ExLabInstrInf> labInstrInfList);
}
