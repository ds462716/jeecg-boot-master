package org.jeecg.modules.quartz.job;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.service.IPdDepartConfigService;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/***
 * 更新库存总表及库存明细表的久存状态定时任务
 */
@Slf4j
public class QuartPdStockLongJob implements Job {
    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private IPdProductStockService pdProductStockService;
    @Autowired
    private IPdDepartConfigService PdDepartConfigService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /**
         * 定时获取库存总表及明细表数据，进行判断是否超过设置的久存值，并根据规则进行更新久存状态值
         *
         */
        log.info("-------------------更新久存状态开始-------------------");
        Integer stockRemind = Integer.valueOf(PdConstant.REMINDER_DETE_4);//设定的常量值（默认的有效期限）
        List<PdProductStock> list = pdProductStockService.selectList(new PdProductStock());
        Map<String, Set<String>> m=new HashMap<String, Set<String>>();
        for (PdProductStock pdProductStock : list) {
            PdProductStock pd = new PdProductStock();
            String deptId = pdProductStock.getDepartId();
            String departParentId = pdProductStock.getDepartParentId();
            Date createTime = pdProductStock.getCreateTime();
            Date date = new Date();
            String stockDay = PdDepartConfigService.findPdDepartConfig(PdConstant.REMINDER_TYPE_4,departParentId);
            if (!StringUtils.isEmpty(stockDay)) {
                stockRemind = Integer.valueOf(stockDay);
            }
            if (ObjectUtil.isNotEmpty(createTime)){
                Date afterMonth = DateUtils.getDateToAddDate(createTime, stockRemind);
                if((DateUtils.isSameDay(date,afterMonth)) && date.after(afterMonth)){
                    pd.setIsLong(PdConstant.IS_LONG_1);
                }
                String isLong = pd.getIsLong();
                if (StringUtils.isNotEmpty(isLong) && !PdConstant.IS_LONG_0.equals(isLong)) {
                    pd.setId(pdProductStock.getId());
                    pd.setDepartId(deptId);
                    pdProductStockService.updateProductStock(pd);
                    if (m.containsKey(deptId)) {
                        Set<String> pids = (Set<String>) m.get(deptId);
                        String pid = pdProductStock.getProductId();
                        if (pids.contains(pid)) {
                            continue;
                        } else {
                            pids.add(pid);
                            m.put(deptId, pids);
                        }
                    } else {
                        Set<String> pids = new HashSet<String>();
                        String pid = pdProductStock.getProductId();
                        pids.add(pid);
                        m.put(deptId, pids);
                    }
                }
            }
        }


        log.info("-------------------更新的主表产品久存状态-------------------");
        log.info(m.toString());
        Iterator<String> iter = m.keySet().iterator();
        while(iter.hasNext()){
            String deptId = iter.next();
            Set<String> set = m.get(deptId);
            for (String pid : set) {
                PdProductStock productStock = new PdProductStock();
                productStock.setDepartId(deptId);
                productStock.setProductId(pid);
                List<PdProductStock> l = pdProductStockService.selectList(productStock);
                PdProductStock pk = l.get(0);
                PdProductStockTotal stockTotal = new PdProductStockTotal();
                stockTotal.setIsLong(pk.getIsLong());
                stockTotal.setProductId(pid);
                stockTotal.setDepartId(deptId);
                pdProductStockTotalService.updateProductStockTotal(stockTotal);
            }

        }
        log.info("-------------------更新久存状态结束-------------------");
    }


}
