package org.jeecg.modules.external.fengcheng.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.external.fengcheng.service.IPdDosageFCZYYService;
import org.jeecg.modules.external.fengcheng.util.HisApiForFCZhongyiUtils;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdStockLog;
import org.jeecg.modules.pd.mapper.PdDosageMapper;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author jiangxz
 * @description 丰城中医院计费
 * @date 2020-5-26
 */
@Service
public class PdDosageFCZYYServiceImpl extends ServiceImpl<PdDosageMapper, PdDosage> implements IPdDosageFCZYYService {

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

    private static Logger logger = LoggerFactory.getLogger(PdDosageFCZYYServiceImpl.class);

    @Override
    public List<PdDosage> queryPatientInfoList(PdDosage pdDosage) {
        JSONObject returnJson = HisApiForFCZhongyiUtils.queryHisPatientInfo(pdDosage.getMedicalRecordNo());
        List<PdDosage> dosageList= null;
        if(PdConstant.SUCCESS_0.equals(returnJson.getString("statusCode"))){
            JSONArray dosageArray = returnJson.getJSONArray("data");
            dosageList = new ArrayList<>();
            for(int i = 0; i < dosageArray.size() ; i++){
                JSONObject obj = dosageArray.getJSONObject(i);
                PdDosage pd = new PdDosage();
                pd.setId(i+"");
                pd.setVisitNo(obj.getString("vaa07"));//His患者就诊流水号
                pd.setInHospitalNo(obj.getString("hitaionNo"));//住院号
                pd.setPatientInfo(obj.getString("patientName"));//病人姓名
                pd.setFsfXb(obj.getString("sex"));//性别
                pd.setOperativeNumber(obj.getString("operNo"));//手术编号
                pd.setOperationName(obj.getString("projectName"));//项目名称
                pd.setOutpatientNumber(obj.getString("outpatCode"));//门诊号
                pd.setMedicalRecordNo("");//病历号
                pd.setSqrtDoctorId("");//开单科室ID
                pd.setOprDeptId(obj.getString("blngNo"));//执行科室id
                pd.setOprDeptName(obj.getString("blngName"));//执行科室名称
                pd.setSurgeonId(obj.getString("doctorCode"));//诊治医生
                pd.setSurgeonName(obj.getString("doctorName"));//诊治医生
                pd.setOprDate(obj.getString("createDate"));//登记日期
                pd.setBedNumber(obj.getString("bedCode"));//床位号
                pd.setSubordinateWardId(obj.getString("blngDptmNo"));//所属病区id
                pd.setSubordinateWardName(obj.getString("blngDptmName"));//所属病区名称
                dosageList.add(pd);
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
                    JSONObject result = HisApiForFCZhongyiUtils.exeCharge(pdDosage,chargeArray);
                    if(result == null || result.getJSONArray("data") == null || result.getJSONArray("data").size() <= 0){
                        logger.error("HIS返回数据为空，请重新计费或联系管理员！！");
                        throw new RuntimeException("HIS返回数据为空，请重新计费或联系管理员！！");
                    }

                    if(!PdConstant.SUCCESS_0.equals(result.getString("statusCode"))){
                        logger.error("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
                        throw new RuntimeException("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
                    }

                    JSONArray array = result.getJSONArray("data");
                    for(int k = 0; k < array.size(); k++){
                        JSONObject obj = array.getJSONObject(k);   // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        String prodNo = obj.getString("prodNo");//产品编码
                        String visitNo = obj.getString("vaa07");//就诊流水号
                        String hisChargeId = obj.getString("vai01");//计费单据id
                        String hisChargeItemId = obj.getString("vaj01");//计费单据明细id (退费用)
                        for(PdDosageDetail pdd : chargeArray){
                            if(pdd.getProductId().equals(prodNo)){
                                pdd.setHisChargeId(hisChargeId);
                                pdd.setHisChargeItemId(hisChargeItemId);
                            }
                        }
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
                pdProductStockTotalService.updateUseStock(sysUser.getCurrentDepartId(),afterDealList);
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