package org.jeecg.modules.quartz.job;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.PdNumericalInf;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdNumericalInfService;
import org.jeecg.modules.system.entity.SysDepart;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 定时统计试剂/耗材消耗、使用、库存数量及金额统计报表
 *
 * @Author Scott
 */
@Slf4j
public class PdNumericalInfTaskJob implements Job {
    @Autowired
    private IPdNumericalInfService pdNumericalInfService;
    @Autowired
    private IPdDepartService pdDepartService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("定时统计试剂/耗材消耗、使用、库存数量及金额任务开始，时间:" + DateUtils.getTimestamp());
        SysDepart sysDepart=new SysDepart();
        sysDepart.setDepartTypeList(Arrays.asList("1,2".split(",")));
        List<SysDepart> departList=pdDepartService.selectList(sysDepart);//获取科室
        PdNumericalInf inf=pdNumericalInfService.getMonth(); //获取当前月份
        String month=inf.getMonth();
        // month ="2020-06";
        for(SysDepart depart : departList){
            //统计试剂类
            this.reagentCallCount(depart,month);
            //统计耗材类
            this.consumableCallCount(depart,month);
        }
        log.info("定时统计试剂/耗材消耗、使用、库存数量及金额任务结束，时间:" + DateUtils.getTimestamp());
    }




     //试剂类统计
    public void  reagentCallCount(SysDepart depart,String  month){
        //如果科室不用到试剂，则不需要统计  ，这里目前只能写死，后续在优化；
             if(!depart.getDepartId().equals("dea5919617234ef9854c5806d4b44efa")||
                !depart.getDepartId().equals("743dc34c1bcd4e4fa9503ccebce7edc6")||
                !depart.getDepartId().equals("298120171f5347e79686b5c2a2002a92")||
                !depart.getDepartId().equals("6032fec9fdb24007a74a2535859ac3f1")){
               return;
             }
        PdNumericalInf  numericalInf=new PdNumericalInf();
        numericalInf.setMonth(month);
        numericalInf.setDepartId(depart.getId());
        numericalInf.setTjType(PdConstant.PRODUCT_FLAG_1);


        PdNumericalInf numericalInf_1=new PdNumericalInf();
        numericalInf_1.setMonth(month);
        numericalInf_1.setDepartIdList(Arrays.asList(depart.getId()));
        numericalInf_1.setProductFlag(PdConstant.PRODUCT_FLAG_1);//查试剂
        /*统计   入库数量、入库金额*/
        numericalInf_1.setRecordType(PdConstant.RECODE_TYPE_1);
        numericalInf_1.setInTypeList(Arrays.asList("1"));
        if(!PdConstant.DEPART_TYPE_1.equals(depart.getDepartType())){
            numericalInf_1.setOutDepartIdList(Arrays.asList(depart.getParentId()));
        }
        numericalInf_1.setAuditStatus(PdConstant.AUDIT_STATE_2);
        HashMap sjMap=pdNumericalInfService.selectItemOrRecordNum(numericalInf_1);
        Double sjInRecordNum= MapUtils.getDouble(sjMap,"productNum");//入库数量
        Double sjInRecordPrice=MapUtils.getDouble(sjMap,"purchasePrice");//入库金额
        numericalInf.setInRecordNum(sjInRecordNum);//试剂入库数量
        numericalInf.setInRecordPrice(sjInRecordPrice != null ?   new BigDecimal(sjInRecordPrice) : BigDecimal.ZERO);//试剂入库金额

        /*统计  调入数量、调入金额*/
        numericalInf_1.setRecordType(PdConstant.RECODE_TYPE_1);
        numericalInf_1.setInTypeList(Arrays.asList("2,3,4".split(",")));//包括退货入库、调拨入库、盘盈入库
        numericalInf_1.setAuditStatus(PdConstant.AUDIT_STATE_2);
        HashMap callMap=pdNumericalInfService.selectItemOrRecordNum(numericalInf_1);
        Double callInNum= MapUtils.getDouble(callMap,"productNum");////调入数量
        Double callInNumPrice=MapUtils.getDouble(callMap,"purchasePrice");//调入金额
        numericalInf.setCallInNum(callInNum);//试剂调入数量
        numericalInf.setCallInPrice(callInNumPrice != null ?   new BigDecimal(callInNumPrice) : BigDecimal.ZERO);//试剂调入金额

        /*统计    使用数量  、使用金额*/
        //先判断当前科室是否是二级科室，并且二级科室是否存在三级科室，如果存在，则统一查询统计，数量及金额归并到二级科室下；
        String departType=depart.getDepartType();
        if(departType.equals("2")){
            List<String> departList_1=pdDepartService.queryDepartIdByParentId(depart.getId());
            if(CollectionUtils.isEmpty(departList_1)){
                departList_1=new ArrayList<String>();
            }
            departList_1.add(depart.getId());
            numericalInf_1.setDepartIdList(departList_1);
        }

        /*统计   库存数量 、库存金额*/
        HashMap  sjStockMap=pdNumericalInfService.selectStockNumOrStockPrice(numericalInf_1);
        Double sjStockNum= MapUtils.getDouble(sjStockMap,"stockCount");//试剂库存数量
        Double sjStockPrice=MapUtils.getDouble(sjStockMap,"stockPrice");//试剂库存金额
        numericalInf.setStockNum(sjStockNum);//试剂库存数量
        numericalInf.setStockPrice(sjStockPrice != null ?   new BigDecimal(sjStockPrice) : BigDecimal.ZERO);//试剂库存金额



        HashMap  itemMap=pdNumericalInfService.selectItemNumOrItemPrice(numericalInf_1);
        Double sjNum= MapUtils.getDouble(itemMap,"itemNum");//试剂使用数量
        BigDecimal sjPrice=(BigDecimal)MapUtils.getObject(itemMap,"itemPrice");//试剂使用金额
        numericalInf.setPurchaseNum(sjNum);//试剂使用数量
        numericalInf.setPurchasePrice(sjPrice);//试剂使用金额


        /*统计试剂理论使用规格数量   实际使用规格数量*/
        HashMap  itemSpecMap=pdNumericalInfService.selectItemSpecNum(numericalInf_1);
        Double specQuantityNum= MapUtils.getDouble(itemSpecMap,"specQuantityNum");//使用理论规格数量
        Double specRealityNum= MapUtils.getDouble(itemSpecMap,"specRealityNum");//实际使用规格数量
        Double disSpecNum= MapUtils.getDouble(itemSpecMap,"disSpecNum");//差异规格数量
        numericalInf.setSpecQuantityNum(specQuantityNum);//使用理论规格数量
        numericalInf.setSpecRealityNum(specRealityNum);//实际使用规格数量
        numericalInf.setDisSpecNum(disSpecNum);//差异规格数量

        /*统计    检验项目数量、 检验项目收入金额*/
        HashMap  map= pdNumericalInfService.selectExInspectionPrice(numericalInf_1);
        Double itemNum= MapUtils.getDouble(map,"num");//检验项目数量
        BigDecimal itemPrice=(BigDecimal)MapUtils.getObject(map,"price");//检验项目收入金额
        numericalInf.setItemNum(itemNum);//检验项目数量
        numericalInf.setItemPrice(itemPrice);//检验项目收入金额

        /*统计  调出数量、调出金额*/
        numericalInf_1.setRecordType(PdConstant.RECODE_TYPE_2);
        numericalInf_1.setAuditStatus(PdConstant.AUDIT_STATE_2);
        numericalInf_1.setInTypeList(null);
        numericalInf_1.setOutDepartIdList(numericalInf_1.getDepartIdList());
        numericalInf_1.setDepartIdList(null);
        if(!"1".equals(depart.getDepartType())){
            numericalInf_1.setOutTypeList(Arrays.asList("4".split(",")));
            numericalInf_1.setDepartIdList(Arrays.asList(depart.getParentId().split(",")));
        }
        HashMap callOutMap=pdNumericalInfService.selectItemOrRecordNum(numericalInf_1);
        Double callOutNum= MapUtils.getDouble(callOutMap,"productNum");////调出数量
        Double callOutNumPrice=MapUtils.getDouble(callOutMap,"purchasePrice");//调出金额
        numericalInf.setCallOutNum(callOutNum);//试剂调出数量
        numericalInf.setCallOutPrice(callOutNumPrice != null ?   new BigDecimal(callOutNumPrice) : BigDecimal.ZERO);//试剂调出金额


        /*统计   退货数量 、退货金额*/
        numericalInf_1.setDepartIdList(Arrays.asList(depart.getId()));
        HashMap  rejectedMap=pdNumericalInfService.selectrejectedNumOrPrice(numericalInf_1);
        Double rejectedNum= MapUtils.getDouble(rejectedMap,"rejectedNum");//退货数量
        Double rejectedPrice=MapUtils.getDouble(rejectedMap,"rejectedPrice");//退货金额
        numericalInf.setRejectedNum(rejectedNum != null ? rejectedNum : 0);//退货数量
        numericalInf.setRejectedPrice(rejectedPrice != null ?   new BigDecimal(rejectedPrice) : BigDecimal.ZERO);//退货金额


        //判断该科室当月是否已经存在数据，如果不存在，则新增，如果存在，则更新数据即可
        numericalInf_1.setTjType(PdConstant.PRODUCT_FLAG_1);
        numericalInf_1.setDepartIdList(Arrays.asList(depart.getId()));
        PdNumericalInf numericalInf_2=  pdNumericalInfService.getOne(numericalInf_1);
        if(ObjectUtils.isNotEmpty(numericalInf_2)){  //如果不为空
            numericalInf.setId(numericalInf_2.getId());
            pdNumericalInfService.updateById(numericalInf);
        }else{  //如果为空，则新增
            numericalInf.setTheStockNum(numericalInf.getStockNum());
            numericalInf.setTheStockPrice(numericalInf.getStockPrice());
            pdNumericalInfService.save(numericalInf);
        }
    }

    //耗材类统计
    public void  consumableCallCount(SysDepart depart,String  month){
       /* 统计月份   科室    入库数量	 入库金额   调入数量  调入金额    调出数量  调出金额	   库存数量    库存金额    使用数量    使用金额
           差异数量    差异金额  	月环比 */
        PdNumericalInf  numericalInf=new PdNumericalInf();
        numericalInf.setMonth(month);
        numericalInf.setDepartId(depart.getId());
        numericalInf.setTjType(PdConstant.PRODUCT_FLAG_0);

        PdNumericalInf numericalInf_1=new PdNumericalInf();
        numericalInf_1.setMonth(month);
        numericalInf_1.setDepartIdList(Arrays.asList(depart.getId()));
        numericalInf_1.setProductFlag(PdConstant.PRODUCT_FLAG_0);//查耗材
        /*统计   入库数量、入库金额*/
        numericalInf_1.setRecordType(PdConstant.RECODE_TYPE_1);
        numericalInf_1.setInTypeList(Arrays.asList("1"));
        numericalInf_1.setAuditStatus(PdConstant.AUDIT_STATE_2);
        HashMap hcMap=pdNumericalInfService.selectItemOrRecordNum(numericalInf_1);
        Double  inRecordNum= MapUtils.getDouble(hcMap,"productNum");//入库数量
        Double  inRecordPrice=MapUtils.getDouble(hcMap,"purchasePrice");//入库金额
        numericalInf.setInRecordNum(inRecordNum != null ? inRecordNum : 0);//入库数量
        numericalInf.setInRecordPrice(inRecordPrice != null ?   new BigDecimal(inRecordPrice) : BigDecimal.ZERO);//入库金额

        /*统计  调入数量、调入金额*/
        numericalInf_1.setRecordType(PdConstant.RECODE_TYPE_1);
        numericalInf_1.setInTypeList(Arrays.asList("2,3,4".split(",")));//包括退货入库、调拨入库、盘盈入库
        numericalInf_1.setAuditStatus(PdConstant.AUDIT_STATE_2);
        HashMap callMap=pdNumericalInfService.selectItemOrRecordNum(numericalInf_1);
        Double callInNum= MapUtils.getDouble(callMap,"productNum");////调入数量
        Double callInNumPrice=MapUtils.getDouble(callMap,"purchasePrice");//调入金额
        numericalInf.setCallInNum(callInNum != null ? callInNum : 0);//调入数量
        numericalInf.setCallInPrice(callInNumPrice != null ?   new BigDecimal(callInNumPrice) : BigDecimal.ZERO);//调入金额

        /*统计   使用数量、 使用金额*/
        //先判断当前科室是否是二级科室，并且二级科室是否存在三级科室，如果存在，则统一查询统计，数量及金额归并到二级科室下；
        String departType=depart.getDepartType();
        if(departType.equals("2")){
            List<String> departList_1=pdDepartService.queryDepartIdByParentId(depart.getId());
            if(CollectionUtils.isEmpty(departList_1)){
                departList_1=new ArrayList<String>();
            }
            departList_1.add(depart.getId());
            numericalInf_1.setDepartIdList(departList_1);
        }
        HashMap hcDosageMap = pdNumericalInfService.selectDosageNumOrPrice(numericalInf_1);
        Double hcNum = MapUtils.getDouble(hcDosageMap, "dosageCount");//耗材使用数量
        BigDecimal hcPrice = (BigDecimal) MapUtils.getObject(hcDosageMap, "dosagePrice");//耗材使用金额
        numericalInf.setPurchaseNum(hcNum != null ? hcNum : 0);//耗材使用数量
        numericalInf.setPurchasePrice(hcPrice);//耗材使用金额

        /*统计   库存数量 、库存金额*/
        HashMap  stockMap=pdNumericalInfService.selectStockNumOrStockPrice(numericalInf_1);
        Double stockNum= MapUtils.getDouble(stockMap,"stockCount");//库存数量
        Double stockPrice=MapUtils.getDouble(stockMap,"stockPrice");//库存金额
        numericalInf.setStockNum(stockNum != null ? stockNum : 0);//库存数量
        numericalInf.setStockPrice(stockPrice != null ?   new BigDecimal(stockPrice) : BigDecimal.ZERO);//库存金额


        /*统计  调出数量、调出金额*/
        numericalInf_1.setRecordType(PdConstant.RECODE_TYPE_2);
        numericalInf_1.setInTypeList(null);
        numericalInf_1.setOutDepartIdList(numericalInf_1.getDepartIdList());
        numericalInf_1.setDepartIdList(null);
        if(!"1".equals(depart.getDepartType())){
         numericalInf_1.setOutTypeList(Arrays.asList("4".split(",")));
         numericalInf_1.setDepartIdList(Arrays.asList(depart.getParentId().split(",")));
        }
        HashMap callOutMap=pdNumericalInfService.selectItemOrRecordNum(numericalInf_1);
        Double callOutNum= MapUtils.getDouble(callOutMap,"productNum");////调出数量
        Double callOutNumPrice=MapUtils.getDouble(callOutMap,"purchasePrice");//调出金额
        numericalInf.setCallOutNum(callOutNum != null ? callOutNum : 0);//调出数量
        numericalInf.setCallOutPrice(callOutNumPrice != null ?   new BigDecimal(callOutNumPrice) : BigDecimal.ZERO);//调出金额


        /*统计   退货数量 、退货金额*/
        numericalInf_1.setDepartIdList(Arrays.asList(depart.getId()));
        HashMap  rejectedMap=pdNumericalInfService.selectrejectedNumOrPrice(numericalInf_1);
        Double rejectedNum= MapUtils.getDouble(rejectedMap,"rejectedNum");//退货数量
        Double rejectedPrice=MapUtils.getDouble(rejectedMap,"rejectedPrice");//退货金额
        numericalInf.setRejectedNum(rejectedNum != null ? rejectedNum : 0);//退货数量
        numericalInf.setRejectedPrice(rejectedPrice != null ?   new BigDecimal(rejectedPrice) : BigDecimal.ZERO);//退货金额

        //判断该科室当月是否已经存在数据，如果不存在，则新增，如果存在，则更新数据即可
        numericalInf_1.setTjType(PdConstant.PRODUCT_FLAG_0);
        numericalInf_1.setDepartIdList(Arrays.asList(depart.getId()));
        PdNumericalInf numericalInf_2=  pdNumericalInfService.getOne(numericalInf_1);

        if(ObjectUtils.isNotEmpty(numericalInf_2)){//如果不为空
            numericalInf.setId(numericalInf_2.getId());
            pdNumericalInfService.updateById(numericalInf);
        }else{  //如果为空，则新增
            numericalInf.setTheStockNum(numericalInf.getStockNum());
            numericalInf.setTheStockPrice(numericalInf.getStockPrice());
            pdNumericalInfService.save(numericalInf);
        }
    }
}
