package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdSupplier;

/**
 * @Description: 供应商
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
public interface PdSupplierMapper extends BaseMapper<PdSupplier> {

    PdSupplier verify(PdSupplier pdSupplier);
}
