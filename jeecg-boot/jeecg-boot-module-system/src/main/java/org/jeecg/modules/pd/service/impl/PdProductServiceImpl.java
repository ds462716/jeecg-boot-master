package org.jeecg.modules.pd.service.impl;

import com.aliyuncs.regions.ProductDomain;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.MessageConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.*;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.BarCodeUtil;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
            pdProductStock.setDepartId(sysUser.getCurrentDepartId());
            pdProductStocks = pdProductStockService.selectList(pdProductStock);
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
                        //查询产品是否存在
                        List<PdProduct> pdProducts = pdProductMapper.selectList(pdProduct);
                        if(pdProducts!=null && pdProducts.size()>0){
                            pdProduct = pdProducts.get(0);
                            //根据产品id直接查询库存
                            pdProductStock = new PdProductStock();
                            pdProductStock.setProductId(pdProduct.getId());
                            pdProductStock.setDepartId(sysUser.getCurrentDepartId());
                            //根据条件查询库存
                            pdProductStocks = pdProductStockService.selectList(pdProductStock);
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
                            //查询产品是否存在
                            List<PdProduct> pdProducts = pdProductMapper.selectList(pdProduct);
                            if(pdProducts!=null && pdProducts.size()>0){
                                pdProduct = pdProducts.get(0);
                                //根据产品id直接查询库存
                                pdProductStock = new PdProductStock();
                                pdProductStock.setProductId(pdProduct.getId());
                                pdProductStock.setDepartId(sysUser.getCurrentDepartId());
                                //根据条件查询库存
                                pdProductStocks = pdProductStockService.selectList(pdProductStock);
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
                    //根据条件查询库存
                    pdProductStocks = pdProductStockService.selectList(pdProductStock);
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
        return  result;

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
        PdUnitMapper unitDao = sqlsession.getMapper(PdUnitMapper.class);
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
                    //校验产品编号
                    if(oConvertUtils.isEmpty(ps.getNumber())){
                        ps.setNumber(UUIDUtil.generateNumber("93"));//生成编码
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
                    //产品单位转换成id
                    if(oConvertUtils.isNotEmpty(ps.getUnitName())){
                        PdUnit pdUnit = new PdUnit();
                        pdUnit.setDepartParentId(sysUser.getDepartParentId());
                        pdUnit.setName(ps.getUnitName());
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
     * @param pdProduct
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
