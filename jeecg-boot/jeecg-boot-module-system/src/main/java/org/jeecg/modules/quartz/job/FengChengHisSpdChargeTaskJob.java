package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.external.fengcheng.service.IHisSpdChargeFCService;
import org.jeecg.modules.external.fengcheng.service.IPdDosageFCRMYYService;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.service.IPdProductService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author jiangxz
 * @description 丰城门诊收费定时任务
 * @date 2020-6-18
 */
@Slf4j
public class FengChengHisSpdChargeTaskJob implements Job {

    @Autowired
    private IHisSpdChargeFCService hisSpdChargeFCService;
    @Autowired
    private IPdDosageFCRMYYService pdDosageFCRMYYService;
    @Autowired
    private IPdProductService pdProductService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        PdDosage pdDosage = new PdDosage();
        //1.查询his视图，门诊收费嘻嘻
        List<PdDosage> pdDosageList = hisSpdChargeFCService.selectList(pdDosage);

        if(CollectionUtils.isNotEmpty(pdDosageList)){
            for(PdDosage dosage : pdDosageList){
                PdDosageDetail pdDosageDetail = new PdDosageDetail();
                pdDosageDetail.setHisChargeId(dosage.getExtension2());
                //2.查询门诊收费明细
                List<PdDosageDetail> detailList = hisSpdChargeFCService.selectDetailList(pdDosageDetail);
                if(CollectionUtils.isNotEmpty(detailList)){
                    dosage.setPdDosageDetails(detailList);
                    for(PdDosageDetail detail : detailList){
                        PdProduct entity = new PdProduct();
                        entity.setChargeCode(detail.getChargeCode());
                        //3.根据收费代码查询产品信息
                        List<PdProduct> pdProductList = pdProductService.selectListByChargeCode(entity);
                        if(CollectionUtils.isNotEmpty(pdProductList)){
                            PdProduct pdProduct = pdProductList.get(0);
                            //4.根据产品查询库存信息
                            //5.依次扣减效期最近的库存，如果是退费，则需要加上库存
                        }


                        if(detail.getDosageCount() < 0){
                            detail.setHyCharged(PdConstant.CHARGE_FLAG_3);
                        }else{
                            detail.setHyCharged(PdConstant.CHARGE_FLAG_0);
                        }
                    }
                    pdDosageFCRMYYService.saveMainMZ(dosage, PdConstant.IS_CHARGE_FLAG_0);
                }
            }
        }

    }
}
