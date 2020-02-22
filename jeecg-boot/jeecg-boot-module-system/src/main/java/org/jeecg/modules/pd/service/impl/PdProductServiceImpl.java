package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.PdEncodingRuleDetailMapper;
import org.jeecg.modules.pd.mapper.PdProductMapper;
import org.jeecg.modules.pd.mapper.PdProductRuleMapper;
import org.jeecg.modules.pd.service.IPdEncodingRuleService;
import org.jeecg.modules.pd.service.IPdProductRuleService;
import org.jeecg.modules.pd.service.IPdProductService;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.util.BarCodeUtil;
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

    @Autowired
    private IPdProductStockService pdProductStockService;

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
     * @param Barcode1
     * @param Barcode2
     * @return
     */
    @Override
    public Result<Map>  getScanCode(String Barcode1,String Barcode2,  Result<Map> result) {
        Map<String,Object> resultMap = new HashMap<>();
        //去空格转为大写
        Barcode1 = BarCodeUtil.trimStr(Barcode1.toUpperCase());
        Barcode2 = BarCodeUtil.trimStr(Barcode2.toUpperCase());
        if(Barcode1 != null && !"".equals(Barcode1) && Barcode2 != null && !"".equals(Barcode2) ){
            //获得产品编号
            String productNumber = BarCodeUtil.getPrdNumber(Barcode1);
            //查询产品是否存在
            PdProduct pdProduct = this.findByNumber(productNumber);
            if(pdProduct!=null){
                result.setCode(200);
                result.setMessage("成功");
                //产品对象
                resultMap.put("pdProduct",pdProduct);
                String productBarCode;
                //产品条码修正
                if(Barcode1.equals(Barcode2)){
                    productBarCode = Barcode1;
                }else{
                    productBarCode = Barcode1 + Barcode2;
                }
                //产品完整条形码
                resultMap.put("productBarCode",productBarCode);
                //初始化
                resultMap.put("code","500");
                resultMap.put("msg","解析失败，请记录条码并联系管理员");
                resultMap.put("number","");
                resultMap.put("expDate","");
                resultMap.put("batchNo","");
                //如果是HIB条码直接使用工具类解析，暂无条码规则
                if(productBarCode.startsWith("+")){
                    resultMap =  BarCodeUtil.scanCode(Barcode1,Barcode2,resultMap);
                }else{
                    PdProductRule pdProductRule = new PdProductRule();
                    pdProductRule.setProductId(pdProduct.getId());
                    //查询产品对应得编号规则
                    List<PdProductRule> pdProductRules = pdProductRuleMapper.selectList(pdProductRule);
                    //如果编码规则存在
                    if(pdProductRules!=null && pdProductRules.size()>0){
                        Iterator it=pdProductRules.iterator();
                        while(it.hasNext()) {
                            PdProductRule pr = (PdProductRule) it.next();
                            if(productBarCode.length()!=Integer.valueOf(pr.getTotalDigit())){
                                it.remove();
                            }
                        }
                        //如果绑定多条编码规则且编码规则长度一致 只能取其中一条作为扫码规则
                        PdEncodingRule encodingRule = pdEncodingRuleService.getById(pdProductRules.get(0).getRuleId());
                        PdEncodingRuleDetail pdEncodingRuleDetail = new PdEncodingRuleDetail();
                        pdEncodingRuleDetail.setCodeId(pdProductRules.get(0).getRuleId());
                        List<PdEncodingRuleDetail> pdEncodingRuleDetails = pdEncodingRuleDetailMapper.selectList(pdEncodingRuleDetail);
                        //如果编码和条码为同一条
                        if(productBarCode.length()==encodingRule.getTotalDigit()){
                            //完整条码根据扫码来
                            Map<String,String> tempMap = new HashMap<>();
                            String temp = productBarCode;
                            for(PdEncodingRuleDetail erd :pdEncodingRuleDetails){
                                String key = erd.getValue();
                                if(temp.startsWith(key)){
                                    int tempLength = temp.length();
                                    temp = temp.substring(key.length(),tempLength);
                                    if(PdConstant.IDENTIFIER_TYPE_1.equals(erd.getType())){
                                        String value = temp.substring(0,Integer.parseInt(erd.getSize()));
                                        tempMap.put(key,value);
                                        tempLength = temp.length();
                                        temp = temp.substring(value.length(),tempLength);
                                    }else{
                                        String value = temp.substring(0,erd.getLength());
                                        tempMap.put(key,value);
                                        tempLength = temp.length();
                                        temp = temp.substring(value.length(),tempLength);
                                    }
                                }else if ("#".equals(key)){
                                    int tempLength = temp.length();
                                    String value = temp.substring(0,Integer.parseInt(erd.getSize()));
                                    tempMap.put(key,value);
                                    tempLength = temp.length();
                                    temp = temp.substring(value.length(),tempLength);
                                }
                            }
                            resultMap.put("code","200");
                            resultMap.put("msg","扫码成功");
                            //解析产品编号，批号，有效期
                            resultMap = BarCodeUtil.getProductNumber(resultMap,tempMap);
                            resultMap = BarCodeUtil.getProductBatchNo(resultMap,tempMap);
                            resultMap = BarCodeUtil.getProductExpDate(resultMap,tempMap);
                        }else{
                            result.setCode(500);
                            result.setMessage("解析失败，扫描的编码与绑定的规则长度不一致");
                        }
                    }else{
                        //没有绑定扫码规则 使用条码规则进行扫码
                        resultMap =  BarCodeUtil.scanCode(Barcode1,Barcode2,resultMap);
                    }
                }
            }else{
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
    /**
     * 条码规则解析并查询库存
     * @param Barcode1
     * @param Barcode2
     * @return
     */
    @Override
    public Result<List<PdProductStock>> getStocks(String Barcode1,String Barcode2, Result<List<PdProductStock>> result) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(Barcode1 != null && !"".equals(Barcode1) && Barcode2 != null && !"".equals(Barcode2) ){
            String productBarCode;
            //产品条码修正
            if(Barcode1.indexOf(Barcode2) != -1){
                productBarCode = Barcode1;
            }else{
                productBarCode = Barcode1 + Barcode2;
            }
            //先根据条码查询库存
            PdProductStock pdProductStock = new PdProductStock();
            pdProductStock.setProductBarCode(productBarCode);
            List<PdProductStock> pdProductStocks = pdProductStockService.selectList(pdProductStock);
            //有库存直接返回
            if (pdProductStocks != null && pdProductStocks.size() > 0) {
                result.setCode(200);
                result.setResult(pdProductStocks);
            } else {
                String productNumber = BarCodeUtil.getPrdNumber(Barcode1);
                PdProduct pdProduct = this.findByNumber(productNumber);
                if (pdProduct != null) {
                    //产品对象
                    PdProductRule pdProductRule = new PdProductRule();
                    pdProductRule.setProductId(pdProduct.getId());
                    List<PdProductRule> pdProductRules = pdProductRuleMapper.selectList(pdProductRule);
                    if (pdProductRules != null && pdProductRules.size() > 0) {
                        Iterator it = pdProductRules.iterator();
                        while (it.hasNext()) {
                            PdProductRule pr = (PdProductRule) it.next();
                            if (productBarCode.length() != Integer.valueOf(pr.getTotalDigit())) {
                                it.remove();
                            }
                        }
                        //如果绑定多条编码规则且编码规则长度一致 只能取其中一条作为扫码规则
                        PdEncodingRule encodingRule = pdEncodingRuleService.getById(pdProductRules.get(0).getRuleId());
                        PdEncodingRuleDetail pdEncodingRuleDetail = new PdEncodingRuleDetail();
                        pdEncodingRuleDetail.setCodeId(pdProductRules.get(0).getRuleId());
                        List<PdEncodingRuleDetail> pdEncodingRuleDetails = pdEncodingRuleDetailMapper.selectList(pdEncodingRuleDetail);
                        //如果编码和条码为同一条
                        if (productBarCode.length() == encodingRule.getTotalDigit()) {
                            String temp = productBarCode;
                            for (PdEncodingRuleDetail erd : pdEncodingRuleDetails) {
                                String key = erd.getValue();
                                if (temp.startsWith(key)) {
                                    int tempLength = temp.length();
                                    temp = temp.substring(key.length(), tempLength);
                                    if (PdConstant.IDENTIFIER_TYPE_1.equals(erd.getType())) {
                                        String value = temp.substring(0, Integer.parseInt(erd.getSize()));
                                        resultMap.put(key, value);
                                        tempLength = temp.length();
                                        temp = temp.substring(value.length(), tempLength);
                                    } else {
                                        String value = temp.substring(0, erd.getLength());
                                        resultMap.put(key, value);
                                        tempLength = temp.length();
                                        temp = temp.substring(value.length(), tempLength);
                                    }
                                } else if ("#".equals(key)) {
                                    int tempLength = temp.length();
                                    String value = temp.substring(0, Integer.parseInt(erd.getSize()));
                                    resultMap.put(key, value);
                                    tempLength = temp.length();
                                    temp = temp.substring(value.length(), tempLength);
                                }
                            }
                        } else {
                            result.setCode(500);
                            result.setMessage("解析失败，扫描的编码与绑定的规则长度不一致");
                        }
                    } else {
                        //没有绑定扫码规则
                        result.setCode(201);
                    }
                }else{
                    result.setCode(500);
                    result.setMessage("没有找到该条码对应的库存");
                }
            }
        }else{
            result.setCode(500);
            result.setMessage("解析失败，参数不正确");
        }
        return  result;

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
