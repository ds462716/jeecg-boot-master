package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdSupplier;

import java.util.List;

/**
 * @Description: 供应商
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
public interface IPdSupplierService extends IService<PdSupplier> {

    PdSupplier verify(PdSupplier pdSupplier);

    List<PdSupplier> selectList(PdSupplier pdSupplier);
    /**
     * 修改证照有效期标识
     */
    public void updateValidityFlag(PdSupplier pdSupplier);
}
