package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.mapper.PdProductMapper;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: pd_product
 * @Author: zxh
 * @Date:   2020-02-05
 * @Version: V1.0
 */
@Service
public class PdProductServiceImpl extends ServiceImpl<PdProductMapper, PdProduct> implements IPdProductService {

    @Autowired
    private PdProductMapper pdProductMapper;

    @Override
    public Page<PdProductPage> chooseProductList(Page<PdProductPage> pageList, PdProduct pdProduct) {

        return pageList.setRecords(pdProductMapper.chooseProductList(pdProduct));
    }
}
