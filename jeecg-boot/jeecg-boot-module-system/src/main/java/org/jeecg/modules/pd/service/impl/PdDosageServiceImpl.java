package org.jeecg.modules.pd.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.exception.JeecgBootException;
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
    @Autowired
    private IHisChargeService hisChargeService;
    @Autowired
    private IExHisZyInfService exHisZyInfService;
    @Autowired
    private IPdOnOffService pdOnOffService;
    @Autowired
    private IPdProductStockUniqueCodeService pdProductStockUniqueCodeService;


    @Override
    public PdDosage initModal(String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysDepart sysDepart = pdDepartService.getById(sysUser.getCurrentDepartId());
        PdDosage pdDosage = new PdDosage();
        if (oConvertUtils.isNotEmpty(id)) { // 查看页面
            pdDosage.setId(id);
            pdDosage = baseMapper.getByOne(pdDosage);
            // 新增页面
            //pdDosage.setDepartName(sysDepart.getDepartName());
            //pdDosage.setDosageByName(sysUser.getRealname());
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

            BigDecimal jfTotalPrice = new BigDecimal(0);
            for (PdDosageDetail item : pdDosageDetails) {
                if(PdConstant.IS_CHARGE_0.equals(item.getIsCharge())){
                    jfTotalPrice = jfTotalPrice.add(item.getAmountMoney());
                }
            }
            pdDosage.setJfTotalPrice(jfTotalPrice);

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
        PdOnOff query = new PdOnOff();
        //开关-是否显示二级条码框（入库、出库、退货）   1-显示；0-不显示
        query.setCode(PdConstant.ON_OFF_SHOW_S_BARCODE);
        query.setDepartParentId(sysUser.getDepartParentId());
        PdOnOff showSBarcode = pdOnOffService.getOne(query);
        if (showSBarcode != null && showSBarcode.getValue() != null) {
            pdDosage.setShowSBarcode(showSBarcode.getValue().toString());
        }
       //开关-是否根据规格数量扣减库存   1-是；0-否
        query.setCode(PdConstant.ON_OFF_SPEC_NUM);
        query.setDepartParentId(sysUser.getDepartParentId());
        PdOnOff offSpecNum = pdOnOffService.getOne(query);
        if (offSpecNum != null && offSpecNum.getValue() != null) {
            pdDosage.setOffSpecNum(offSpecNum.getValue().toString());
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
            List<PdDosageDetail> chargeArray = new ArrayList<>();
            //产品物流
            List<PdStockLog> logList = new ArrayList<PdStockLog>();
            //数据合并
//            List<PdDosageDetail> afterDealList = dealRepeatData(detailList);
            JSONObject json = new JSONObject();
            int i = 0;
            boolean validFlag = true;
            for(PdDosageDetail pdd : detailList){
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
                pdd.setAmountMoney(pdMoney);
                pdd.setLeftRefundNum(pdd.getDosageCount());
                //产品追踪信息
                PdStockLog prodLog = new PdStockLog();
                //需要收费的产品集合
                if(PdConstant.CHARGE_FLAG_0.equals(pdDosage.getHyCharged())
                        && PdConstant.CHARGE_FLAG_0.equals(pdd.getIsCharge())
                        && !"".equals(pdd.getChargeCode())){
                    pdd.setHyCharged(PdConstant.CHARGE_FLAG_0);
                    prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_6);
                    chargeArray.add(pdd);
                }else{
                    //不收费的产品集合
                    pdd.setHyCharged(PdConstant.CHARGE_FLAG_1);
                    prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_3);
                    tempArray.add(pdd);
                }
                prodLog.setBatchNo(pdd.getBatchNo());
                prodLog.setProductBarCode(pdd.getProductBarCode());
                prodLog.setExpDate(pdd.getExpDate());
                prodLog.setProductId(pdd.getProductId());
                prodLog.setProductNum(pdd.getDosageCount());
                prodLog.setInFrom(pdDosage.getDepartName());
                prodLog.setOutTo("病人:"+(pdDosage.getPatientInfo()!=null?pdDosage.getPatientInfo():""));
                prodLog.setPatientInfo(pdDosage.getPatientDetailInfo());
                prodLog.setInvoiceNo(pdDosage.getDosageNo());
                prodLog.setChargeDeptName(pdDosage.getExeDeptName());
                prodLog.setRecordTime(DateUtils.getDate());
                logList.add(prodLog);
            }
            if (!validFlag) {//数据校验没通过
                throw new JeecgBootException("数据校验失败");
            } else {
                //收费调用接口
                if (PdConstant.CHARGE_FLAG_0.equals(pdDosage.getHyCharged())
                        &&chargeArray.size()>0){
                    //TODO 是计费切有收费代码的产品才会发往his系统
                    throw new JeecgBootException("没有配置收费接口");
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

                pdProductStockTotalService.updateUseStock(sysUser.getCurrentDepartId(),detailList);
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
        return pdDosageMapper.selectListByPage(page,pdDosage);
    }

    //合并相同的用量
//    public List<PdDosageDetail> dealRepeatData(final List<PdDosageDetail> list){
//        List<PdDosageDetail> tempArray = new ArrayList<PdDosageDetail>();
//        Set<String> pids = new HashSet<String>();
//        if(list != null && list.size() > 0){
//            for(PdDosageDetail temp : list){
//                String expdate = DateUtils.date2Str(temp.getExpDate(),DateUtils.yyMMdd.get());
//                if (temp == null || StringUtils.isEmpty(temp.getProductId()) || StringUtils.isEmpty(temp.getProductBarCode())
//                || StringUtils.isEmpty(temp.getBatchNo()) || StringUtils.isEmpty(expdate)) {
//                    continue;
//                }
//                StringBuilder sb = new StringBuilder();
//                sb.append(temp.getProductId()).append(temp.getProductBarCode()).append(temp.getProductBarCode()).append(temp.getBatchNo());
//                if(pids.contains(sb.toString())){
//                    continue;
//                }
//                BigDecimal dosageTotal = new BigDecimal(0);
//                for(PdDosageDetail tp : list){
//                    if ( tp != null) {
//                        String tExpdate = DateUtils.date2Str(tp.getExpDate(),DateUtils.yyMMdd.get());
//                        if(temp.getBatchNo().equals(tp.getBatchNo())
//                                && expdate.equals(tExpdate)
//                                && temp.getProductBarCode().equals(tp.getProductBarCode())
//                                && temp.getProductId().equals(tp.getProductId())){
//                            pids.add(sb.toString());
//                            BigDecimal dosageCount = new BigDecimal(tp.getDosageCount());
//                            dosageTotal = dosageCount.add(dosageTotal);
//                        }
//                    }
//                }
//                temp.setDosageCount(dosageTotal.doubleValue());
//                tempArray.add(temp);
//            }
//        }
//        return tempArray;
//    }

    /**
     * 首页查询当日使用量
     * @param pdDosage
     * @return
     */
    @Override
	public Map<String,Object> queryPdDosageCount(PdDosage pdDosage) {
		Map<String,Object> params = pdDosageMapper.queryPdDosageCount(pdDosage);
		return params;
	}

    /**
     * 首页查询每周使用量
     * @param pdDosage
     * @return
     */
	@Override
	public List<HashMap> queryPdDosageDateList(PdDosage pdDosage) {
		return pdDosageMapper.queryPdDosageDateList(pdDosage);
	}

    /**
     * 首页查询  根据采购产品类区分统计使用金额
     * @param pdDosage
     * @return
     */
    @Override
    public List<HashMap> queryPurchaseOrderTotalList(PdDosage pdDosage) {
        return pdDosageMapper.queryPdDosageTotalList(pdDosage);
    }

    /**
     * 统计查询-用量明细
     * @param page
     * @param pdDosage
     * @return
     */
    @Override
    public Page<PdDosage> queryPdDosageList(Page<PdDosage> page, PdDosage pdDosage) {
        return page.setRecords(pdDosageMapper.queryPdDosageList(pdDosage));
    }

    @Override
    public List<PdDosage> queryPdDosageList(PdDosage pdDosage) {
        return pdDosageMapper.queryPdDosageList(pdDosage);
    }

    @Override
    public List<HashMap> queryPdDosageTotalList(PdDosage pdDosage) {
        return pdDosageMapper.queryPdDosageTotalList(pdDosage);
    }

    /**
     * 取消收费
     * @param pdDosage
     */
    @Override
    @Transactional
    public void dosageCnclFee(PdDosage pdDosage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        if(detailList!=null && detailList.size()>0){
            //产品物流
            List<PdStockLog> logList = new ArrayList<PdStockLog>();
            for(PdDosageDetail pdd : detailList){
                //产品追踪信息
                PdStockLog prodLog = new PdStockLog();
                if(pdd.getLeftRefundNum()==0L){
                    throw new JeecgBootException("参数不正确");
                }
                BigDecimal leftRefundNum = new BigDecimal(0);
                pdd.setLeftRefundNum(leftRefundNum.doubleValue());
                pdd.setHyCharged(PdConstant.CHARGE_FLAG_3);
                prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_10);
                prodLog.setBatchNo(pdd.getBatchNo());
                prodLog.setProductBarCode(pdd.getProductBarCode());
                prodLog.setExpDate(pdd.getExpDate());
                prodLog.setProductId(pdd.getProductId());
                prodLog.setProductNum(pdd.getDosageCount());
                prodLog.setInFrom(pdDosage.getDepartName());
                prodLog.setOutTo("病人:"+(pdDosage.getPatientInfo()!=null?pdDosage.getPatientInfo():""));
                prodLog.setPatientInfo(pdDosage.getPatientDetailInfo());
                prodLog.setInvoiceNo(pdDosage.getDosageNo());
                prodLog.setChargeDeptName(pdDosage.getExeDeptName());
                prodLog.setRecordTime(DateUtils.getDate());
                logList.add(prodLog);
            }
            if(!logList.isEmpty())
                pdStockLogService.saveBatch(logList);
            pdDosageDetailService.updateBatchById(detailList);
            pdProductStockTotalService.updateRetunuseStock(sysUser.getCurrentDepartId(),detailList);
        }
    }

    /**
     * 收费
     * @param pdDosage
     */
    @Override
    @Transactional
    public void dosageFee(PdDosage pdDosage) {
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        if(detailList!=null && detailList.size()>0){
            for(PdDosageDetail pdd : detailList){
                pdd.setHyCharged(PdConstant.CHARGE_FLAG_0);
            }
              pdDosageDetailService.updateBatchById(detailList);
        }
    }


    /**
     * 库存回退
     * @param pdDosage
     */
    @Override
    @Transactional
    public void dosageReturned(PdDosage pdDosage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        if(detailList!=null && detailList.size()>0){
            //产品物流
            List<PdStockLog> logList = new ArrayList<PdStockLog>();
            for(PdDosageDetail pdd : detailList){
                //产品追踪信息
                PdStockLog prodLog = new PdStockLog();
                if(pdd.getLeftRefundNum()==0L){
                    throw new JeecgBootException("参数不正确");
                }
                BigDecimal leftRefundNum = new BigDecimal(0);
                pdd.setLeftRefundNum(leftRefundNum.doubleValue());
                pdd.setHyCharged(PdConstant.CHARGE_FLAG_2);
                prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_4);
                prodLog.setBatchNo(pdd.getBatchNo());
                prodLog.setProductBarCode(pdd.getProductBarCode());
                prodLog.setExpDate(pdd.getExpDate());
                prodLog.setProductId(pdd.getProductId());
                prodLog.setProductNum(pdd.getDosageCount());
                prodLog.setInFrom(pdDosage.getDepartName());
                prodLog.setOutTo("病人:"+(pdDosage.getPatientInfo()!=null?pdDosage.getPatientInfo():""));
                prodLog.setPatientInfo(pdDosage.getPatientDetailInfo());
                prodLog.setInvoiceNo(pdDosage.getDosageNo());
                prodLog.setChargeDeptName(pdDosage.getExeDeptName());
                prodLog.setRecordTime(DateUtils.getDate());
                logList.add(prodLog);
            }
            if(!logList.isEmpty())
                pdStockLogService.saveBatch(logList);
            pdDosageDetailService.updateBatchById(detailList);
            pdProductStockTotalService.updateRetunuseStock(sysUser.getCurrentDepartId(),detailList);
        }
    }



    /**
     * 器械使用保存
     * @param pdDosage
     */
    @Transactional
    @Override
    public List<PdDosageDetail> newSaveMain(PdDosage pdDosage,String displayFlag) {
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
        List<PdDosageDetail> tempArray = new ArrayList<>();
        List<PdDosageDetail> chargeArray = new ArrayList<>();
        if(detailList != null && detailList.size() > 0) {
            //总数量
            BigDecimal dosageTotal = new BigDecimal(0);
            //总金额
            BigDecimal moneyTotal = new BigDecimal(0);
            //产品物流
            List<PdStockLog> logList = new ArrayList<PdStockLog>();
            //数据合并
//            List<PdDosageDetail> afterDealList = dealRepeatData(detailList);
            JSONObject json = new JSONObject();
            int i = 0;
            boolean validFlag = true;
            for(PdDosageDetail pdd : detailList){
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
                pdd.setAmountMoney(pdMoney);
                pdd.setLeftRefundNum(pdd.getDosageCount());
                //产品追踪信息
                PdStockLog prodLog = new PdStockLog();
                //需要收费的产品集合
                if(PdConstant.CHARGE_FLAG_0.equals(pdDosage.getHyCharged())
                        && PdConstant.CHARGE_FLAG_0.equals(pdd.getIsCharge())
                        && !"".equals(pdd.getChargeCode())){
                    pdd.setHyCharged(PdConstant.CHARGE_FLAG_0);
                    prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_6);
                    chargeArray.add(pdd);
                }else{
                    //不收费的产品集合
                    pdd.setHyCharged(PdConstant.CHARGE_FLAG_1);
                    prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_3);
                    tempArray.add(pdd);
                }
                prodLog.setBatchNo(pdd.getBatchNo());
                prodLog.setProductBarCode(pdd.getProductBarCode());
                prodLog.setExpDate(pdd.getExpDate());
                prodLog.setProductId(pdd.getProductId());
                prodLog.setProductNum(pdd.getDosageCount());
                prodLog.setInFrom(pdDosage.getDepartName());
                prodLog.setOutTo("病人:"+(pdDosage.getPatientInfo()!=null?pdDosage.getPatientInfo():""));
                prodLog.setPatientInfo(pdDosage.getPatientDetailInfo());
                prodLog.setInvoiceNo(pdDosage.getDosageNo());
                prodLog.setChargeDeptName(pdDosage.getExeDeptName());
                prodLog.setRecordTime(DateUtils.getDate());
                logList.add(prodLog);
            }
            if (!validFlag) {//数据校验没通过
                throw new JeecgBootException("数据校验失败");
            } else {
                //收费调用接口
                if (PdConstant.CHARGE_FLAG_0.equals(pdDosage.getHyCharged())
                        &&chargeArray.size()>0){
                    //这样会有问题，一个service实现类中不能存在多个不同数据源的service实现类
                  /*if(StringUtils.isNotEmpty(pdDosage.getInHospitalNo())){
                        exHisZyInfService.saveExHisZyInf(pdDosage,chargeArray);
                    }*/
                    pdDosageDetailService.saveBatch(chargeArray);
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
                pdDosage.setDosageType(PdConstant.DOSAGE_TYPE_1);//普通码使用
                this.save(pdDosage);
                //扣减当前库房的库存
                pdProductStockTotalService.updateUseStock(sysUser.getCurrentDepartId(),detailList);
            }
        }
        return chargeArray;
    }

    @Transactional
    @Override
    public List<PdDosageDetail> uniqueSubmit(PdDosage pdDosage,String displayFlag) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
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
        List<PdDosageDetail> tempArray = new ArrayList<>();
        List<PdDosageDetail> chargeArray = new ArrayList<>();
        if(detailList != null && detailList.size() > 0) {
            //总数量
            BigDecimal dosageTotal = new BigDecimal(0);
            //总金额
            BigDecimal moneyTotal = new BigDecimal(0);
            //产品物流
            List<PdStockLog> logList = new ArrayList<PdStockLog>();
            //唯一码
            List<PdProductStockUniqueCode> productStockUniqueCodes = new ArrayList<>();
            //数据合并
//            List<PdDosageDetail> afterDealList = dealRepeatData(detailList);
            JSONObject json = new JSONObject();
            int i = 0;
            boolean validFlag = true;
            for(PdDosageDetail pdd : detailList){
                //校验不合法数据和大于库存数据
                PdProductStock pps = new PdProductStock();
                pps.setId(pdd.getProductStockId());
                //pps.setBatchNo(pdd.getBatchNo());
                //pps.setProductId(pdd.getProductId());
                //pps.setExpDate(pdd.getExpDate());
                //pps.setProductBarCode(pdd.getProductBarCode());
                PdProductStock tempPps = pdProductStockService.getById(pps);
                PdOnOff query = new PdOnOff();
                query.setDepartParentId(sysUser.getDepartParentId());
                query.setCode(PdConstant.ON_OFF_SPEC_NUM);
                PdOnOff pdOnOff = pdOnOffService.getOne(query);
                if (ObjectUtil.isNotEmpty(tempPps.getSpecQuantity()) && pdOnOff != null && pdOnOff.getValue() == PdConstant.ON_OFF_SPEC_NUM_1) { //是
                    if (null == tempPps || pdd.getDosageCount() > tempPps.getSpecNum()) {
                        validFlag = false;
                        json.put(String.valueOf(i), pdd);
                        i = i + 1;
                        continue;
                    }
                }else{
                    if (null == tempPps || pdd.getDosageCount() > tempPps.getStockNum()) {
                        validFlag = false;
                        json.put(String.valueOf(i), pdd);
                        i = i + 1;
                        continue;
                    }
                }
                BigDecimal sprice = pdd.getSellingPrice()==null?new BigDecimal(0):pdd.getSellingPrice();
                BigDecimal pdMoney = new BigDecimal(pdd.getDosageCount()).multiply(sprice);
                BigDecimal dosageCount = new BigDecimal(pdd.getDosageCount());
                dosageTotal = dosageCount.add(dosageTotal);
                moneyTotal = pdMoney.add(moneyTotal);
                pdd.setDosageId(pdDosage.getId());
                pdd.setAmountMoney(pdMoney);
                pdd.setLeftRefundNum(pdd.getDosageCount());
                //产品追踪信息
                PdStockLog prodLog = new PdStockLog();
                //需要收费的产品集合
                if(PdConstant.CHARGE_FLAG_0.equals(pdDosage.getHyCharged())
                        && PdConstant.CHARGE_FLAG_0.equals(pdd.getIsCharge())
                        && !"".equals(pdd.getChargeCode())){
                    pdd.setHyCharged(PdConstant.CHARGE_FLAG_0);
                    prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_6);
                    chargeArray.add(pdd);
                }else{
                    //不收费的产品集合
                    pdd.setHyCharged(PdConstant.CHARGE_FLAG_1);
                    prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_3);
                    tempArray.add(pdd);
                }
                prodLog.setBatchNo(pdd.getBatchNo());
                prodLog.setProductBarCode(pdd.getProductBarCode());
                prodLog.setRefBarCode(pdd.getRefBarCode());
                prodLog.setExpDate(pdd.getExpDate());
                prodLog.setProductId(pdd.getProductId());
                prodLog.setProductNum(pdd.getDosageCount());
                prodLog.setInFrom(pdDosage.getDepartName());
                prodLog.setOutTo("病人:"+(pdDosage.getPatientInfo()!=null?pdDosage.getPatientInfo():""));
                prodLog.setPatientInfo(pdDosage.getPatientDetailInfo());
                prodLog.setInvoiceNo(pdDosage.getDosageNo());
                prodLog.setChargeDeptName(pdDosage.getExeDeptName());
                prodLog.setRecordTime(DateUtils.getDate());
                logList.add(prodLog);
                /*PdProductStockUniqueCode pdProductStockUniqueCode = new PdProductStockUniqueCode();
                pdProductStockUniqueCode.setId(pdd.getRefBarCode());
                pdProductStockUniqueCode.setCodeState(PdConstant.CODE_PRINT_STATE_2);//已用完状态
                productStockUniqueCodes.add(pdProductStockUniqueCode);*/
            }
            if (!validFlag) {//数据校验没通过
                throw new JeecgBootException("数据校验失败");
            } else {
                //收费调用接口
                if (PdConstant.CHARGE_FLAG_0.equals(pdDosage.getHyCharged())
                        &&chargeArray.size()>0){
                    //这样会有问题，一个service实现类中不能存在多个不同数据源的service实现类
                  /*if(StringUtils.isNotEmpty(pdDosage.getInHospitalNo())){
                        exHisZyInfService.saveExHisZyInf(pdDosage,chargeArray);
                    }*/
                    pdDosageDetailService.saveBatch(chargeArray);
                }
                if(!tempArray.isEmpty())
                    pdDosageDetailService.saveBatch(tempArray);
                if(!logList.isEmpty())
                    pdStockLogService.saveBatch(logList);
                //保存用量
                pdDosage.setDisplayFlag(displayFlag);//是否有收费接口标识，0有1没有
                pdDosage.setTotalSum(dosageTotal.doubleValue());
                pdDosage.setUpdateTime(DateUtils.getDate());
                pdDosage.setTotalPrice(moneyTotal);
                pdDosage.setDosageBy(sysUser.getId());
                pdDosage.setDosageDate(DateUtils.getDate());
                pdDosage.setDosageType(PdConstant.DOSAGE_TYPE_0);//唯一码使用
                this.save(pdDosage);
                //扣减当前库房的库存
                 pdProductStockTotalService.updateUseStock(sysUser.getCurrentDepartId(),detailList);
                //批量更新条码状态
                // pdProductStockUniqueCodeService.updateBatchById(productStockUniqueCodes);

            }
        }
        return chargeArray;
    }


    /**
     * 唯一码用量退回
     * @param pdDosage
     */
    @Transactional
    @Override
    public void uniqueDosageReturned(PdDosage pdDosage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        if(detailList!=null && detailList.size()>0){
            //产品物流
            List<PdStockLog> logList = new ArrayList<PdStockLog>();
            //唯一码
            List<PdProductStockUniqueCode> productStockUniqueCodes = new ArrayList<>();
            for(PdDosageDetail pdd : detailList){
                //产品追踪信息
                PdStockLog prodLog = new PdStockLog();
                if(pdd.getLeftRefundNum()==0L){
                    throw new JeecgBootException("参数不正确");
                }
                BigDecimal leftRefundNum = new BigDecimal(0);
                pdd.setLeftRefundNum(leftRefundNum.doubleValue());
                pdd.setHyCharged(PdConstant.CHARGE_FLAG_2);
                prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_4);
                prodLog.setBatchNo(pdd.getBatchNo());
                prodLog.setProductBarCode(pdd.getProductBarCode());
                prodLog.setRefBarCode(pdd.getRefBarCode());
                prodLog.setExpDate(pdd.getExpDate());
                prodLog.setProductId(pdd.getProductId());
                prodLog.setProductNum(pdd.getDosageCount());
                prodLog.setInFrom(pdDosage.getDepartName());
                prodLog.setOutTo("病人:"+(pdDosage.getPatientInfo()!=null?pdDosage.getPatientInfo():""));
                prodLog.setPatientInfo(pdDosage.getPatientDetailInfo());
                prodLog.setInvoiceNo(pdDosage.getDosageNo());
                prodLog.setChargeDeptName(pdDosage.getExeDeptName());
                prodLog.setRecordTime(DateUtils.getDate());
                logList.add(prodLog);
                PdProductStockUniqueCode pdProductStockUniqueCode = new PdProductStockUniqueCode();
                pdProductStockUniqueCode.setId(pdd.getRefBarCode());
                pdProductStockUniqueCode.setCodeState(PdConstant.CODE_PRINT_STATE_0);//正常状态
                productStockUniqueCodes.add(pdProductStockUniqueCode);
            }
            if(!logList.isEmpty())
                pdStockLogService.saveBatch(logList);
            pdDosageDetailService.updateBatchById(detailList);
            pdProductStockTotalService.updateRefBarCodeRetunuseStock(sysUser.getCurrentDepartId(),detailList);
            //批量更新条码状态
            pdProductStockUniqueCodeService.updateBatchById(productStockUniqueCodes);
        }

    }

    /**
     * 唯一码取消收费
     * @param pdDosage
     */
    @Transactional
    @Override
    public void uniqueDosageCnclFee(PdDosage pdDosage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        if(detailList!=null && detailList.size()>0){
            //产品物流
            List<PdStockLog> logList = new ArrayList<PdStockLog>();
            //唯一码
            List<PdProductStockUniqueCode> productStockUniqueCodes = new ArrayList<>();
            for(PdDosageDetail pdd : detailList){
                //产品追踪信息
                PdStockLog prodLog = new PdStockLog();
                if(pdd.getLeftRefundNum()==0L){
                    throw new JeecgBootException("参数不正确");
                }
                BigDecimal leftRefundNum = new BigDecimal(0);
                pdd.setLeftRefundNum(leftRefundNum.doubleValue());
                pdd.setHyCharged(PdConstant.CHARGE_FLAG_3);
                prodLog.setLogType(PdConstant.STOCK_LOG_TYPE_10);
                prodLog.setBatchNo(pdd.getBatchNo());
                prodLog.setProductBarCode(pdd.getProductBarCode());
                prodLog.setExpDate(pdd.getExpDate());
                prodLog.setProductId(pdd.getProductId());
                prodLog.setProductNum(pdd.getDosageCount());
                prodLog.setInFrom(pdDosage.getDepartName());
                prodLog.setOutTo("病人:"+(pdDosage.getPatientInfo()!=null?pdDosage.getPatientInfo():""));
                prodLog.setPatientInfo(pdDosage.getPatientDetailInfo());
                prodLog.setInvoiceNo(pdDosage.getDosageNo());
                prodLog.setChargeDeptName(pdDosage.getExeDeptName());
                prodLog.setRecordTime(DateUtils.getDate());
                logList.add(prodLog);
                PdProductStockUniqueCode pdProductStockUniqueCode = new PdProductStockUniqueCode();
                pdProductStockUniqueCode.setId(pdd.getRefBarCode());
                pdProductStockUniqueCode.setCodeState(PdConstant.CODE_PRINT_STATE_0);//正常状态
                productStockUniqueCodes.add(pdProductStockUniqueCode);
            }
            if(!logList.isEmpty())
                pdStockLogService.saveBatch(logList);
            pdDosageDetailService.updateBatchById(detailList);
            pdProductStockTotalService.updateRefBarCodeRetunuseStock(sysUser.getCurrentDepartId(),detailList);
            //批量更新条码状态
            pdProductStockUniqueCodeService.updateBatchById(productStockUniqueCodes);
        }
    }

}
