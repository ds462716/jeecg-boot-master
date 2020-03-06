package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.pd.mapper.PdSupplierMapper;
import org.jeecg.modules.pd.service.IPdSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 供应商
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
@Service
public class PdSupplierServiceImpl extends ServiceImpl<PdSupplierMapper, PdSupplier> implements IPdSupplierService {

    @Autowired
    private PdSupplierMapper pdSupplierMapper;
    @Override
    public List<PdSupplier> verify(PdSupplier pdSupplier) {
        return pdSupplierMapper.verify(pdSupplier);
    }

    @Override
    public List<PdSupplier> selectList(PdSupplier pdSupplier) {
        return pdSupplierMapper.selectList(pdSupplier);
    }

    @Override
    public List<PdSupplier> selectAllList(PdSupplier pdSupplier) {
        return pdSupplierMapper.selectAllList(pdSupplier);
    }

    @Override
    public void updateValidityFlag(PdSupplier pdSupplier){
        pdSupplierMapper.updateValidityFlag(pdSupplier);
    }
}
