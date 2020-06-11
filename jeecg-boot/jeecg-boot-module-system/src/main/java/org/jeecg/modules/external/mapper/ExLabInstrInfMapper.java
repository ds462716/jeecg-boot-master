package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.external.entity.ExLabInstrInf;

import java.util.List;

/**
 * @Description: ex_lab_instr_inf
 * @Author: jiangxz
 * @Date:   2020-06-09
 * @Version: V1.0
 */
public interface ExLabInstrInfMapper extends BaseMapper<ExLabInstrInf> {

    List<ExLabInstrInf> selectList(ExLabInstrInf exLabInstrInf);

    List<ExLabInstrInf> getExLabInstrInf(ExLabInstrInf exLabInstrInf);

    /**
     * 获取一条记录
     * @param exLabInstrInf
     * @return
     */
    ExLabInstrInf getOne(ExLabInstrInf exLabInstrInf);
}
