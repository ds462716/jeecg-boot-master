package org.jeecg.modules.external.ganzhouzhongliu.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.external.ganzhouzhongliu.service.IPdDosageGZZLService;
import org.jeecg.modules.external.ganzhouzhongliu.util.AxisGZZLUtils;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.entity.PdProductStockCheckPermission;
import org.jeecg.modules.pd.service.IPdDosageService;
import org.jeecg.modules.pd.service.IPdProductStockCheckPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 赣州五院计费controller
 * @Author: zxh
 * @Date: 2020-03-13
 * @Version: V1.0
 */
@Api(tags = "用量表")
@RestController
@RequestMapping("/pd/pdDosageGZZL")
@Slf4j
public class PdDosageGZZLController {
    @Autowired
    private IPdDosageGZZLService pdDosageGZZLService;
    @Autowired
    private IPdDosageService pdDosageService;
    @Autowired
    private IPdProductStockCheckPermissionService pdProductStockCheckPermissionService;

    private static Logger logger = LoggerFactory.getLogger(PdDosageGZZLController.class);


    /**
     * 初始化Modal页面
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/initModal")
    public Result<?> initModal(PdDosage pdDosage, HttpServletRequest req) {
        PdDosage entity = pdDosageGZZLService.initModal(pdDosage);
        return Result.ok(entity);
    }

    /**
     * 提交(收费)
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/submit")
    public Result<?> submit(@RequestBody PdDosage pdDosage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, sysUser.getCurrentDepartId()));
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(checkList)){
            return Result.error("本库房正在盘点，不能使用！");
        }
        List<PdDosageDetail> list = pdDosageGZZLService.saveMain(pdDosage, PdConstant.IS_CHARGE_FLAG_0);
        return Result.ok("添加成功！");
    }

    /**
     * 用量退回
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/dosageReturned")
    public Result<?> dosageReturned(@RequestBody PdDosage pdDosage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, sysUser.getCurrentDepartId()));
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(checkList)){
            return Result.error("本库房正在盘点，不能使用！");
        }
        //不收费
        pdDosageGZZLService.dosageReturned(pdDosage);
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
            List<PdDosage> list = pdDosageGZZLService.queryPatientInfoList(pdDosage);
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
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, sysUser.getCurrentDepartId()));
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(checkList)){
            return Result.error("本库房正在盘点，不能使用！");
        }
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        if (CollectionUtils.isNotEmpty(detailList)) {
            //HIS退费接口
            for(PdDosageDetail detail : detailList) {
                JSONObject result = AxisGZZLUtils.exeRefund(pdDosage, detail);
                if (!PdConstant.SUCCESS_0.equals(result.getString("statusCode"))) {
                    logger.error("执行HIS退费接口失败！HIS返回：" + result.getString("msg"));
                    return Result.error("执行HIS退费接口失败！HIS返回：" + result.getString("msg"));
                }
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
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, sysUser.getCurrentDepartId()));
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(checkList)){
            return Result.error("本库房正在盘点，不能使用！");
        }
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        if (CollectionUtils.isNotEmpty(detailList)) {

            for(PdDosageDetail detail : detailList){
            //HIS计费接口
            JSONObject result = AxisGZZLUtils.exeCharge(pdDosage,detail);
            if(result == null || result.getJSONArray("data") == null || result.getJSONArray("data").size() <= 0){
                logger.error("HIS返回数据为空，请重新计费或联系管理员！！");
                return Result.error("HIS返回数据为空，请重新计费或联系管理员！！");
            }
            if(!PdConstant.SUCCESS_0.equals(result.getString("code"))){
                logger.error("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
                return Result.error("执行HIS收费接口失败！HIS返回："+result.getString("msg"));
            }
            }
            pdDosage.setPdDosageDetails(detailList);
            pdDosageGZZLService.dosageFee(pdDosage);
        }
        return Result.ok("操作成功！");
    }

}
