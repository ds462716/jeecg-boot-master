package org.jeecg.modules.external.ganzhouwuyuan.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.external.ganzhouwuyuan.service.IPdDosageGZWYService;
import org.jeecg.modules.external.ganzhouwuyuan.util.AxisGZWYUtils;
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
@RequestMapping("/pd/pdDosageGZWY")
@Slf4j
public class PdDosageGZWYController {
    @Autowired
    private IPdDosageGZWYService pdDosageGZWYService;
    @Autowired
    private IPdDosageService pdDosageService;
    @Autowired
    private IPdProductStockCheckPermissionService pdProductStockCheckPermissionService;

    private static Logger logger = LoggerFactory.getLogger(PdDosageGZWYController.class);


    /**
     * 初始化Modal页面
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/initModal")
    public Result<?> initModal(PdDosage pdDosage, HttpServletRequest req) {
        PdDosage entity = pdDosageGZWYService.initModal(pdDosage);
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
        List<PdDosageDetail> list = pdDosageGZWYService.saveMain(pdDosage, PdConstant.IS_CHARGE_FLAG_0);
        return Result.ok("添加成功！");
    }

    /**
     * 唯一码提交(收费)
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/uniqueSubmit")
    public Result<?> uniqueSubmit(@RequestBody PdDosage pdDosage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, sysUser.getCurrentDepartId()));
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(checkList)){
            return Result.error("本库房正在盘点，不能使用！");
        }
        List<PdDosageDetail> list = pdDosageGZWYService.saveUniqueMain(pdDosage, PdConstant.IS_CHARGE_FLAG_0);
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
            return Result.error("本库房正在盘点，不能操作！");
        }
        //不收费
        pdDosageGZWYService.dosageReturned(pdDosage);
        return Result.ok("退回成功！");
    }


    /**
     * 唯一码用量退回
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/uniqueDosageReturned")
    public Result<?> uniqueDosageReturned(@RequestBody PdDosage pdDosage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, sysUser.getCurrentDepartId()));
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(checkList)){
            return Result.error("本库房正在盘点，不能操作！");
        }
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
            List<PdDosage> list = pdDosageGZWYService.queryPatientInfoList(pdDosage);
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
                JSONObject result = AxisGZWYUtils.exeRefund(pdDosage, detail);
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
     * 唯一码取消收费
     * @param pdDosage
     * @return
     */
    @PostMapping(value = "/uniqueDosageCnclFee")
    public Result<?> uniqueDosageCnclFee(@RequestBody PdDosage pdDosage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, sysUser.getCurrentDepartId()));
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(checkList)){
            return Result.error("本库房正在盘点，不能使用！");
        }
        List<PdDosageDetail> detailList = pdDosage.getPdDosageDetails();
        if (CollectionUtils.isNotEmpty(detailList)) {
            //HIS退费接口
            for(PdDosageDetail detail : detailList) {
                JSONObject result = AxisGZWYUtils.exeRefund(pdDosage, detail);
                if (!PdConstant.SUCCESS_0.equals(result.getString("code"))) {
                    logger.error("执行HIS退费接口失败！HIS返回：" + result.getString("msg"));
                    return Result.error("执行HIS退费接口失败！HIS返回：" + result.getString("msg"));
                }
            }
            pdDosageService.uniqueDosageCnclFee(pdDosage);
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
            JSONObject result = AxisGZWYUtils.exeCharge(pdDosage,detail);
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
            pdDosageGZWYService.dosageFee(pdDosage);
        }
        return Result.ok("操作成功！");
    }

}
