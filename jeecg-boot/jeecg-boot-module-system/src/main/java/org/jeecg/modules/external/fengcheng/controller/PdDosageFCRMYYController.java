package org.jeecg.modules.external.fengcheng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.fengcheng.service.IPdDosageFCRMYYService;
import org.jeecg.modules.external.fengcheng.util.HisApiForFCRenminUtils;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.service.IPdDosageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 丰城中医院计费controller
 * @Author: zxh
 * @Date: 2020-03-13
 * @Version: V1.0
 */
@Api(tags = "用量表")
@RestController
@RequestMapping("/pd/pdDosageFCRMYY")
@Slf4j
public class PdDosageFCRMYYController {
    @Autowired
    private IPdDosageFCRMYYService pdDosageFCRMYYService;
    @Autowired
    private IPdDosageService pdDosageService;

    private static Logger logger = LoggerFactory.getLogger(PdDosageFCRMYYController.class);


    /**
     * 初始化Modal页面
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/initModal")
    public Result<?> initModal(PdDosage pdDosage, HttpServletRequest req) {
        PdDosage entity = pdDosageFCRMYYService.initModal(pdDosage);
        return Result.ok(entity);
    }

    /**
     * 提交(收费)
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/submit")
    public Result<?> submit(@RequestBody PdDosage pdDosage) {
        List<PdDosageDetail> list = pdDosageFCRMYYService.saveMain(pdDosage, PdConstant.IS_CHARGE_FLAG_0);
        return Result.ok("添加成功！");
    }

    /**
     * 用量退回
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/dosageReturned")
    public Result<?> dosageReturned(@RequestBody PdDosage pdDosage) {
        //不收费
        pdDosageFCRMYYService.dosageReturned(pdDosage);
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
            List<PdDosage> list = pdDosageFCRMYYService.queryPatientInfoList(pdDosage);
            if (CollectionUtils.isEmpty(list) || list.size() == 0) {
                return Result.error("查询不到病人信息");
            }
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
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
        if (CollectionUtils.isNotEmpty(detailList)) {
            //HIS退费接口
            JSONObject result = HisApiForFCRenminUtils.exeRefund(pdDosage,detailList);
            if(!PdConstant.SUCCESS_0.equals(result.getString("statusCode"))){
                logger.error("执行HIS退费接口失败！HIS返回："+result.getString("msg"));
                return Result.error("执行HIS退费接口失败！HIS返回："+result.getString("msg"));
            }

            pdDosageService.dosageCnclFee(pdDosage);
        }
        return Result.ok("操作成功！");
    }

    /**
     * 补收费
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/dosageFee")
    public Result<?> dosageFee(@RequestBody PdDosage pdDosage) {
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        if (CollectionUtils.isNotEmpty(detailList)) {

            List<PdDosageDetail> newList = new ArrayList<>();
            Set<String> hisPackageCodeList = new HashSet<>();
            for(PdDosageDetail pdd : detailList){
                if(oConvertUtils.isNotEmpty(pdd.getHisPackageCode())){
                    hisPackageCodeList.add(pdd.getHisPackageCode()+pdd.getHisPackageFlag());
                }else{
                    newList.add(pdd);
                }
            }

            if(CollectionUtils.isNotEmpty(hisPackageCodeList)){
                for(String hisPackageCode : hisPackageCodeList){
                    int index = 0;
                    List<PdDosageDetail> addList = new ArrayList<>();
                    for(PdDosageDetail chargeItem : detailList){
                        if(oConvertUtils.isNotEmpty(chargeItem.getHisPackageCode())
                                && hisPackageCode.equals(chargeItem.getHisPackageCode()+chargeItem.getHisPackageFlag())){
                            chargeItem.setHisPackageIndex(index+"");
                            addList.add(chargeItem);
                            index = index + 1;
                        }
                    }
                    newList.addAll(addList);
                }
            }
            //HIS计费接口
            JSONObject result = HisApiForFCRenminUtils.exeCharge(pdDosage,newList);
            if(!PdConstant.SUCCESS_0.equals(result.getString("statusCode"))){
                logger.error("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
                return Result.error("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
            }

            pdDosage.setPdDosageDetails(newList);
            pdDosageFCRMYYService.dosageFee(pdDosage);
        }
        return Result.ok("操作成功！");
    }

}
