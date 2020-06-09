package org.jeecg.modules.external.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.external.entity.ExLabInstrInf;

/**
 * @Description: ex_lab_instr_inf
 * @Author: jiangxz
 * @Date:   2020-06-09
 * @Version: V1.0
 */
public interface ExLabInstrInfMapper extends BaseMapper<ExLabInstrInf> {

    List<ExLabInstrInf> getExLabInstrInf(ExLabInstrInf exLabInstrInf);
}
