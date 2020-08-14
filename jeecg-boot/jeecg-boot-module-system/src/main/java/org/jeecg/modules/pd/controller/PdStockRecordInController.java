package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.message.util.PushMsgUtil;
import org.jeecg.modules.pd.entity.PdProductStockCheckPermission;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.vo.*;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysPermissionService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/pd/pdStockRecordIn")
@Slf4j
public class PdStockRecordInController {
    @Autowired
    private IPdStockRecordService pdStockRecordService;
    @Autowired
    private IPdStockRecordDetailService pdStockRecordDetailService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IPdGoodsAllocationService pdGoodsAllocationService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private IPdSupplierService pdSupplierService;
    @Autowired
    private IPdPurchaseDetailService pdPurchaseDetailService;
    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private ISysPermissionService sysPermissionService;
    @Autowired
    private PushMsgUtil pushMsgUtil;
    @Autowired
    private IPdProductStockCheckPermissionService pdProductStockCheckPermissionService;



    /**
     * 初始化Modal页面
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/initModal")
    public Result<?> initModal(@RequestParam(name = "id") String id, HttpServletRequest req) {
        PdStockRecord pdStockRecord = pdStockRecordService.initInModal(id);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecord.setHospitalCode(sysUser.getHospitalCode());
        return Result.ok(pdStockRecord);
    }

    @GetMapping(value = "/getOnOff")
    public Result<?> getOnOff(@RequestParam(name = "id") String id, HttpServletRequest req) {
        PdStockRecord pdStockRecord = pdStockRecordService.getOnOff();
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
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1);
        IPage<PdStockRecord> pageList = pdStockRecordService.queryList(page, pdStockRecord, PdConstant.RECODE_TYPE_1);
        return Result.ok(pageList);
    }


    @GetMapping(value = "/queryUniqueDetailPageList")
    public Result<?> queryUniqueDetailPageList(PdStockRecord pdStockRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdStockRecordDetail> page = new Page<PdStockRecordDetail>(pageNo, pageSize);
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1);

        if(oConvertUtils.isNotEmpty(pdStockRecord.getId())){
            pdStockRecord = pdStockRecordService.getById(pdStockRecord.getId());
            if(PdConstant.CODE_PRINT_TYPE_1.equals(pdStockRecord.getBarCodeType())){

                PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
                pdStockRecordDetail.setRecordId(pdStockRecord.getId());
                List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
                List<PdStockRecordDetail> uniqueList = new ArrayList<>();

                int i = 0;
                for (PdStockRecordDetail item : pdStockRecordDetailList) {
                    //唯一码 拆分，用于唯一码列表显示
                    List<String> refBarCodeList = Arrays.asList(item.getRefBarCode().split(","));
                    for(String refBarCode : refBarCodeList){
                        i += 1;
                        PdStockRecordDetail unique = new PdStockRecordDetail();
                        BeanUtils.copyProperties(item, unique);
                        unique.setRefBarCode(refBarCode);
                        unique.setProductNum(1D);
                        unique.setId(i+"");
                        uniqueList.add(unique);
                    }
                }
                page = page.setRecords(uniqueList);
            }
        }

        return Result.ok(page);
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
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1);
        pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); //已提交状态
        IPage<PdStockRecord> pageList = pdStockRecordService.queryList(page, pdStockRecord, PdConstant.RECODE_TYPE_1);
        return Result.ok(pageList);
    }

    /**
     * 保存
     *
     * @param pdStockRecord
     * @return
     */
    @PostMapping(value = "/add")
    @RequiresPermissions("stock:form:inRecord")
    public Result<?> add(@RequestBody PdStockRecord pdStockRecord) {
        if(oConvertUtils.isEmpty(pdStockRecord.getId())){
            List<PdStockRecord> list = pdStockRecordService.queryList(pdStockRecord,PdConstant.RECODE_TYPE_1);
            if(CollectionUtils.isNotEmpty(list)){
                return Result.error("入库单已被保存或提交，不能保存草稿！");
            }
        }else{
            PdStockRecord entity = pdStockRecordService.getById(pdStockRecord.getId());
            if(entity != null && PdConstant.SUBMIT_STATE_2.equals(entity.getSubmitStatus())){
                return Result.error("入库单已被提交，不能保存草稿！");
            }
        }

//        // 盘点不能 操作库存
//        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, pdStockRecord.getInDepartId()));
//        if(CollectionUtils.isNotEmpty(checkList)){
//            return Result.error("本库房正在盘点，不能入库！");
//        }

        String recordId = pdStockRecordService.saveMain(pdStockRecord, pdStockRecord.getPdStockRecordDetailList(), PdConstant.RECODE_TYPE_1);

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
    @RequiresPermissions("stock:form:inRecord")
    public Result<?> submit(@RequestBody PdStockRecord pdStockRecord) {
        if (oConvertUtils.isNotEmpty(pdStockRecord.getId())) {
            PdStockRecord entity = pdStockRecordService.getById(pdStockRecord.getId());
            if(entity != null && PdConstant.SUBMIT_STATE_2.equals(entity.getSubmitStatus())){
                return Result.error("入库单已被提交，不能再次提交！");
            }
        }

        // 盘点不能 操作库存
        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, pdStockRecord.getInDepartId()));
        if(CollectionUtils.isNotEmpty(checkList)){
            return Result.error("本库房正在盘点，不能入库！");
        }

