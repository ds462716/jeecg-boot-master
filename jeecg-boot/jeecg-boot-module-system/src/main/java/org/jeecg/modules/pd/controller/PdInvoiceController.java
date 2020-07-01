package org.jeecg.modules.pd.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdInvoiceDetail;
import org.jeecg.modules.pd.entity.PdInvoice;
import org.jeecg.modules.pd.service.IPdInvoiceService;
import org.jeecg.modules.pd.service.IPdInvoiceDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: pd_invoice
 * @Author: jiangxz
 * @Date: 2020-06-24
 * @Version: V1.0
 */
@Api(tags = "pd_invoice")
@RestController
@RequestMapping("/pd/pdInvoice")
@Slf4j
public class PdInvoiceController {
    @Autowired
    private IPdInvoiceService pdInvoiceService;
    @Autowired
    private IPdInvoiceDetailService pdInvoiceDetailService;

    /**
     * 初始化Modal页面
     * @param id
     * @param req
     * @return
     */
    @GetMapping(value = "/initModal")
    public Result<?> initModal(@RequestParam(name = "id") String id, HttpServletRequest req) {
        PdInvoice pdInvoice = new PdInvoice();
        pdInvoice.setInvoiceRegNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_FP));
        return Result.ok(pdInvoice);
    }

    /**
     * 分页列表查询
     *
     * @param pdInvoiceDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "pd_invoice-分页列表查询")
    @ApiOperation(value = "pd_invoice-分页列表查询", notes = "pd_invoice-分页列表查询")
    @GetMapping(value = "/listByStockRecord")
    public Result<?> queryPageListByStockRecord(PdInvoiceDetail pdInvoiceDetail,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
        Page<PdInvoiceDetail> page = new Page<PdInvoiceDetail>(pageNo, pageSize);
        IPage<PdInvoiceDetail> pageList = pdInvoiceDetailService.selectByStockRecord(page, pdInvoiceDetail);
        return Result.ok(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param pdInvoice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "pd_invoice-分页列表查询")
    @ApiOperation(value = "pd_invoice-分页列表查询", notes = "pd_invoice-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PdInvoice pdInvoice,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<PdInvoice> queryWrapper = QueryGenerator.initQueryWrapper(pdInvoice, req.getParameterMap());
        Page<PdInvoice> page = new Page<PdInvoice>(pageNo, pageSize);
        IPage<PdInvoice> pageList = pdInvoiceService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param pdInvoice
     * @return
     */
    @AutoLog(value = "pd_invoice-添加")
    @ApiOperation(value = "pd_invoice-添加", notes = "pd_invoice-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PdInvoice pdInvoice) {
        pdInvoiceService.saveMain(pdInvoice);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param PdInvoice
     * @return
     */
    @AutoLog(value = "pd_invoice-编辑")
    @ApiOperation(value = "pd_invoice-编辑", notes = "pd_invoice-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PdInvoice PdInvoice) {
        PdInvoice pdInvoice = new PdInvoice();
        BeanUtils.copyProperties(PdInvoice, pdInvoice);
        PdInvoice pdInvoiceEntity = pdInvoiceService.getById(pdInvoice.getId());
        if (pdInvoiceEntity == null) {
            return Result.error("未找到对应数据");
        }
        pdInvoiceService.updateMain(pdInvoice, PdInvoice.getPdInvoiceDetailList());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "pd_invoice-通过id删除")
    @ApiOperation(value = "pd_invoice-通过id删除", notes = "pd_invoice-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        pdInvoiceService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "pd_invoice-批量删除")
    @ApiOperation(value = "pd_invoice-批量删除", notes = "pd_invoice-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.pdInvoiceService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "pd_invoice-通过id查询")
    @ApiOperation(value = "pd_invoice-通过id查询", notes = "pd_invoice-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PdInvoice pdInvoice = pdInvoiceService.getById(id);
        if (pdInvoice == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(pdInvoice);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "pd_invoice_detail集合-通过id查询")
    @ApiOperation(value = "pd_invoice_detail集合-通过id查询", notes = "pd_invoice_detail-通过id查询")
    @GetMapping(value = "/queryPdInvoiceDetailByMainId")
    public Result<?> queryPdInvoiceDetailListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<PdInvoiceDetail> pdInvoiceDetailList = pdInvoiceDetailService.selectByMainId(id);
        return Result.ok(pdInvoiceDetailList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param pdInvoice
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdInvoice pdInvoice) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<PdInvoice> queryWrapper = QueryGenerator.initQueryWrapper(pdInvoice, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<PdInvoice> queryList = pdInvoiceService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<PdInvoice> pdInvoiceList = new ArrayList<PdInvoice>();
        if (oConvertUtils.isEmpty(selections)) {
            pdInvoiceList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            pdInvoiceList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<PdInvoice> pageList = new ArrayList<PdInvoice>();
        for (PdInvoice main : pdInvoiceList) {
            PdInvoice vo = new PdInvoice();
            BeanUtils.copyProperties(main, vo);
            List<PdInvoiceDetail> pdInvoiceDetailList = pdInvoiceDetailService.selectByMainId(main.getId());
            vo.setPdInvoiceDetailList(pdInvoiceDetailList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "pd_invoice列表");
        mv.addObject(NormalExcelConstants.CLASS, PdInvoice.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("pd_invoice数据", "导出人:" + sysUser.getRealname(), "pd_invoice"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
//        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
//            MultipartFile file = entity.getValue();// 获取上传文件对象
//            ImportParams params = new ImportParams();
//            params.setTitleRows(2);
//            params.setHeadRows(1);
//            params.setNeedSave(true);
//            try {
//                List<PdInvoice> list = ExcelImportUtil.importExcel(file.getInputStream(), PdInvoice.class, params);
//                for (PdInvoice page : list) {
//                    PdInvoice po = new PdInvoice();
//                    BeanUtils.copyProperties(page, po);
//                    pdInvoiceService.saveMain(po, page.getPdInvoiceDetailList());
//                }
//                return Result.ok("文件导入成功！数据行数:" + list.size());
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//                return Result.error("文件导入失败:" + e.getMessage());
//            } finally {
//                try {
//                    file.getInputStream().close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return Result.ok("文件导入失败！");
    }

}
