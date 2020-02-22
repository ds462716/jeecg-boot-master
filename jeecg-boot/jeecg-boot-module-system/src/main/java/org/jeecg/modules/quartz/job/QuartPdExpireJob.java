package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

/***
 * 更新库存总表及库存明细表的过期状态定时任务
 */
@Slf4j
public class QuartPdExpireJob implements Job {
    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;
    @Autowired
    private IPdProductStockService pdProductStockService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /**
         * 定时获取库存总表及明细表数据，进行判断是否超过设置的有效期值，并根据规则进行更新过期状态值
         *
         */
        log.info("-------------------更新过期状态开始-------------------");

        PdProductStockTotalPage stockTotalPage = new PdProductStockTotalPage();
        List<PdProductStock> list = pdProductStockService.findListForQuery(stockTotalPage);
        Map<String, Set<String>> m=new HashMap<String, Set<String>>();
        for (PdProductStock pdProductStock : list) {
            PdProductStock pd=new PdProductStock();
            String deptId = pdProductStock.getDeptId();
            Integer remind = 7;//有效期提醒
            Date validDate = pdProductStock.getExpDate();
            Date ndate = new Date();
            if((!DateUtils.isSameDay(ndate,validDate))&&ndate.after(validDate)){//过期
                pd.setExpStatus(PdConstant.PD_STATE_2);
            }
            Date afterMonthDate = DateUtils.getDateToAddDate(validDate, remind);
            if((ndate.before(validDate)&&ndate.after(afterMonthDate))||(DateUtils.isSameDay(ndate,validDate)||DateUtils.isSameDay(ndate,afterMonthDate))){//近效期
                pd.setExpStatus(PdConstant.PD_STATE_1);
            }

            String pdState = pd.getExpStatus();
            if(StringUtils.isNotEmpty(pdState) && !PdConstant.PD_STATE_0.equals(pdState)){
                pd.setId(pdProductStock.getId());
                pd.setDeptId(deptId);
                pdProductStockService.updateProductStock(pd);
                if(m.containsKey(deptId)){
                    Set<String> pids  = (Set<String>) m.get(deptId);
                    String pid = pdProductStock.getProductId();
                    if(pids.contains(pid)){
                        continue;
                    }else{
                        pids.add(pid);
                        m.put(deptId, pids);
                    }
                }else{
                    Set<String> pids=new HashSet<String>();
                    String pid = pdProductStock.getProductId();
                    pids.add(pid);
                    m.put(deptId, pids);
                }
            }
        }



        log.info("-------------------更新的主表产品状态-------------------");
        log.info(m.toString());
        Iterator<String> iter = m.keySet().iterator();
        while(iter.hasNext()){
            String deptId = iter.next();
            Set<String> set = m.get(deptId);
            for (String pid : set) {
                PdProductStockTotalPage totalPage = new PdProductStockTotalPage();
                totalPage.setDeptId(deptId);
                totalPage.setProductId(pid);
                List<PdProductStock> l = pdProductStockService.findListForQuery(totalPage);
                PdProductStock pk = l.get(0);
                PdProductStockTotal stockTotal = new PdProductStockTotal();
                stockTotal.setExpStatus(pk.getExpStatus());
                stockTotal.setProductId(pid);
                stockTotal.setDeptId(deptId);
                pdProductStockTotalService.updateProductStockTotal(stockTotal);
            }

        }
        log.info("-------------------更新过期状态结束-------------------");
    }
}
