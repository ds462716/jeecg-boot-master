package org.jeecg.modules.external.fengcheng.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.fengcheng.service.IPdDosageFCRMYYService;
import org.jeecg.modules.external.fengcheng.util.HisApiForFCRenminUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.PdDosageMapper;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataUnit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author jiangxz
 * @description 丰城中医院计费
 * @date 2020-5-26
 */
@Service
public class PdDosageFCRMYYServiceImpl extends ServiceImpl<PdDosageMapper, PdDosage> implements IPdDosageFCRMYYService {

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

    private static Logger logger = LoggerFactory.getLogger(PdDosageFCRMYYServiceImpl.class);

    @Override
    public PdDosage initModal(PdDosage param) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysDepart sysDepart = pdDepartService.getById(sysUser.getCurrentDepartId());
        PdDosage pdDosage = new PdDosage();
        if (pdDosage !=null && oConvertUtils.isNotEmpty(param.getId())) { // 查看页面
            pdDosage = this.getById(param.getId());
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
            pdDosageDetail.setDosageId(param.getId());
            pdDosageDetail.setHyCharged(param.getDhyCharged());
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
        return pdDosage;
    }

    @Override
    public List<PdDosage> queryPatientInfoList(PdDosage pdDosage) {
        JSONObject returnJson = HisApiForFCRenminUtils.queryHisPatientInfo(pdDosage);
        List<PdDosage> dosageList= null;
        if(PdConstant.SUCCESS_0.equals(returnJson.getString("statusCode"))){
            try{
                JSONArray dosageArray = returnJson.getJSONArray("data");
                // his返回报文最后会带一条空数据，这边把空数据移除
                for (int i = 0; i < dosageArray.size(); i++){
                    if(oConvertUtils.isEmpty(dosageArray.getJSONObject(i).getString("hitaionNo"))){
                        dosageArray.remove(i);
                    }
                }
                dosageList = new ArrayList<>();
                for(int i = 0; i < dosageArray.size() ; i++){
                    JSONObject obj = dosageArray.getJSONObject(i);
                    PdDosage pd = new PdDosage();
                    pd.setId(i+"");
                    pd.setOperativeNumber(obj.getString("operNo"));//手术编号
                    pd.setSubordinateWardId(obj.getString("blngDptmNo"));//所属病区id
                    pd.setSubordinateWardName(obj.getString("blngDptmName"));//所属病区名称
                    pd.setOprDeptId(obj.getString("blngNo"));//科室id
                    pd.setOprDeptName(obj.getString("blngName"));//科室名称
                    pd.setType(obj.getString("type"));//住院标识（1：是  2：否）
                    pd.setOperationName(obj.getString("projectName"));//项目名称
                    pd.setExtension1(obj.getString("departName"));//手术或检查项目科室
                    if(oConvertUtils.isNotEmpty(obj.getString("createDate"))){
                        String date = DateUtils.dateformat(obj.getString("createDate"),"yyyyMMdd","yyyy-MM-dd");
                        pd.setOprDate(date);//登记日期
                    }
                    pd.setSurgeonId(obj.getString("doctorCode"));//诊治医生
                    pd.setSurgeonName(obj.getString("doctorName"));//诊治医生
                    pd.setOutpatientNumber(obj.getString("outpatCode"));//门诊号
                    pd.setFsfXb(obj.getString("sex"));//性别
                    pd.setPatientInfo(obj.getString("patientName"));//病人姓名
                    pd.setInHospitalNo(obj.getString("hitaionNo"));//住院号
                    pd.setBedNumber(obj.getString("bedCode"));//床位号
                    dosageList.add(pd);
                }
            }catch (Exception e){
                //his不到数据，会返回一个空JSONObject，不会返回一个空数组，返回报文如下。  转换JSONArray会抛异常，这边捕获异常，并直接返回null
//                { "code": "0", "msg": "", "data": { "aplyNo": "0", "operNo": "0", "blngDptmNo": "", "blngDptmName": "", "blngNo": "", "blngName": "", "type": "", "projectName": "", "departName": "", "createDate": "", "doctorCode": "", "doctorName": "", "outpatCode": "", "sex": "", "patientName": "", "hitaionNo": "", "bedCode": "" } }
                return null;
            }

        }
        return dosageList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PdDosageDetail> saveMain(PdDosage pdDosage, String displayFlag) {
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
                if(PdConstant.CHARGE_FLAG_0.equals(pdDosage.getHyCharged()) && PdConstant.CHARGE_FLAG_0.equals(pdd.getIsCharge()) && !"".equals(pdd.getChargeCode())){
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
                prodLog.setOutTo("病人:"+pdDosage.getPatientInfo()!=null?pdDosage.getPatientInfo():"");
                prodLog.setPatientInfo(pdDosage.getPatientDetailInfo());
                prodLog.setInvoiceNo(pdDosage.getDosageNo());
                prodLog.setChargeDeptName(pdDosage.getExeDeptName());
                prodLog.setRecordTime(DateUtils.getDate());
                logList.add(prodLog);
            }
            if (!validFlag) {//数据校验没通过
                throw new JeecgBootException("数据校验失败");
            } else {

                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                pdDosage.setDisplayFlag(displayFlag);//是否有收费接口标识，0有1没有
                pdDosage.setTotalSum(dosageTotal.doubleValue());
                pdDosage.setUpdateTime(DateUtils.getDate());
                pdDosage.setTotalPrice(moneyTotal);
                pdDosage.setDosageBy(sysUser.getId());
                pdDosage.setDosageDate(DateUtils.getDate());

                if (PdConstant.CHARGE_FLAG_0.equals(pdDosage.getHyCharged()) && chargeArray.size() > 0){
                    // 计费接口
                    JSONObject result = HisApiForFCRenminUtils.exeCharge(pdDosage,chargeArray);
                    if(!PdConstant.SUCCESS_0.equals(result.getString("statusCode"))){
                        logger.error("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
                        throw new RuntimeException("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
                    }

                    pdDosageDetailService.saveBatch(chargeArray);
                }
                if(!tempArray.isEmpty()){
                    pdDosageDetailService.saveBatch(tempArray);
                }
                if(!logList.isEmpty()){
                    pdStockLogService.saveBatch(logList);
                }
                //保存用量
                this.save(pdDosage);
                //扣减当前库房的库存
                pdProductStockTotalService.updateUseStock(sysUser.getCurrentDepartId(),detailList);
            }
        }
        return chargeArray;
    }

    @Override
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
                prodLog.setOutTo("病人:"+pdDosage.getPatientInfo()!=null?pdDosage.getPatientInfo():"");
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

    @Override
    public void dosageFee(PdDosage pdDosage) {
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();

        //更新病人信息
        PdDosage update = new PdDosage();
        update.setId(pdDosage.getId());
        update.setPatientInfo(pdDosage.getPatientInfo());
        update.setPatientDetailInfo(pdDosage.getPatientDetailInfo());
        update.setExeDeptId(pdDosage.getExeDeptId());
        update.setExeDeptName(pdDosage.getExeDeptName());
        update.setOprDeptId(pdDosage.getOprDeptId());
        update.setOprDeptName(pdDosage.getOprDeptName());
        update.setSurgeonId(pdDosage.getSurgeonId());
        update.setSurgeonName(pdDosage.getSurgeonName());
        update.setSqrtDoctorId(pdDosage.getSqrtDoctorId());
        update.setSqrtDoctorName(pdDosage.getSqrtDoctorName());
        update.setInHospitalNo(pdDosage.getInHospitalNo());
        update.setSubordinateWardId(pdDosage.getSubordinateWardId());
        update.setSubordinateWardName(pdDosage.getSubordinateWardName());
        update.setOutpatientNumber(pdDosage.getOutpatientNumber());
        update.setPatientType(pdDosage.getPatientType());
        update.setOperativeNumber(pdDosage.getOperativeNumber());
        update.setOperationName(pdDosage.getOperationName());
        update.setVisitNo(pdDosage.getVisitNo());
        pdDosageMapper.updateById(update);

        if(detailList!=null && detailList.size()>0){
            for(PdDosageDetail pdd : detailList){
                pdd.setHyCharged(PdConstant.CHARGE_FLAG_0);
            }
            pdDosageDetailService.updateBatchById(detailList);
        }
    }


    //合并相同的用量
//    private List<PdDosageDetail> dealRepeatData(final List<PdDosageDetail> list){
//        List<PdDosageDetail> tempArray = new ArrayList<PdDosageDetail>();
//        Set<String> pids = new HashSet<String>();
//        if(list != null && list.size() > 0){
//            for(PdDosageDetail temp : list){
//                String expdate = DateUtils.date2Str(temp.getExpDate(),DateUtils.yyMMdd.get());
//                if (temp == null || StringUtils.isEmpty(temp.getProductId()) || StringUtils.isEmpty(temp.getProductBarCode())
//                        || StringUtils.isEmpty(temp.getBatchNo()) || StringUtils.isEmpty(expdate)) {
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

}
