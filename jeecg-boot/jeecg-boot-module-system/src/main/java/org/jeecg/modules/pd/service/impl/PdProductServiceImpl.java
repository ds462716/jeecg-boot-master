package org.jeecg.modules.pd.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.MessageConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.entity.PdBottleInf;
import org.jeecg.modules.external.mapper.PdBottleInfMapper;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.*;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.BarCodeUtil;
import org.jeecg.modules.pd.util.JmUtil;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecg.modules.pd.vo.PdProductReagents;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private SqlSession sqlsession;

    @Autowired
    private IPdPurchaseDetailService pdPurchaseDetailService;

    @Autowired
    private IPdStockRecordDetailService pdStockRecordDetailService;

    @Autowired
    private IPdProductStockUniqueCodeService pdProductStockUniqueCodeService;

    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;

    @Autowired
    private PdBottleInfMapper pdBottleInfMapper;




    @Override
    public Page<PdProductPage> chooseProductList(Page<PdProductPage> page, PdProduct pdProduct) {
        return pdProductMapper.chooseProductList(page,pdProduct);
    }

    @Override
    public Page<PdProduct> selectList(Page<PdProduct> page, PdProduct pdProduct) {
        return pdProductMapper.selectListByPage(page,pdProduct);
    }

    /**
     * 关联ex_his_charge_inf表的查询
     * @param pdProduct
     * @return
     */
    @Override
    public List<PdProduct> selectListForHisCharge(PdProduct pdProduct) {
        return pdProductMapper.selectListForHisCharge(pdProduct);
    }

    /**
     * 关联ex_his_charge_inf表的分页查询
     * @param page
     * @param pdProduct
     * @return
     */
    @Override
    public IPage<PdProduct> selectListForHisChargeByPage(Page<PdProduct> page, PdProduct pdProduct) {
        return pdProductMapper.selectListForHisCharge(page,pdProduct);
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
     * 条码规则解析
     * @param Barcode1
     * @param Barcode2
     * @return
     */
    @Override
    public Result<Map>  getScanCode(String Barcode1,String Barcode2,  Result<Map> result) {
        Map<String,Object> resultMap = new HashMap<>();
        if(Barcode1!=null && Barcode2!=null){
            //去空格转为大写
            Barcode1 = BarCodeUtil.trimStr(Barcode1.toUpperCase());
            Barcode2 = BarCodeUtil.trimStr(Barcode2.toUpperCase());
            if(!"".equals(Barcode1) && !"".equals(Barcode2) ){
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                //判断是否是根据产品编号查询
                if(Barcode1.equals(Barcode2)){
                    if(((Barcode1.startsWith("01") || Barcode1.startsWith("02")) && Barcode1.length()==16)
                            || (Barcode1.startsWith("00") && Barcode1.length()==20)
                            || (Barcode1.startsWith("93") && Barcode1.length()==19)
                            ||(Barcode1.length()==13)){
                        String productNumber = BarCodeUtil.GetEANUPN(Barcode1);
                        PdProduct pdProduct = new PdProduct();
                        pdProduct.setNumber(productNumber);
                        pdProduct.setDepartParentId(sysUser.getDepartParentId());
                        pdProduct.setStatus(PdConstant.DISABLE_ENABLE_STATUS_0);//只查启用
                        //查询产品是否存在
                        List<PdProduct> pdProducts = pdProductMapper.selectList(pdProduct);
                        if(pdProducts!=null && pdProducts.size()>0){
                            pdProduct = pdProducts.get(0);
                            result.setCode(MessageConstant.ICODE_STATE_200);
                            result.setMessage(MessageConstant.CODE_MESSAGE_2);
                            resultMap.put("code",MessageConstant.ICODE_STATE_200);
                            resultMap.put("msg",MessageConstant.CODE_MESSAGE_2);
                            //产品对象
                            resultMap.put("pdProduct",pdProduct);
                            resultMap.put("number",productNumber);
                            resultMap.put("expDate","");
                            resultMap.put("batchNo","");
                            resultMap.put("productBarCode","");
                            resultMap.put("produceDate","");
                            result.setResult(resultMap);
                            return result;
                        }
                    }else{
                        if(Barcode1.length()<24){
                            PdProduct pdProduct = new PdProduct();
                            pdProduct.setNumber(Barcode1);

                            pdProduct.setDepartParentId(sysUser.getDepartParentId());
                            pdProduct.setStatus(PdConstant.DISABLE_ENABLE_STATUS_0);//只查启用
                            //查询产品是否存在
                            List<PdProduct> pdProducts = pdProductMapper.selectList(pdProduct);
                            if(pdProducts!=null && pdProducts.size()>0){
                                pdProduct = pdProducts.get(0);
                                result.setCode(MessageConstant.ICODE_STATE_200);
                                result.setMessage(MessageConstant.CODE_MESSAGE_2);
                                resultMap.put("code",MessageConstant.ICODE_STATE_200);
                                resultMap.put("msg",MessageConstant.CODE_MESSAGE_2);
                                //产品对象
                                resultMap.put("pdProduct",pdProduct);
                                resultMap.put("number",Barcode1);
                                resultMap.put("expDate","");
                                resultMap.put("batchNo","");
                                resultMap.put("productBarCode","");
                                resultMap.put("produceDate","");
                                result.setResult(resultMap);
                                return result;
                            }
                        }
                    }
                }
                //获得产品编号
                String productNumber = BarCodeUtil.getPrdNumber(Barcode1);
                PdProduct pdProduct = new PdProduct();
                pdProduct.setNumber(productNumber);
                pdProduct.setDepartParentId(sysUser.getDepartParentId());
                pdProduct.setStatus(PdConstant.DISABLE_ENABLE_STATUS_0);//只查启用
                //查询产品是否存在
                List<PdProduct> pdProducts = pdProductMapper.selectList(pdProduct);
                if(pdProducts!=null && pdProducts.size()>0){
                    pdProduct = pdProducts.get(0);
                    result.setCode(MessageConstant.ICODE_STATE_200);
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
                    resultMap.put("produceDate","");
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
                            if(pdProductRules!=null && pdProductRules.size()>0){
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
                                    resultMap = BarCodeUtil.getProductProduceDate(resultMap,tempMap);
                                }else{
                                    result.setCode(MessageConstant.ICODE_STATE_500);
                                    result.setMessage(MessageConstant.CODE_MESSAGE_3);
                                }
                            }else{
                                //没有绑定扫码规则 使用条码规则进行扫码
                                resultMap =  BarCodeUtil.scanCode(Barcode1,Barcode2,resultMap);
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
    public Result<List<PdProductStock>> getStocks(String Barcode1,String Barcode2,String productFlag,String nestatStatus,String barCodeType, Result<List<PdProductStock>> result) {
        try{
            List<PdProductStock> pdProductStocks = new ArrayList<>();
            //去空格转为大写
            if(Barcode1!=null && Barcode2!=null){
                Barcode1 = BarCodeUtil.trimStr(Barcode1.toUpperCase());
                Barcode2 = BarCodeUtil.trimStr(Barcode2.toUpperCase());
                if(!"".equals(Barcode1) && !"".equals(Barcode2) ){
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    String productBarCode;
                    //产品条码修正
                    if(Barcode1.indexOf(Barcode2) != -1){
                        productBarCode = Barcode1;
                    }else{
                        productBarCode = Barcode1 + Barcode2;
                    }
                    //先根据条码查询库存
                    PdProductStock pdProductStock = new PdProductStock();
                    //pdProductStock.setProductId(pdProduct.getId());
                    pdProductStock.setProductBarCode(productBarCode);
                    pdProductStock.setProductFlag(productFlag);
                    pdProductStock.setDepartId(sysUser.getCurrentDepartId());
                    pdProductStock.setNestatStatus(nestatStatus);
                    pdProductStock.setBarCodeType(barCodeType);//批量打印的条码类型
                    pdProductStocks = pdProductStockService.queryProductStockList(pdProductStock);
                    //有库存直接返回
                    if (pdProductStocks != null && pdProductStocks.size() > 0) {
                        result.setCode(MessageConstant.ICODE_STATE_200);
                        result.setMessage(MessageConstant.CODE_MESSAGE_2);
                        result.setResult(pdProductStocks);
                        return  result;
                    }else{
                        //判断是否是根据产品编号查询
                        if(Barcode1.equals(Barcode2)){
                            if(((Barcode1.startsWith("01") || Barcode1.startsWith("02")) && Barcode1.length()==16)
                                    || (Barcode1.startsWith("00") && Barcode1.length()==20)
                                    || (Barcode1.startsWith("93") && Barcode1.length()==19)
                                    ||(Barcode1.length()==13)){
                                String productNumber = BarCodeUtil.GetEANUPN(Barcode1);
                                PdProduct pdProduct = new PdProduct();
                                pdProduct.setNumber(productNumber);
                                pdProduct.setDepartId(sysUser.getCurrentDepartId());
                                pdProduct.setDepartParentId(sysUser.getDepartParentId());
                                pdProduct.setProductFlag(productFlag);
                                //查询产品是否存在
                                List<PdProduct> pdProducts = pdProductMapper.selectList(pdProduct);
                                if(pdProducts!=null && pdProducts.size()>0){
                                    pdProduct = pdProducts.get(0);
                                    //根据产品id直接查询库存
                                    pdProductStock = new PdProductStock();
                                    pdProductStock.setProductId(pdProduct.getId());
                                    pdProductStock.setDepartId(sysUser.getCurrentDepartId());
                                    pdProductStock.setProductFlag(productFlag);
                                    pdProductStock.setNestatStatus(nestatStatus);
                                    pdProductStock.setBarCodeType(barCodeType);//批量打印的条码类型
                                    //根据条件查询库存
                                    pdProductStocks = pdProductStockService.queryProductStockList(pdProductStock);
                                    if (pdProductStocks != null && pdProductStocks.size() > 0) {
                                        result.setCode(MessageConstant.ICODE_STATE_200);
                                        result.setMessage(MessageConstant.CODE_MESSAGE_2);
                                        result.setResult(pdProductStocks);
                                        return result;
                                    }
                                }
                            }else{
                                if(Barcode1.length()<24){
                                    PdProduct pdProduct = new PdProduct();
                                    pdProduct.setNumber(Barcode1);
                                    pdProduct.setDepartParentId(sysUser.getDepartParentId());
                                    pdProduct.setProductFlag(productFlag);
                                    //查询产品是否存在
                                    List<PdProduct> pdProducts = pdProductMapper.selectList(pdProduct);
                                    if(pdProducts!=null && pdProducts.size()>0){
                                        pdProduct = pdProducts.get(0);
                                        //根据产品id直接查询库存
                                        pdProductStock = new PdProductStock();
                                        pdProductStock.setProductId(pdProduct.getId());
                                        pdProductStock.setDepartId(sysUser.getCurrentDepartId());
                                        pdProductStock.setProductFlag(productFlag);
                                        pdProductStock.setNestatStatus(nestatStatus);
                                        pdProductStock.setBarCodeType(barCodeType);//批量打印的条码类型
                                        //根据条件查询库存
                                        pdProductStocks = pdProductStockService.queryProductStockList(pdProductStock);
                                        if (pdProductStocks != null && pdProductStocks.size() > 0) {
                                            result.setCode(MessageConstant.ICODE_STATE_200);
                                            result.setMessage(MessageConstant.CODE_MESSAGE_2);
                                            result.setResult(pdProductStocks);
                                            return result;
                                        }
                                    }
                                }
                            }
                        }
                        //获得产品编号
                        String productNumber = BarCodeUtil.getPrdNumber(Barcode1);
                        PdProduct pdProduct = new PdProduct();
                        pdProduct.setNumber(productNumber);
                        //pdProduct.setDepartId(sysUser.getCurrentDepartId());
                        pdProduct.setDepartParentId(sysUser.getDepartParentId());
                        pdProduct.setProductFlag(productFlag);
                        //查询产品是否存在
                        List<PdProduct> pdProducts = pdProductMapper.selectList(pdProduct);
                        if(pdProducts!=null && pdProducts.size()>0){
                            pdProduct = pdProducts.get(0);
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
                                    if(pdProductRules!=null && pdProductRules.size()>0){
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
                                }else{
                                    //没有绑定扫码规则 使用条码规则进行扫码
                                    resultMap =  BarCodeUtil.scanCode(Barcode1,Barcode2,resultMap);
                                }
                            }
                            //判断库存的有效期
                            if(MessageConstant.CODE_STATE_201.equals(String.valueOf(resultMap.get("code")))){
                                //库存已过期
                                result.setCode(MessageConstant.ICODE_STATE_201);
                                result.setMessage((String)resultMap.get("msg"));
                            }else if(MessageConstant.CODE_STATE_203.equals(String.valueOf(resultMap.get("code")))){
                                //库存接近有效期  请注意
                                result.setCode(MessageConstant.ICODE_STATE_203);
                                result.setMessage((String)resultMap.get("msg"));
                            }else{
                                result.setCode(MessageConstant.ICODE_STATE_200);
                                result.setMessage(MessageConstant.CODE_MESSAGE_2);
                            }
                            //如果扫码时客户修改入库时直接修改批号有效期，不能修改实际条码，所以不能用条码
                            pdProductStock = new PdProductStock();
                            pdProductStock.setProductId(pdProduct.getId());
                            pdProductStock.setProductBarCode("");
                            //pdProductStock.setProductBarCode((String) resultMap.get("productBarCode"));
                            //不根据number查询
                            //pdProductStock.setNumber((String) resultMap.get("number"));
                            //查询条件为  产品id 产品批号  产品有效期
                            pdProductStock.setExpDate(DateUtils.str2Date((String)resultMap.get("expDate"),DateUtils.date_sdf.get()));
                            pdProductStock.setBatchNo((String) resultMap.get("batchNo"));
                            pdProductStock.setDepartId(sysUser.getCurrentDepartId());
                            pdProductStock.setProductFlag(productFlag);
                            pdProductStock.setNestatStatus(nestatStatus);
                            pdProductStock.setBarCodeType(barCodeType);//批量打印的条码类型
                            //根据条件查询库存
                            pdProductStocks = pdProductStockService.queryProductStockList(pdProductStock);
                        }else {
                            //没有绑定扫码规则
                            result.setCode(MessageConstant.ICODE_STATE_500);
                            result.setMessage(MessageConstant.CODE_MESSAGE_4);
                        }
                    }
                }else{
                    result.setCode(MessageConstant.ICODE_STATE_500);
                    result.setMessage(MessageConstant.CODE_MESSAGE_5);
                }
                result.setResult(pdProductStocks);
            }
        }catch(Exception e){
            e.printStackTrace();
            result.setCode(MessageConstant.ICODE_STATE_500);
            result.setMessage(MessageConstant.CODE_MESSAGE_8);
        }
        return  result;

    }

    /**
     * 开瓶扫码
     * @param Barcode
     * @param result
     * @return
     */
    @Override
    public Result<List<PdProductStock>> openingQuotation(String Barcode,String instrCode, Result<List<PdProductStock>> result) {
        if(Barcode!=null || instrCode!=null){
            Barcode = BarCodeUtil.trimStr(Barcode.toUpperCase());
            if(!"".equals(Barcode) || !"".equals(instrCode)){
                result.setCode(MessageConstant.ICODE_STATE_200);
                PdProductStock pdProductStock = new PdProductStock();
                pdProductStock.setRefBarCode(Barcode);
                //查询该条码是否开瓶
                pdProductStock.setProductFlag(PdConstant.PRODUCT_FLAG_1);//试剂
                pdProductStock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);//使用中
                List<PdProductStock> pdProductStocks = pdProductStockService.queryProductStockList(pdProductStock);
                if(pdProductStocks!=null && pdProductStocks.size()>0){
                    result.setCode(MessageConstant.ICODE_STATE_500);
                    result.setMessage("该试剂已被使用");
                }else{
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    LambdaQueryWrapper<PdProductStockUniqueCode> query = new LambdaQueryWrapper<PdProductStockUniqueCode>()
                            .eq(PdProductStockUniqueCode::getId, Barcode)
                            .eq(PdProductStockUniqueCode::getCodeState,PdConstant.CODE_PRINT_STATE_0);//正常状态不包括已退货和已用完的
                    // .eq(PdProductStockUniqueCode::getDepartId,sysUser.getCurrentDepartId());//当前科室下的
                    //查询状态是正常状态
                    PdProductStockUniqueCode pdProductStockUniqueCode = pdProductStockUniqueCodeService.getOne(query);
                    if(pdProductStockUniqueCode!=null){
                        PdProductStock ps = new PdProductStock();
                        ps.setId(pdProductStockUniqueCode.getProductStockId());
                        //查询该条码是否开瓶
                        ps.setProductFlag(PdConstant.PRODUCT_FLAG_1);//试剂
                        ps.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);//未使用
                        //查询该条码是否是试剂且未使用
                        List<PdProductStock> pds = pdProductStockService.queryProductStockList(ps);
                        if(pds!=null && pds.size()>0){
                            PdProductStock newPdProductStock = pds.get(0);
                            String productFlag= newPdProductStock.getProductFlag();
                            String departId= newPdProductStock.getDepartId();
                            if(!departId.equals(sysUser.getCurrentDepartId())){
                                result.setCode(MessageConstant.ICODE_STATE_500);
                                result.setMessage("该试剂不在当前部门库存中，请确认在开瓶");
                            }else {
                                newPdProductStock.setRefBarCode(Barcode);
                                PdProductStock productStock_i = pdProductStockTotalService.insertProdStock(newPdProductStock);
                                //开瓶记录数据插入
                                PdBottleInf bottleInf = new PdBottleInf();
                                bottleInf.setBoottleBy(sysUser.getRealname());//开瓶操作人
                                bottleInf.setBoottleDate(new Date());//开瓶时间
                                bottleInf.setRefBarCode(Barcode);//试剂对应条码
                                bottleInf.setStockId(productStock_i.getId());//对应库存明细ID
                                bottleInf.setRemarks("");//备注
                                bottleInf.setDepartId(sysUser.getCurrentDepartId());//所属部门
                                bottleInf.setDepartParentId(sysUser.getDepartParentId());//所属机构
                                bottleInf.setInstrCode(instrCode);//仪器设备code
                                pdBottleInfMapper.insert(bottleInf);
                                //批新条码表信息
                                PdProductStockUniqueCode productStockUniqueCode = new PdProductStockUniqueCode();
                                productStockUniqueCode.setId(Barcode);
                                productStockUniqueCode.setProductStockId(productStock_i.getId());
                                pdProductStockUniqueCodeService.updateById(productStockUniqueCode);
                            }
                        }else{
                            result.setCode(MessageConstant.ICODE_STATE_500);
                            result.setMessage("没有扫描到记录,请确认是否是试剂");
                        }
                    }else{
                        result.setCode(MessageConstant.ICODE_STATE_500);
                        result.setMessage("没有扫描到记录，该试剂可能已退货和已用完");
                    }
                }
            }else{
                result.setCode(MessageConstant.ICODE_STATE_500);
                result.setMessage("请扫描正确的条码");
            }
        }else{
            result.setCode(MessageConstant.ICODE_STATE_500);
            result.setMessage("请扫描正确的条码");
        }
        return result;
    }


    /**
     * 闭瓶扫码
     * @param Barcode
     * @param result
     * @return
     */
    @Override
    public Result<List<PdProductStock>> closeIngQuotation(String Barcode,String closeRemarks,String instrCode,Result<List<PdProductStock>> result) {
        if(Barcode!=null){
            Barcode = BarCodeUtil.trimStr(Barcode.toUpperCase());
            if(!"".equals(Barcode)){
                result.setCode(MessageConstant.ICODE_STATE_200);
                PdProductStock pdProductStock = new PdProductStock();
                pdProductStock.setRefBarCode(Barcode);
                //查询该条码是否已开瓶
                pdProductStock.setProductFlag(PdConstant.PRODUCT_FLAG_1);//试剂
                pdProductStock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_0);//使用中
                List<PdProductStock> pdProductStocks = pdProductStockService.queryProductStockList(pdProductStock);
                if(CollectionUtils.isEmpty(pdProductStocks)){
                    result.setCode(MessageConstant.ICODE_STATE_500);
                    result.setMessage("该试剂未开瓶或已闭瓶");
                }else{
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    LambdaQueryWrapper<PdProductStockUniqueCode> query = new LambdaQueryWrapper<PdProductStockUniqueCode>()
                            .eq(PdProductStockUniqueCode::getId, Barcode)
                            .eq(PdProductStockUniqueCode::getCodeState,PdConstant.CODE_PRINT_STATE_0);//正常状态不包括已退货和已用完的
                    //.eq(PdProductStockUniqueCode::getDepartId,sysUser.getCurrentDepartId());//当前科室下的
                    //查询状态是正常状态
                    PdProductStockUniqueCode pdProductStockUniqueCode = pdProductStockUniqueCodeService.getOne(query);
                    if(pdProductStockUniqueCode!=null){
                        PdProductStock newPdProductStock = pdProductStocks.get(0);
                        String departId=newPdProductStock.getDepartId();
                        if(!departId.equals(sysUser.getCurrentDepartId())){
                            result.setCode(MessageConstant.ICODE_STATE_500);
                            result.setMessage("该试剂不在当前部门库存中，请确认在闭瓶");
                        }else{
                            if(PdConstant.CLOSE_REMARKS_2.equals(closeRemarks)){//如果是试剂迁移设备使用
                                //开闭瓶记录数据更新闭瓶时间及操作人
                                PdBottleInf bottleInf = new PdBottleInf();
                                bottleInf.setRefBarCode(Barcode);
                                bottleInf.setFilterType("1");
                                PdBottleInf inf = pdBottleInfMapper.getOne(bottleInf);
                                if(StringUtils.isNotEmpty(inf.getInstrCode()) && inf.getInstrCode().equals(instrCode)){
                                    result.setCode(MessageConstant.ICODE_STATE_500);
                                    result.setMessage("该试剂已在当前仪器中使用，不需要迁移仪器");
                                }else {
                                    inf.setCloseDate(new Date());
                                    inf.setCloseBy(sysUser.getRealname());
                                    inf.setCloseRemarks(closeRemarks);
                                    pdBottleInfMapper.updateById(inf);
                                    //新增一条开瓶记录数据；
                                    PdBottleInf newBottleInf = new PdBottleInf();
                                    newBottleInf.setBoottleBy(sysUser.getRealname());
                                    newBottleInf.setBoottleDate(new Date());
                                    newBottleInf.setInstrCode(instrCode);
                                    newBottleInf.setRefBarCode(inf.getRefBarCode());
                                    newBottleInf.setStockId(inf.getStockId());
                                    newBottleInf.setSpecNum(inf.getSpecNum());
                                    pdBottleInfMapper.insert(newBottleInf);
                                }
                            }else {
                                newPdProductStock.setRefBarCode(Barcode);
                                PdProductStock productStock_i = pdProductStockTotalService.closeProdStock(newPdProductStock);
                                //开闭瓶记录数据更新闭瓶时间及操作人
                                PdBottleInf bottleInf = new PdBottleInf();
                                bottleInf.setRefBarCode(Barcode);
                                bottleInf.setFilterType("1");
                                PdBottleInf inf = pdBottleInfMapper.getOne(bottleInf);
                                inf.setCloseDate(new Date());
                                inf.setCloseBy(sysUser.getRealname());
                                inf.setCloseRemarks(closeRemarks);
                                pdBottleInfMapper.updateById(inf);
                                //批量更新条码状态
                                PdProductStockUniqueCode productStockUniqueCode = new PdProductStockUniqueCode();
                                productStockUniqueCode.setId(Barcode);
                                productStockUniqueCode.setCodeState(PdConstant.CODE_PRINT_STATE_2);
                                pdProductStockUniqueCodeService.updateById(productStockUniqueCode);
                            }
                        }
                    }else{
                        result.setCode(MessageConstant.ICODE_STATE_500);
                        result.setMessage("没有扫描到记录，该产品可能已退货和已用完");
                    }
                }
            }else{
                result.setCode(MessageConstant.ICODE_STATE_500);
                result.setMessage("请扫描正确的条码");
            }
        }else{
            result.setCode(MessageConstant.ICODE_STATE_500);
            result.setMessage("请扫描正确的条码");
        }
        return result;
    }

    /**
     * 唯一码扫码
     * @param Barcode
     * @param result
     * @return
     */
    @Override
    public Result<PdProductStock> uniqueScanCodeUrl(String Barcode,String  productFlag,String nestatStatus,Result<PdProductStock> result) {
        if(Barcode!=null){
            Barcode = BarCodeUtil.trimStr(Barcode.toUpperCase());
            if(!"".equals(Barcode)){
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                LambdaQueryWrapper<PdProductStockUniqueCode> query = new LambdaQueryWrapper<PdProductStockUniqueCode>()
                        .eq(PdProductStockUniqueCode::getId, Barcode)
                        .eq(PdProductStockUniqueCode::getPrintType, PdConstant.CODE_PRINT_TYPE_1)
                        .eq(PdProductStockUniqueCode::getCodeState,PdConstant.CODE_PRINT_STATE_0)//正常状态不包括已退货和已用完的
                        /* .eq(PdProductStockUniqueCode::getDepartId,sysUser.getCurrentDepartId())*/;//当前科室下的
                //查询状态是正常状态且是当前科室下的
                PdProductStockUniqueCode pdProductStockUniqueCode = pdProductStockUniqueCodeService.getOne(query);
                if(pdProductStockUniqueCode!=null){
                    PdProductStock ps = new PdProductStock();
                    ps.setId(pdProductStockUniqueCode.getProductStockId());
                    ps.setBarCodeType(PdConstant.CODE_PRINT_TYPE_1);
                    ps.setProductFlag(productFlag);
                    ps.setNestatStatus(nestatStatus);//状态
                    ps.setDepartId(sysUser.getCurrentDepartId());
                    //查询该条码是否是试剂且未使用
                    List<PdProductStock> pds = pdProductStockService.queryUniqueProductStockList(ps);
                    if(pds!=null && pds.size()>0){
                        ps = pds.get(0);
                        ps.setRefBarCode(Barcode);
                        result.setResult(ps);
                        result.setCode(MessageConstant.ICODE_STATE_200);
                        result.setMessage(MessageConstant.PACKAGE_CODE_MESSAGE_2);
                    }else{
                        result.setCode(MessageConstant.ICODE_STATE_500);
                        result.setMessage("没有扫描到记录，该产品不属于当前科室或正在使用中");
                    }
                }else{
                    result.setCode(MessageConstant.ICODE_STATE_500);
                    result.setMessage("没有扫描到记录，该产品可能不存在、已使用或已退货和已用完");
                }
            }else{
                result.setCode(MessageConstant.ICODE_STATE_500);
                result.setMessage("请扫描正确的条码");
            }
        }else{
            result.setCode(MessageConstant.ICODE_STATE_500);
            result.setMessage("请扫描正确的条码");
        }
        return result;
    }




    //批量更新产品收费代码
    @Override
    public void editChargeCodeBatch(String ids, String chargeCode) {
        PdProductMapper dao = sqlsession.getMapper(PdProductMapper.class);
        PdProduct pdProduct = new PdProduct();
        String[] idList = ids.split(",");
        for(int i = 0 ; i < idList.length ; i ++){
            pdProduct.setId(idList[i]);
            pdProduct.setChargeCode(chargeCode);
            dao.updateChargeCode(pdProduct);
        }
    }

    //校验产品编号是否唯
    @Override
    public List<PdProduct> selectList(PdProduct pdProduct) {
        return pdProductMapper.selectList(pdProduct);
    }

    @Override
    public List<PdProduct> verify(PdProduct pdProduct) {
        return pdProductMapper.verify(pdProduct);
    }

    //产品分类删除校验
    @Override
    public List<PdProduct> selectListByCT(PdProduct pdProduct) {
        return pdProductMapper.selectListByCT(pdProduct);
    }

    //产品分类删除校验
    @Override
    public List<PdProduct> selectListByCTs(Map<String,Object> map) {
        return pdProductMapper.selectListByCTs(map);
    }

    @Override
    public List<PdProduct> selectListByChargeCode(PdProduct entity) {
        return pdProductMapper.selectListByChargeCode(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> deleteV(String id) {
        try{
            PdPurchaseDetail pdPurchaseDetail = new PdPurchaseDetail();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            pdPurchaseDetail.setProductId(id);
            //查询采购记录
            List<PdPurchaseDetail> pdPurchaseDetails = pdPurchaseDetailService.queryPdPurchaseDetail(pdPurchaseDetail);
            if(CollectionUtils.isNotEmpty(pdPurchaseDetails)){
                return Result.error("删除失败!，当前产品被使用不能删除");
            }
            //查询入库记录
            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setProductId(id);
            pdStockRecordDetail.setDepartParentId(sysUser.getDepartParentId());
            List<PdStockRecordDetail> pdStockRecordDetails = pdStockRecordDetailService.queryPdStockRecordDetail(pdStockRecordDetail);
            if(CollectionUtils.isNotEmpty(pdStockRecordDetails)){
                return Result.error("删除失败!，当前产品被使用不能删除");
            }
            //查询定数包记录  TODO
            this.removeById(id);
            return Result.ok("删除成功!");
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("删除失败!，系统异常");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> deleteBatchV(String ids) {
        try{
            PdProductMapper dao = sqlsession.getMapper(PdProductMapper.class);
            List<String> idList = Arrays.asList(ids.split(","));
            if(idList!=null && idList.size()>0){
                boolean bl = true;
                for(String id : idList){
                    PdPurchaseDetail pdPurchaseDetail = new PdPurchaseDetail();
                    LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    pdPurchaseDetail.setProductId(id);
                    //查询采购记录
                    List<PdPurchaseDetail> pdPurchaseDetails = pdPurchaseDetailService.queryPdPurchaseDetail(pdPurchaseDetail);
                    if(CollectionUtils.isNotEmpty(pdPurchaseDetails)){
                        bl = false;
                        continue;
                    }
                    //查询入库记录
                    PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
                    pdStockRecordDetail.setProductId(id);
                    pdStockRecordDetail.setDepartParentId(sysUser.getDepartParentId());
                    List<PdStockRecordDetail> pdStockRecordDetails = pdStockRecordDetailService.queryPdStockRecordDetail(pdStockRecordDetail);
                    if(CollectionUtils.isNotEmpty(pdStockRecordDetails)){
                        bl = false;
                        continue;
                    }
                    //查询定数包记录  TODO
                    dao.deleteById(id);
                }
                if(bl){
                    return Result.ok("批量删除成功!");
                }else{
                    return Result.ok("部分删除成功，被使用的不能删除!");
                }
            }else{
                return Result.error("删除失败,参数不正确!");
            }
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("删除失败!，系统异常");
        }


    }

    /**
     * 判断产品编号是否禁用
     * @param pdProduct
     * @return
     */
    @Override
    public Result<Object> isDisabledNumber(PdProduct pdProduct) {
        if(oConvertUtils.isEmpty(pdProduct.getId())){
            return Result.ok();
        }
        //查询入库记录
        PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
        pdStockRecordDetail.setProductId(pdProduct.getId());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecordDetail.setDepartParentId(sysUser.getDepartParentId());
        List<PdStockRecordDetail> pdStockRecordDetails = pdStockRecordDetailService.queryPdStockRecordDetail(pdStockRecordDetail);
        if(CollectionUtils.isNotEmpty(pdStockRecordDetails)){
            return Result.error("");
        }else{
            return Result.ok();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> importExcel(Map<String, MultipartFile> fileMap) {
        PdProductMapper dao = sqlsession.getMapper(PdProductMapper.class);
        PdVenderMapper venderDao = sqlsession.getMapper(PdVenderMapper.class);
        PdSupplierMapper supplierDao = sqlsession.getMapper(PdSupplierMapper.class);
        PdUnitMapper unitDao = sqlsession.getMapper(PdUnitMapper.class);
        //PdCategoryMapper pdCategoryDao = sqlsession.getMapper(PdCategoryMapper.class);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdProduct> list = new ArrayList<>();
        boolean bl = true;
        String message = "表格内没有内容";
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                list = ExcelImportUtil.importExcel(file.getInputStream(), PdProduct.class, params);
                int i = 0;
                //查询所有的产品
                PdProduct pdProduct = new PdProduct();
                pdProduct.setDepartParentId(sysUser.getDepartParentId());
                List<PdProduct> pdProducts = this.selectList(pdProduct);
                for(PdProduct ps :list){ //校验产品编号是否唯
                    ps.setProductFlag(PdConstant.PRODUCT_FLAG_0);
                    //校验产品编号
                    if(oConvertUtils.isEmpty(ps.getNumber())){
                        String number = UUIDUtil.generateNumber("93");
                        ps.setNumber(number.substring(0,number.length()-2)+i);//生成编码
                    }else{
                        if(checkNumber(pdProducts,ps)){
                            pdProducts.add(ps);
                        }else{
                            message = "导入失败,第"+(i+1)+"行产品编号不能重复";
                            bl = false;
                            break;
                        }
                    }
                    //校验产品名称
                    if(oConvertUtils.isEmpty(ps.getName())){
                        message = "导入失败,第"+(i+1)+"行产品名称不能为空";
                        bl = false;
                        break;
                    }else{
                        if(oConvertUtils.isEmpty(ps.getPy())){
                            //生成拼音简码
                            ps.setPy(JmUtil.getAllFirstLetter(ps.getName()));
                        }
                        if(oConvertUtils.isEmpty(ps.getWb())){
                            //生成五笔简码
                            ps.setWb(JmUtil.getWBCode(ps.getName()));
                        }
                    }
                    //产品单位转换成id
                    if(oConvertUtils.isNotEmpty(ps.getUnitName())){
                        PdUnit pdUnit = new PdUnit();
                        pdUnit.setDepartParentId(sysUser.getDepartParentId());
                        pdUnit.setName(ps.getUnitName());
                        pdUnit.setUnitType(PdConstant.PRODUCT_UNIT_TYPE_0);
                        List<PdUnit> units = unitDao.verify(pdUnit);
                        if(units.size()==1){
                            ps.setUnitId(units.get(0).getId());
                        }else{
                            message = "导入失败,第"+(i+1)+"行产品单位填写错误";
                            bl = false;
                            break;
                        }
                    }else{
                        message = "导入失败,第"+(i+1)+"行产品单位不存在";
                        bl = false;
                        break;
                    }
                    //校验规格
                    if(oConvertUtils.isEmpty(ps.getSpec())){
                        message = "导入失败,第"+(i+1)+"行规格不能为空";
                        bl = false;
                        break;
                    }
                    //产品生产厂家转换成id
                    if(oConvertUtils.isNotEmpty(ps.getVenderName())){
                        PdVender pdVender = new PdVender();
                        pdVender.setDepartParentId(sysUser.getDepartParentId());
                        pdVender.setName(ps.getVenderName());
                        List<PdVender> venders = venderDao.verify(pdVender);
                        if(venders.size()==1){
                            ps.setVenderId(venders.get(0).getId());
                        }else{
                            message = "导入失败,第"+(i+1)+"行生产厂家填写错误";
                            bl = false;
                            break;
                        }
                    }else{
                        message = "导入失败,第"+(i+1)+"行生产厂家不存在";
                        bl = false;
                        break;
                    }
                    //产品供应商转换成id
                    if(oConvertUtils.isNotEmpty(ps.getSupplierName())){
                        PdSupplier pdSupplier = new PdSupplier();
                        pdSupplier.setDepartParentId(sysUser.getDepartParentId());
                        pdSupplier.setName(ps.getSupplierName());
                        List<PdSupplier> pdSuppliers = supplierDao.verify(pdSupplier);
                        if(pdSuppliers.size()==1){
                            ps.setSupplierId(pdSuppliers.get(0).getId());
                        }else{
                            message = "导入失败,第"+(i+1)+"行供应商填写错误";
                            bl = false;
                            break;
                        }
                    }
                    //校验注册证
                    if(oConvertUtils.isEmpty(ps.getRegistration())){
                        message = "导入失败,第"+(i+1)+"行注册证不能为空";
                        bl = false;
                        break;
                    }
                    //校验是否计费
                    if(!checksStr(ps.getIsCharge())){
                        message = "导入失败,第"+(i+1)+"行是否计费不能为空或填写错误";
                        bl = false;
                        break;
                    }
                    //校验产品收费代码是否填写
                    if("0".equals(ps.getIsCharge()) && oConvertUtils.isEmpty(ps.getChargeCode())){
                        message = "导入失败,第"+(i+1)+"行没有填写产品收费代码";
                        bl = false;
                        break;
                    }
                    //校验是否紧急产品
                    if(!checksStr(ps.getIsUrgent())){
                        message = "导入失败,第"+(i+1)+"行是否紧急产品不能为空或填写错误";
                        bl = false;
                        break;
                    }
                    //校验产品进价
                    if(!isMoney(ps.getPurchasePrice())){
                        message = "导入失败,第"+(i+1)+"行产品进价格式不正确";
                        bl = false;
                        break;
                    }
                    //校验产品出价
                    if(!isMoney(ps.getSellingPrice())){
                        message = "导入失败,第"+(i+1)+"行产品出价格式不正确";
                        bl = false;
                        break;
                    }
                    //校验产品权限
                    if(!checksStr(ps.getPower())){
                        message = "导入失败,第"+(i+1)+"行产品权限不能为空或填写错误";
                        bl = false;
                        break;
                    }
                    //校验器械分类
                    if(!checkDeviceClassification(ps.getDeviceClassification())){
                        message = "导入失败,第"+(i+1)+"行器械分类不能为空或填写错误";
                        bl = false;
                        break;
                    }
                    //一级分类
                   /* if(ps.getCategoryOneName()!=null && !"".equals(ps.getCategoryOneName())){
                        LambdaQueryWrapper<PdCategory> query = new LambdaQueryWrapper<PdCategory>()
                                .eq(PdCategory::getName, ps.getCategoryOneName());
                        PdCategory pdCategory = pdCategoryDao.selectOne(query);
                        if(pdCategory!=null ){
                            ps.setCategoryOne(pdCategory.getId());
                        }
                    }
                    //二级分类
                    if(ps.getCategoryTwoName()!=null && !"".equals(ps.getCategoryTwoName())){
                        LambdaQueryWrapper<PdCategory> query = new LambdaQueryWrapper<PdCategory>()
                                .eq(PdCategory::getName, ps.getCategoryTwoName());
                        PdCategory pdCategory = pdCategoryDao.selectOne(query);
                        if(pdCategory!=null ){
                            ps.setCategoryTwo(pdCategory.getId());
                        }
                    }*/
                    ps.setValidityFlag(PdConstant.PD_STATE_0);
                    i ++;
                }

            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("文件导入失败:"+e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //全部正确才能导入
        if(bl && list.size()>0){
            this.saveBatch(list);
            return Result.ok("文件导入成功");
        }else{
            return Result.error("文件导入失败:"+message);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Object> importExcelReagents(Map<String, MultipartFile> fileMap) {
        PdProductMapper dao = sqlsession.getMapper(PdProductMapper.class);
        PdVenderMapper venderDao = sqlsession.getMapper(PdVenderMapper.class);
        PdSupplierMapper supplierDao = sqlsession.getMapper(PdSupplierMapper.class);
        PdUnitMapper unitDao = sqlsession.getMapper(PdUnitMapper.class);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdProductReagents> pdProductReagentsList = new ArrayList<>();
        List<PdProduct> list = new ArrayList<>();
        boolean bl = true;
        String message = "表格内没有内容";
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                pdProductReagentsList = ExcelImportUtil.importExcel(file.getInputStream(), PdProductReagents.class, params);
                list = JSON.parseArray(JSON.toJSONString(pdProductReagentsList), PdProduct.class);
                int i = 0;
                //查询所有的产品
                PdProduct pdProduct = new PdProduct();
                pdProduct.setDepartParentId(sysUser.getDepartParentId());
                List<PdProduct> pdProducts = this.selectList(pdProduct);
                for(PdProduct ps :list){ //校验产品编号是否唯
                    ps.setProductFlag(PdConstant.PRODUCT_FLAG_1);
                    //校验产品编号
                    if(oConvertUtils.isEmpty(ps.getNumber())){
                        String number = UUIDUtil.generateNumber("93");
                        ps.setNumber(number.substring(0,number.length()-2)+i);//生成编码
                    }else{
                        if(checkNumber(pdProducts,ps)){
                            pdProducts.add(ps);
                        }else{
                            message = "导入失败,第"+(i+1)+"行产品编号不能重复";
                            bl = false;
                            break;
                        }
                    }
                    //校验产品名称
                    if(oConvertUtils.isEmpty(ps.getName())){
                        message = "导入失败,第"+(i+1)+"行产品名称不能为空";
                        bl = false;
                        break;
                    }
                    //包装单位转换成id
                    if(oConvertUtils.isNotEmpty(ps.getUnitName())){
                        PdUnit pdUnit = new PdUnit();
                        pdUnit.setDepartParentId(sysUser.getDepartParentId());
                        pdUnit.setName(ps.getUnitName());
                        pdUnit.setUnitType(PdConstant.PRODUCT_UNIT_TYPE_0);
                        List<PdUnit> units = unitDao.verify(pdUnit);
                        if(units.size()==1){
                            ps.setUnitId(units.get(0).getId());
                        }else{
                            message = "导入失败,第"+(i+1)+"行产品单位填写错误";
                            bl = false;
                            break;
                        }
                    }else{
                        message = "导入失败,第"+(i+1)+"行产品单位不存在";
                        bl = false;
                        break;
                    }
                    //规格单位转换成id
                    if(oConvertUtils.isNotEmpty(ps.getSpecUnitName())){
                        PdUnit pdUnit = new PdUnit();
                        pdUnit.setDepartParentId(sysUser.getDepartParentId());
                        pdUnit.setName(ps.getSpecUnitName());
                        pdUnit.setUnitType(PdConstant.PRODUCT_UNIT_TYPE_1);
                        List<PdUnit> units = unitDao.verify(pdUnit);
                        if(units.size()==1){
                            ps.setSpecUnitId(units.get(0).getId());
                        }else{
                            message = "导入失败,第"+(i+1)+"行产品规格单位填写错误";
                            bl = false;
                            break;
                        }
                    }else{
                        message = "导入失败,第"+(i+1)+"行产品规格单位不存在";
                        bl = false;
                        break;
                    }
                    //校验规格数量
                    if(!checkDouble(ps.getSpecQuantity())){
                        message = "导入失败,第"+(i+1)+"行规格数量格式不正确";
                        bl = false;
                        break;
                    }
                    //校验规格
                    if(oConvertUtils.isEmpty(ps.getSpec())){
                        message = "导入失败,第"+(i+1)+"行规格不能为空";
                        bl = false;
                        break;
                    }
                    //产品生产厂家转换成id
                    if(oConvertUtils.isNotEmpty(ps.getVenderName())){
                        PdVender pdVender = new PdVender();
                        pdVender.setDepartParentId(sysUser.getDepartParentId());
                        pdVender.setName(ps.getVenderName());
                        List<PdVender> venders = venderDao.verify(pdVender);
                        if(venders.size()==1){
                            ps.setVenderId(venders.get(0).getId());
                        }else{
                            message = "导入失败,第"+(i+1)+"行生产厂家填写错误";
                            bl = false;
                            break;
                        }
                    }else{
                        message = "导入失败,第"+(i+1)+"行生产厂家不存在";
                        bl = false;
                        break;
                    }
                    //产品供应商转换成id
                    if(oConvertUtils.isNotEmpty(ps.getSupplierName())){
                        PdSupplier pdSupplier = new PdSupplier();
                        pdSupplier.setDepartParentId(sysUser.getDepartParentId());
                        pdSupplier.setName(ps.getSupplierName());
                        List<PdSupplier> pdSuppliers = supplierDao.verify(pdSupplier);
                        if(pdSuppliers.size()==1){
                            ps.setSupplierId(pdSuppliers.get(0).getId());
                        }else{
                            message = "导入失败,第"+(i+1)+"行供应商填写错误";
                            bl = false;
                            break;
                        }
                    }
                    //校验注册证
                    if(oConvertUtils.isEmpty(ps.getRegistration())){
                        message = "导入失败,第"+(i+1)+"行注册证不能为空";
                        bl = false;
                        break;
                    }
                    //校验是否计费
                    if(!checksStr(ps.getIsCharge())){
                        message = "导入失败,第"+(i+1)+"行是否计费不能为空或填写错误";
                        bl = false;
                        break;
                    }
                    //校验产品收费代码是否填写
                    if("0".equals(ps.getIsCharge()) && oConvertUtils.isEmpty(ps.getChargeCode())){
                        message = "导入失败,第"+(i+1)+"行没有填写产品收费代码";
                        bl = false;
                        break;
                    }
                    //校验是否紧急产品
                    if(!checksStr(ps.getIsUrgent())){
                        message = "导入失败,第"+(i+1)+"行是否紧急产品不能为空或填写错误";
                        bl = false;
                        break;
                    }
                    //校验产品进价
                    if(!isMoney(ps.getPurchasePrice())){
                        message = "导入失败,第"+(i+1)+"行产品进价格式不正确";
                        bl = false;
                        break;
                    }
                    //校验产品出价
                    if(!isMoney(ps.getSellingPrice())){
                        message = "导入失败,第"+(i+1)+"行产品出价格式不正确";
                        bl = false;
                        break;
                    }
                    //校验产品权限
                    if(!checksStr(ps.getPower())){
                        message = "导入失败,第"+(i+1)+"行产品权限不能为空或填写错误";
                        bl = false;
                        break;
                    }
                    //校验器械分类
                    if(!checkDeviceClassification(ps.getDeviceClassification())){
                        message = "导入失败,第"+(i+1)+"行器械分类不能为空或填写错误";
                        bl = false;
                        break;
                    }
                    ps.setValidityFlag(PdConstant.PD_STATE_0);
                    i ++;
                }

            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("文件导入失败:"+e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //全部正确才能导入
        if(bl && list.size()>0){
            this.saveBatch(list);
            return Result.ok("文件导入成功");
        }else{
            return Result.error("文件导入失败:"+message);
        }
    }
    /**
     * 校验是否计费和是否紧急产品
     * @param str
     * @return
     */
    private boolean checksStr(String str){
        if(oConvertUtils.isEmpty(str)){
            return false;
        }
        String isCharge = str.trim();
        if("0".equals(isCharge)){
            return true;
        }else if("1".equals(isCharge)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 校验器械分类
     * @param str
     * @return
     */
    private boolean checkDeviceClassification(String str){
        if(oConvertUtils.isEmpty(str)){
            return false;
        }
        String isCharge = str.trim();
        if("0".equals(isCharge)){
            return true;
        }else if("1".equals(isCharge)){
            return true;
        }else if("2".equals(isCharge)){
            return true;
        }else if("3".equals(isCharge)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 校验金额是否正确
     * @param purPrice
     * @return
     */
    public static boolean isMoney(BigDecimal purPrice) {
        if(purPrice!=null){
            Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,4})?$"); // 判断小数点后4位的数字的正则表达式
            Matcher match=pattern.matcher(purPrice.toString());
            if(match.matches()==false) {
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }

    }

    //校验是否是数字类型
    public static boolean checkDouble(Double num){
        if(num ==null){
            return false;
        }else if(num>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 校验编号是否重复
     * @param pdProducts
     * @param pdProduct
     * @return
     */
    private boolean checkNumber(List<PdProduct> pdProducts, PdProduct pdProduct) {
        for (PdProduct p : pdProducts) {
            if(p.getNumber().equals(pdProduct.getNumber().trim())){
                return false;
            }
        }
        return true;
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
    @Override
    public void updateValidityFlag(PdProduct pdProduct){
        pdProductMapper.updateValidityFlag(pdProduct);
    }

}
