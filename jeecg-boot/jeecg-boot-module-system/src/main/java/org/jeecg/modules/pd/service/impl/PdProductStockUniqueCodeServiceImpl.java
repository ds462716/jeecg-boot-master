package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockUniqueCode;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdProductStockUniqueCodeMapper;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockUniqueCodeService;
import org.jeecg.modules.pd.service.IPdStockRecordDetailService;
import org.jeecg.modules.pd.util.SnowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 库存关联条码表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Service
public class PdProductStockUniqueCodeServiceImpl extends ServiceImpl<PdProductStockUniqueCodeMapper, PdProductStockUniqueCode> implements IPdProductStockUniqueCodeService {

    @Autowired
    private IPdProductStockService pdProductStockService;
    @Autowired
    private IPdStockRecordDetailService pdStockRecordDetailService;
    /**
     * 生成唯一码并保存
     * @param pdProductStockUniqueCode
     * @return
     */
    @Transactional
    @Override
    public Result<List<PdProductStockUniqueCode>> uniqueCodeGeneration(PdProductStockUniqueCode pdProductStockUniqueCode) {
        Result<List<PdProductStockUniqueCode>> result =  new Result<>();
        List<PdProductStockUniqueCode> pdProductStockUniqueCodeList = new ArrayList<>();
        //校验 如果有未审核的出库单则不能打印需要先删除出库单
        PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
        pdStockRecordDetail.setProductStockId(pdProductStockUniqueCode.getProductStockId());
        List<PdStockRecordDetail> pdStockRecordDetails  = pdStockRecordDetailService.selectListForRefBarCodeCheck(pdStockRecordDetail);
        if(pdStockRecordDetails!=null && pdStockRecordDetails.size()>0){
            result.setResult(new ArrayList<>());
            result.setCode(500);
            result.setMessage("有未审核的出库单存在，请先操作该出库单再打印");
            return result;
        }else{
            PdProductStock pdProductStock = new PdProductStock();
            pdProductStock.setId(pdProductStockUniqueCode.getProductStockId());
            List<PdProductStock> pdProductStocks = pdProductStockService.selectList(pdProductStock);
            if(pdProductStocks!=null && pdProductStocks.size()>0){
                pdProductStock = pdProductStocks.get(0);
                LambdaQueryWrapper<PdProductStockUniqueCode> query = new LambdaQueryWrapper<>();
                query.eq(PdProductStockUniqueCode::getProductStockId, pdProductStock.getId());
                query.eq(PdProductStockUniqueCode::getPrintType, PdConstant.CODE_PRINT_TYPE_1);//唯一码
                //如果存在区间则只查询区间内的
                if(pdProductStockUniqueCode.getStartOrder()!=null && pdProductStockUniqueCode.getEndOrder()!=null){
                    query.between(PdProductStockUniqueCode::getUniqueCodeOrder,pdProductStockUniqueCode.getStartOrder(),pdProductStockUniqueCode.getEndOrder());
                }
                pdProductStockUniqueCodeList = this.list(query);
                if(pdProductStockUniqueCodeList!=null && pdProductStockUniqueCodeList.size()>0){
                    //重复打条码
                    for(PdProductStockUniqueCode psc :pdProductStockUniqueCodeList){
                        psc.setProductName(pdProductStock.getProductName());
                        psc.setExpDate(pdProductStock.getExpDate());
                        psc.setBatchNo(pdProductStock.getBatchNo());
                        psc.setProductFlagName(pdProductStock.getProductFlagName());
                    }
                }else{
                    //判断是否已经打印了普通码
                    if(!oConvertUtils.isEmpty(pdProductStock.getRefBarCode())){
                        result.setResult(new ArrayList<>());
                        result.setCode(202);
                        result.setMessage("已经生成了普通码，不能生成新的条码，如需生成新条码请删除原有条码记录");
                        return result;
                    }
                    //第一次打印条码
                    if(pdProductStock.getStockNum()>0){
                        int index = pdProductStock.getStockNum().intValue();
                        for(int i=1;i<=index;i++){
                            PdProductStockUniqueCode psc = new PdProductStockUniqueCode();
                            psc.setId(SnowUtils.onlyBigKey(i));
                            psc.setProductStockId(pdProductStock.getId());
                            psc.setPrintType(PdConstant.CODE_PRINT_TYPE_1);//唯一码
                            psc.setCodeState(PdConstant.CODE_PRINT_STATE_0);//正常状态
                            psc.setUniqueCodeOrder(i);
                            psc.setProductName(pdProductStock.getProductName());
                            psc.setExpDate(pdProductStock.getExpDate());
                            psc.setBatchNo(pdProductStock.getBatchNo());
                            psc.setProductFlagName(pdProductStock.getProductFlagName());
                            pdProductStockUniqueCodeList.add(psc);
                        }
                        this.saveBatch(pdProductStockUniqueCodeList);
                        PdProductStock ps = new PdProductStock();
                        ps.setId(pdProductStockUniqueCode.getProductStockId());
                        ps.setBarCodeType(PdConstant.CODE_PRINT_TYPE_1);
                        //更新库存表的条码状态
                        pdProductStockService.updateStockBarCodeType(ps);
                        //如果存在区间则只查询区间内的
                        if(pdProductStockUniqueCode.getStartOrder()!=null && pdProductStockUniqueCode.getEndOrder()!=null){
                            LambdaQueryWrapper<PdProductStockUniqueCode> queryQ = new LambdaQueryWrapper<>();
                            queryQ.between(PdProductStockUniqueCode::getUniqueCodeOrder,pdProductStockUniqueCode.getStartOrder(),pdProductStockUniqueCode.getEndOrder());
                            queryQ.eq(PdProductStockUniqueCode::getProductStockId, pdProductStock.getId());
                            pdProductStockUniqueCodeList = this.list(queryQ);
                            if(pdProductStockUniqueCodeList!=null && pdProductStockUniqueCodeList.size()>0){
                                //重复打条码
                                for(PdProductStockUniqueCode psc :pdProductStockUniqueCodeList){
                                    psc.setProductName(pdProductStock.getProductName());
                                    psc.setExpDate(pdProductStock.getExpDate());
                                    psc.setBatchNo(pdProductStock.getBatchNo());
                                    psc.setProductFlagName(pdProductStock.getProductFlagName());
                                }
                            }else{
                                result.setResult(new ArrayList<>());
                                result.setCode(500);
                                result.setMessage("没有查询到内容");
                                return result;
                            }
                        }
                    }else{
                        result.setResult(new ArrayList<>());
                        result.setCode(500);
                        result.setMessage("库存数量不足");
                        return result;
                    }
                }
            }

        }
        result.setCode(200);
        result.setResult(pdProductStockUniqueCodeList);
        return result;
    }
    /**
     * 批量生成条码
     * @param asList
     * @return
     */
    @Transactional
    @Override
    public Result<List<PdProductStockUniqueCode>> batchCodeGeneration(List<String> asList) {
        Result<List<PdProductStockUniqueCode>> result =  new Result<>();
        PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
        pdStockRecordDetail.setProductStockIdList(asList);
        List<PdStockRecordDetail> pdStockRecordDetails  = pdStockRecordDetailService.selectListForRefBarCodeCheck(pdStockRecordDetail);
        if(pdStockRecordDetails!=null && pdStockRecordDetails.size()>0){
            result.setResult(new ArrayList<>());
            result.setCode(500);
            result.setMessage("有未审核的出库单存在，请先操作该出库单再打印");
            return result;
        }else{
            PdProductStock pdProductStock = new PdProductStock();
            pdProductStock.setStockIdList(asList);
            List<PdProductStock> pdProductStocks = pdProductStockService.selectList(pdProductStock);
            List<PdProductStockUniqueCode> pdProductStockUniqueCodeList = new ArrayList<>();
            //更新refBarCode
            List<PdProductStock> pdProductStockList = new ArrayList<>();
            boolean flag = true;
            if(pdProductStocks!=null && pdProductStocks.size()>0){
                //判断是否全部通过
                for(PdProductStock ps :pdProductStocks){
                    if(!oConvertUtils.isEmpty(ps.getRefBarCode())){
                        //查询是否已经存在
                        LambdaQueryWrapper<PdProductStockUniqueCode> query = new LambdaQueryWrapper<>();
                        query.eq(PdProductStockUniqueCode::getId, ps.getRefBarCode());
                        PdProductStockUniqueCode psucs = this.getOne(query);
                        //重复打条码
                        psucs.setProductName(ps.getProductName());
                        psucs.setProductFlagName(ps.getProductFlagName());
                        psucs.setExpDate(ps.getExpDate());
                        psucs.setBatchNo(ps.getBatchNo());
                        pdProductStockUniqueCodeList.add(psucs);
                    }else{
                        //判断是否已经打印了唯一码
                        LambdaQueryWrapper<PdProductStockUniqueCode> wyquery = new LambdaQueryWrapper<>();
                        wyquery.eq(PdProductStockUniqueCode::getProductStockId, ps.getId());
                        wyquery.eq(PdProductStockUniqueCode::getPrintType, PdConstant.CODE_PRINT_TYPE_1);//唯一码
                        List<PdProductStockUniqueCode> wypsucs = this.list(wyquery);
                        if(wypsucs!=null && wypsucs.size()>0){
                            flag = false;
                            continue;
                        }
                        //第一次打印条码
                        if(ps.getStockNum()>0){
                            PdProductStockUniqueCode psc = new PdProductStockUniqueCode();
                            psc.setId(SnowUtils.onlyBigKey(0));//为了看条码立马能区别是唯一码还是普通条码
                            psc.setProductStockId(ps.getId());
                            psc.setPrintType(PdConstant.CODE_PRINT_TYPE_0);//普通码
                            psc.setCodeState(PdConstant.CODE_PRINT_STATE_0);//正常状态
                            psc.setUniqueCodeOrder(1);
                            psc.setProductName(ps.getProductName());
                            psc.setProductFlagName(ps.getProductFlagName());
                            psc.setExpDate(ps.getExpDate());
                            psc.setBatchNo(ps.getBatchNo());
                            this.save(psc);
                            pdProductStockUniqueCodeList.add(psc);
                            //生成条码后把打印的条码值更新到库存明细表（pd_product_stock）的ref_bar_code中
                            //更新库存明细表的状态
                            PdProductStock productStock = new PdProductStock();
                            productStock.setId(ps.getId());
                            productStock.setRefBarCode(psc.getId());
                            //更新库存表的条码状态
                            pdProductStockList.add(productStock);
                        }else{
                            result.setResult(new ArrayList<>());
                            result.setCode(500);
                            result.setMessage("库存数量不足");
                            return result;
                        }
                    }
                }
                //生成条码后把打印的条码值更新到库存明细表（pd_product_stock）的ref_bar_code中
                if(pdProductStockList!=null && pdProductStockList.size()>0){
                    pdProductStockService.updateBatchById(pdProductStockList);
                }
            }
            if(!flag){
                result.setCode(202);
                result.setMessage("部分已经生成了唯一码，不能生成新的条码，如需生成新条码请删除原有条码记录");
            }else{
                result.setCode(200);
                result.setMessage("生成条码成功");
            }
            result.setResult(pdProductStockUniqueCodeList);
            return result;
        }
    }

