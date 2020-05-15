package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdDosage;
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

    /**
     * 提交
     *
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/submit")
    public Result<?> submit(@RequestBody PdDosage pdDosage) {
        //不收费
         pdDosageService.newSaveMain(pdDosage, PdConstant.IS_CHARGE_FLAG_1);
        return Result.ok("添加成功！");
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

}
