package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdStockRecordDetailService;
import org.jeecg.modules.pd.service.IPdStockRecordService;
import org.jeecg.modules.pd.vo.PdStockRecordOutPage;
import org.jeecg.modules.pd.vo.PdSupplierRecordExcel;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date: 2020-02-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdStockRecordReturn")
@Slf4j
public class PdStockRecordReturnController {
    @Autowired
    private IPdStockRecordService pdStockRecordService;
    @Autowired
    private IPdStockRecordDetailService pdStockRecordDetailService;
    @Autowired
    private IPdProductStockService pdProductStockService;
    @Autowired
    private IPdDepartService pdDepartService;

    /**
     * 初始化Modal页面
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/initModal")
    public Result<?> initModal(@RequestParam(name = "id") String id, HttpServletRequest req) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        PdStockRecord pdStockRecord = pdStockRecordService.initReturnModal(id);
        pdStockRecord.setHospitalCode(sysUser.getHospitalCode());
        return Result.ok(pdStockRecord);
    }

    /**
     * 分页列表查询
     *
     * @param pdStockRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PdStockRecord pdStockRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdStockRecord> page = new Page<PdStockRecord>(pageNo, pageSize);
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_2);
        pdStockRecord.setOnlyReturn("1");//只查退货出库
        IPage<PdStockRecord> pageList = pdStockRecordService.queryList(page, pdStockRecord, PdConstant.RECODE_TYPE_2);
        return Result.ok(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param pdStockRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/examineList")
    public Result<?> queryExaminePageList(PdStockRecord pdStockRecord,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {
        Page<PdStockRecord> page = new Page<PdStockRecord>(pageNo, pageSize);
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_2);
        pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); //已提交状态
        pdStockRecord.setOnlyReturn("1");//只查退货出库
        IPage<PdStockRecord> pageList = pdStockRecordService.queryList(page, pdStockRecord, PdConstant.RECODE_TYPE_2);
        return Result.ok(pageList);
    }

    /**
     * 保存
     *
     * @param pdStockRecord
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PdStockRecord pdStockRecord) {
        if(oConvertUtils.isEmpty(pdStockRecord.getId())){
            pdStockRecord.setOnlyReturn("1");//只查退货出库
            List<PdStockRecord> list = pdStockRecordService.queryList(pdStockRecord,PdConstant.RECODE_TYPE_2);
            if(CollectionUtils.isNotEmpty(list)){
                return Result.error("退货出库单已被保存或提交，不能保存草稿！");
            }
        }else{
            PdStockRecord entity = pdStockRecordService.getById(pdStockRecord.getId());
            if(entity != null && PdConstant.SUBMIT_STATE_2.equals(entity.getSubmitStatus())){
                return Result.error("退货出库单已被提交，不能保存草稿！");
            }
        }

        String recordId = pdStockRecordService.saveMain(pdStockRecord, pdStockRecord.getPdStockRecordDetailList(), PdConstant.RECODE_TYPE_2);

        Map<String,Object> result = new HashMap<>();
        result.put("recordId",recordId);
        result.put("message","保存成功！");
        return Result.ok(result);
    }

    /**
     * 提交
     *
     * @param pdStockRecord
     * @return
     */
    @PostMapping(value = "/submit")
    public Result<?> submit(@RequestBody PdStockRecord pdStockRecord) {
        if (oConvertUtils.isNotEmpty(pdStockRecord.getId())) {
            PdStockRecord entity = pdStockRecordService.getById(pdStockRecord.getId());
            if(entity != null && PdConstant.SUBMIT_STATE_2.equals(entity.getSubmitStatus())){
                return Result.error("退货出库单已被提交，不能再次提交！");
            }
        }
        String recordId = pdStockRecordService.submit(pdStockRecord, pdStockRecord.getPdStockRecordDetailList(), PdConstant.RECODE_TYPE_2);
        Map<String,Object> result = new HashMap<>();
        result.put("recordId",recordId);
        result.put("message","提交成功！");
        return Result.ok(result);
    }

    /**
     * 审批
     *
     * @param pdStockRecord
     * @return
     */
    @PostMapping(value = "/audit")
    public Result<?> audit(@RequestBody PdStockRecord pdStockRecord) {
        PdStockRecord entity = pdStockRecordService.getOne(pdStockRecord);
        if (entity == null) {
            return Result.error("未找到对应数据");
        }

        if(!PdConstant.SUBMIT_STATE_2.equals(entity.getSubmitStatus())){
            return Result.error("退货出库单未提交，不能审批！");
        }
        if(PdConstant.AUDIT_STATE_2.equals(entity.getAuditStatus()) || PdConstant.AUDIT_STATE_3.equals(entity.getAuditStatus())){
            return Result.error("退货出库单已被审批，不能再次审批！");
        }
        Map<String,String> result = pdStockRecordService.audit(pdStockRecord,entity, PdConstant.RECODE_TYPE_2);
        if(PdConstant.SUCCESS_200.equals(result.get("code"))) {
            return Result.ok(result.get("message"));
        }else{
            return Result.error(result.get("message"));
        }
    }