        String recordId = pdStockRecordService.submit(pdStockRecord, pdStockRecord.getPdStockRecordDetailList(), PdConstant.RECODE_TYPE_1);
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
    @RequiresPermissions("stock:form:inRecordExamine")
    public Result<?> audit(@RequestBody PdStockRecord pdStockRecord) {
        PdStockRecord entity = pdStockRecordService.getOne(pdStockRecord);
        if (entity == null) {
            return Result.error("未找到对应数据");
        }
        if(PdConstant.AUDIT_STATE_2.equals(entity.getAuditStatus()) || PdConstant.AUDIT_STATE_3.equals(entity.getAuditStatus())){
            return Result.error("入库单已被审批，不能再次审批！");
        }

        // 盘点不能 操作库存
        List<PdProductStockCheckPermission> checkList = pdProductStockCheckPermissionService.list(new LambdaQueryWrapper<PdProductStockCheckPermission>().eq(PdProductStockCheckPermission::getTargetDepartId, entity.getInDepartId()));
        if(CollectionUtils.isNotEmpty(checkList)){
            return Result.error("本库房正在盘点，不能入库！");
        }

        Map<String, String> result = pdStockRecordService.audit(pdStockRecord, entity, PdConstant.RECODE_TYPE_1);
        if (PdConstant.SUCCESS_200.equals(result.get("code"))) {
            return Result.ok(result.get("message"));
        } else {
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
            return Result.error("当前入库单状态已被审批或已撤回，不能撤回！");
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
            return Result.error("当前入库单状态非待提交或已撤回状态，不能删除！");
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


        BigDecimal totalPrice = new BigDecimal(0);//总金额
        Double totalSum = new Double(0);//总数量
        for (PdStockRecordDetail item : pdStockRecordDetailList) {
            totalSum = totalSum + item.getProductNum();
            BigDecimal purchasePrice = item.getPurchasePrice() == null ? new BigDecimal(0) : item.getPurchasePrice();
            totalPrice = totalPrice.add(purchasePrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
        }
        pdStockRecord.setTotalSum(totalSum);
        pdStockRecord.setInTotalPrice(totalPrice);

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
        pdStockRecordDetail.setRecordType(PdConstant.RECODE_TYPE_1);
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
		List<PdStockRecordInPage> exportList = JSON.parseArray(JSON.toJSONString(detailList), PdStockRecordInPage.class);

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "入库记录表");
        mv.addObject(NormalExcelConstants.CLASS, PdStockRecordInPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("入库记录表数据", "导出人:" + sysUser.getRealname(), "入库记录表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 查询入库明细  mcb  --20200224 用于统计查询
     *
     * @param pdStockRecordDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/queryPdStockRecordInList")
    public Result<?> queryPdStockRecordInList(PdStockRecordDetail pdStockRecordDetail,
                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                              HttpServletRequest req) {

        Page<PdStockRecordDetail> page = new Page<PdStockRecordDetail>(pageNo, pageSize);
        pdStockRecordDetail.setRecordType(PdConstant.RECODE_TYPE_1);
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

        IPage<PdStockRecordDetail> pageList = pdStockRecordDetailService.selectList(page, pdStockRecordDetail);
        return Result.ok(pageList);
    }

    /**
     * 调入明细查询  mcb  --20200308 用于统计查询
     */
    @GetMapping(value = "stockRecordCallInQuery")
    public Result<?> stockRecordCallInQuery(PdStockRecord stockRecord,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        stockRecord.setRecordType(PdConstant.RECODE_TYPE_1);
        stockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);//只查已通过的明细
        stockRecord.setInType(PdConstant.IN_TYPE_3);//只查调拨入库的明细
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        stockRecord.setDepartParentId(sysUser.getDepartParentId());
        Page<PdStockRecord> page = new Page<PdStockRecord>(pageNo, pageSize);
        page = pdStockRecordService.selectTransferList(page, stockRecord);
        return Result.ok(page);
    }



    /**
     * 入库统计报表  mcb  --20200616 用于统计查询  入库统计报表
     */
    @GetMapping(value = "stockRecordReportQuery")
    public Result<?> stockRecordReportQuery(PdStockRecord stockRecord,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        stockRecord.setRecordType(PdConstant.RECODE_TYPE_1);
        stockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);//只查已通过的明细
        stockRecord.setInType(PdConstant.IN_TYPE_1);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        stockRecord.setInDepartId(sysUser.getCurrentDepartId());
        stockRecord.setDepartParentId(sysUser.getDepartParentId());
        Page<PdStockRecord> page = new Page<PdStockRecord>(pageNo, pageSize);
        IPage<PdStockRecord> pageList = pdStockRecordService.stockRecordReportQuery(page, stockRecord);//
        //page = pdStockRecordService.stockRecordReportQuery(page, stockRecord);
        return Result.ok(pageList);
    }

    /**
     * 入库统计视图  mcb  --20200617 用于统计查询  入库统计报表视图
     */
    @GetMapping(value = "queryRecordView")
    public Result<?> queryRecordView(PdStockRecord stockRecord){
        Map map=new HashMap();
        List<HashMap> orderMoney=new  ArrayList<HashMap>();//金额
        List<HashMap> orderCount=new  ArrayList<HashMap>();//数量
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        stockRecord.setDepartParentId(sysUser.getDepartParentId());
        stockRecord.setInDepartId(sysUser.getCurrentDepartId());
        //根据产品按月统计入库金额
        orderMoney=pdStockRecordService.queryRecordViewMoney(stockRecord);
        //根据产品按月统计入库数量
        orderCount=pdStockRecordService.queryRecordViewCount(stockRecord);
        map.put("orderMoney",orderMoney);
        map.put("orderCount",orderCount);
        return Result.ok(map);
    }


    /**
     * 导出excel(入库统计报表导出)
     *
     * @param request
     * @param pdStockRecord
     */
    @RequestMapping(value = "/ReportQueryExportXls")
    public ModelAndView ReportQueryExportXls(HttpServletRequest request, PdStockRecord pdStockRecord) {
        pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1);
        pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);//只查已通过的明细
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdStockRecord.setDepartParentId(sysUser.getDepartParentId());
        pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
        List<PdStockRecord> detailList =  pdStockRecordService.stockRecordReportQuery(pdStockRecord);
        List<PdStockRecordInExcle> exportList = JSON.parseArray(JSON.toJSONString(detailList), PdStockRecordInExcle.class);
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "入库统计报表");
        mv.addObject(NormalExcelConstants.CLASS, PdStockRecordInExcle.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("入库统计报表数据", "导出人:" + sysUser.getRealname(), "入库统计报表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 供应商用量明细查询
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
}
