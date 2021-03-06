package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.service.IExHisZyInfService;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.jeecg.modules.pd.service.IPdDosageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Description: 用量表
* @Author: zxh
* @Date:   2020-03-13
* @Version: V1.0
*/
@Api(tags="用量表")
@RestController
@RequestMapping("/pd/newPdDosage")
@Slf4j
public class NewPdDosageController {
   @Autowired
   private IHisChargeService hisChargeService;

    @Autowired
    private IPdDosageService pdDosageService;

    @Autowired
    private IExHisZyInfService exHisZyInfService;

    /**
     * 提交
     *
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/submit")
    public Result<?> submit(@RequestBody PdDosage pdDosage) {
        List<PdDosageDetail> list= pdDosageService.newSaveMain(pdDosage, PdConstant.IS_CHARGE_FLAG_0);
         //数据推送到HIS中间表
        if(CollectionUtils.isNotEmpty(list)){
              if(StringUtils.isNotEmpty(pdDosage.getInHospitalNo())){//住院
                  exHisZyInfService.saveExHisZyInf(pdDosage, list,PdConstant.IS_CHARGE_TYPE_1);
              }else{//门诊
                  exHisZyInfService.saveExHisMzInf(pdDosage, list,PdConstant.IS_CHARGE_TYPE_1);
              }
        }
         return Result.ok("添加成功！");
    }


    /**
     * 提交
     *
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/uniqueSubmit")
    public Result<?> uniqueSubmit(@RequestBody PdDosage pdDosage) {
        List<PdDosageDetail> list= pdDosageService.uniqueSubmit(pdDosage, PdConstant.IS_CHARGE_FLAG_0);
        //数据推送到HIS中间表
        if(CollectionUtils.isNotEmpty(list)){
            if(StringUtils.isNotEmpty(pdDosage.getInHospitalNo())){//住院
                exHisZyInfService.saveExHisZyInf(pdDosage, list,PdConstant.IS_CHARGE_TYPE_1);
            }else{//门诊
                exHisZyInfService.saveExHisMzInf(pdDosage, list,PdConstant.IS_CHARGE_TYPE_1);
            }
        }
        return Result.ok("添加成功！");
    }

    @PostMapping(value = "/uniqueDosageReturned")
    public Result<?> uniqueDosageReturned(@RequestBody PdDosage pdDosage) {
        //不收费
        pdDosageService.uniqueDosageReturned(pdDosage);
        return Result.ok("退回成功！");
    }


    /**
     * 查詢病人信息
     * @param pdDosage
     * @return
     */
    @GetMapping(value = "/queryPatientInfoList")
    public Result<?> queryPatientInfoList(PdDosage pdDosage) {
        Result<List<PdDosage>> result = new Result<>();
        try {
             List<PdDosage> list =  hisChargeService.queryPatientInfoList(pdDosage);
            if(CollectionUtils.isEmpty(list) || list.size()==0){
                return Result.error("查询不到病人信息");
            }
             result.setResult(list);
            result.setSuccess(true);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return result;
    }



    /**
     * 取消收费
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/dosageCnclFee")
    public Result<?> dosageCnclFee(@RequestBody PdDosage pdDosage) {
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        //数据推送到HIS中间表
        if(CollectionUtils.isNotEmpty(detailList)){
            if(StringUtils.isNotEmpty(pdDosage.getInHospitalNo())){//住院
                exHisZyInfService.saveExHisZyInf(pdDosage, detailList,PdConstant.IS_CHARGE_TYPE_0);
            }else{//门诊
                String  prodNames= hisChargeService.queryMztfList(pdDosage);
                if(StringUtils.isNotEmpty(prodNames)){
                   //exHisZyInfService.saveExHisMzInf(pdDosage, detailList,PdConstant.IS_CHARGE_TYPE_0);
                }else{
                    return  Result.error(prodNames+"在HIS系统还没有进行退费，不允许操作");
                   }
                 }
            pdDosageService.dosageCnclFee(pdDosage);
        }
        return Result.ok("操作成功！");
    }


    /**
     * 收费
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/dosageFee")
    public Result<?> dosageFee(@RequestBody PdDosage pdDosage) {
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        //数据推送到HIS中间表
        if(CollectionUtils.isNotEmpty(detailList)){
            if(StringUtils.isNotEmpty(pdDosage.getInHospitalNo())){//住院
                exHisZyInfService.saveExHisZyInf(pdDosage, detailList,PdConstant.IS_CHARGE_TYPE_1);
            }else{//门诊
                exHisZyInfService.saveExHisMzInf(pdDosage, detailList,PdConstant.IS_CHARGE_TYPE_1);
            }
            pdDosageService.dosageFee(pdDosage);
        }
        return Result.ok("操作成功！");
    }

    /**
     * 唯一码取消收费
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/uniqueDosageCnclFee")
    public Result<?> uniqueDosageCnclFee(@RequestBody PdDosage pdDosage) {
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        //数据推送到HIS中间表
        if(CollectionUtils.isNotEmpty(detailList)){
            if(StringUtils.isNotEmpty(pdDosage.getInHospitalNo())){//住院
                exHisZyInfService.saveExHisZyInf(pdDosage, detailList,PdConstant.IS_CHARGE_TYPE_0);
            }else{//门诊
                String  prodNames= hisChargeService.queryMztfList(pdDosage);
                if(StringUtils.isNotEmpty(prodNames)){
                    //exHisZyInfService.saveExHisMzInf(pdDosage, detailList,PdConstant.IS_CHARGE_TYPE_0);
                }else{
                    return  Result.error(prodNames+"在HIS系统还没有进行退费，不允许操作");
                }
            }
            pdDosageService.uniqueDosageCnclFee(pdDosage);
        }
        return Result.ok("操作成功！");
    }

}
