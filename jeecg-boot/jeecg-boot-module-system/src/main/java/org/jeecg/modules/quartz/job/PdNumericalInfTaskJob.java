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
        //统计月份	所属科室	试剂入库数量	试剂入库金额 耗材入库数量	耗材入库金额  试剂库存量	试剂库存金额  耗材库存数量	耗材库存金额
        //试剂使用金额	试剂使用数量			耗材使用数量	耗材使用金额		检验项目收入金额	检验项目数量

        SysDepart sysDepart=new SysDepart();
        sysDepart.setDepartTypeList(Arrays.asList("1,2".split(",")));
        List<SysDepart> departList=pdDepartService.selectList(sysDepart);//获取科室

        PdNumericalInf inf=pdNumericalInfService.getMonth(); //获取当前月份
        String month=inf.getMonth();
          month="2020-06";
        for(SysDepart depart : departList){
            PdNumericalInf numericalInf=new PdNumericalInf();
            numericalInf.setMonth(month);
             //先获取该科室下耗材入库数量及耗材入库金额
            PdNumericalInf pdNumericalInf=new PdNumericalInf();
            pdNumericalInf.setProductFlag(PdConstant.PRODUCT_FLAG_0);//先查耗材
            pdNumericalInf.setMonth(month);
            pdNumericalInf.setDepartId(depart.getId());
            HashMap  hcMap=pdNumericalInfService.selectItemOrRecordNum(pdNumericalInf);
            Double hcInRecordNum= MapUtils.getDouble(hcMap,"productNum");//耗材入库数量
            BigDecimal hcInRecordPrice=(BigDecimal)MapUtils.getObject(hcMap,"purchasePrice");//耗材入库金额
            numericalInf.setHcInRecordNum(hcInRecordNum);//耗材入库数量
            numericalInf.setHcInRecordPrice(hcInRecordPrice);//耗材入库金额

            pdNumericalInf.setProductFlag(PdConstant.PRODUCT_FLAG_1);//查试剂
            pdNumericalInf.setMonth(month);
            HashMap  sjMap=pdNumericalInfService.selectItemOrRecordNum(pdNumericalInf);
            Double sjInRecordNum= MapUtils.getDouble(sjMap,"productNum");//试剂入库数量
            BigDecimal sjInRecordPrice=(BigDecimal)MapUtils.getObject(sjMap,"purchasePrice");//试剂入库金额
            numericalInf.setSjInRecordNum(sjInRecordNum);//试剂入库数量
            numericalInf.setSjInRecordPrice(sjInRecordPrice);//试剂入库金额


            //先判断当前科室是否是二级科室，并且二姐科室是否存在三级科室，如果存在，则统一查询统计，数量及金额归并到二级科室下；
            String departType=depart.getDepartType();
            if(departType.equals("2")){
                List<String> departList_1=pdDepartService.queryDepartIdByParentId(depart.getId());
                 if(CollectionUtils.isEmpty(departList_1)){
                     departList_1=new ArrayList<String>();
                }
                departList_1.add(depart.getId());
                pdNumericalInf.setDepartIdList(departList_1);
            }
            pdNumericalInf.setProductFlag(PdConstant.PRODUCT_FLAG_1);//查试剂
            HashMap  sjStockMap=pdNumericalInfService.selectStockNumOrStockPrice(pdNumericalInf);
            Double sjStockNum= MapUtils.getDouble(sjStockMap,"stockCount");//试剂库存数量
            Double sjStockPrice=MapUtils.getDouble(sjStockMap,"stockPrice");//试剂库存金额
            numericalInf.setSjStockNum(sjStockNum);//试剂库存数量
            numericalInf.setSjStockPrice(new BigDecimal(sjStockPrice));//试剂库存金额

            pdNumericalInf.setProductFlag(PdConstant.PRODUCT_FLAG_0);//查耗材
            HashMap  hcStockMap=pdNumericalInfService.selectStockNumOrStockPrice(pdNumericalInf);
            Double hcStockNum= MapUtils.getDouble(hcStockMap,"stockCount");//耗材库存数量
            Double hcStockPrice=MapUtils.getDouble(hcStockMap,"stockPrice");//耗材库存金额
            numericalInf.setHcStockNum(hcStockNum);//耗材库存数量
            numericalInf.setHcStockPrice(new BigDecimal(hcStockPrice));//耗材库存金额




            HashMap  hcDosageMap=pdNumericalInfService.selectDosageNumOrPrice(pdNumericalInf);
            Double hcNum= MapUtils.getDouble(hcDosageMap,"dosageCount");//耗材使用数量
            BigDecimal hcPrice=(BigDecimal)MapUtils.getObject(hcDosageMap,"dosagePrice");//耗材使用金额
            numericalInf.setHcNum(hcNum);//耗材使用数量
            numericalInf.setHcPrice(hcPrice);//耗材使用金额


            HashMap  itemMap=pdNumericalInfService.selectItemNumOrItemPrice(pdNumericalInf);
            Double sjNum= MapUtils.getDouble(itemMap,"itemNum");//试剂使用数量
            BigDecimal sjPrice=(BigDecimal)MapUtils.getObject(itemMap,"itemPrice");//试剂使用金额
            numericalInf.setSjNum(sjNum);//试剂使用数量
            numericalInf.setSjPrice(sjPrice);//试剂使用金额

         //判断是否是检验科，如果是检验科则统计
            HashMap  map= pdNumericalInfService.selectExInspectionPrice(pdNumericalInf);
            Double itemNum= MapUtils.getDouble(map,"num");//检验项目数量
            BigDecimal itemPrice=(BigDecimal)MapUtils.getObject(map,"price");//检验项目收入金额
            numericalInf.setItemNum(itemNum);//检验项目数量
            numericalInf.setItemPrice(itemPrice);//检验项目收入金额

            //判断该科室当月是否已经存在数据，如果不存在，则新增，如果存在，则更新数据即可
            numericalInf.setDepartId(depart.getId());
            PdNumericalInf numericalInf_1=  pdNumericalInfService.getOne(numericalInf);
           if(ObjectUtils.isNotEmpty(numericalInf_1)){  //如果不为空
               numericalInf.setId(numericalInf_1.getId());
               pdNumericalInfService.updateById(numericalInf);
           }else{  //如果为空，则新增
               pdNumericalInfService.save(numericalInf);
           }
        }
        log.info("定时统计试剂/耗材消耗、使用、库存数量及金额任务结束，时间:" + DateUtils.getTimestamp());
    }
}
