package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdEncodingRuleDetail;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdProductRule;
import org.jeecg.modules.pd.mapper.PdEncodingRuleDetailMapper;
import org.jeecg.modules.pd.mapper.PdEncodingRuleMapper;
import org.jeecg.modules.pd.mapper.PdProductMapper;
import org.jeecg.modules.pd.mapper.PdProductRuleMapper;
import org.jeecg.modules.pd.service.IPdEncodingRuleService;
import org.jeecg.modules.pd.service.IPdProductRuleService;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private PdEncodingRuleDetailMapper pdEncodingRuleDetailMapper;

    @Autowired
    private PdProductMapper pdProductMapper;

    @Autowired
    private IPdEncodingRuleService pdEncodingRuleService;

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

    //根据编号查询产品信息
    public PdProduct findByNumber(String productNumber) {
        return pdProductMapper.findByNumber(productNumber);
    }

    /**
     * 条码规则解析
     * @param barcode1
     * @param barcode2
     * @return
     */
    @Override
    public Result<Map>  getScanCode(String barcode1, String barcode2, Result<Map> result) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(barcode1 != null && !"".equals(barcode1) && barcode2 != null && !"".equals(barcode2)){
            //如果是一个条码，两个值相同，不同是两段条码
            String barcode;
            if(barcode1.equals(barcode2)){
                barcode = barcode1;
            }else{
                barcode = barcode1+barcode2;
            }
            int endIndex = getCodeLength(barcode);
            int startIndex = 2;
            if(endIndex==13){
                startIndex = 0;
            }
            String productNumber = barcode.substring(startIndex,endIndex);
            PdProduct pdProduct = this.findByNumber(productNumber);
            if(pdProduct!=null){
                //产品对象
                resultMap.put("pdProduct",pdProduct);
                PdProductRule pdProductRule = new PdProductRule();
                pdProductRule.setProductId(pdProduct.getId());
                List<PdProductRule> pdProductRules = pdProductRuleMapper.selectList(pdProductRule);
                if(pdProductRules!=null && pdProductRules.size()>0){
                    Iterator it=pdProductRules.iterator();
                    while(it.hasNext()) {
                        PdProductRule pr = (PdProductRule) it.next();
                        if(barcode.length()!=Integer.valueOf(pr.getTotalDigit())){
                            it.remove();
                        }
                    }
                    //如果绑定多条编码规则且编码规则长度一致 只能取其中一条作为扫码规则
                    PdEncodingRule encodingRule = pdEncodingRuleService.getById(pdProductRules.get(0).getRuleId());
                    PdEncodingRuleDetail pdEncodingRuleDetail = new PdEncodingRuleDetail();
                    pdEncodingRuleDetail.setCodeId(pdProductRules.get(0).getRuleId());
                    List<PdEncodingRuleDetail> pdEncodingRuleDetails = pdEncodingRuleDetailMapper.selectList(pdEncodingRuleDetail);
                    //如果编码和条码为同一条
                    if(barcode.length()==encodingRule.getTotalDigit()){
                        result.setCode(200);
                        //完整条码根据扫码来
                        /*int barLength = barcode.length();
                        resultMap.put("secondCode",barcode.substring(endIndex,barLength));*/
                        String temp = barcode;
                        for(PdEncodingRuleDetail erd :pdEncodingRuleDetails){
                            String key = erd.getValue();
                            if(temp.startsWith(key)){
                                int tempLength = temp.length();
                                temp = temp.substring(key.length(),tempLength);
                                if(PdConstant.IDENTIFIER_TYPE_1.equals(erd.getType())){
                                    String value = temp.substring(0,Integer.parseInt(erd.getSize()));
                                    resultMap.put(key,value);
                                    tempLength = temp.length();
                                    temp = temp.substring(value.length(),tempLength);
                                }else{
                                    String value = temp.substring(0,erd.getLength());
                                    resultMap.put(key,value);
                                    tempLength = temp.length();
                                    temp = temp.substring(value.length(),tempLength);
                                }
                            }else if ("#".equals(key)){
                                int tempLength = temp.length();
                                String value = temp.substring(0,Integer.parseInt(erd.getSize()));
                                resultMap.put(key,value);
                                tempLength = temp.length();
                                temp = temp.substring(value.length(),tempLength);
                            }
                        }

                    }
                    else{
                        result.setCode(500);
                        result.setMessage("解析失败，扫描的编码与绑定的规则长度不一致");
                    }
                }
                else{
                    //没有绑定扫码规则
                    result.setCode(201);
                }
            }
            else{
                //没有绑定扫码规则
                result.setCode(500);
                result.setMessage("没有找到该条码对应的产品");
            }

        }else{
            result.setCode(500);
            result.setMessage("解析失败，参数不正确");
        }
        result.setResult(resultMap);
        return  result;

    }

    //00(13)位为产品编号，//01(14)为产品编号//其他为(13)位为产品编号
    public static int getCodeLength(String barcode){
        String startStr = barcode.substring(0, 2);
        if("01".equals(startStr)){
            return 16;
        }
        if("00".equals(startStr)){
            return 20;
        }
        return 13;
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
