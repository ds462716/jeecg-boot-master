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
import org.jeecg.common.util.DateUtils;
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
import org.jeecg.modules.pd.entity.PdPackageRecordDetail;
import org.jeecg.modules.pd.entity.PdPackageRecord;
import org.jeecg.modules.pd.service.IPdPackageRecordService;
import org.jeecg.modules.pd.service.IPdPackageRecordDetailService;
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
 * @Description: pd_package_record
 * @Author: jiangxz
 * @Date: 2020-04-22
 * @Version: V1.0
 */
@Api(tags = "pd_package_record")
@RestController
@RequestMapping("/pd/pdPackageRecord")
@Slf4j
public class PdPackageRecordController {
    @Autowired
    private IPdPackageRecordService pdPackageRecordService;
    @Autowired
    private IPdPackageRecordDetailService pdPackageRecordDetailService;

    /**
     * 初始化Modal页面
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/initModal")
    public Result<?> initModal(@RequestParam(name = "id") String id, HttpServletRequest req) {
        PdPackageRecord pdPackageRecord = new PdPackageRecord();

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        pdPackageRecord.setCreateTime(DateUtils.getDate());
        pdPackageRecord.setCreateBy(sysUser.getRealname());
        pdPackageRecord.setPackageBarCode(DateUtils.date2Str(DateUtils.getDate(),DateUtils.yyMMdd.get()));;

        return Result.ok(pdPackageRecord);
    }

    /**
     * 分页列表查询
     *
     * @param pdPackageRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "pd_package_record-分页列表查询")
    @ApiOperation(value = "pd_package_record-分页列表查询", notes = "pd_package_record-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PdPackageRecord pdPackageRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdPackageRecord> page = new Page<PdPackageRecord>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdPackageRecord.setDepartParentId(sysUser.getDepartParentId());
        IPage<PdPackageRecord> pageList = pdPackageRecordService.queryList(page, pdPackageRecord);
        return Result.ok(pageList);
    }

    /**
     * 定数包选择器用
     * @param pdPackageRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/pdChoosePackageRecordList")
    public Result<?> pdChoosePackageRecordList(PdPackageRecord pdPackageRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdPackageRecord> page = new Page<PdPackageRecord>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdPackageRecord.setDepartParentId(sysUser.getDepartParentId());
        pdPackageRecord.setStatus(PdConstant.PACKAGE_RECORD_STATUS_1);
        IPage<PdPackageRecord> pageList = pdPackageRecordService.queryList(page, pdPackageRecord);
        return Result.ok(pageList);
    }

    /**
     * 定数包打包记录扫码
     * @param Barcode1
     * @param req
     * @return
     */
    @PostMapping(value = "packageRecordScanCode")
    public Result<Map> packageRecordScanCode(String Barcode1, HttpServletRequest req) {
        Result<Map> result = new Result<Map>();
        try{
            result = pdPackageRecordService.packageRecordScanCode(Barcode1, result);
            result.setCode(200);
        }catch(Exception e){
            log.error(e.getMessage(), e);
            result.setCode(500);
            result.setMessage("条码格式不正确");
        }
        return result;
    }

