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
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.service.IPdProductStockService;
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
import org.jeecg.modules.pd.entity.PdStoreroomPatrolDetail;
import org.jeecg.modules.pd.entity.PdStoreroomPatrol;
import org.jeecg.modules.pd.service.IPdStoreroomPatrolService;
import org.jeecg.modules.pd.service.IPdStoreroomPatrolDetailService;
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
 * @Description: pd_storeroom_patrol
 * @Author: jiangxz
 * @Date: 2020-04-07
 * @Version: V1.0
 */
@Api(tags = "pd_storeroom_patrol")
@RestController
@RequestMapping("/pd/pdStoreroomPatrol")
@Slf4j
public class PdStoreroomPatrolController {
    @Autowired
    private IPdStoreroomPatrolService pdStoreroomPatrolService;
    @Autowired
    private IPdStoreroomPatrolDetailService pdStoreroomPatrolDetailService;
    @Autowired
    private IPdProductStockService pdProductStockService;


    /**
     * 初始化Modal页面
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/initModal")
    public Result<?> initModal(@RequestParam(name = "id") String id, HttpServletRequest req) {
//        PdStockRecord pdStockRecord = pdStockRecordService.initOutModal(id);
        PdStoreroomPatrol pdStoreroomPatrol = new PdStoreroomPatrol();
        if (oConvertUtils.isNotEmpty(id)) { // 查看页面

        }else{
            pdStoreroomPatrol.setPatrolNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_XC));
            pdStoreroomPatrol.setPatrolDate(DateUtils.getDate());
        }

        return Result.ok(pdStoreroomPatrol);
    }

    /**
     * 查询库存列表
     * @param pdStoreroomPatrol
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/stockList")
    public Result<?> queryStockList(PdStoreroomPatrol pdStoreroomPatrol,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdStoreroomPatrolDetail> page = new Page<PdStoreroomPatrolDetail>(pageNo, pageSize);
        List<PdStoreroomPatrolDetail> patrolList = new ArrayList<>();

        Page<PdProductStock> stockPage = new Page<PdProductStock>(pageNo, pageSize);
        PdProductStock productStock = new PdProductStock();
        productStock.setDepartId(pdStoreroomPatrol.getDepartId());
        productStock.setProductName(pdStoreroomPatrol.getProductName());
        productStock.setNumber(pdStoreroomPatrol.getNumber());
        productStock.setBatchNo(pdStoreroomPatrol.getBatchNo());
        productStock.setQueryDateStart(pdStoreroomPatrol.getQueryDateStart());
        productStock.setQueryDateEnd(pdStoreroomPatrol.getQueryDateEnd());

        Page<PdProductStock> stockPageList = pdProductStockService.selectList(stockPage, productStock);
        List<PdProductStock> stockList = stockPageList.getRecords();

        for (PdProductStock item : stockList) {
            PdStoreroomPatrolDetail patrol = new PdStoreroomPatrolDetail();
            BeanUtils.copyProperties(item, patrol);

            patrol.setIsExp("");
            if(patrol.getExpDate() != null){
                if(patrol.getExpDate().before(DateUtils.getDate())){
                    patrol.setIsExp("是");
                }else{
                    patrol.setIsExp("否");
                }
            }
            patrolList.add(patrol);
        }
        page.setRecords(patrolList);

        return Result.ok(page);
    }

    /**
     * 分页列表查询
     *
     * @param pdStoreroomPatrol
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "pd_storeroom_patrol-分页列表查询")
    @ApiOperation(value = "pd_storeroom_patrol-分页列表查询", notes = "pd_storeroom_patrol-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PdStoreroomPatrol pdStoreroomPatrol,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdStoreroomPatrol> page = new Page<PdStoreroomPatrol>(pageNo, pageSize);
        IPage<PdStoreroomPatrol> pageList = pdStoreroomPatrolService.selectList(page,pdStoreroomPatrol);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param PdStoreroomPatrol
     * @return
     */
    @AutoLog(value = "pd_storeroom_patrol-添加")
    @ApiOperation(value = "pd_storeroom_patrol-添加", notes = "pd_storeroom_patrol-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PdStoreroomPatrol PdStoreroomPatrol) {
        PdStoreroomPatrol pdStoreroomPatrol = new PdStoreroomPatrol();
        BeanUtils.copyProperties(PdStoreroomPatrol, pdStoreroomPatrol);
        pdStoreroomPatrolService.saveMain(pdStoreroomPatrol, PdStoreroomPatrol.getPdStoreroomPatrolDetailList());
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param PdStoreroomPatrol
     * @return
     */
    @AutoLog(value = "pd_storeroom_patrol-编辑")
    @ApiOperation(value = "pd_storeroom_patrol-编辑", notes = "pd_storeroom_patrol-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PdStoreroomPatrol PdStoreroomPatrol) {
        PdStoreroomPatrol pdStoreroomPatrol = new PdStoreroomPatrol();
        BeanUtils.copyProperties(PdStoreroomPatrol, pdStoreroomPatrol);
        PdStoreroomPatrol pdStoreroomPatrolEntity = pdStoreroomPatrolService.getById(pdStoreroomPatrol.getId());
        if (pdStoreroomPatrolEntity == null) {
            return Result.error("未找到对应数据");
        }
        pdStoreroomPatrolService.updateMain(pdStoreroomPatrol, PdStoreroomPatrol.getPdStoreroomPatrolDetailList());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "pd_storeroom_patrol-通过id删除")
    @ApiOperation(value = "pd_storeroom_patrol-通过id删除", notes = "pd_storeroom_patrol-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        pdStoreroomPatrolService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "pd_storeroom_patrol-批量删除")
    @ApiOperation(value = "pd_storeroom_patrol-批量删除", notes = "pd_storeroom_patrol-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.pdStoreroomPatrolService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "pd_storeroom_patrol-通过id查询")
    @ApiOperation(value = "pd_storeroom_patrol-通过id查询", notes = "pd_storeroom_patrol-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PdStoreroomPatrol pdStoreroomPatrol = pdStoreroomPatrolService.getById(id);
        if (pdStoreroomPatrol == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(pdStoreroomPatrol);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "pd_storeroom_patrol_detail集合-通过id查询")
    @ApiOperation(value = "pd_storeroom_patrol_detail集合-通过id查询", notes = "pd_storeroom_patrol_detail-通过id查询")
    @GetMapping(value = "/queryPdStoreroomPatrolDetailByMainId")
    public Result<?> queryPdStoreroomPatrolDetailListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<PdStoreroomPatrolDetail> pdStoreroomPatrolDetailList = pdStoreroomPatrolDetailService.selectByMainId(id);
        return Result.ok(pdStoreroomPatrolDetailList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param pdStoreroomPatrol
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdStoreroomPatrol pdStoreroomPatrol) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<PdStoreroomPatrol> queryWrapper = QueryGenerator.initQueryWrapper(pdStoreroomPatrol, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<PdStoreroomPatrol> queryList = pdStoreroomPatrolService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<PdStoreroomPatrol> pdStoreroomPatrolList = new ArrayList<PdStoreroomPatrol>();
        if (oConvertUtils.isEmpty(selections)) {
            pdStoreroomPatrolList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            pdStoreroomPatrolList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<PdStoreroomPatrol> pageList = new ArrayList<PdStoreroomPatrol>();
        for (PdStoreroomPatrol main : pdStoreroomPatrolList) {
            PdStoreroomPatrol vo = new PdStoreroomPatrol();
            BeanUtils.copyProperties(main, vo);
            List<PdStoreroomPatrolDetail> pdStoreroomPatrolDetailList = pdStoreroomPatrolDetailService.selectByMainId(main.getId());
            vo.setPdStoreroomPatrolDetailList(pdStoreroomPatrolDetailList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "pd_storeroom_patrol列表");
        mv.addObject(NormalExcelConstants.CLASS, PdStoreroomPatrol.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("pd_storeroom_patrol数据", "导出人:" + sysUser.getRealname(), "pd_storeroom_patrol"));
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
                List<PdStoreroomPatrol> list = ExcelImportUtil.importExcel(file.getInputStream(), PdStoreroomPatrol.class, params);
                for (PdStoreroomPatrol page : list) {
                    PdStoreroomPatrol po = new PdStoreroomPatrol();
                    BeanUtils.copyProperties(page, po);
                    pdStoreroomPatrolService.saveMain(po, page.getPdStoreroomPatrolDetailList());
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
