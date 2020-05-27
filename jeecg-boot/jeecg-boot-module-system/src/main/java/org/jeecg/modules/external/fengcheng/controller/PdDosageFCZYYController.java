package org.jeecg.modules.external.fengcheng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.external.fengcheng.service.IPdDosageFCZYYService;
import org.jeecg.modules.external.fengcheng.util.HisApiForFCZhongyiUtils;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.service.IExHisZyInfService;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.jeecg.modules.pd.service.IPdDosageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 丰城中医院计费controller
 * @Author: zxh
 * @Date: 2020-03-13
 * @Version: V1.0
 */
@Api(tags = "用量表")
@RestController
@RequestMapping("/pd/pdDosageFCZYY")
@Slf4j
public class PdDosageFCZYYController {
    @Autowired
    private IHisChargeService hisChargeService;
    @Autowired
    private IPdDosageFCZYYService pdDosageFCZYYService;
    @Autowired
    private IPdDosageService pdDosageService;
    @Autowired
    private IExHisZyInfService exHisZyInfService;

    private static Logger logger = LoggerFactory.getLogger(PdDosageFCZYYController.class);

    /**
     * 提交(收费)
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/submit")
    public Result<?> submit(@RequestBody PdDosage pdDosage) {
        List<PdDosageDetail> list = pdDosageFCZYYService.saveMain(pdDosage, PdConstant.IS_CHARGE_FLAG_0);
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
        pdDosageFCZYYService.dosageReturned(pdDosage);
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
            List<PdDosage> list = pdDosageFCZYYService.queryPatientInfoList(pdDosage);
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
            JSONObject result = HisApiForFCZhongyiUtils.exeRefund(pdDosage,detailList);
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
            //HIS计费接口
            JSONObject result = HisApiForFCZhongyiUtils.exeCharge(pdDosage,detailList);
            if(result == null || result.getJSONArray("data") == null || result.getJSONArray("data").size() <= 0){
                logger.error("HIS返回数据为空，请重新计费或联系管理员！！");
                return Result.error("HIS返回数据为空，请重新计费或联系管理员！！");
            }

            if(!PdConstant.SUCCESS_0.equals(result.getString("statusCode"))){
                logger.error("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
                return Result.error("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
            }

            JSONArray array = result.getJSONArray("data");
            for(int k = 0; k < array.size(); k++){
                JSONObject obj = array.getJSONObject(k);   // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                String prodNo = obj.getString("prodNo");//产品编码
                String visitNo = obj.getString("vaa07");//就诊流水号
                String hisChargeId = obj.getString("vai01");//计费单据id
                String hisChargeItemId = obj.getString("vaj01");//计费单据明细id (退费用)
                for(PdDosageDetail pdd : detailList){
                    if(pdd.getProductId().equals(prodNo)){
                        pdd.setHisChargeId(hisChargeId);
                        pdd.setHisChargeItemId(hisChargeItemId);
                    }
                }
            }
            pdDosage.setPdDosageDetails(detailList);
            pdDosageService.dosageFee(pdDosage);
        }
        return Result.ok("操作成功！");
    }

}