    /**
     * 根据ids查询列表
     * @param ids
     * @return
     */
    @GetMapping(value = "/queryPackageRecordListByIds")
    public Result<?> queryPackageRecordListByIds(@RequestParam(name = "ids", required = true) String ids) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        PdPackageRecord pdPackageRecord = new PdPackageRecord();
        pdPackageRecord.setDepartParentId(sysUser.getDepartParentId());
        pdPackageRecord.setIdList(Arrays.asList(ids.split(",")));
        List<PdPackageRecord> list = pdPackageRecordService.queryList(pdPackageRecord);
        for (PdPackageRecord record : list) {
            List<PdPackageRecordDetail> detailList = pdPackageRecordDetailService.selectByMainId(record.getId());
            record.setPdPackageRecordDetailList(detailList);
        }
        return Result.ok(list);
    }

    /**
     * 添加
     *
     * @param pdPackageRecord
     * @return
     */
    @AutoLog(value = "pd_package_record-添加")
    @ApiOperation(value = "pd_package_record-添加", notes = "pd_package_record-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PdPackageRecord pdPackageRecord) {

        Map<String, String> result = pdPackageRecordService.saveMain(pdPackageRecord, pdPackageRecord.getPdPackageRecordDetailList());

        if (PdConstant.SUCCESS_200.equals(result.get("code"))) {
            return Result.ok(result.get("message"));
        } else {
            return Result.error(result.get("message"));
        }
    }

    /**
     * 编辑
     *
     * @param pdPackageRecord
     * @return
     */
    @AutoLog(value = "pd_package_record-编辑")
    @ApiOperation(value = "pd_package_record-编辑", notes = "pd_package_record-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PdPackageRecord pdPackageRecord) {
        PdPackageRecord pdPackageRecordEntity = pdPackageRecordService.getById(pdPackageRecord.getId());
        if (pdPackageRecordEntity == null) {
            return Result.error("未找到对应数据");
        }
        pdPackageRecordService.updateMain(pdPackageRecord, pdPackageRecord.getPdPackageRecordDetailList());
        return Result.ok("编辑成功!");
    }

    /**
     * 定数包拆包
     *
     * @param id
     * @return
     */
    @AutoLog(value = "pd_package_record-通过id删除")
    @ApiOperation(value = "pd_package_record-通过id删除", notes = "pd_package_record-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        pdPackageRecordService.delMain(id);
        return Result.ok("拆包成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "pd_package_record-批量删除")
    @ApiOperation(value = "pd_package_record-批量删除", notes = "pd_package_record-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
//        this.pdPackageRecordService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "pd_package_record-通过id查询")
    @ApiOperation(value = "pd_package_record-通过id查询", notes = "pd_package_record-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PdPackageRecord pdPackageRecord = pdPackageRecordService.getById(id);
        if (pdPackageRecord == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(pdPackageRecord);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "pd_package_record_detail集合-通过id查询")
    @ApiOperation(value = "pd_package_record_detail集合-通过id查询", notes = "pd_package_record_detail-通过id查询")
    @GetMapping(value = "/queryPdPackageRecordDetailByMainId")
    public Result<?> queryPdPackageRecordDetailListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<PdPackageRecordDetail> pdPackageRecordDetailList = pdPackageRecordDetailService.selectByMainId(id);
        return Result.ok(pdPackageRecordDetailList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param pdPackageRecord
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdPackageRecord pdPackageRecord) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<PdPackageRecord> queryWrapper = QueryGenerator.initQueryWrapper(pdPackageRecord, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<PdPackageRecord> queryList = pdPackageRecordService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<PdPackageRecord> pdPackageRecordList = new ArrayList<PdPackageRecord>();
        if (oConvertUtils.isEmpty(selections)) {
            pdPackageRecordList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            pdPackageRecordList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<PdPackageRecord> pageList = new ArrayList<PdPackageRecord>();
        for (PdPackageRecord main : pdPackageRecordList) {
            PdPackageRecord vo = new PdPackageRecord();
            BeanUtils.copyProperties(main, vo);
            List<PdPackageRecordDetail> pdPackageRecordDetailList = pdPackageRecordDetailService.selectByMainId(main.getId());
            vo.setPdPackageRecordDetailList(pdPackageRecordDetailList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "pd_package_record列表");
        mv.addObject(NormalExcelConstants.CLASS, PdPackageRecord.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("pd_package_record数据", "导出人:" + sysUser.getRealname(), "pd_package_record"));
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<PdPackageRecord> list = ExcelImportUtil.importExcel(file.getInputStream(), PdPackageRecord.class, params);
                for (PdPackageRecord page : list) {
                    PdPackageRecord po = new PdPackageRecord();
                    BeanUtils.copyProperties(page, po);
                    pdPackageRecordService.saveMain(po, page.getPdPackageRecordDetailList());
                }
                return Result.ok("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

}
