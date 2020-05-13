package org.jeecg.modules.pd.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.NewPdDosage;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 查詢病人信息
     * @param newPdDosage
     * @return
     */
    @GetMapping(value = "/queryPatientInfoList")
    public Result<List<NewPdDosage>> queryPatientInfoList(NewPdDosage newPdDosage) {
        Result<List<NewPdDosage>> result = new Result<>();
        try {
             List<NewPdDosage> list =  hisChargeService.queryPatientInfoList(newPdDosage);
            result.setResult(list);
            result.setSuccess(true);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return result;
    }

}
