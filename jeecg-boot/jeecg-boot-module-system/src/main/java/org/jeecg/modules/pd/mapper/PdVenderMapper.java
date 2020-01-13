package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdVender;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 生产厂家
 * @Author: jeecg-boot
 * @Date:   2020-01-09
 * @Version: V1.0
 */
public interface PdVenderMapper extends BaseMapper<PdVender> {

    PdVender verify(PdVender pdVender);
}
