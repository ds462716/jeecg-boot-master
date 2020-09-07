package org.jeecg.modules.external.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.external.entity.ExHisChargeCodeGzzl;
import org.jeecg.modules.external.entity.ExHisZyChargeInfGzzl;
import org.jeecg.modules.external.entity.ExHisMzChargeInfGzzl;
import org.jeecg.modules.external.service.IExHisChargeInfGzzlService;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.service.IPdProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @Description: 用量表
* @Author: zxh
* @Date:   2020-03-13
* @Version: V1.0
*/
@Api(tags="用量表")
@RestController
@RequestMapping("/external/exPdDosageGzzl")
@Slf4j
public class ExPdDosageGzzlController {

    @Autowired
    private IExHisChargeInfGzzlService exHisChargeInfGzzlService;

    @Autowired
    private IPdProductService pdProductService;

    /**
     * 查詢住院病人信息
     * @param exHisZyChargeInfGzzl
     * @return
     * http://localhost:3000/jeecg-boot/external/exPdDosageGzzl/queryZyPatientInfoList?_t=1599100947
     */
    @GetMapping(value = "/queryZyPatientInfoList")
    public Result<?> queryZyPatientInfoList(ExHisZyChargeInfGzzl exHisZyChargeInfGzzl,
                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Result<List<ExHisZyChargeInfGzzl>> result = new Result<>();
        try {
            Page<ExHisZyChargeInfGzzl> page = new Page<>(pageNo,pageSize);
            IPage<ExHisZyChargeInfGzzl> pageList =  exHisChargeInfGzzlService.queryZyPatientInfoList(page,exHisZyChargeInfGzzl);
            return Result.ok(pageList);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询门诊用户信息
     * @param exHisMzChargeInfGzzl
     * @return
     * http://localhost:3000/jeecg-boot/external/exPdDosageGzzl/queryMzPatientInfoList?_t=1599100947
     */
    @GetMapping(value = "/queryMzPatientInfoList")
    public Result<?> queryMzPatientInfoList(ExHisMzChargeInfGzzl exHisMzChargeInfGzzl,
                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Result<List<ExHisMzChargeInfGzzl>> result = new Result<>();
        try {
            Page<ExHisMzChargeInfGzzl> page = new Page<>(pageNo,pageSize);
            IPage<ExHisMzChargeInfGzzl> pageList =  exHisChargeInfGzzlService.queryMzPatientInfoList(page,exHisMzChargeInfGzzl);
            return Result.ok(pageList);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询收费项目代码
     * @param exHisChargeCodeGzzl
     * @return
     * http://localhost:3000/jeecg-boot/external/exPdDosageGzzl/queryHisChargeCode?_t=1599100947
     */
    @GetMapping(value = "/queryHisChargeCode")
    public Result<?> queryHisChargeCode(ExHisChargeCodeGzzl exHisChargeCodeGzzl,
                                        @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Result<List<ExHisChargeCodeGzzl>> result = new Result<>();
        try {
            Page<ExHisChargeCodeGzzl> page = new Page<>(pageNo,pageSize);
            IPage<ExHisChargeCodeGzzl> pageList =  exHisChargeInfGzzlService.queryHisChargeCode(page,exHisChargeCodeGzzl);
            return Result.ok(pageList);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询产品关联收费代码用
     * @param pdProduct
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/listForHisCharge")
    public Result<?> queryPageListForHisCharge(PdProduct pdProduct,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               HttpServletRequest req) {
        Page<PdProduct> page = new Page<PdProduct>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdProduct.setDepartParentId(sysUser.getDepartParentId());
        pdProduct.setStatus(PdConstant.DISABLE_ENABLE_STATUS_0);//只查启用
        IPage<PdProduct> pageList = pdProductService.queryPageListForHisCharge(page, pdProduct);
        return Result.ok(pageList);
    }

}
