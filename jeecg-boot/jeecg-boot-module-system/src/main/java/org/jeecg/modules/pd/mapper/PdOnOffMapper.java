package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdOnOff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: pd_on_off
 * @Author: jiangxz
 * @Date:   2020-04-01
 * @Version: V1.0
 */
public interface PdOnOffMapper extends BaseMapper<PdOnOff> {

    PdOnOff getOne(PdOnOff pdOnOff);

}