    /**
     * 清除条码
     * @param id
     */
    @Transactional
    @Override
    public Result<Object> deleteCode(String id) {
        //查询是否已经存在
        LambdaQueryWrapper<PdProductStock> query = new LambdaQueryWrapper<>();
        query.eq(PdProductStock::getId,id);
        query.eq(PdProductStock::getNestatStatus,PdConstant.STOCK_NESTAT_STATUS_1);
        PdProductStock psk = pdProductStockService.getOne(query);
        if(!oConvertUtils.isEmpty(psk)){
            //校验 如果有未审核的出库单则不能打印需要先删除出库单
            PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
            pdStockRecordDetail.setProductStockId(psk.getId());
            List<PdStockRecordDetail> pdStockRecordDetails  = pdStockRecordDetailService.selectListForRefBarCodeCheck(pdStockRecordDetail);
            if(pdStockRecordDetails!=null && pdStockRecordDetails.size()>0){
                return Result.error("有未审核的出库单存在，请先操作该出库单再清除");
            }
            //如果是唯一码清除
            if(PdConstant.CODE_PRINT_TYPE_1.equals(psk.getBarCodeType())){
                LambdaQueryWrapper<PdProductStockUniqueCode> queryC = new LambdaQueryWrapper<>();
                // 封装查询条件parentId为主键,
                queryC.eq(PdProductStockUniqueCode::getProductStockId, id);
                this.remove(queryC);
            }/*else{
            //不需要校验直接清除生成新条码
            if(!oConvertUtils.isEmpty(psk.getRefBarCode())){
                //查询条码是不是已经被使用
                PdProductStockUniqueCode pdProductStockUniqueCode = new PdProductStockUniqueCode();
                pdProductStockUniqueCode.setId(psk.getRefBarCode());
                List<PdProductStock> pdProductStockUniqueCodes = this.selectListByGroup(pdProductStockUniqueCode);
                if(pdProductStockUniqueCodes!=null && pdProductStockUniqueCodes.size()>1){
                    return Result.error("产品已经发往其他科室使用，不能删除。如需删除请全部退回!");
                }
            }
        }*/
            //更新库存明细表的状态
            PdProductStock ps = new PdProductStock();
            ps.setId(id);
            ps.setBarCodeType(PdConstant.CODE_PRINT_TYPE_0);
            ps.setRefBarCode("");//清空库存表中的条码
            //更新库存表的条码状态
            pdProductStockService.updateStockBarCodeType(ps);
            return Result.ok("清除成功");
        }else{
            return Result.error("该产品已使用或者已用完");
        }
    }

