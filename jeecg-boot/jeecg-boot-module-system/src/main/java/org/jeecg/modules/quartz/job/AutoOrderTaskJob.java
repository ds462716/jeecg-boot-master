package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.PdAutoOrderInf;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.service.IPdApplyOrderService;
import org.jeecg.modules.pd.service.IPdAutoOrderService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 库存不足自动补货定时任务
 *
 * @Author Scott
 */
@Slf4j
public class AutoOrderTaskJob implements Job {

    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IPdApplyOrderService pdApplyOrderService;
    @Autowired
    private IPdAutoOrderService autoOrderService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /**
         * 1.先获取所有库存总表中设置了库存下限及自动补货数量的数据
         * 2.循环数据，对比库存总表库存数量及库存下限数量，如果库存数量小于库存下限数量，则自动补货
         * 3.根据科室来存储需要补货的产品数据
         */
        log.info("库存不足自动补货定时任务进来了，时间:" + DateUtils.getTimestamp());
        PdProductStockTotal total = new PdProductStockTotal();
        List<PdProductStockTotalPage> list = pdProductStockTotalService.findListForAutoNum(total);
        Map<String, HashSet<PdProductStockTotal>> m = new HashMap<String, HashSet<PdProductStockTotal>>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (PdProductStockTotal stockTotal : list) {
                PdProductStockTotal stockTotal_1=new PdProductStockTotal();
                org.springframework.beans.BeanUtils.copyProperties(stockTotal,stockTotal_1);
                String departId = stockTotal.getDepartId();//库存科室
                Double stockNum = stockTotal.getStockNum();//库存数量
                Double limitDown = stockTotal.getLimitDown();//库存下限
                Double autoNum = stockTotal.getAutoNum();//自动补货量
                if (stockNum < limitDown) { //需要补货
                    //判断是一级科室还是二级科室，如果是二级科室，则生成申领单，
                    // 如果是一级科室，则生成采购单
                    SysDepart sysDepart = sysDepartService.queryDepartByOrgCode(stockTotal.getSysOrgCode());
                    if (sysDepart.getParentId().equals(stockTotal.getDepartParentId())) { //说明是一级科室
                        //生成采购单




                    } else {
                        //判断是否已经生成过申领单了
                        PdAutoOrderInf autoOrderInfo = new PdAutoOrderInf();
                        autoOrderInfo.setDepartId(departId);
                        autoOrderInfo.setProductId(stockTotal.getProductId());
                        List<PdAutoOrderInf> autoList = autoOrderService.queryList(autoOrderInfo);
                        if (CollectionUtils.isEmpty(autoList) || autoList.size()==0) {
                            if (m.containsKey(departId)) {
                                HashSet<PdProductStockTotal> totals = (HashSet<PdProductStockTotal>)m.get(departId);
                                totals.add(stockTotal_1);
                                 m.put(departId, totals);
                            } else {
                                HashSet<PdProductStockTotal> totals =new HashSet<PdProductStockTotal>();
                                totals.add(stockTotal_1);
                                m.put(departId, totals);
                           }
                        //保存到表里
                            PdAutoOrderInf info = new PdAutoOrderInf();
                            info.setDepartId(departId);
                            info.setProductId(stockTotal.getProductId());
                            info.setAutoDate(new Date());
                            info.setAutoNum(autoNum);
                            info.setLimitDown(limitDown);
                            info.setStockNum(stockNum);
                            info.setOrderType("0");
                        autoOrderService.save(info);
                }
            }
                } else {
                    //如果不需要补货，查询下之前是否已经生成过申领单或采购单了，
                    // 如果之前已经生成了，则逻辑删除掉该订单
                    PdAutoOrderInf autoOrderInfo = new PdAutoOrderInf();
                    autoOrderInfo.setDepartId(departId);
                    autoOrderInfo.setProductId(stockTotal.getProductId());
                    List<PdAutoOrderInf> autoList = autoOrderService.queryList(autoOrderInfo);
                    if (CollectionUtils.isNotEmpty(autoList)) {
                        for (PdAutoOrderInf info : autoList) {
                            autoOrderService.removeById(info.getId());
                        }
                    }
                }
            }
        }
//---------------------------------------
        //生成申领单
        log.info(m.toString());
        Iterator<String> iter = m.keySet().iterator();
           while (iter.hasNext()) {
            String deptId = iter.next();
               Set<PdProductStockTotal> set = m.get(deptId);
            pdApplyOrderService.autoApplyOrderInfo(set, deptId);
        }
//--------------------------------------------
        log.info("库存不足自动补货定时任务结束了，时间:" + DateUtils.getTimestamp());
    }
}
