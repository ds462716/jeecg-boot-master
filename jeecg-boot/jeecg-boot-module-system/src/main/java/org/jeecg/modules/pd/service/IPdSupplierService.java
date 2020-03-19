package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    List<PdSupplier> verify(PdSupplier pdSupplier);

    List<PdSupplier> selectList(PdSupplier pdSupplier);

    List<PdSupplier> selectAllList(PdSupplier pdSupplier);
    /**
     * 修改证照有效期标识
     */
    void updateValidityFlag(PdSupplier pdSupplier);

    Page<PdSupplier> selectList(Page<PdSupplier> pageList, PdSupplier pdSupplier);
}
