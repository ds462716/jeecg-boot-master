package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.vo.PdNumericalInfHcExlce;
import org.jeecg.modules.external.vo.PdNumericalInfHcExlceGZSLYY;
import org.jeecg.modules.external.vo.PdNumericalInfSjExlce;
import org.jeecg.modules.external.vo.PdNumericalInfSjExlceGZSLYY;
import org.jeecg.modules.pd.entity.PdNumericalInf;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdNumericalInfService;
import org.jeecg.modules.pd.service.IPdStatisticalReportService;
import org.jeecg.modules.pd.vo.*;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Description: 统计报表
* @Author:
* @Date:   2020-08-13
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/pdStatisticalReport")
@Slf4j
public class PdStatisticalReportController extends JeecgController<PdStatisticalReport, IPdStatisticalReportService> {

    @Autowired
    private IPdStatisticalReportService pdStatisticalReportService;
    @Autowired
    private IPdDepartService pdDepartService;
    @Autowired
    private IPdNumericalInfService pdNumericalInfService;



    //供应商用量使用统计 start

    /**
     * 供应商用量使用统计
     *
     * @param rpSupplierUseReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/supplierUseReport")
    public Result<?> supplierUseReport(RpSupplierUseReportPage rpSupplierUseReportPage,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<RpSupplierUseReportPage> page = new Page<RpSupplierUseReportPage>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpSupplierUseReportPage.setDepartParentId(sysUser.getDepartParentId());
        IPage<RpSupplierUseReportPage> pageList = pdStatisticalReportService.supplierUseReport(page, rpSupplierUseReportPage);
        return Result.ok(pageList);
    }

    /**
     * 导出excel(供应商用量使用统计导出)
     *
     * @param request
     * @param rpSupplierUseReportPage
     */
    @RequestMapping(value = "/exportSupplierUseReportXls")
    public ModelAndView exportSupplierUseReportXls(HttpServletRequest request, RpSupplierUseReportPage rpSupplierUseReportPage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpSupplierUseReportPage.setDepartParentId(sysUser.getDepartParentId());
        List<RpSupplierUseReportPage> pageList = pdStatisticalReportService.supplierUseReport(rpSupplierUseReportPage);
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "供应商用量使用统计报表");
        mv.addObject(NormalExcelConstants.CLASS, RpSupplierUseReportPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商用量使用统计报表数据", "导出人:" + sysUser.getRealname(), "出库统计报表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * zxh供应商用量统计查询入库明细
     *
     * @param inDetail
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/supplierInDetailReport")
    public Result<?> supplierInDetailReport(PdStockRecordDetail inDetail,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        inDetail.setDepartParentId(sysUser.getDepartParentId());
        Page<PdStockRecordDetail> inPageDetail = new Page<PdStockRecordDetail>(pageNo, pageSize);
        IPage<PdStockRecordDetail> inPageDetailList = pdStatisticalReportService.supplierInDetailReport(inPageDetail, inDetail);
        List<PdStockRecordDetail> inList = inPageDetailList.getRecords();
        List<RpInAndOutDetailReportPage> inReportList = JSON.parseArray(JSON.toJSONString(inList), RpInAndOutDetailReportPage.class);
        Page<RpInAndOutDetailReportPage> inPage = new Page<RpInAndOutDetailReportPage>(pageNo, pageSize);
        inPage.setTotal(inPageDetailList.getTotal());
        inPage.setSize(inPageDetailList.getSize());
        inPage.setCurrent(inPageDetailList.getCurrent());
        inPage.setRecords(inReportList);
        return Result.ok(inPage);
    }

    /**
     * zxh用量明细统计报表
     *
     * @param rpUseDetailReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpUseDetailReport")
    public Result<?> rpUseDetailReport(RpUseDetailReportPage rpUseDetailReportPage,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpUseDetailReportPage.setDepartParentId(sysUser.getDepartParentId());
        Page<RpUseDetailReportPage> usePageDetail = new Page<RpUseDetailReportPage>(pageNo, pageSize);
        IPage<RpUseDetailReportPage> usePageDetailList = pdStatisticalReportService.rpUseDetailReport(usePageDetail, rpUseDetailReportPage);
        List<RpUseDetailReportPage> useList = usePageDetailList.getRecords();
        List<RpUseDetailReportPage> inReportList = JSON.parseArray(JSON.toJSONString(useList), RpUseDetailReportPage.class);
        Page<RpUseDetailReportPage> usePage = new Page<RpUseDetailReportPage>(pageNo, pageSize);
        usePage.setTotal(usePageDetailList.getTotal());
        usePage.setSize(usePageDetailList.getSize());
        usePage.setCurrent(usePageDetailList.getCurrent());
        usePage.setRecords(inReportList);
        return Result.ok(usePage);
    }

    /**
     * zxh用量明细统计报表
     *
     * @param rpReDetailReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpReDetailReport")
    public Result<?> rpReDetailReport(RpReDetailReportPage rpReDetailReportPage,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpReDetailReportPage.setDepartParentId(sysUser.getDepartParentId());
        Page<RpReDetailReportPage> rePageDetail = new Page<RpReDetailReportPage>(pageNo, pageSize);
        IPage<RpReDetailReportPage> usePageDetailList = pdStatisticalReportService.rpReDetailReport(rePageDetail, rpReDetailReportPage);
        List<RpReDetailReportPage> useList = usePageDetailList.getRecords();
        List<RpReDetailReportPage> inReportList = JSON.parseArray(JSON.toJSONString(useList), RpReDetailReportPage.class);
        Page<RpReDetailReportPage> rePage = new Page<RpReDetailReportPage>(pageNo, pageSize);
        rePage.setTotal(usePageDetailList.getTotal());
        rePage.setSize(usePageDetailList.getSize());
        rePage.setCurrent(usePageDetailList.getCurrent());
        rePage.setRecords(inReportList);
        return Result.ok(rePage);
    }

    //供应商用量使用统计 end

    //部门用量使用统计 start

    /**
     * zxh部门用量使用统计报表
     *
     * @param rpDepartUseReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/departUseReport")
    public Result<?> departUseReport(RpDepartUseReportPage rpDepartUseReportPage,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<RpDepartUseReportPage> page = new Page<RpDepartUseReportPage>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpDepartUseReportPage.setDepartParentId(sysUser.getDepartParentId());
        if (oConvertUtils.isNotEmpty(rpDepartUseReportPage.getDepartIds()) && !"undefined".equals(rpDepartUseReportPage.getDepartIds())) {
            rpDepartUseReportPage.setDepartIdList(Arrays.asList(rpDepartUseReportPage.getDepartIds().split(",")));
        } else {
            //查询科室下所有下级科室的ID
            SysDepart sysDepart = new SysDepart();
            List<String> departList = pdDepartService.selectListDepart(sysDepart);
            rpDepartUseReportPage.setDepartIdList(departList);
        }
        IPage<RpDepartUseReportPage> pageList = pdStatisticalReportService.departUseReport(page, rpDepartUseReportPage);
        return Result.ok(pageList);
    }

    /**
     * zxh导出excel(部门用量使用统计)
     *
     * @param request
     * @param rpDepartUseReportPage
     */
    @RequestMapping(value = "/exportDepartUseReportXls")
    public ModelAndView exportDepartUseReportXls(HttpServletRequest request, RpDepartUseReportPage rpDepartUseReportPage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpDepartUseReportPage.setDepartParentId(sysUser.getDepartParentId());
        if (oConvertUtils.isNotEmpty(rpDepartUseReportPage.getDepartIds()) && !"undefined".equals(rpDepartUseReportPage.getDepartIds())) {
            rpDepartUseReportPage.setDepartIdList(Arrays.asList(rpDepartUseReportPage.getDepartIds().split(",")));
        } else {
            //查询科室下所有下级科室的ID
            SysDepart sysDepart = new SysDepart();
            List<String> departList = pdDepartService.selectListDepart(sysDepart);
            rpDepartUseReportPage.setDepartIdList(departList);
        }
        List<RpDepartUseReportPage> pageList = pdStatisticalReportService.departUseReport(rpDepartUseReportPage);
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "部门用量使用统计报表");
        mv.addObject(NormalExcelConstants.CLASS, RpDepartUseReportPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("部门用量使用统计报表数据", "导出人:" + sysUser.getRealname(), "部门用量报表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * zxh部门用量明细统计报表
     *
     * @param rpDepartUseDetailReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpDepartUseDetailReport")
    public Result<?> rpDepartUseDetailReport(RpDepartUseDetailReportPage rpDepartUseDetailReportPage,
                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpDepartUseDetailReportPage.setDepartParentId(sysUser.getDepartParentId());
        Page<RpDepartUseDetailReportPage> usePageDetail = new Page<RpDepartUseDetailReportPage>(pageNo, pageSize);
        IPage<RpDepartUseDetailReportPage> usePageDetailList = pdStatisticalReportService.rpDepartUseDetailReport(usePageDetail, rpDepartUseDetailReportPage);
        List<RpDepartUseDetailReportPage> useList = usePageDetailList.getRecords();
        List<RpDepartUseDetailReportPage> inReportList = JSON.parseArray(JSON.toJSONString(useList), RpDepartUseDetailReportPage.class);
        Page<RpDepartUseDetailReportPage> usePage = new Page<RpDepartUseDetailReportPage>(pageNo, pageSize);
        usePage.setTotal(usePageDetailList.getTotal());
        usePage.setSize(usePageDetailList.getSize());
        usePage.setCurrent(usePageDetailList.getCurrent());
        usePage.setRecords(inReportList);
        return Result.ok(usePage);
    }

    //部门用量使用统计 end

    //出入库统计报表 jiangxz  20200814  start

    /**
     * 出入库统计报表
     *
     * @param rpInAndOutReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpInAndOutReport")
    public Result<?> rpInAndOutReport(RpInAndOutReportPage rpInAndOutReportPage,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpInAndOutReportPage.setDepartParentId(sysUser.getDepartParentId());

        if (oConvertUtils.isNotEmpty(rpInAndOutReportPage.getDepartIds()) && !"undefined".equals(rpInAndOutReportPage.getDepartIds())) {
            rpInAndOutReportPage.setDepartIdList(Arrays.asList(rpInAndOutReportPage.getDepartIds().split(",")));
        } else {
            //查询科室下所有下级科室的ID
            SysDepart sysDepart = new SysDepart();
            List<String> departList = pdDepartService.selectListDepart(sysDepart);
            rpInAndOutReportPage.setDepartIdList(departList);
        }

        Page<RpInAndOutReportPage> page = new Page<RpInAndOutReportPage>(pageNo, pageSize);
        IPage<RpInAndOutReportPage> pageList = pdStatisticalReportService.rpInAndOutReport(page, rpInAndOutReportPage);
        return Result.ok(pageList);
    }

    /**
     * 出入库明细统计报表
     *
     * @param inDetail
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpInDetailReport")
    public Result<?> rpInDetailReport(RpInAndOutDetailReportPage inDetail,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //查询入库明细
        if (oConvertUtils.isNotEmpty(inDetail.getDepartId())) {
            List<String> inDepartList = new ArrayList<>();
            inDepartList.add(inDetail.getDepartId());
            inDetail.setInDepartIdList(inDepartList);
        }
        inDetail.setDepartParentId(sysUser.getDepartParentId());
        inDetail.setRecordType(PdConstant.RECODE_TYPE_1);
        inDetail.setAuditStatus(PdConstant.AUDIT_STATE_2);
        inDetail.setDepartId(null);

        Page<RpInAndOutDetailReportPage> inPageDetail = new Page<RpInAndOutDetailReportPage>(pageNo, pageSize);
        IPage<RpInAndOutDetailReportPage> inPageDetailList = pdStatisticalReportService.rpInAndOutDetailReport(inPageDetail, inDetail);

        return Result.ok(inPageDetailList);
    }

    @GetMapping(value = "/rpOutDetailReport")
    public Result<?> rpOutDetailReport(RpInAndOutDetailReportPage outDetail,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if (oConvertUtils.isEmpty(outDetail.getDepartId())) {
            return Result.error("参数不正确，请重新查询！");
        }
        // 查询出库明细
        List<String> outDepartList = new ArrayList<>();
        outDepartList.add(outDetail.getDepartId());
        outDetail.setOutDepartIdList(outDepartList);
        outDetail.setDepartParentId(sysUser.getDepartParentId());
        outDetail.setRecordType(PdConstant.RECODE_TYPE_2);
        outDetail.setAuditStatus(PdConstant.AUDIT_STATE_2);
        outDetail.setDepartId(null);

        Page<RpInAndOutDetailReportPage> outPageDetail = new Page<RpInAndOutDetailReportPage>(pageNo, pageSize);
        IPage<RpInAndOutDetailReportPage> outPageDetailList = pdStatisticalReportService.rpInAndOutDetailReport(outPageDetail, outDetail);

        return Result.ok(outPageDetailList);
    }

    /**
     * 出入库报表导出
     *
     * @param request
     * @param rpInAndOutReportPage
     * @return
     */
    @RequestMapping(value = "/exportInAndOutReportXls")
    public ModelAndView exportInAndOutReportXls(HttpServletRequest request, RpInAndOutReportPage rpInAndOutReportPage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpInAndOutReportPage.setDepartParentId(sysUser.getDepartParentId());

        if (oConvertUtils.isNotEmpty(rpInAndOutReportPage.getDepartIds()) && !"undefined".equals(rpInAndOutReportPage.getDepartIds())) {
            rpInAndOutReportPage.setDepartIdList(Arrays.asList(rpInAndOutReportPage.getDepartIds().split(",")));
        } else {
            //查询科室下所有下级科室的ID
            SysDepart sysDepart = new SysDepart();
            List<String> departList = pdDepartService.selectListDepart(sysDepart);
            rpInAndOutReportPage.setDepartIdList(departList);
        }

        List<RpInAndOutReportPage> pageList = pdStatisticalReportService.rpInAndOutReport(rpInAndOutReportPage);

        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "出入库统计报表");
        mv.addObject(NormalExcelConstants.CLASS, RpInAndOutReportPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("出库统计报表数据", "导出人:" + sysUser.getRealname(), "出库统计报表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 入库明细报表导出
     *
     * @param request
     * @param inDetail
     * @return
     */
    @RequestMapping(value = "/exportInReportXls")
    public ModelAndView exportInReportXls(HttpServletRequest request, RpInAndOutDetailReportPage inDetail) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //查询入库明细
        List<String> inDepartList = new ArrayList<>();
        inDepartList.add(inDetail.getDepartId());
        inDetail.setInDepartIdList(inDepartList);
        inDetail.setDepartParentId(sysUser.getDepartParentId());
        inDetail.setRecordType(PdConstant.RECODE_TYPE_1);
        inDetail.setAuditStatus(PdConstant.AUDIT_STATE_2);
        inDetail.setInType(PdConstant.IN_TYPE_1);
        inDetail.setDepartId(null);

        List<RpInAndOutDetailReportPage> inList = pdStatisticalReportService.rpInAndOutDetailReport(inDetail);
        List<RpInDetailReportExcel> inReportList = JSON.parseArray(JSON.toJSONString(inList), RpInDetailReportExcel.class);

        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "入库明细统计报表");
        mv.addObject(NormalExcelConstants.CLASS, RpInDetailReportExcel.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("入库明细统计报表数据", "导出人:" + sysUser.getRealname(), "入库明细统计报表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, inReportList);

        return mv;
    }

    /**
     * 出库明细报表导出
     *
     * @param request
     * @param outDetail
     * @return
     */
    @RequestMapping(value = "/exportOutReportXls")
    public ModelAndView exportOutReportXls(HttpServletRequest request, RpInAndOutDetailReportPage outDetail) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 查询出库明细
        List<String> outDepartList = new ArrayList<>();
        outDepartList.add(outDetail.getDepartId());
        outDetail.setOutDepartIdList(outDepartList);
        outDetail.setDepartParentId(sysUser.getDepartParentId());
        outDetail.setRecordType(PdConstant.RECODE_TYPE_2);
        outDetail.setAuditStatus(PdConstant.AUDIT_STATE_2);
        outDetail.setDepartId(null);

        List<RpInAndOutDetailReportPage> outList = pdStatisticalReportService.rpInAndOutDetailReport(outDetail);
        List<RpOutDetailReportExcel> outReportList = JSON.parseArray(JSON.toJSONString(outList), RpOutDetailReportExcel.class);

        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "出库明细统计报表");
        mv.addObject(NormalExcelConstants.CLASS, RpOutDetailReportExcel.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("出库明细统计报表数据", "导出人:" + sysUser.getRealname(), "出库明细统计报表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, outReportList);

        return mv;
    }
    //出入库统计报表 jiangxz  20200814  end

    /**
     * 试剂/耗材消耗月统计报表查询
     *
     * @param pdNumericalInf
     * @return
     */
    @GetMapping(value = "/queryNumericalInfList")
    public Result<?> queryPageList(PdNumericalInf pdNumericalInf,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdNumericalInf> page = new Page<PdNumericalInf>(pageNo, pageSize);
        if (oConvertUtils.isNotEmpty(pdNumericalInf.getDepartIds()) && !"undefined".equals(pdNumericalInf.getDepartIds())) {
            pdNumericalInf.setDepartIdList(Arrays.asList(pdNumericalInf.getDepartIds().split(",")));
        } else {
            //查询科室下所有下级科室的ID
            SysDepart sysDepart = new SysDepart();
            List<String> departList = pdDepartService.selectListDepart(sysDepart);
            pdNumericalInf.setDepartIdList(departList);
        }
        IPage<PdNumericalInf> pageList = pdNumericalInfService.selectListByPage(page, pdNumericalInf);
        return Result.ok(pageList);
    }

    /**
     * 导出excel(试剂/耗材月消耗统计报表)
     *
     * @param request
     * @param pdNumericalInf
     */
    @RequestMapping(value = "/numericalInfExportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdNumericalInf pdNumericalInf) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String hospitalCode=sysUser.getHospitalCode();
        if (oConvertUtils.isNotEmpty(pdNumericalInf.getDepartIds()) && !"undefined".equals(pdNumericalInf.getDepartIds())) {
            pdNumericalInf.setDepartIdList(Arrays.asList(pdNumericalInf.getDepartIds().split(",")));
        } else {
            //查询科室下所有下级科室的ID
            SysDepart sysDepart = new SysDepart();
            List<String> departList = pdDepartService.selectListDepart(sysDepart);
            pdNumericalInf.setDepartIdList(departList);
        }
        List<PdNumericalInf> list = pdNumericalInfService.selectList(pdNumericalInf);//
        String tjType = pdNumericalInf.getTjType();
        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<PdNumericalInfHcExlce> exportHcList = new ArrayList<>();
        List<PdNumericalInfSjExlce> exportSjList = new ArrayList<>();
        List<PdNumericalInfHcExlceGZSLYY> exportHcGzslList = new ArrayList<>();
        List<PdNumericalInfSjExlceGZSLYY> exportSjGzslList = new ArrayList<>();
        if ("0".equals(tjType)) { //耗材统计导出
            if("GZSLYY".equals(hospitalCode)){ //如果是市立医院，则只导出金额，数量不导出
                exportHcGzslList = JSON.parseArray(JSON.toJSONString(list), PdNumericalInfHcExlceGZSLYY.class);
                mv.addObject(NormalExcelConstants.CLASS, PdNumericalInfHcExlceGZSLYY.class);
                mv.addObject(NormalExcelConstants.DATA_LIST, exportHcGzslList);
            }else{
                exportHcList = JSON.parseArray(JSON.toJSONString(list), PdNumericalInfHcExlce.class);
                mv.addObject(NormalExcelConstants.CLASS, PdNumericalInfHcExlce.class);
                mv.addObject(NormalExcelConstants.DATA_LIST, exportHcList);
            }
            mv.addObject(NormalExcelConstants.FILE_NAME, "耗材月统计报表");
            mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("耗材月统计报表", "导出人:" + sysUser.getRealname(), "耗材月统计报表"));
        } else { //试剂统计导出
            if("GZSLYY".equals(hospitalCode)){ //如果是市立医院，则只导出金额，数量不导出
                exportSjGzslList = JSON.parseArray(JSON.toJSONString(list), PdNumericalInfSjExlceGZSLYY.class);
                mv.addObject(NormalExcelConstants.CLASS, PdNumericalInfSjExlceGZSLYY.class);
                mv.addObject(NormalExcelConstants.DATA_LIST, exportSjGzslList);
            }else{
                exportSjList = JSON.parseArray(JSON.toJSONString(list), PdNumericalInfSjExlce.class);
                mv.addObject(NormalExcelConstants.CLASS, PdNumericalInfSjExlce.class);
                mv.addObject(NormalExcelConstants.DATA_LIST, exportSjList);
            }
            mv.addObject(NormalExcelConstants.FILE_NAME, "试剂月统计报表");
            mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("试剂月统计报表", "导出人:" + sysUser.getRealname(), "试剂月统计报表"));
        }
        return mv;
    }

    //库存统计报表 start

    /**
     * zxh库存统计报表
     *
     * @param rpDepartStockReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/departStockReport")
    public Result<?> departStockReport(RpDepartStockReportPage rpDepartStockReportPage,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<RpDepartStockReportPage> page = new Page<RpDepartStockReportPage>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpDepartStockReportPage.setDepartParentId(sysUser.getDepartParentId());
        if (oConvertUtils.isNotEmpty(rpDepartStockReportPage.getDepartIds()) && !"undefined".equals(rpDepartStockReportPage.getDepartIds())) {
            rpDepartStockReportPage.setDepartIdList(Arrays.asList(rpDepartStockReportPage.getDepartIds().split(",")));
        } else {
            //查询科室下所有下级科室的ID
            SysDepart sysDepart = new SysDepart();
            List<String> departList = pdDepartService.selectListDepart(sysDepart);
            rpDepartStockReportPage.setDepartIdList(departList);
        }
        IPage<RpDepartStockReportPage> pageList = pdStatisticalReportService.departStockReport(page, rpDepartStockReportPage);
        return Result.ok(pageList);
    }

    /**
     * zxh导出excel(库存统计报表)
     *
     * @param request
     * @param rpDepartStockReportPage
     */
    @RequestMapping(value = "/exportDepartStockReportXls")
    public ModelAndView exportDepartStockReportXls(HttpServletRequest request, RpDepartStockReportPage rpDepartStockReportPage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpDepartStockReportPage.setDepartParentId(sysUser.getDepartParentId());
        if (oConvertUtils.isNotEmpty(rpDepartStockReportPage.getDepartIds()) && !"undefined".equals(rpDepartStockReportPage.getDepartIds())) {
            rpDepartStockReportPage.setDepartIdList(Arrays.asList(rpDepartStockReportPage.getDepartIds().split(",")));
        } else {
            //查询科室下所有下级科室的ID
            SysDepart sysDepart = new SysDepart();
            List<String> departList = pdDepartService.selectListDepart(sysDepart);
            rpDepartStockReportPage.setDepartIdList(departList);
        }
        List<RpDepartStockReportPage> pageList = pdStatisticalReportService.departStockReport(rpDepartStockReportPage);
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "部门用量使用统计报表");
        mv.addObject(NormalExcelConstants.CLASS, RpDepartStockReportPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("部门用量使用统计报表数据", "导出人:" + sysUser.getRealname(), "部门用量报表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * zxh部门用量明细统计报表
     *
     * @param rpDepartStockDetailReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpDepartStockDetailReport")
    public Result<?> rpDepartStockDetailReport(RpDepartStockDetailReportPage rpDepartStockDetailReportPage,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpDepartStockDetailReportPage.setDepartParentId(sysUser.getDepartParentId());
        Page<RpDepartStockDetailReportPage> stockPageDetail = new Page<RpDepartStockDetailReportPage>(pageNo, pageSize);
        IPage<RpDepartStockDetailReportPage> usePageDetailList = pdStatisticalReportService.rpDepartStockDetailReport(stockPageDetail, rpDepartStockDetailReportPage);
        List<RpDepartStockDetailReportPage> useList = usePageDetailList.getRecords();
        List<RpDepartStockDetailReportPage> inReportList = JSON.parseArray(JSON.toJSONString(useList), RpDepartStockDetailReportPage.class);
        Page<RpDepartStockDetailReportPage> usePage = new Page<RpDepartStockDetailReportPage>(pageNo, pageSize);
        usePage.setTotal(usePageDetailList.getTotal());
        usePage.setSize(usePageDetailList.getSize());
        usePage.setCurrent(usePageDetailList.getCurrent());
        usePage.setRecords(inReportList);
        return Result.ok(usePage);
    }
    //库存统计报表 end

    //供应商用量使用统计 start

    /**
     * 供应商用量使用统计
     *
     * @param rpSupplierUseReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/supplierReagentUseReport")
    public Result<?> supplierReagentUseReport(RpSupplierUseReportPage rpSupplierUseReportPage,
                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<RpSupplierUseReportPage> page = new Page<RpSupplierUseReportPage>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpSupplierUseReportPage.setDepartParentId(sysUser.getDepartParentId());
        IPage<RpSupplierUseReportPage> pageList = pdStatisticalReportService.supplierReagentUseReport(page, rpSupplierUseReportPage);
        return Result.ok(pageList);
    }

    /**
     * zxh用量明细统计报表
     *
     * @param rpReagentUseDetailReportPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/rpReagentUseDetailReport")
    public Result<?> rpReagentUseDetailReport(RpReagentUseDetailReportPage rpReagentUseDetailReportPage,
                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpReagentUseDetailReportPage.setDepartParentId(sysUser.getDepartParentId());
        Page<RpReagentUseDetailReportPage> usePageDetail = new Page<RpReagentUseDetailReportPage>(pageNo, pageSize);
        IPage<RpReagentUseDetailReportPage> usePageDetailList = pdStatisticalReportService.rpReagentUseDetailReport(usePageDetail, rpReagentUseDetailReportPage);
        List<RpReagentUseDetailReportPage> useList = usePageDetailList.getRecords();
        List<RpReagentUseDetailReportPage> inReportList = JSON.parseArray(JSON.toJSONString(useList), RpReagentUseDetailReportPage.class);
        Page<RpReagentUseDetailReportPage> usePage = new Page<RpReagentUseDetailReportPage>(pageNo, pageSize);
        usePage.setTotal(usePageDetailList.getTotal());
        usePage.setSize(usePageDetailList.getSize());
        usePage.setCurrent(usePageDetailList.getCurrent());
        usePage.setRecords(inReportList);
        return Result.ok(usePage);
    }

    /**
     * 综合统计   ---全院耗材占比  mcb  --20200907
     */
    @GetMapping(value = "queryConsumptionView")
    public Result<?> queryConsumptionView(RpPurchaseUseReportPage purchaseUseReportPage) {
        if (oConvertUtils.isNotEmpty(purchaseUseReportPage.getDepartIds()) && !"undefined".equals(purchaseUseReportPage.getDepartIds())) {
            purchaseUseReportPage.setDepartIdList(Arrays.asList(purchaseUseReportPage.getDepartIds().split(",")));
        }
        Map<String, Object> map = pdStatisticalReportService.queryConsumptionView(purchaseUseReportPage);
        return Result.ok(map);
    }

    /**
     * 综合统计   ---科室采购消耗表 mcb  --20200907
     */
    @GetMapping(value = "queryDepartContionView")
    public Result<?> queryDepartContionView(RpPurchaseUseReportPage purchaseUseReportPage) {
        Map<String, Object> map = pdStatisticalReportService.queryDepartPurchaseCountView(purchaseUseReportPage);
        return Result.ok(map);
    }

    /**
     * 综合统计   ---科室采购趋势图表  mcb  --20200907
     */
    @GetMapping(value = "queryPurchaseCountView")
    public Result<?> queryPurchaseCountView(RpPurchaseUseReportPage purchaseUseReportPage) {
        Map<String, Object> map = pdStatisticalReportService.queryPurchaseCountView(purchaseUseReportPage);
        return Result.ok(map);
    }

    /**
     * zxh采购金额环比
     */
    @GetMapping(value = "queryPurchaseAmountMomTableView")
    public Result<?> queryPurchaseAmountMomTableView(RpPurchaseUseReportPage purchaseUseReportPage) {
        Map<String, Object> map = pdStatisticalReportService.queryPurchaseAmountMomTableView(purchaseUseReportPage);
        return Result.ok(map);
    }

    /**
     * 综合统计   ---检验收入金额统计表  mcb  --20200907
     */
    @GetMapping(value = "queryItemMoneyCountView")
    public Result<?> queryItemMoneyCountView(RpPurchaseUseReportPage purchaseUseReportPage) {
        Map<String, Object> map = pdStatisticalReportService.queryItemMoneyCountView(purchaseUseReportPage);
        return Result.ok(map);
    }

    /**
     * 综合统计   ---科室采购环比-同比报表  mcb  --20200917
     */
    @GetMapping(value = "queryMoOnMoView")
    public Result<?> queryMoOnMoView(RpPurchaseUseReportPage purchaseUseReportPage,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (oConvertUtils.isNotEmpty(purchaseUseReportPage.getDepartIds()) && !"undefined".equals(purchaseUseReportPage.getDepartIds())) {
            purchaseUseReportPage.setDepartIdList(Arrays.asList(purchaseUseReportPage.getDepartIds().split(",")));
        }
        Page<RpPurchaseUseReportPage> pagePage = new Page<RpPurchaseUseReportPage>(pageNo, pageSize);
        IPage<RpPurchaseUseReportPage> list = pdStatisticalReportService.queryMoOnMoView(pagePage,purchaseUseReportPage);
        return Result.ok(list);
    }


    /**
     * 导出excel(科室采购环比-同比报表)
     *
     * @param request
     * @param purchaseUseReportPage
     */
    @RequestMapping(value = "/exportReportXls")
    public ModelAndView exportReportXls(HttpServletRequest request, RpPurchaseUseReportPage purchaseUseReportPage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String selectType=purchaseUseReportPage.getSelectType();
        if (oConvertUtils.isNotEmpty(purchaseUseReportPage.getDepartIds()) && !"undefined".equals(purchaseUseReportPage.getDepartIds())) {
            purchaseUseReportPage.setDepartIdList(Arrays.asList(purchaseUseReportPage.getDepartIds().split(",")));
        }
        List<RpPurchaseUseReportPage> pageList = pdStatisticalReportService.queryMoOnMoView(purchaseUseReportPage);
        String fileName="采购费用环比报表";
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        if("1".equals(selectType)){//同比
             fileName="采购费用同比报表";
            List<RpYrUseReportExcel> exportList = JSON.parseArray(JSON.toJSONString(pageList), RpYrUseReportExcel.class);
            mv.addObject(NormalExcelConstants.CLASS, RpYrUseReportExcel.class);
            mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        }else{//环比
            List<RpMoUseReportExcel> exportList = JSON.parseArray(JSON.toJSONString(pageList), RpMoUseReportExcel.class);
            mv.addObject(NormalExcelConstants.CLASS, RpMoUseReportExcel.class);
            mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        }
        mv.addObject(NormalExcelConstants.FILE_NAME, fileName);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(fileName, "导出人:" + sysUser.getRealname(), fileName));
        return mv;
    }


    /**
     * 综合统计   ---科室采购数据报表（前12各月份的数据）  mcb  --20200917
     */
    @GetMapping(value = "queryPurchaseTableView")
    public Result<?> queryPurchaseTableView(RpPurchaseUseReportPage purchaseUseReportPage,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        RpPurchaseMoneyReportPage moneyReport=new RpPurchaseMoneyReportPage();

        if (oConvertUtils.isNotEmpty(purchaseUseReportPage.getDepartIds()) && !"undefined".equals(purchaseUseReportPage.getDepartIds())) {
            moneyReport.setDepartIdList(Arrays.asList(purchaseUseReportPage.getDepartIds().split(",")));
        }
        Page<RpPurchaseMoneyReportPage> pagePage = new Page<RpPurchaseMoneyReportPage>(pageNo, pageSize);
        //先获取前12个月的月份数据
        List<RpPurchaseUseReportPage> monthList = pdStatisticalReportService.getYearMonth(purchaseUseReportPage);
        //在根据每个月作为条件，查询每个月的采购金额，收费金额，不可收费金额，然后在组装
        moneyReport.setMonth1(monthList.get(0).getYearMonth());
        moneyReport.setMonth2(monthList.get(1).getYearMonth());
        moneyReport.setMonth3(monthList.get(2).getYearMonth());
        moneyReport.setMonth4(monthList.get(3).getYearMonth());
        moneyReport.setMonth5(monthList.get(4).getYearMonth());
        moneyReport.setMonth6(monthList.get(5).getYearMonth());
        moneyReport.setMonth7(monthList.get(6).getYearMonth());
        moneyReport.setMonth8(monthList.get(7).getYearMonth());
        moneyReport.setMonth9(monthList.get(8).getYearMonth());
        moneyReport.setMonth10(monthList.get(9).getYearMonth());
        moneyReport.setMonth11(monthList.get(10).getYearMonth());
        moneyReport.setMonth12(monthList.get(11).getYearMonth());
        IPage<RpPurchaseMoneyReportPage> pageList=  pdStatisticalReportService.queryMonthMoneyList(pagePage,moneyReport);
        return Result.ok(pageList);
    }




    /**
     * 导出excel(科室采购环比-同比报表)
     *
     * @param request
     * @param purchaseUseReportPage
     */
    @RequestMapping(value = "/purchaseExportReportXls")
    public ModelAndView purchaseExportReportXls(HttpServletRequest request, RpPurchaseUseReportPage purchaseUseReportPage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        RpPurchaseMoneyReportPage moneyReport=new RpPurchaseMoneyReportPage();
        if (oConvertUtils.isNotEmpty(purchaseUseReportPage.getDepartIds()) && !"undefined".equals(purchaseUseReportPage.getDepartIds())) {
            moneyReport.setDepartIdList(Arrays.asList(purchaseUseReportPage.getDepartIds().split(",")));
        }
        //先获取前12个月的月份数据
        List<RpPurchaseUseReportPage> monthList = pdStatisticalReportService.getYearMonth(purchaseUseReportPage);
        //在根据每个月作为条件，查询每个月的采购金额，收费金额，不可收费金额，然后在组装

        moneyReport.setMonth1(monthList.get(0).getYearMonth());
        moneyReport.setMonth2(monthList.get(1).getYearMonth());
        moneyReport.setMonth3(monthList.get(2).getYearMonth());
        moneyReport.setMonth4(monthList.get(3).getYearMonth());
        moneyReport.setMonth5(monthList.get(4).getYearMonth());
        moneyReport.setMonth6(monthList.get(5).getYearMonth());
        moneyReport.setMonth7(monthList.get(6).getYearMonth());
        moneyReport.setMonth8(monthList.get(7).getYearMonth());
        moneyReport.setMonth9(monthList.get(8).getYearMonth());
        moneyReport.setMonth10(monthList.get(9).getYearMonth());
        moneyReport.setMonth11(monthList.get(10).getYearMonth());
        moneyReport.setMonth12(monthList.get(11).getYearMonth());
        List<RpPurchaseMoneyReportPage> exportList=  pdStatisticalReportService.queryMonthMoneyList(moneyReport);
        String fileName="采购费用统计报表";
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.CLASS, RpPurchaseMoneyReportPage.class);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        mv.addObject(NormalExcelConstants.FILE_NAME, fileName);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(fileName, "导出人:" + sysUser.getRealname(), fileName));
        return mv;
    }



    /**
     * 科室领用情况统计报表
     *
     * @param rpDepartApplyPage
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/departApplyUseReport")
    public Result<?> departApplyUseReport(RpDepartApplyPage rpDepartApplyPage,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<RpDepartApplyPage> page = new Page<RpDepartApplyPage>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        rpDepartApplyPage.setDepartParentId(sysUser.getDepartParentId());
        if (oConvertUtils.isNotEmpty(rpDepartApplyPage.getDepartIds()) && !"undefined".equals(rpDepartApplyPage.getDepartIds())) {
            rpDepartApplyPage.setDepartIdList(Arrays.asList(rpDepartApplyPage.getDepartIds().split(",")));
        }
        //获取当前年份
        Integer year= DateUtils.getNowYear();
        if(oConvertUtils.isEmpty(rpDepartApplyPage.getYear())){
            rpDepartApplyPage.setYear(String.valueOf(year));
        }
        IPage<RpDepartApplyPage> pageList = pdStatisticalReportService.departApplyUseReportPage(page, rpDepartApplyPage);
        return Result.ok(pageList);
    }


    /**
     * 导出excel(科室领用情况统计报表)
     *
     * @param request
     * @param rpDepartApplyPage
     */
    @RequestMapping(value = "/departApplyExportXls")
    public ModelAndView departApplyExportXls(HttpServletRequest request, RpDepartApplyPage rpDepartApplyPage) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
         if (oConvertUtils.isNotEmpty(rpDepartApplyPage.getDepartIds()) && !"undefined".equals(rpDepartApplyPage.getDepartIds())) {
            rpDepartApplyPage.setDepartIdList(Arrays.asList(rpDepartApplyPage.getDepartIds().split(",")));
        }
        //获取当前年份
        Integer year= DateUtils.getNowYear();
        if(oConvertUtils.isEmpty(rpDepartApplyPage.getYear())){
            rpDepartApplyPage.setYear(String.valueOf(year));
        }
        List<RpDepartApplyPage> exportList=  pdStatisticalReportService.departApplyUseReportList(rpDepartApplyPage);
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.CLASS, RpDepartApplyPage.class);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        mv.addObject(NormalExcelConstants.FILE_NAME, "科室领用情况统计报表");
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("科室领用情况统计报表("+rpDepartApplyPage.getYear()+")", "导出人:" + sysUser.getRealname(), "科室领用情况统计报表"));
        return mv;
    }
}
