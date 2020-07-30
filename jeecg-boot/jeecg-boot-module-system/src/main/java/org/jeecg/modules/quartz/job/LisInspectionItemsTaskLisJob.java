package org.jeecg.modules.quartz.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.entity.ExLabPurpose;
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
import java.util.Map;

/**
 * 获取检验项目信息扣减库存定时任务(从LIS系统获取)
 *
 * @Author Scott
 */
@Slf4j
public class LisInspectionItemsTaskLisJob implements Job {

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
        ExInspectionItems item= new ExInspectionItems();
        //item.setQueryDateEnd(DateUtils.formatDate(DateUtils.getDayEnd()));//当日结束时间
        List<ExInspectionItems> list=hisChargeService.selectExjianYanLis(item);

        List<ExInspectionItems> fcList= hisChargeService.selectExjianYanLisFc(item);//获取复查/质控/以及没有申请号的检验数据

        list.addAll(fcList);//整合合并

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
                    String  testItemCode=items.getTestItemCode();
                    String  testItemName=items.getTestItemName();
                    if(StringUtils.isEmpty(testItemCode) && StringUtils.isEmpty(testItemName)){
                        items.setRemarks(items.getTestItemName()+"无组套ID,获取不到用量信息");
                        items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);// 0:已扣减  1：无检验项目  2：未扣减  3：无试剂用量
                    }else {
                        if(StringUtils.isEmpty(testItemCode) && StringUtils.isNotEmpty(testItemName)){
                            ExLabPurpose exLabPurpose=new ExLabPurpose();
                            exLabPurpose.setYq(items.getInstrCode());
                            exLabPurpose.setPruna(testItemName);
                           List<ExLabPurpose> purposeList= hisChargeService.selectExLabPurpose(exLabPurpose);
                           if(purposeList !=null && purposeList.size()>0){
                               ExLabPurpose purpose=  purposeList.get(0);
                               testItemCode=purpose.getPurno();
                               items.setTestItemCode(purpose.getPurno());
                               ExInspectionItems repItem = this.saveBatch(items, testItemCode);
                               items.setRemarks(repItem.getRemarks());
                               items.setAcceptStatus(repItem.getAcceptStatus());
                           }else{
                               items.setRemarks(items.getTestItemName()+"获取不到检验目的信息");
                               items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);// 0:已扣减  1：无检验项目  2：未扣减  3：无试剂用量
                           }
                        }else {
                            ExInspectionItems repItem1 = this.saveBatch(items, testItemCode);
                            items.setRemarks(repItem1.getRemarks());
                            items.setAcceptStatus(repItem1.getAcceptStatus());
                        }
                    }
                }
                exInspectionItemsService.saveBatch(list);
            }
        }
        log.info("根据检验项目扣减库存任务结束，时间:" + DateUtils.getTimestamp());
    }



    public  ExInspectionItems saveBatch(ExInspectionItems items,String testItemCode){
        LambdaQueryWrapper<PdUsePackage> query = new LambdaQueryWrapper<>();
        query.eq(PdUsePackage::getCode, testItemCode);
        PdUsePackage pdUsePackage = pdUsePackageService.getOne(query);
        //不存在或沒有配置檢驗用量明細
        if (pdUsePackage != null) {
            String testDpeartId = pdUsePackage.getTestDepartId();//检验科室ID
            String deductuinType = pdUsePackage.getDeductuinType();//扣减类型
            if (StringUtils.isEmpty(testDpeartId)) {
                items.setRemarks("未配置检验科室");
                items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);// 0:已扣减  1：无检验项目  2：未扣减  3：无试剂用量
            } else if (PdConstant.DEDUCTUIN_TYPE_1.equals(deductuinType)) {
                items.setRemarks("需人工扣减:" + pdUsePackage.getRemarks());
                items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);// 0:已扣减  1：无检验项目  2：未扣减  3：无试剂用量
            } else if (PdConstant.DEDUCTUIN_TYPE_2.equals(deductuinType)) {
                items.setRemarks("无需扣减:" + pdUsePackage.getRemarks());
                items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);// 0:已扣减  1：无检验项目  2：未扣减  3：无试剂用量
            } else {
                items.setPackageId(pdUsePackage.getId());
                PdUsePackageDetail detail = new PdUsePackageDetail();
                detail.setPackageId(pdUsePackage.getId());
                List<PdUsePackageDetail> pdUsePackageDetails = pdUsePackageDetailService.queryPdUsePackageList(detail);
                if (pdUsePackageDetails != null && pdUsePackageDetails.size() > 0) {
                    try {
                        // String bool = pdProductStockTotalService.lisUpdateUseStockLis(items, testDpeartId, pdUsePackageDetails);
                        Map map = pdProductStockTotalService.lisUpdateUseStockLis(items, testDpeartId, pdUsePackageDetails);
                        String code = MapUtils.getString(map, "code");
                        String msg = MapUtils.getString(map, "msg");
                        if ("400".equals(code)) {
                            items.setRemarks(items.getPatientType() + "病人用量未配置");
                            items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);//未扣减
                        } else if ("300".equals(code)) {
                            items.setRemarks(msg);
                            items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);//未扣减
                        } else if ("500".equals(code)) {
                            items.setRemarks(" ");
                            items.setAcceptStatus(PdConstant.ACCEPT_STATUS_4);//部分扣减
                        } else {
                            items.setRemarks(" ");
                            items.setAcceptStatus(PdConstant.ACCEPT_STATUS_0);//已扣减
                        }
                    } catch (Exception e) {
                        e.getMessage();
                        log.error("扣減用量失敗:" + e.getMessage());
                        items.setRemarks(e.getMessage());
                        items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);//2：未扣减
                    }
                } else {
                    items.setRemarks("检验项目用量未配置:" + pdUsePackage.getRemarks());
                    items.setAcceptStatus(PdConstant.ACCEPT_STATUS_3);// 0:已扣减  1：无检验项目  2：未扣减  3：无试剂用量
                }
            }
        } else {
            items.setRemarks("检验项目未配置");
            items.setAcceptStatus(PdConstant.ACCEPT_STATUS_1);//0:已扣减  1：无检验项目  2：未扣减  3：无试剂用量
        }

        return items;
    }
}
