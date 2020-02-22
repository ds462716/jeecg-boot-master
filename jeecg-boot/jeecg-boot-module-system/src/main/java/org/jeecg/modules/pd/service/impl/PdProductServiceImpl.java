package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.MessageConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
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
                result.setCode(MessageConstant.ICODE_STATE_500);
                result.setMessage(MessageConstant.CODE_MESSAGE_2);
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
                resultMap.put("code",MessageConstant.CODE_STATE_500);
                resultMap.put("msg",MessageConstant.CODE_MESSAGE_1);
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
                            resultMap.put("code",MessageConstant.ICODE_STATE_200);
                            resultMap.put("msg",MessageConstant.CODE_MESSAGE_2);
                            //解析产品编号，批号，有效期
                            resultMap = BarCodeUtil.getProductNumber(resultMap,tempMap);
                            resultMap = BarCodeUtil.getProductBatchNo(resultMap,tempMap);
                            resultMap = BarCodeUtil.getProductExpDate(resultMap,tempMap);
                        }else{
                            result.setCode(MessageConstant.ICODE_STATE_500);
                            result.setMessage(MessageConstant.CODE_MESSAGE_3);
                        }
                    }else{
                        //没有绑定扫码规则 使用条码规则进行扫码
                        resultMap =  BarCodeUtil.scanCode(Barcode1,Barcode2,resultMap);
                    }
                }
            }else{
                //没有绑定扫码规则
                result.setCode(MessageConstant.ICODE_STATE_500);
                result.setMessage(MessageConstant.CODE_MESSAGE_4);
            }
        }else{
            result.setCode(MessageConstant.ICODE_STATE_500);
            result.setMessage(MessageConstant.CODE_MESSAGE_5);
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
        List<PdProductStock> pdProductStocks = new ArrayList<>();
        //去空格转为大写
        Barcode1 = BarCodeUtil.trimStr(Barcode1.toUpperCase());
        Barcode2 = BarCodeUtil.trimStr(Barcode2.toUpperCase());
        if(Barcode1 != null && !"".equals(Barcode1) && Barcode2 != null && !"".equals(Barcode2) ){
            //获得产品编号
            String productNumber = BarCodeUtil.getPrdNumber(Barcode1);
            //查询产品是否存在
            PdProduct pdProduct = this.findByNumber(productNumber);
            if(pdProduct!=null){
                String productBarCode;
                //产品条码修正
                if(Barcode1.indexOf(Barcode2) != -1){
                    productBarCode = Barcode1;
                }else{
                    productBarCode = Barcode1 + Barcode2;
                }
                //先根据条码查询库存
                PdProductStock pdProductStock = new PdProductStock();
                pdProductStock.setProductId(pdProduct.getId());
                pdProductStock.setProductBarCode(productBarCode);
                pdProductStocks = pdProductStockService.selectList(pdProductStock);
                //有库存直接返回
                if (pdProductStocks != null && pdProductStocks.size() > 0) {
                    result.setCode(MessageConstant.ICODE_STATE_200);
                    result.setMessage(MessageConstant.CODE_MESSAGE_2);
                } else {
                    Map<String,Object> resultMap = new HashMap<String,Object>();
                    //产品完整条形码
                    resultMap.put("productBarCode",productBarCode);
                    //初始化
                    resultMap.put("code",MessageConstant.CODE_STATE_500);
                    resultMap.put("msg", MessageConstant.CODE_MESSAGE_1);
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
                                resultMap.put("code",MessageConstant.CODE_STATE_200);
                                resultMap.put("msg",MessageConstant.CODE_MESSAGE_2);
                                //解析产品编号，批号，有效期
                                resultMap = BarCodeUtil.getProductNumber(resultMap,tempMap);
                                resultMap = BarCodeUtil.getProductBatchNo(resultMap,tempMap);
                                resultMap = BarCodeUtil.getProductExpDate(resultMap,tempMap);
                            }else{
                                result.setCode(MessageConstant.ICODE_STATE_500);
                                result.setMessage(MessageConstant.CODE_MESSAGE_3);
                            }
                        }else{
                            //没有绑定扫码规则 使用条码规则进行扫码
                            resultMap =  BarCodeUtil.scanCode(Barcode1,Barcode2,resultMap);
                        }
                    }

                    //判断库存的有效期
                    if(MessageConstant.CODE_STATE_201.equals((String)resultMap.get("code"))){
                        //库存已过期
                        result.setCode(MessageConstant.ICODE_STATE_201);
                        result.setMessage((String)resultMap.get("msg"));
                    }else if(MessageConstant.CODE_STATE_203.equals((String)resultMap.get("code"))){
                        //库存接近有效期  请注意
                        result.setCode(MessageConstant.ICODE_STATE_203);
                        result.setMessage((String)resultMap.get("msg"));
                    }else{
                        result.setCode(MessageConstant.ICODE_STATE_200);
                        result.setMessage(MessageConstant.CODE_MESSAGE_2);
                    }
                    //如果扫码时客户修改入库时直接修改批号有效期，不能修改实际条码，所以不能用条码
                    pdProductStock.setProductBarCode("");
                    //pdProductStock.setProductBarCode((String) resultMap.get("productBarCode"));
                    //不根据number查询
                    //pdProductStock.setNumber((String) resultMap.get("number"));
                    //查询条件为  产品id 产品批号  产品有效期
                    pdProductStock.setExpDate(DateUtils.str2Date((String)resultMap.get("expDate"),DateUtils.date_sdf));
                    pdProductStock.setBatchNo((String) resultMap.get("batchNo"));
                    //根据条件查询库存
                    pdProductStocks = pdProductStockService.selectList(pdProductStock);
                }
            }else{
                //没有绑定扫码规则
                result.setCode(MessageConstant.ICODE_STATE_500);
                result.setMessage(MessageConstant.CODE_MESSAGE_4);
            }
        }else{
            result.setCode(MessageConstant.ICODE_STATE_500);
            result.setMessage(MessageConstant.CODE_MESSAGE_5);
        }
        result.setResult(pdProductStocks);
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