    @Override
    public Page<PdProductStockUniqueCode> selectList(Page<PdProductStockUniqueCode> page, PdProductStockUniqueCode pdProductStockUniqueCode) {
        return baseMapper.selectListByPage(page,pdProductStockUniqueCode);
    }

    @Override
    public List<PdProductStockUniqueCode> selectList(PdProductStockUniqueCode pdProductStockUniqueCode) {
        return baseMapper.selectListOne(pdProductStockUniqueCode);
    }

    @Override
    public List<PdProductStock> selectListByGroup(PdProductStockUniqueCode pdProductStockUniqueCode) {
        return baseMapper.selectListByGroup(pdProductStockUniqueCode);
    }

    /**
     * 批量更新条码打印次数
     * @param pdProductStockUniqueCodes
     */
    @Override
    public void updatePrintNum(List<PdProductStockUniqueCode> pdProductStockUniqueCodes) {
        if(pdProductStockUniqueCodes!=null && pdProductStockUniqueCodes.size()>0){
            List<String> ids = new ArrayList<>();
            for(PdProductStockUniqueCode pdProductStockUniqueCode :pdProductStockUniqueCodes){
                ids.add(pdProductStockUniqueCode.getId());
            }
            baseMapper.updatePrintNum(ids);
        }
    }

