package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.pd.mapper.PdSupplierMapper;
import org.jeecg.modules.pd.service.IPdSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 供应商
 * @Author: jeecg-boot
 * @Date:   2020-01-09
 * @Version: V1.0
 */
@Service
public class PdSupplierServiceImpl extends ServiceImpl<PdSupplierMapper, PdSupplier> implements IPdSupplierService {

    @Autowired
    private PdSupplierMapper pdSupplierMapper;
    @Override
    public PdSupplier verify(PdSupplier pdSupplier) {
        return pdSupplierMapper.verify(pdSupplier);
    }
}
