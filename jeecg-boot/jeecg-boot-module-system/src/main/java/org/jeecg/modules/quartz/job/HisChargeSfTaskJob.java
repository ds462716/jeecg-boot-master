package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.HisChargeInf;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 获取HIS系统收费项目基础信息定时任务
 *
 * @Author Scott
 */
@Slf4j
public class HisChargeSfTaskJob implements Job {

    @Autowired
    private IHisChargeService hisChargeService;




    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /**
         * 定时获取HIS系统收费项目基础信息，并保存到基础信息表中
         * //步骤1：配置HIS系统数据源信息
         * //步骤2：调用HIS系统视图信息；视图名称:spd_his_sf_xm
         */
        log.info("获取HIS系统收费项目基础信息定时任务进来了，时间:" + DateUtils.getTimestamp());
        List<HisChargeInf> list=hisChargeService.selectByHisCharge();
        //if (list != null && list.size() > 0) {
           // hisChargeService.deleteChargeInf();
            //for(HisChargeInf inf:list){
                hisChargeService.saveMain(list);
            //}
        //}
        log.info("获取HIS系统收费项目基础信息定时任务结束，时间:" + DateUtils.getTimestamp());
    }
}
