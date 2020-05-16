package org.jeecg.modules.quartz.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.service.IExInspectionItemsService;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.service.IPdUsePackageDetailService;
import org.jeecg.modules.pd.service.IPdUsePackageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

/**
 * 获取检验项目信息扣减库存定时任务
 *
 * @Author Scott
 */
@Slf4j
public class LisInspectionItemsTaskJob implements Job {

    @Autowired
    private IExInspectionItemsService exInspectionItemsService;

    @Autowired
    private IPdUsePackageService pdUsePackageService;

    @Autowired
    private IPdUsePackageDetailService pdUsePackageDetailService;

    @Autowired
    private IPdProductStockTotalService pdProductStockTotalService;

    @Autowired
    private IHisChargeService hisChargeService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("根据检验项目扣减库存任务开始，时间:" + DateUtils.getTimestamp());
        List<ExInspectionItems> list=hisChargeService.selectExjianYan(new ExInspectionItems());
        if(list!=null && list.size()>0){
            List<String> jyIds = exInspectionItemsService.selectListIds();
            //增量同步
            Iterator<ExInspectionItems> it = list.iterator();
            while(it.hasNext()){
                ExInspectionItems exInspectionItems= it.next();
                if(jyIds.contains(exInspectionItems.getJyId())){
                    it.remove();
                }
            }
            if(list!=null && list.size()>0){
                //扣减库存
                for(ExInspectionItems items :list){
                    LambdaQueryWrapper<PdUsePackage> query = new LambdaQueryWrapper<>();
                    query.eq(PdUsePackage::getCode, items.getTestItemCode());
                    PdUsePackage pdUsePackage = pdUsePackageService.getOne(query);
                    //不存在或沒有配置檢驗用量明細
                    if(pdUsePackage!=null){
                        LambdaQueryWrapper<PdUsePackageDetail> queryOne = new LambdaQueryWrapper<>();
                        queryOne.eq(PdUsePackageDetail::getPackageId, pdUsePackage.getId());
                        List<PdUsePackageDetail> pdUsePackageDetails = pdUsePackageDetailService.list(queryOne);
                        if(pdUsePackageDetails!=null && pdUsePackageDetails.size()>0){
                            try{
                                pdProductStockTotalService.lisUpdateUseStock(items.getTestDepartment(),pdUsePackageDetails);
                                items.setAcceptStatus("0");//已扣减
                            }catch (Exception e){
                                e.getMessage();
                                log.error("扣減用量失敗:" + e.getMessage());
                                items.setRemarks(e.getMessage());
                                items.setAcceptStatus("2");
                            }
                        }
                    }else{
                        items.setRemarks("检验项目用量未配置");
                        items.setAcceptStatus("1");// 0：已扣减  1：未配置检验项目用量  2:未扣减
                    }
                }
                exInspectionItemsService.saveBatch(list);
            }
        }


        log.info("根据检验项目扣减库存任务结束，时间:" + DateUtils.getTimestamp());
    }
}
