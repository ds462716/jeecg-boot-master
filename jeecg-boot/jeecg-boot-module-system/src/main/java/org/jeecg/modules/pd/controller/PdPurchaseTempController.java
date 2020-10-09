package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdPurchaseTemp;
import org.jeecg.modules.pd.entity.PdPurchaseTempDetail;
import org.jeecg.modules.pd.service.IPdPurchaseTempDetailService;
import org.jeecg.modules.pd.service.IPdPurchaseTempService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
* @Description: 申购模板主表
* @Author: mcb
* @Date:   2020-08-04
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/pdPurchaseTemp")
@Slf4j
public class PdPurchaseTempController {
    @Autowired
    private IPdPurchaseTempService pdPurchaseTempService;
    @Autowired
    private IPdPurchaseTempDetailService pdPurchaseTempDetailService;
    @Autowired
    private ISysDepartService sysDepartService;



    /**
     * 分页列表查询
     *
     * @param pdPurchaseTemp
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PdPurchaseTemp pdPurchaseTemp,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdPurchaseTemp> page = new Page<PdPurchaseTemp>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdPurchaseTemp.setDepartId(sysUser.getCurrentDepartId());
        pdPurchaseTemp.setDepartParentId(sysUser.getDepartParentId());
        IPage<PdPurchaseTemp> pageList = pdPurchaseTempService.selectList(page, pdPurchaseTemp);//
        return Result.ok(pageList);
    }


    /**
     * 新增初始化操作
     *
     * @return
     */
     @GetMapping(value = "/purchaseInfo")
    public Result<PdPurchaseTemp> purchaseInfo() {
        Result<PdPurchaseTemp> result = new Result<>();
         PdPurchaseTemp pdPurchasetemp = new PdPurchaseTemp();
        String tempNo = UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_SGMB);
         pdPurchasetemp.setTempNo(tempNo);
         pdPurchasetemp.setTotalNum(0.00);
         LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysDepart sysDepart = sysDepartService.getDepartByOrgCode(sysUser.getOrgCode());
         pdPurchasetemp.setDepartId(sysDepart.getId());
         pdPurchasetemp.setDepartName(sysDepart.getDepartName());
         pdPurchasetemp.setCreateTime(new Date());
         pdPurchasetemp.setRealname(sysUser.getRealname());
         pdPurchasetemp.setUserId(sysUser.getId());
         result.setResult(pdPurchasetemp);
        result.setSuccess(true);
        return result;
    }

    /**
     * 添加
     *
     * @param pdPurchaseTemp
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PdPurchaseTemp pdPurchaseTemp) {
        PdPurchaseTemp pdPurchasetemp = new PdPurchaseTemp();
        BeanUtils.copyProperties(pdPurchaseTemp, pdPurchasetemp);
        pdPurchasetemp.setDelFlag(PdConstant.DEL_FLAG_0);
        pdPurchaseTempService.saveMain(pdPurchasetemp, pdPurchaseTemp.getPdPurchaseTempDetailList());
        return Result.ok("操作成功！");
    }

    /**
     * 编辑
     *
     * @param pdPurchaseTemp
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PdPurchaseTemp pdPurchaseTemp) {
        PdPurchaseTemp pdPurchasetemp = new PdPurchaseTemp();
        BeanUtils.copyProperties(pdPurchaseTemp, pdPurchasetemp);
        PdPurchaseTemp pdPurchaseTempEntity = pdPurchaseTempService.getById(pdPurchasetemp.getId());
        if (pdPurchaseTempEntity == null) {
            return Result.error("未找到对应数据");
        }
        pdPurchaseTempService.updateMain(pdPurchaseTemp, pdPurchaseTemp.getPdPurchaseTempDetailList());
        return Result.ok("操作成功!");
    }

     /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        pdPurchaseTempService.removeById(id);
        return Result.ok("删除成功!");
    }


    /**
     * 查询明细表
     *
     * @param purchaseTempDetail
     * @return
     */
    @GetMapping(value = "/queryPdPurchaseTempDetail")
    public Result<?> queryPdPurchaseDetail(PdPurchaseTempDetail purchaseTempDetail) {
        List<PdPurchaseTempDetail> pdPurchaseTempDetailList = pdPurchaseTempDetailService.queryPdPurchaseTempDetail(purchaseTempDetail);
        return Result.ok(pdPurchaseTempDetailList);
    }


    /**
     * 模板选择器用
     *
     * @param pdPurchaseTemp
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/queryPurchaseTempList")
    public Result<?> queryPackgeList(PdPurchaseTemp pdPurchaseTemp,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
        Page<PdPurchaseTemp> page = new Page<PdPurchaseTemp>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdPurchaseTemp.setDepartId(sysUser.getCurrentDepartId());
        pdPurchaseTemp.setDepartParentId(sysUser.getDepartParentId());
        IPage<PdPurchaseTemp> pageList =pdPurchaseTempService.selectList(page, pdPurchaseTemp);
        return Result.ok(pageList);
    }
}
