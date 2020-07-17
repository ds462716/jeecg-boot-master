package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdDistributor;
import org.jeecg.modules.pd.service.IPdDistributorService;
import org.jeecg.modules.pd.util.FileUploadUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 配送商
 * @Author: jiangxz
 * @Date:   2020年7月17日 11:34:42
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdDistributor")
@Slf4j
public class PdDistributorController extends JeecgController<PdDistributor, IPdDistributorService> {
   @Autowired
   private IPdDistributorService pdDistributorService;

   /**
    * 分页列表查询
    *
    * @param pdDistributor
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   public Result<?> queryPageList(PdDistributor pdDistributor,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       Page<PdDistributor> page = new Page<>(pageNo,pageSize);
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       pdDistributor.setDepartParentId(sysUser.getDepartParentId());
       IPage<PdDistributor> pageList =pdDistributorService.selectList(page,pdDistributor);
       return Result.ok(pageList);
   }

    /**
     * 不分页列表查询
     *
     * @return
     */
    @GetMapping(value = "/getDistributorList")
    public Result<List<PdDistributor>> getDistributorList(PdDistributor pdDistributor) {
        long start = System.currentTimeMillis();
        Result<List<PdDistributor>> result = new Result<>();
        try {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            pdDistributor.setDepartParentId(sysUser.getDepartParentId());
            pdDistributor.setStatus(PdConstant.DISABLE_ENABLE_STATUS_0);//只查启用
            List<PdDistributor> list = pdDistributorService.selectList(pdDistributor);
            result.setResult(list);
            result.setSuccess(true);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        log.info("======获取产品数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
        return result;
    }

   /**
    *   添加
    *
    * @param PdDistributor
    * @return
    */
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody PdDistributor pdDistributor) {
       pdDistributorService.save(pdDistributor);
       return Result.ok("添加成功！");
   }

    /**
     * 包含文件上传功能
     * @param pdDistributor
     * @return
     */
    @PostMapping(value = "/save")
    public Result<?> save(PdDistributor pdDistributor,@RequestParam MultipartFile[] licenceSiteUp0,
                          @RequestParam MultipartFile[] licenceSiteUp1,
                          @RequestParam MultipartFile[] licenceSiteUp2,@RequestParam MultipartFile[] licenceSiteUp3,
                          @RequestParam MultipartFile[] licenceSiteUp4,@RequestParam MultipartFile[] licenceSiteUp5,
                          @RequestParam MultipartFile[] licenceSiteUp6,@RequestParam MultipartFile[] licenceSiteUp7,
                          @RequestParam MultipartFile[] licenceSiteUp8,@RequestParam MultipartFile[] licenceSiteUp9,
                          @RequestParam MultipartFile[] licenceSiteUp10,@RequestParam MultipartFile[] licenceSiteUp11
                          ) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdDistributor.setDepartParentId(sysUser.getDepartParentId());
        List<PdDistributor> obj = pdDistributorService.verify(pdDistributor);
        if (obj != null && obj.size()>0) {
            return Result.error("配送商已存在");
        }
        //存入图片
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp0)){
            String filePath = FileUploadUtil.upload(licenceSiteUp0);
            pdDistributor.setLicenceSite0(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp1)){
            String filePath = FileUploadUtil.upload(licenceSiteUp1);
            pdDistributor.setLicenceSite1(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp2)){
            String filePath = FileUploadUtil.upload(licenceSiteUp2);
            pdDistributor.setLicenceSite2(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp3)){
            String filePath = FileUploadUtil.upload(licenceSiteUp3);
            pdDistributor.setLicenceSite3(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp4)){
            String filePath = FileUploadUtil.upload(licenceSiteUp4);
            pdDistributor.setLicenceSite4(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp5)){
            String filePath = FileUploadUtil.upload(licenceSiteUp5);
            pdDistributor.setLicenceSite5(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp6)){
            String filePath = FileUploadUtil.upload(licenceSiteUp6);
            pdDistributor.setLicenceSite6(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp7)){
            String filePath = FileUploadUtil.upload(licenceSiteUp7);
            pdDistributor.setLicenceSite7(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp8)){
            String filePath = FileUploadUtil.upload(licenceSiteUp8);
            pdDistributor.setLicenceSite8(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp9)){
            String filePath = FileUploadUtil.upload(licenceSiteUp9);
            pdDistributor.setLicenceSite9(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp10)){
            String filePath = FileUploadUtil.upload(licenceSiteUp10);
            pdDistributor.setLicenceSite10(filePath);
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp11)){
            String filePath = FileUploadUtil.upload(licenceSiteUp11);
            pdDistributor.setLicenceSite11(filePath);
        }
        pdDistributor.setValidityFlag(PdConstant.PD_STATE_0);
        pdDistributorService.save(pdDistributor);
        return Result.ok("添加成功！");
    }


    /**
     * 包含文件上传功能
     * @param pdDistributor
     * @return
     */
    @PostMapping(value = "/update")
    public Result<?> update(PdDistributor pdDistributor,@RequestParam MultipartFile[] licenceSiteUp0,
                          @RequestParam MultipartFile[] licenceSiteUp1,
                          @RequestParam MultipartFile[] licenceSiteUp2,@RequestParam MultipartFile[] licenceSiteUp3,
                          @RequestParam MultipartFile[] licenceSiteUp4,@RequestParam MultipartFile[] licenceSiteUp5,
                          @RequestParam MultipartFile[] licenceSiteUp6,@RequestParam MultipartFile[] licenceSiteUp7,
                          @RequestParam MultipartFile[] licenceSiteUp8,@RequestParam MultipartFile[] licenceSiteUp9,
                          @RequestParam MultipartFile[] licenceSiteUp10,@RequestParam MultipartFile[] licenceSiteUp11
    ) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdDistributor.setDepartParentId(sysUser.getDepartParentId());
        List<PdDistributor> obj = pdDistributorService.verify(pdDistributor);
        if (obj != null && obj.size()>0) {
            return Result.error("配送商已存在");
        }
        //存入图片
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp0)){
            String filePath = FileUploadUtil.upload(licenceSiteUp0);
            if(pdDistributor.getLicenceSite0()!=null && !"".equals(pdDistributor.getLicenceSite0())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite0());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite0(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp1)){
            String filePath = FileUploadUtil.upload(licenceSiteUp1);
            if(pdDistributor.getLicenceSite1()!=null && !"".equals(pdDistributor.getLicenceSite1())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite1());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite1(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp2)){
            String filePath = FileUploadUtil.upload(licenceSiteUp2);
            if(pdDistributor.getLicenceSite2()!=null && !"".equals(pdDistributor.getLicenceSite2())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite2());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite2(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp3)){
            String filePath = FileUploadUtil.upload(licenceSiteUp3);
            if(pdDistributor.getLicenceSite3()!=null && !"".equals(pdDistributor.getLicenceSite3())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite3());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite3(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp4)){
            String filePath = FileUploadUtil.upload(licenceSiteUp4);
            if(pdDistributor.getLicenceSite4()!=null && !"".equals(pdDistributor.getLicenceSite4())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite4());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite4(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp5)){
            String filePath = FileUploadUtil.upload(licenceSiteUp5);
            if(pdDistributor.getLicenceSite5()!=null && !"".equals(pdDistributor.getLicenceSite5())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite5());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite5(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp6)){
            String filePath = FileUploadUtil.upload(licenceSiteUp6);
            if(pdDistributor.getLicenceSite6()!=null && !"".equals(pdDistributor.getLicenceSite6())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite6());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite6(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp7)){
            String filePath = FileUploadUtil.upload(licenceSiteUp7);
            if(pdDistributor.getLicenceSite7()!=null && !"".equals(pdDistributor.getLicenceSite7())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite7());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite7(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp8)){
            String filePath = FileUploadUtil.upload(licenceSiteUp8);
            if(pdDistributor.getLicenceSite8()!=null && !"".equals(pdDistributor.getLicenceSite8())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite8());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite8(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp9)){
            String filePath = FileUploadUtil.upload(licenceSiteUp9);
            if(pdDistributor.getLicenceSite9()!=null && !"".equals(pdDistributor.getLicenceSite9())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite9());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite9(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp10)){
            String filePath = FileUploadUtil.upload(licenceSiteUp10);
            if(pdDistributor.getLicenceSite10()!=null && !"".equals(pdDistributor.getLicenceSite10())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite10());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite10(filePath);
            }
        }
        if(!FileUploadUtil.isImgEmpty(licenceSiteUp11)){
            String filePath = FileUploadUtil.upload(licenceSiteUp11);
            if(pdDistributor.getLicenceSite11()!=null && !"".equals(pdDistributor.getLicenceSite11())){
                //先删除再更新
                FileUploadUtil.deleteFile(pdDistributor.getLicenceSite11());
            }
            if(!"".equals(filePath)){
                pdDistributor.setLicenceSite11(filePath);
            }
        }
        pdDistributorService.updateById(pdDistributor);
        return Result.ok("编辑成功！");
    }

   /**
    *  编辑
    *
    * @param pdDistributor
    * @return
    */
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody PdDistributor pdDistributor) {
       pdDistributorService.updateById(pdDistributor);
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
       Result<Object> resul = pdDistributorService.deleteV(id);
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
       Result<Object> resul = pdDistributorService.deleteBatchV(ids);
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
       PdDistributor pdDistributor = pdDistributorService.getById(id);
       if(pdDistributor==null) {
           return Result.error("未找到对应数据");
       }
       return Result.ok(pdDistributor);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param pdDistributor
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, PdDistributor pdDistributor) {
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       pdDistributor.setDepartParentId(sysUser.getDepartParentId());
       //Step.1 获取导出数据
       List<PdDistributor> pdProducts = pdDistributorService.selectList(pdDistributor);
       // Step.2 AutoPoi 导出Excel
       ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
       mv.addObject(NormalExcelConstants.FILE_NAME, "配送商列表");
       mv.addObject(NormalExcelConstants.CLASS, PdDistributor.class);
       mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("配送商列表数据", "导出人:" + sysUser.getRealname(), "产品数据"));
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
       Result<Object> resul = pdDistributorService.importExcel(fileMap);
       return resul;
   }

    /**
     * 批量停用和启用status 0启用1停用
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/batchDisable", method = RequestMethod.POST)
    public Result<PdDistributor> batchDisable(@RequestBody JSONObject jsonObject) {
        Result<PdDistributor> result = new Result<PdDistributor>();
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            List<PdDistributor> pdDistributors= new ArrayList<>();
            for (String id : arr) {
                if(oConvertUtils.isNotEmpty(id)) {
                    PdDistributor pdDistributor = new PdDistributor();
                    pdDistributor.setId(id);
                    pdDistributor.setStatus(status);
                    pdDistributors.add(pdDistributor);
                }
            }
            if(pdDistributors!=null && pdDistributors.size()>0){
                pdDistributorService.updateBatchById(pdDistributors);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败"+e.getMessage());
        }
        result.success("操作成功!");
        return result;

    }

}
