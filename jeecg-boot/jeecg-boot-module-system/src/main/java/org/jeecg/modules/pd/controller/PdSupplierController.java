package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProduct;
import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.pd.model.PdSupplierTreeModel;
import org.jeecg.modules.pd.service.IPdSupplierService;
import org.jeecg.modules.pd.util.FileUploadUtil;
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

/**
* @Description: 供应商
* @Author: zxh
* @Date:   2020-01-09
* @Version: V1.0
*/
@RestController
@RequestMapping("/pd/pdSupplier")
@Slf4j
public class PdSupplierController extends JeecgController<PdSupplier, IPdSupplierService> {
   @Autowired
   private IPdSupplierService pdSupplierService;

   /**
    * 分页列表查询
    *
    * @param pdSupplier
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   public Result<?> queryPageList(PdSupplier pdSupplier,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       Page<PdSupplier> page = new Page<>(pageNo,pageSize);
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       pdSupplier.setDepartParentId(sysUser.getDepartParentId());
       IPage<PdSupplier> pageList =pdSupplierService.selectList(page,pdSupplier);
       return Result.ok(pageList);
   }

    /**
     * 不分页列表查询
     *
     * @return
     */
    @GetMapping(value = "/getSupplierList")
    public Result<List<PdSupplier>> getSupplierList(PdSupplier pdSupplier) {
        long start = System.currentTimeMillis();
        Result<List<PdSupplier>> result = new Result<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            pdSupplier.setDepartParentId(sysUser.getDepartParentId());
            pdSupplier.setStatus(PdConstant.DISABLE_ENABLE_STATUS_0);//只查启用
            List<PdSupplier> list = pdSupplierService.selectList(pdSupplier);
            result.setResult(list);
            result.setSuccess(true);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        log.info("======获取产品数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        return result;
    }

    /**
     * 供应商大屏跳转
     * @param modelAndView
     * @return
     */
    @RequestMapping("/跳转")
    public ModelAndView index1(ModelAndView modelAndView) {
        modelAndView.setViewName("/bigscreen/template1/index");
        return modelAndView;
    }

    /**
     * 查询供应商以树节点的形式展示
     * @return
     */
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<List<PdSupplierTreeModel>> queryTreeList() {
        Result<List<PdSupplierTreeModel>> result = new Result<>();
        try {
            PdSupplier pdSupplier = new PdSupplier();
            List<PdSupplierTreeModel> list = pdSupplierService.queryTreeList(pdSupplier);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }

   /**
    *   添加
    *
    * @param PdSupplier
    * @return
    */
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody PdSupplier pdSupplier) {
       pdSupplierService.save(pdSupplier);
       return Result.ok("添加成功！");
   }

    /**
     * 包含文件上传功能
     * @param pdSupplier
     * @return
     */
    @PostMapping(value = "/save")
    public Result<?> save(PdSupplier pdSupplier,@RequestParam MultipartFile[] licenceSiteUp0,
                          @RequestParam MultipartFile[] licenceSiteUp1,
                          @RequestParam MultipartFile[] licenceSiteUp2,@RequestParam MultipartFile[] licenceSiteUp3,
                          @RequestParam MultipartFile[] licenceSiteUp4,@RequestParam MultipartFile[] licenceSiteUp5,
                          @RequestParam MultipartFile[] licenceSiteUp6,@RequestParam MultipartFile[] licenceSiteUp7,
                          @RequestParam MultipartFile[] licenceSiteUp8,@RequestParam MultipartFile[] licenceSiteUp9,
                          @RequestParam MultipartFile[] licenceSiteUp10,@RequestParam MultipartFile[] licenceSiteUp11
                          ) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdSupplier.setDepartParentId(sysUser.getDepartParentId());
        List<PdSupplier> obj = pdSupplierService.verify(pdSupplier);
        if (obj != null && obj.size()>0) {
            return Result.error("供应商已存在");
        }
        //存入图片
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp0)){
            String filePath = FileUploadUtil.upload(licenceSiteUp0);
            pdSupplier.setLicenceSite0(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp1)){
            String filePath = FileUploadUtil.upload(licenceSiteUp1);
            pdSupplier.setLicenceSite1(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp2)){
            String filePath = FileUploadUtil.upload(licenceSiteUp2);
            pdSupplier.setLicenceSite2(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp3)){
            String filePath = FileUploadUtil.upload(licenceSiteUp3);
            pdSupplier.setLicenceSite3(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp4)){
            String filePath = FileUploadUtil.upload(licenceSiteUp4);
            pdSupplier.setLicenceSite4(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp5)){
            String filePath = FileUploadUtil.upload(licenceSiteUp5);
            pdSupplier.setLicenceSite5(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp6)){
            String filePath = FileUploadUtil.upload(licenceSiteUp6);
            pdSupplier.setLicenceSite6(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp7)){
            String filePath = FileUploadUtil.upload(licenceSiteUp7);
            pdSupplier.setLicenceSite7(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp8)){
            String filePath = FileUploadUtil.upload(licenceSiteUp8);
            pdSupplier.setLicenceSite8(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp9)){
            String filePath = FileUploadUtil.upload(licenceSiteUp9);
            pdSupplier.setLicenceSite9(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp10)){
            String filePath = FileUploadUtil.upload(licenceSiteUp10);
            pdSupplier.setLicenceSite10(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp11)){
            String filePath = FileUploadUtil.upload(licenceSiteUp11);
            pdSupplier.setLicenceSite11(filePath);
        }
        pdSupplier.setValidityFlag(PdConstant.PD_STATE_0);
        pdSupplierService.save(pdSupplier);
        return Result.ok("添加成功！");
    }


    /**
     * 包含文件上传功能
     * @param pdSupplier
     * @return
     */
    @PostMapping(value = "/update")
    public Result<?> update(PdSupplier pdSupplier,@RequestParam MultipartFile[] licenceSiteUp0,
                          @RequestParam MultipartFile[] licenceSiteUp1,
                          @RequestParam MultipartFile[] licenceSiteUp2,@RequestParam MultipartFile[] licenceSiteUp3,
                          @RequestParam MultipartFile[] licenceSiteUp4,@RequestParam MultipartFile[] licenceSiteUp5,
                          @RequestParam MultipartFile[] licenceSiteUp6,@RequestParam MultipartFile[] licenceSiteUp7,
                          @RequestParam MultipartFile[] licenceSiteUp8,@RequestParam MultipartFile[] licenceSiteUp9,
                          @RequestParam MultipartFile[] licenceSiteUp10,@RequestParam MultipartFile[] licenceSiteUp11
    ) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdSupplier.setDepartParentId(sysUser.getDepartParentId());
        List<PdSupplier> obj = pdSupplierService.verify(pdSupplier);
        if (obj != null && obj.size()>0) {
            return Result.error("供应商已存在");
        }
        //存入图片
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp0)){
            String filePath = FileUploadUtil.upload(licenceSiteUp0);
            if(pdSupplier.getLicenceSite0()!=null && !"".equals(pdSupplier.getLicenceSite0())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite0());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite0(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp1)){
            String filePath = FileUploadUtil.upload(licenceSiteUp1);
            if(pdSupplier.getLicenceSite1()!=null && !"".equals(pdSupplier.getLicenceSite1())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite1());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite1(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp2)){
            String filePath = FileUploadUtil.upload(licenceSiteUp2);
            if(pdSupplier.getLicenceSite2()!=null && !"".equals(pdSupplier.getLicenceSite2())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite2());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite2(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp3)){
            String filePath = FileUploadUtil.upload(licenceSiteUp3);
            if(pdSupplier.getLicenceSite3()!=null && !"".equals(pdSupplier.getLicenceSite3())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite3());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite3(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp4)){
            String filePath = FileUploadUtil.upload(licenceSiteUp4);
            if(pdSupplier.getLicenceSite4()!=null && !"".equals(pdSupplier.getLicenceSite4())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite4());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite4(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp5)){
            String filePath = FileUploadUtil.upload(licenceSiteUp5);
            if(pdSupplier.getLicenceSite5()!=null && !"".equals(pdSupplier.getLicenceSite5())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite5());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite5(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp6)){
            String filePath = FileUploadUtil.upload(licenceSiteUp6);
            if(pdSupplier.getLicenceSite6()!=null && !"".equals(pdSupplier.getLicenceSite6())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite6());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite6(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp7)){
            String filePath = FileUploadUtil.upload(licenceSiteUp7);
            if(pdSupplier.getLicenceSite7()!=null && !"".equals(pdSupplier.getLicenceSite7())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite7());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite7(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp8)){
            String filePath = FileUploadUtil.upload(licenceSiteUp8);
            if(pdSupplier.getLicenceSite8()!=null && !"".equals(pdSupplier.getLicenceSite8())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite8());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite8(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp9)){
            String filePath = FileUploadUtil.upload(licenceSiteUp9);
            if(pdSupplier.getLicenceSite9()!=null && !"".equals(pdSupplier.getLicenceSite9())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite9());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite9(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp10)){
            String filePath = FileUploadUtil.upload(licenceSiteUp10);
            if(pdSupplier.getLicenceSite10()!=null && !"".equals(pdSupplier.getLicenceSite10())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite10());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite10(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp11)){
            String filePath = FileUploadUtil.upload(licenceSiteUp11);
            if(pdSupplier.getLicenceSite11()!=null && !"".equals(pdSupplier.getLicenceSite11())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdSupplier.getLicenceSite11());
            }
            if(!"".equals(filePath)){
                pdSupplier.setLicenceSite11(filePath);
            }
        }
        pdSupplierService.updateById(pdSupplier);
        return Result.ok("编辑成功！");
    }

   /**
    *  编辑
    *
    * @param pdSupplier
    * @return
    */
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody PdSupplier pdSupplier) {
       pdSupplierService.updateById(pdSupplier);
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
       Result<Object> resul = pdSupplierService.deleteV(id);
       return resul;
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       Result<Object> resul = pdSupplierService.deleteBatchV(ids);
       return resul;
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       PdSupplier pdSupplier = pdSupplierService.getById(id);
       if(pdSupplier==null) {
           return Result.error("未找到对应数据");
       }
       return Result.ok(pdSupplier);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param pdSupplier
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, PdSupplier pdSupplier) {
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       pdSupplier.setDepartParentId(sysUser.getDepartParentId());
       //Step.1 获取导出数据
       List<PdSupplier> pdProducts = pdSupplierService.selectList(pdSupplier);
       // Step.2 AutoPoi 导出Excel
       ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
       mv.addObject(NormalExcelConstants.FILE_NAME, "供应商列表");
       mv.addObject(NormalExcelConstants.CLASS, PdSupplier.class);
       mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商列表数据", "导出人:" + sysUser.getRealname(), "产品数据"));
       mv.addObject(NormalExcelConstants.DATA_LIST, pdProducts);
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
       Result<Object> resul = pdSupplierService.importExcel(fileMap);
       return resul;
   }

    /**
     * 批量停用和启用status 0启用1停用
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/batchDisable", method = RequestMethod.POST)
    public Result<PdSupplier> batchDisable(@RequestBody JSONObject jsonObject) {
        Result<PdSupplier> result = new Result<PdSupplier>();
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            List<PdSupplier> pdSuppliers= new ArrayList<>();
            for (String id : arr) {
                if(oConvertUtils.isNotEmpty(id)) {
                    PdSupplier pdSupplier = new PdSupplier();
                    pdSupplier.setId(id);
                    pdSupplier.setStatus(status);
                    pdSuppliers.add(pdSupplier);
                }
            }
            if(pdSuppliers!=null && pdSuppliers.size()>0){
                pdSupplierService.updateBatchById(pdSuppliers);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败"+e.getMessage());
        }
        result.success("操作成功!");
        return result;

    }

}
