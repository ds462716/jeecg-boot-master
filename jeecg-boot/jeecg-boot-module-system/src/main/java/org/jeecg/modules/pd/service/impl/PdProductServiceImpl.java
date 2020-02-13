package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdProductRule;
import org.jeecg.modules.pd.mapper.PdProductMapper;
import org.jeecg.modules.pd.mapper.PdProductRuleMapper;
import org.jeecg.modules.pd.service.IPdProductRuleService;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: pd_product
 * @Author: zxh
 * @Date:   2020-02-05
 * @Version: V1.0
 */
@Service
public class PdProductServiceImpl extends ServiceImpl<PdProductMapper, PdProduct> implements IPdProductService {

    @Autowired
    private IPdProductRuleService pdProductRuleService;

    @Autowired
    private PdProductRuleMapper pdProductRuleMapper;

    @Autowired
    private PdProductMapper pdProductMapper;

    @Override
    public Page<PdProductPage> chooseProductList(Page<PdProductPage> pageList, PdProduct pdProduct) {

        return pageList.setRecords(pdProductMapper.chooseProductList(pdProduct));
    }

    @Override
    public Page<PdProduct> selectList(Page<PdProduct> page, PdProduct pdProduct) {
        return page.setRecords(pdProductMapper.selectList(pdProduct));
    }

    /**
     * 包含事务的保存方法，编码规则
     * @param pdProduct
     */
    @Override
    @Transactional
    public void saveProduct(PdProduct pdProduct) {
        pdProductMapper.insert(pdProduct);
        //判断有没有编码规则
        if(pdProduct.getPdProductRules()!=null && !"".equals(pdProduct.getPdProductRules())){
            List<String> rules = pdProduct.getPdProductRules();
            List<PdProductRule> pdProductRules = new ArrayList<>();
            for(String str :rules){
                PdProductRule pdProductRule = new PdProductRule();
                pdProductRule.setProductId(pdProduct.getId());
                pdProductRule.setCreateBy(pdProduct.getUpdateBy());
                pdProductRule.setCreateTime(pdProduct.getUpdateTime());
                pdProductRule.setRuleId(str);
                pdProductRules.add(pdProductRule);
            }
            pdProductRuleService.saveBatch(pdProductRules);
        }

    }

    /**
     * 包含事务的更新方法，编码规则
     * @param pdProduct
     */
    @Override
    @Transactional
    public void updateProduct(PdProduct pdProduct) {
        pdProductMapper.updateById(pdProduct);
        //判断有没有编码规则
        if(pdProduct.getPdProductRules()!=null && !"".equals(pdProduct.getPdProductRules())){
            List<String> rules = pdProduct.getPdProductRules();
            List<PdProductRule> pdProductRules = new ArrayList<>();
            for(String str :rules){
                PdProductRule pdProductRule = new PdProductRule();
                pdProductRule.setProductId(pdProduct.getId());
                pdProductRule.setCreateBy(pdProduct.getUpdateBy());
                pdProductRule.setCreateTime(pdProduct.getUpdateTime());
                pdProductRule.setRuleId(str);
                pdProductRules.add(pdProductRule);
            }
            //先删除所有的再保存
            PdProductRule pdProductRule = new PdProductRule();
            pdProductRule.setProductId(pdProduct.getId());
            pdProductRule.setUpdateBy(pdProduct.getUpdateBy());
            pdProductRule.setUpdateTime(pdProduct.getUpdateTime());
            pdProductRuleMapper.removeByProductId(pdProductRule);
            pdProductRuleService.saveBatch(pdProductRules);
        }
    }


}
