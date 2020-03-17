package org.jeecg.modules.pd.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.PdDosageMapper;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.shiro.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 用量表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Service
public class PdDosageServiceImpl extends ServiceImpl<PdDosageMapper, PdDosage> implements IPdDosageService {

    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private IPdGoodsAllocationService pdGoodsAllocationService;
    @Autowired
    private IPdProductStockService pdProductStockService;
    @Autowired
    private IPdDosageDetailService pdDosageDetailService;
    @Autowired
    private IPdStockLogService pdStockLogService;
    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private PdDosageMapper pdDosageMapper;




    @Override
    public PdDosage initModal(String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysDepart sysDepart = pdDepartService.getById(sysUser.getCurrentDepartId());
        PdDosage pdDosage = new PdDosage();
        if (oConvertUtils.isNotEmpty(id)) { // 查看页面
            pdDosage = this.getById(id);
            // 新增页面
            pdDosage.setDepartName(sysDepart.getDepartName());
            pdDosage.setDosageByName(sysUser.getRealname());
            PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
            pdGoodsAllocation.setDepartId(sysUser.getCurrentDepartId());
            pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
            List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);
            //库区库位下拉框
            pdDosage.setGoodsAllocationList(goodsAllocationList);
            PdDosageDetail pdDosageDetail = new PdDosageDetail();
            pdDosageDetail.setDosageId(id);
            List<PdDosageDetail> pdDosageDetails = pdDosageDetailService.selectList(pdDosageDetail);
            pdDosage.setPdDosageDetails(pdDosageDetails);

        } else {  // 新增页面
            pdDosage.setDepartId(sysDepart.getId());
            pdDosage.setDepartName(sysDepart.getDepartName());
            //获取出库单号
            pdDosage.setDosageNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_YL));
            //获取当前日期
            pdDosage.setDosageDate(DateUtils.getDate());
            //登录人姓名
            pdDosage.setDosageBy(sysUser.getId());
            pdDosage.setDosageByName(sysUser.getRealname());
            PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
            pdGoodsAllocation.setDepartId(sysUser.getCurrentDepartId());
            pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
            List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);
            //库区库位下拉框
            pdDosage.setGoodsAllocationList(goodsAllocationList);
        }
        return pdDosage;

    }

    /**
     * 器械使用保存
     * @param pdDosage
     */
    @Transactional
    @Override
    public void saveMain(PdDosage pdDosage,String displayFlag) {
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        pdDosage.setId(UUIDUtil.getUuid());
        //校验数据的合法性
        Iterator<PdDosageDetail> it = detailList.iterator();
        while(it.hasNext()){
            PdDosageDetail child = it.next();
            if(child.getDosageCount()==null || child.getProductId()==null){
                it.remove();
            }
        }
        if(detailList != null && detailList.size() > 0) {
            //总数量
            BigDecimal dosageTotal = new BigDecimal(0);
            //总金额
            BigDecimal moneyTotal = new BigDecimal(0);
            List<PdDosageDetail> tempArray = new ArrayList<>();
            //产品物流
            List<PdStockLog> logList = new ArrayList<PdStockLog>();
            //数据合并
            List<PdDosageDetail> afterDealList = dealRepeatData(detailList);
            JSONObject json = new JSONObject();
            int i = 0;
            boolean validFlag = true;
            for(PdDosageDetail pdd : afterDealList){
                //校验不合法数据和大于库存数据
                PdProductStock pps = new PdProductStock();
                pps.setId(pdd.getProductStockId());
                //pps.setBatchNo(pdd.getBatchNo());
                //pps.setProductId(pdd.getProductId());
                //pps.setExpDate(pdd.getExpDate());
                //pps.setProductBarCode(pdd.getProductBarCode());
                PdProductStock tempPps = pdProductStockService.getById(pps);
                if( null == tempPps || pdd.getDosageCount() > tempPps.getStockNum()){
                    validFlag = false;
                    json.put(String.valueOf(i), pdd);
                    i = i + 1;
                    continue;
                }

                BigDecimal sprice = pdd.getSellingPrice()==null?new BigDecimal(0):pdd.getSellingPrice();
                BigDecimal pdMoney = new BigDecimal(pdd.getDosageCount()).multiply(sprice);
                BigDecimal dosageCount = new BigDecimal(pdd.getDosageCount());
                dosageTotal = dosageCount.add(dosageTotal);
                moneyTotal = pdMoney.add(moneyTotal);
                pdd.setDosageId(pdDosage.getId());
                //如果没有接口
                if(displayFlag.equals(PdConstant.IS_CHARGE_FLAG_1)){
                    pdd.setHyCharged(displayFlag);
                }else{
                    pdd.setHyCharged(pdDosage.getHyCharged());
                }
                pdd.setAmountMoney(pdMoney);
                pdd.setLeftRefundNum(pdd.getDosageCount());
                tempArray.add(pdd);
                //产品追踪信息
                PdStockLog prodLog = new PdStockLog();
                prodLog.setBatchNo(pdd.getBatchNo());
                prodLog.setProductBarCode(pdd.getProductBarCode());
                prodLog.setProductId(pdd.getProductId());
                prodLog.setProductNum(pdd.getDosageCount());
                prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_3);
                prodLog.setInFrom(pdDosage.getDepartName());
                prodLog.setOutTo("病人:"+pdDosage.getPatientInfo());
                prodLog.setPatientInfo(pdDosage.getPatientDetailInfo());
                prodLog.setChargeDeptName(pdDosage.getExeDeptName());
                prodLog.setRecordTime(DateUtils.getDate());
                logList.add(prodLog);
            }
            if (!validFlag) {//数据校验没通过

            } else {
                //收费调用接口
                if (PdConstant.CHARGE_FLAG_0.equals(pdDosage.getHyCharged()) && PdConstant.IS_CHARGE_FLAG_0.equals(displayFlag)){

                }
                if(!tempArray.isEmpty())
                    pdDosageDetailService.saveBatch(tempArray);
                if(!logList.isEmpty())
                    pdStockLogService.saveBatch(logList);
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                //保存用量
                pdDosage.setDisplayFlag(displayFlag);//是否有收费接口标识，0有1没有
                pdDosage.setTotalSum(dosageTotal.doubleValue());
                pdDosage.setUpdateTime(DateUtils.getDate());
                pdDosage.setTotalPrice(moneyTotal);
                pdDosage.setDosageBy(sysUser.getId());
                pdDosage.setDosageDate(DateUtils.getDate());
                this.save(pdDosage);
                //扣减当前库房的库存

                pdProductStockTotalService.updateUseStock(sysUser.getCurrentDepartId(),afterDealList);
            }
        }
    }

    /**
     * 分页查询使用列表
     * @param page
     * @param pdDosage
     * @return
     */
    @Override
    public IPage<PdDosage> queryList(Page<PdDosage> page, PdDosage pdDosage) {
        return page.setRecords(pdDosageMapper.selectList(pdDosage));
    }

    //合并相同的用量
    private List<PdDosageDetail> dealRepeatData(final List<PdDosageDetail> list){
        List<PdDosageDetail> tempArray = new ArrayList<PdDosageDetail>();
        Set<String> pids = new HashSet<String>();
        if(list != null && list.size() > 0){
            for(PdDosageDetail temp : list){
                String expdate = DateUtils.date2Str(temp.getExpDate(),DateUtils.yyMMdd.get());
                if (temp == null || StringUtils.isEmpty(temp.getProductId()) || StringUtils.isEmpty(temp.getProductBarCode())
                || StringUtils.isEmpty(temp.getBatchNo()) || StringUtils.isEmpty(expdate)) {
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(temp.getProductId()).append(temp.getProductBarCode()).append(temp.getProductBarCode()).append(temp.getBatchNo());
                if(pids.contains(sb.toString())){
                    continue;
                }
                BigDecimal dosageTotal = new BigDecimal(0);
                for(PdDosageDetail tp : list){
                    if ( tp != null) {
                        String tExpdate = DateUtils.date2Str(tp.getExpDate(),DateUtils.yyMMdd.get());
                        if(temp.getBatchNo().equals(tp.getBatchNo())
                                && expdate.equals(tExpdate)
                                && temp.getProductBarCode().equals(tp.getProductBarCode())
                                && temp.getProductId().equals(tp.getProductId())){
                            pids.add(sb.toString());
                            BigDecimal dosageCount = new BigDecimal(tp.getDosageCount());
                            dosageTotal = dosageCount.add(dosageTotal);
                        }
                    }
                }
                temp.setDosageCount(dosageTotal.doubleValue());
                tempArray.add(temp);
            }
        }
        return tempArray;
    }
}