    @Override
    public Page<PdProductStockUniqueCode> findList(Page<PdProductStockUniqueCode> page, PdProductStockUniqueCode pdProductStockUniqueCode) {
        Page<PdProductStockUniqueCode> p = new Page<>();
        //查询是否已经存在
        PdProductStock pdProductStock = new PdProductStock();
        pdProductStock.setId(pdProductStockUniqueCode.getProductStockId());
        List<PdProductStock> pdProductStocks = pdProductStockService.selectList(pdProductStock);
        if(pdProductStocks!=null && pdProductStocks.size()>0){
            PdProductStock psk = pdProductStocks.get(0);
            if(PdConstant.CODE_PRINT_TYPE_1.equals(psk.getBarCodeType())){
                pdProductStockUniqueCode.setPrintType(PdConstant.CODE_PRINT_TYPE_1);
                return baseMapper.selectListByPage(page,pdProductStockUniqueCode);
            }else{
                if(psk.getRefBarCode()!=null && !"".equals(psk.getRefBarCode())){
                    List<PdProductStockUniqueCode> pdProductStockUniqueCodes = new ArrayList<>();
                    LambdaQueryWrapper<PdProductStockUniqueCode> query1 = new LambdaQueryWrapper<>();
                    query1.eq(PdProductStockUniqueCode::getId,psk.getRefBarCode());
                    PdProductStockUniqueCode pskq = this.getOne(query1);
                    if(pskq!=null){
                        pskq.setProductName(pdProductStock.getProductName());
                        pskq.setExpDate(pdProductStock.getExpDate());
                        pskq.setBatchNo(pdProductStock.getBatchNo());
                        pdProductStockUniqueCodes.add(pskq);
                        p.setRecords(pdProductStockUniqueCodes);
                        p.setTotal(1);
                        return p;
                    }
                }
            }
        }
        p.setRecords(new ArrayList<>());
        p.setTotal(0);
        return p;
    }


    @Override
    public String queryUniqueCode(PdProductStockUniqueCode pdProductStockUniqueCode) {
        List<String> strList = new ArrayList<>();
        String str="";
        //查询是否已经存在
        PdProductStock pdProductStock = new PdProductStock();
        pdProductStock.setId(pdProductStockUniqueCode.getProductStockId());
        List<PdProductStock> pdProductStocks = pdProductStockService.selectList(pdProductStock);
        if(pdProductStocks!=null && pdProductStocks.size()>0){
            PdProductStock psk = pdProductStocks.get(0);
            if(PdConstant.CODE_PRINT_TYPE_1.equals(psk.getBarCodeType())){
                pdProductStockUniqueCode.setPrintType(PdConstant.CODE_PRINT_TYPE_1);
                strList= baseMapper.selectListUniqueCode(pdProductStockUniqueCode);
                     if(strList!=null && strList.size()>0){
                         str= StringUtils.join(strList.toArray(), ",");
                     }
            }
        }
        return str;
    }

    /**
     * 已经有唯一码保存(智能柜唯一码)
     * @param psc
     */
    @Override
    public  void foreignSaveUniqueCode(PdProductStockUniqueCode psc){
        if(!oConvertUtils.isEmpty(psc)){
            psc.setPrintType(PdConstant.CODE_PRINT_TYPE_1);//唯一码
            psc.setCodeState(PdConstant.CODE_PRINT_STATE_0);//正常状态
            psc.setPrintNum(1);
            baseMapper.insert(psc);
            PdProductStock ps = new PdProductStock();
            ps.setId(psc.getProductStockId());
            ps.setBarCodeType(PdConstant.CODE_PRINT_TYPE_1);
            //更新库存表的条码状态
            pdProductStockService.updateStockBarCodeType(ps);
        }
    }
}
