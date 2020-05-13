package org.jeecg.modules.quartz.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.service.IExInspectionItemsService;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
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
 * 获取HIS系统收费项目基础信息定时任务
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

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /**
         * 定时获取HIS系统收费项目基础信息，并保存到基础信息表中
         * //步骤1：配置Lis系统数据源信息
         * //步骤2：调用Lis系统视图信息；视图名称:XXX
         */
        log.info("获取LIS检验项目任务开始，时间:" + DateUtils.getTimestamp());
        List<ExInspectionItems> list=exInspectionItemsService.selectList(new ExInspectionItems());
        if(list!=null && list.size()>0){
            List<String> ids = exInspectionItemsService.selectListIds();
            //增量同步
            Iterator<ExInspectionItems> it = list.iterator();
            while(it.hasNext()){
                ExInspectionItems exInspectionItems= it.next();
                if(ids.contains(exInspectionItems.getId())){
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
                            }catch (Exception e){
                                e.getMessage();
                                log.error("扣減用量失敗:" + e.getMessage());
                                items.setAcceptStatus("0");
                            }
                        }
                    }else{
                        //TODO 是否发消息给管理
                    }
                }
                exInspectionItemsService.saveBatch(list);
            }
        }


        log.info("获取LIS检验项目任务结束，时间:" + DateUtils.getTimestamp());
    }
}
