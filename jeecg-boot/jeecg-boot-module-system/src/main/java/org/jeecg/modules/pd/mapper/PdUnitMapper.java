package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 产品单位表
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
public interface PdUnitMapper extends BaseMapper<PdUnit> {

    List<PdUnit> queryList(PdUnit pdUnit);

    List<PdUnit> verify(PdUnit pdUnit);
}
