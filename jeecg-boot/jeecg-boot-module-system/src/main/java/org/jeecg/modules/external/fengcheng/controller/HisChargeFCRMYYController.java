package org.jeecg.modules.external.fengcheng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.fengcheng.entity.HisChargeFCRMYY;
import org.jeecg.modules.external.fengcheng.entity.HisChargeFCZYY;
import org.jeecg.modules.external.fengcheng.util.HisApiForFCRenminUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @Description: HIS系统收费项目基础信息
* @Author: jeecg-boot
* @Date:   2020-02-11
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/hisChargeFCRMYY")
@Slf4j
public class HisChargeFCRMYYController {

    /**
     * 查询HIS收费代码列表
     * @param hisCharge
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> hisChargeCodeList(HisChargeFCRMYY hisCharge, HttpServletRequest req) {

        if(hisCharge == null){
            return Result.error("请输入查询条件查询！");
        }
        if(oConvertUtils.isEmpty(hisCharge.getChargeCode()) && oConvertUtils.isEmpty(hisCharge.getProName())){
            return Result.error("请输入查询条件查询！");
        }

        JSONObject chargeCodeJson = HisApiForFCRenminUtils.queryHisChargeCode(hisCharge);
        if(chargeCodeJson == null){
            return Result.error("未查询到数据！");
        }
        if(!PdConstant.SUCCESS_0.equals(chargeCodeJson.getString("statusCode"))){
            return Result.error("查询HIS项目收费代码失败，请重新查询或联系管理员！HIS返回："+chargeCodeJson.getString("msg"));
        }

        try{
            JSONArray jsonArray = chargeCodeJson.getJSONArray("data");
            // his返回报文最后会带一条空数据，这边把空数据移除
            for (int i = 0; i < jsonArray.size(); i++){
                if(oConvertUtils.isEmpty(jsonArray.getJSONObject(i).getString("chargeCode"))){
                    jsonArray.remove(i);
                }
            }
            List<HisChargeFCRMYY> hisChargeCodeList = JSONArray.parseArray(jsonArray.toJSONString(),HisChargeFCRMYY.class);
            return Result.ok(hisChargeCodeList);
        }catch (Exception e){
            //his不到数据，会返回一个空JSONObject，不会返回一个空数组，返回报文如下。  转换JSONArray会抛异常，这边捕获异常，并提示未查询到数据
//        "data": { "chargeCode": "", "proName": "", "price": "", "spec": "" } }
            return Result.error("未查询到数据！");
        }
    }

}
