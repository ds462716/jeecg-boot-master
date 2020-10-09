package org.jeecg.modules.external.fengcheng.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.fengcheng.entity.HisChargeFCZYY;
import org.jeecg.modules.external.fengcheng.util.HisApiForFCZhongyiUtils;
import org.jeecg.modules.pd.entity.HisChargeInf;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @Description: HIS系统收费项目基础信息
* @Author: jeecg-boot
* @Date:   2020-02-11
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/hisChargeFCZYY")
@Slf4j
public class HisChargeFCZYYController {

    /**
     * 查询HIS收费代码列表
     * @param hisCharge
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> hisChargeCodeList(HisChargeFCZYY hisCharge, HttpServletRequest req) {

        if(hisCharge == null){
            return Result.error("请输入查询条件查询！");
        }
        if(oConvertUtils.isEmpty(hisCharge.getSFCODE()) && oConvertUtils.isEmpty(hisCharge.getSFNAME())){
            return Result.error("请输入查询条件查询！");
        }

        JSONObject chargeCodeJson = HisApiForFCZhongyiUtils.queryHisChargeCode(hisCharge.getSFCODE(),hisCharge.getSFNAME());
        if(chargeCodeJson == null){
            return Result.error("未查询到数据！");
        }
        if(!PdConstant.SUCCESS_0.equals(chargeCodeJson.getString("statusCode"))){
            return Result.error("查询HIS项目收费代码失败，请重新查询或联系管理员！HIS返回："+chargeCodeJson.getString("msg"));
        }

        JSONArray jsonArray = chargeCodeJson.getJSONArray("data");
        List<HisChargeFCZYY> hisChargeCodeList = JSONArray.parseArray(jsonArray.toJSONString(),HisChargeFCZYY.class);


        return Result.ok(hisChargeCodeList);
    }

}