    /**
     * 撤回
     * @param pdStockRecord
     * @return
     */
    @PutMapping(value = "/cancel")
    public Result<?> cancel(@RequestBody PdStockRecord pdStockRecord) {
        PdStockRecord entity = pdStockRecordService.getById(pdStockRecord.getId());
        if (entity == null) {
            return Result.error("未找到对应数据");
        }
        if (PdConstant.SUBMIT_STATE_2.equals(entity.getSubmitStatus()) && PdConstant.AUDIT_STATE_1.equals(entity.getAuditStatus())) {
            pdStockRecordService.updateStatus(pdStockRecord);
            return Result.ok("撤回成功!");
        }else{
            return Result.error("当前退货出库单状态已被审批或已撤回，不能撤回！");
        }
    }

    /**
     * 目前只用于打印后更新注册号
     * @param pdStockRecord
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PdStockRecord pdStockRecord) {

        if(CollectionUtils.isNotEmpty(pdStockRecord.getPdStockRecordDetailList())){
            Date date = DateUtils.getDate();
            for(PdStockRecordDetail item : pdStockRecord.getPdStockRecordDetailList()){
                item.setUpdateTime(date);

                // 更新对应的入库单注册证号
                PdStockRecordDetail detail = pdStockRecordDetailService.getById(item.getId());
                if(oConvertUtils.isNotEmpty(detail.getProductStockId())){
                    PdProductStock stock = pdProductStockService.getById(detail.getProductStockId());
                    if(stock != null && oConvertUtils.isNotEmpty(stock.getRecordDetailId())){
                        PdStockRecordDetail inRecord = new PdStockRecordDetail();
                        inRecord.setId(stock.getRecordDetailId());
                        inRecord.setRegistration(item.getRegistration());
                        inRecord.setUpdateTime(date);
                        pdStockRecordDetailService.updateById(inRecord);
                    }
                }
            }
            pdStockRecordDetailService.updateBatchById(pdStockRecord.getPdStockRecordDetailList());
        }
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {

        PdStockRecord entity = pdStockRecordService.getById(id);
        if (entity == null) {
            return Result.error("未找到对应数据");
        }
        if (PdConstant.SUBMIT_STATE_1.equals(entity.getSubmitStatus()) || PdConstant.SUBMIT_STATE_3.equals(entity.getSubmitStatus())) {
            pdStockRecordService.delMainByDelFlag(id);
            return Result.ok("删除成功!");
        }else{
            return Result.error("当前退货出库单状态非待提交或已撤回状态，不能删除！");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
//        this.pdStockRecordService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {

        PdStockRecord pdStockRecord = new PdStockRecord();
        pdStockRecord.setId(id);
        pdStockRecord = pdStockRecordService.getOne(pdStockRecord);

        PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
        pdStockRecordDetail.setRecordId(id);
        List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
        pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);


        BigDecimal inTotalPrice = new BigDecimal(0);//总金额
        BigDecimal outTotalPrice = new BigDecimal(0);//总金额
        Double totalSum = new Double(0);//总数量
        for (PdStockRecordDetail item : pdStockRecordDetailList) {
            totalSum = totalSum + item.getProductNum();
            BigDecimal purchasePrice = item.getPurchasePrice() == null ? new BigDecimal(0) : item.getPurchasePrice();
            BigDecimal sellingPrice = item.getSellingPrice() == null ? new BigDecimal(0) : item.getSellingPrice();
            inTotalPrice = inTotalPrice.add(purchasePrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
            outTotalPrice = outTotalPrice.add(sellingPrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
        }
        pdStockRecord.setTotalSum(totalSum);
        pdStockRecord.setInTotalPrice(inTotalPrice);
        pdStockRecord.setOutTotalPrice(outTotalPrice);

        if (pdStockRecord == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(pdStockRecord);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryPdStockRecordDetailByMainId")
    public Result<?> queryPdStockRecordDetailListByMainId(@RequestParam(name = "id", required = true) String id) {
        PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
        pdStockRecordDetail.setRecordId(id);
        List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
        return Result.ok(pdStockRecordDetailList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param pdStockRecordDetail
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdStockRecordDetail pdStockRecordDetail) {
        pdStockRecordDetail.setRecordType(PdConstant.RECODE_TYPE_2);
        pdStockRecordDetail.setAuditStatus(PdConstant.AUDIT_STATE_2);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecordDetail.setDepartParentId(sysUser.getDepartParentId());

        if(oConvertUtils.isNotEmpty(pdStockRecordDetail.getInDepartIds()) && !"undefined".equals(pdStockRecordDetail.getInDepartIds())){
            pdStockRecordDetail.setInDepartIdList(Arrays.asList(pdStockRecordDetail.getInDepartIds().split(",")));
        }else{
            //查询科室下所有下级科室的ID
            SysDepart sysDepart=new SysDepart();
            List<String> departList=pdDepartService.selectListDepart(sysDepart);
            pdStockRecordDetail.setInDepartIdList(departList);
        }

        List<PdStockRecordDetail> detailList =  pdStockRecordDetailService.selectList(pdStockRecordDetail);
        List<PdStockRecordOutPage> exportList = JSON.parseArray(JSON.toJSONString(detailList), PdStockRecordOutPage.class);

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "退货出库记录表");
        mv.addObject(NormalExcelConstants.CLASS, PdStockRecordOutPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("退货出库记录表数据", "导出人:" + sysUser.getRealname(), "退货出库记录表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 查询出库明细  mcb  --20200224 用于统计查询
     *
     * @param pdStockRecordDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/queryPdStockRecordOutList")
    public Result<?> queryPdStockRecordOutList(PdStockRecordDetail pdStockRecordDetail,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               HttpServletRequest req) {

        Page<PdStockRecordDetail> page = new Page<PdStockRecordDetail>(pageNo, pageSize);
        pdStockRecordDetail.setRecordType(PdConstant.RECODE_TYPE_2);
        pdStockRecordDetail.setAuditStatus(PdConstant.AUDIT_STATE_2);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecordDetail.setDepartParentId(sysUser.getDepartParentId());

        if(oConvertUtils.isNotEmpty(pdStockRecordDetail.getOutDepartIds()) && !"undefined".equals(pdStockRecordDetail.getOutDepartIds())){
            pdStockRecordDetail.setOutDepartIdList(Arrays.asList(pdStockRecordDetail.getOutDepartIds().split(",")));
        }else{
            //查询科室下所有下级科室的ID
            SysDepart sysDepart=new SysDepart();
            List<String> departList=pdDepartService.selectListDepart(sysDepart);
            pdStockRecordDetail.setOutDepartIdList(departList);
        }

        if(oConvertUtils.isNotEmpty(pdStockRecordDetail.getInDepartIds()) && !"undefined".equals(pdStockRecordDetail.getInDepartIds())){
            pdStockRecordDetail.setInDepartIdList(Arrays.asList(pdStockRecordDetail.getInDepartIds().split(",")));
        }else{
            //查询科室下所有下级科室的ID
//            SysDepart sysDepart=new SysDepart();
//            sysDepart.setDepartParentId(sysUser.getDepartParentId());
//            List<SysDepart> list = pdDepartService.selectList(sysDepart);
//            List<String> departList=pdDepartService.selectListDepart(sysDepart);
//            pdStockRecordDetail.setInDepartIdList(departList);
        }
        IPage<PdStockRecordDetail> pageList = pdStockRecordDetailService.selectList(page, pdStockRecordDetail);
        return Result.ok(pageList);
    }



    /**
     * 自动补货查询库存明细(根据科室领用量查询)
     * @param stockRecord
     * @return
     */
    @GetMapping(value = "/chooseStockRecordDetailList")
    public Result<?> chooseStockTotalList(PdStockRecord stockRecord) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        stockRecord.setDepartId(sysUser.getCurrentDepartId());
        stockRecord.setOutDepartId(sysUser.getCurrentDepartId());
        stockRecord.setDepartParentId(sysUser.getDepartParentId());
        stockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);
        //stockRecord.setProductFlag(PdConstant.PRODUCT_FLAG_0);//只查产品
        List<PdStockRecordDetail>  list=pdStockRecordDetailService.chooseStockRecordDetailList(stockRecord);
        return Result.ok(list);
    }



    /**
     * 供应商产品领用明细（供应商用量查询）-- 分页列表查询
     *
     * @param pdStockRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/querySupplierCountList")
    public Result<?> querySupplierCountPageList(PdStockRecord pdStockRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdStockRecord> page = new Page<PdStockRecord>(pageNo, pageSize);
        IPage<PdStockRecord> pageList = pdStockRecordService.querySupplierCountPageList(page, pdStockRecord);
        return Result.ok(pageList);
    }




    /**
     * 供应商入库用量明细导出excel
     *
     * @param request
     * @param pdStockRecord
     */
    @RequestMapping(value = "/supplierExportXls")
    public ModelAndView supplierExportXls(HttpServletRequest request, PdStockRecord pdStockRecord) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdStockRecord> recordList =  pdStockRecordService.querySupplierCountList(pdStockRecord);
        List<PdSupplierRecordExcel> exportList = JSON.parseArray(JSON.toJSONString(recordList), PdSupplierRecordExcel.class);
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "供应商入库用量明细");
        mv.addObject(NormalExcelConstants.CLASS, PdSupplierRecordExcel.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商入库用量明细数据", "导出人:" + sysUser.getRealname(), "供应商入库用量明细"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }
}
