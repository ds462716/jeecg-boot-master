package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdSupplier;

/**
 * @Description: 供应商
 * @Author: jeecg-boot
 * @Date:   2020-01-09
 * @Version: V1.0
 */
public interface IPdSupplierService extends IService<PdSupplier> {

    PdSupplier verify(PdSupplier pdSupplier);
}
