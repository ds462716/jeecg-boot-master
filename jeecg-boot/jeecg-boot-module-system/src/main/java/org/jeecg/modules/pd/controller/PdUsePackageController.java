package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Description: 检验项目
* @Author: zxh
* @Date:   2020年4月21日09:18:57
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/pdUsePackage")
@Slf4j
public class PdUsePackageController {
   @Autowired
   private IPdUsePackageService pdUsePackageService;
   @Autowired
   private IPdUsePackageDetailService pdUsePackageDetailService;
   @Autowired
   private IPdProductStockTotalService pdProductStockTotalService;

    /**
     * 初始化Modal页面
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/initModal")
    public Result<?> initModal(@RequestParam(name = "id") String id, HttpServletRequest req) {
        PdUsePackage pdUsePackage = new PdUsePackage();
        pdUsePackage.setCode(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_DSB));
        return Result.ok(pdUsePackage);
    }

    /**
    * 分页列表查询
    *
    * @param pdUsePackage
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   public Result<?> queryPageList(PdUsePackage pdUsePackage,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       Page<PdUsePackage> page = new Page<>(pageNo, pageSize);
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       pdUsePackage.setDepartParentId(sysUser.getDepartParentId());
       IPage<PdUsePackage> pageList = pdUsePackageService.queryList(page, pdUsePackage);
       return Result.ok(pageList);
   }

   /**
    *   添加
    *
    * @param pdUsePackage
    * @return
    */
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody PdUsePackage pdUsePackage) {
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       pdUsePackage.setDepartParentId(sysUser.getDepartParentId());
       List<PdUsePackage> obj = pdUsePackageService.verify(pdUsePackage);
       if (obj != null && obj.size()>0) {
           return Result.error("检验包已存在");
       }
       pdUsePackageService.saveMain(pdUsePackage, pdUsePackage.getPdUsePackageDetailList());
       return Result.ok("添加成功！");
   }

   /**
    *  编辑
    *
    * @param pdUsePackage
    * @return
    */
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody PdUsePackage pdUsePackage) {
       PdUsePackage pdUsePackageEntity = pdUsePackageService.getById(pdUsePackage.getId());
       if(pdUsePackageEntity==null) {
           return Result.error("未找到对应数据");
       }
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       pdUsePackage.setDepartParentId(sysUser.getDepartParentId());
       List<PdUsePackage> obj = pdUsePackageService.verify(pdUsePackage);
       if (obj != null && obj.size()>0) {
           return Result.error("检验包已存在");
       }
       pdUsePackageService.updateMain(pdUsePackage, pdUsePackage.getPdUsePackageDetailList());
       return Result.ok("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       pdUsePackageService.delMain(id);
       return Result.ok("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.pdUsePackageService.delBatchMain(Arrays.asList(ids.split(",")));
       return Result.ok("批量删除成功！");
   }

   /**
    * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       PdUsePackage pdUsePackage = pdUsePackageService.getById(id);
       if(pdUsePackage==null) {
           return Result.error("未找到对应数据");
       }
       return Result.ok(pdUsePackage);

   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @GetMapping(value = "/queryPdUsePackageDetailByMainId")
   public Result<?> queryPdUsePackageDetailByMainId(@RequestParam(name="id",required=true) String id) {
       List<PdUsePackageDetail> pdUsePackageDetailList = pdUsePackageDetailService.selectByMainId(id);
       return Result.ok(pdUsePackageDetailList);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param pdUsePackage
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, PdUsePackage pdUsePackage) {
     // Step.1 组装查询条件查询数据
     QueryWrapper<PdUsePackage> queryWrapper = QueryGenerator.initQueryWrapper(pdUsePackage, request.getParameterMap());
     LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //Step.2 获取导出数据
     List<PdUsePackage> queryList = pdUsePackageService.list(queryWrapper);
     // 过滤选中数据
     String selections = request.getParameter("selections");
     List<PdUsePackage> pdUsePackageList = new ArrayList<PdUsePackage>();
     if(oConvertUtils.isEmpty(selections)) {
         pdUsePackageList = queryList;
     }else {
         List<String> selectionList = Arrays.asList(selections.split(","));
         pdUsePackageList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
     }

     // Step.3 组装pageList
     List<PdUsePackage> pageList = new ArrayList<PdUsePackage>();
     for (PdUsePackage main : pdUsePackageList) {
         List<PdUsePackageDetail> pdUsePackageDetailList = pdUsePackageDetailService.selectByMainId(main.getId());
         main.setPdUsePackageDetailList(pdUsePackageDetailList);
         pageList.add(main);
     }

     // Step.4 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     mv.addObject(NormalExcelConstants.FILE_NAME, "检验项目列表");
     mv.addObject(NormalExcelConstants.CLASS, PdUsePackage.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("检验项目数据", "导出人:"+sysUser.getRealname(), "检验项目"));
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
             List<PdUsePackage> list = ExcelImportUtil.importExcel(file.getInputStream(), PdUsePackage.class, params);
             for (PdUsePackage page : list) {
                 pdUsePackageService.saveMain(page, page.getPdUsePackageDetailList());
             }
             return Result.ok("文件导入成功！数据行数:" + list.size());
         } catch (Exception e) {
             log.error(e.getMessage(),e);
             return Result.error("文件导入失败:"+e.getMessage());
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


    /**
     * 检验项目选择器用
     *
     * @param pdUsePackage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/queryPackgeList")
    public Result<?> queryPackgeList(PdUsePackage pdUsePackage,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<PdUsePackage> page = new Page<PdUsePackage>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdUsePackage.setDepartParentId(sysUser.getDepartParentId());
        IPage<PdUsePackage> pageList = pdUsePackageService.queryList(page, pdUsePackage);
        return Result.ok(pageList);
    }
    /**
     *  查询检验项目明细   申领单及调拨单用
     * @param pdUsePackageDetail
     * @return
     */
    @GetMapping(value = "queryPdUsePackageDetailList")
    public Result<?> queryPdUsePackageList(PdUsePackageDetail pdUsePackageDetail,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<PdUsePackageDetail> detailList = pdUsePackageDetailService.queryPdUsePackageList(pdUsePackageDetail);
           if(CollectionUtils.isNotEmpty(detailList)){
                 for(PdUsePackageDetail detail:detailList){
                     //查询本科室下库存数量
                     PdProductStockTotalPage total = new PdProductStockTotalPage();
                     total.setDepartId(sysUser.getCurrentDepartId());
                     total.setProductId(detail.getProductId());
                     List<PdProductStockTotalPage> list = pdProductStockTotalService.findListForQuery(total);
                     if(CollectionUtils.isNotEmpty(list)){
                         detail.setStockNum(list.get(0).getStockNum());
                     }else{
                         detail.setStockNum(0.00);
                     }
                 }
             }
        return Result.ok(detailList);
    }
}
