package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockUniqueCode;
import org.jeecg.modules.pd.mapper.PdProductStockUniqueCodeMapper;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockUniqueCodeService;
import org.jeecg.modules.pd.util.SnowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
    /**
     * 生成唯一码并保存
     * @param pdProductStockUniqueCode
     * @return
     */
    @Transactional
    @Override
    public Result<List<PdProductStockUniqueCode>> uniqueCodeGeneration(PdProductStockUniqueCode pdProductStockUniqueCode) {
        Result<List<PdProductStockUniqueCode>> result =  new Result<>();
        PdProductStock pdProductStock = new PdProductStock();
        pdProductStock.setId(pdProductStockUniqueCode.getProductStockId());
        List<PdProductStockUniqueCode> pdProductStockUniqueCodeList = new ArrayList<>();
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
                }
            }else{
                //判断是否已经打印了普通码
                LambdaQueryWrapper<PdProductStockUniqueCode> wyquery = new LambdaQueryWrapper<>();
                wyquery.eq(PdProductStockUniqueCode::getProductStockId, pdProductStock.getId());
                wyquery.eq(PdProductStockUniqueCode::getPrintType, PdConstant.CODE_PRINT_TYPE_0);//普通码
                List<PdProductStockUniqueCode> wypsucs = this.list(wyquery);
                if(wypsucs!=null && wypsucs.size()>0){
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
        PdProductStock pdProductStock = new PdProductStock();
        pdProductStock.setStockIdList(asList);
        List<PdProductStock> pdProductStocks = pdProductStockService.selectList(pdProductStock);
        List<PdProductStockUniqueCode> pdProductStockUniqueCodeList = new ArrayList<>();
        boolean flag = true;
        if(pdProductStocks!=null && pdProductStocks.size()>0){
            //判断是否全部通过
            for(PdProductStock ps :pdProductStocks){
                //查询是否已经存在
                LambdaQueryWrapper<PdProductStockUniqueCode> query = new LambdaQueryWrapper<>();
                query.eq(PdProductStockUniqueCode::getProductStockId, ps.getId());
                query.eq(PdProductStockUniqueCode::getPrintType, PdConstant.CODE_PRINT_TYPE_0);//普通码
                PdProductStockUniqueCode psucs = this.getOne(query);
                if(psucs!=null){
                    //重复打条码
                    psucs.setProductName(ps.getProductName());
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
                        psc.setId(SnowUtils.onlyBigKey(1));
                        psc.setProductStockId(ps.getId());
                        psc.setPrintType(PdConstant.CODE_PRINT_TYPE_0);//普通码
                        psc.setCodeState(PdConstant.CODE_PRINT_STATE_0);//正常状态
                        psc.setUniqueCodeOrder(1);
                        psc.setProductName(ps.getProductName());
                        psc.setExpDate(ps.getExpDate());
                        psc.setBatchNo(ps.getBatchNo());
                        this.save(psc);
                        pdProductStockUniqueCodeList.add(psc);
                    }else{
                        result.setResult(new ArrayList<>());
                        result.setCode(500);
                        result.setMessage("库存数量不足");
                        return result;
                    }
                }
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

    /**
     * 清除条码
     * @param id
     */
    @Transactional
    @Override
    public void deleteCode(String id) {
        LambdaQueryWrapper<PdProductStockUniqueCode> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(PdProductStockUniqueCode::getProductStockId, id);
        this.remove(query);
        //更新库存明细表的状态
        PdProductStock ps = new PdProductStock();
        ps.setId(id);
        ps.setBarCodeType(PdConstant.CODE_PRINT_TYPE_0);
        //更新库存表的条码状态
        pdProductStockService.updateStockBarCodeType(ps);
    }
}
