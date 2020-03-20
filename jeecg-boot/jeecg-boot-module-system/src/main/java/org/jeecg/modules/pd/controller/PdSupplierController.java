package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdSupplier;
import org.jeecg.modules.pd.service.IPdSupplierService;
import org.jeecg.modules.pd.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

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
       Result<Page<PdSupplier>> result = new Result<>();
       Page<PdSupplier> pageList = new Page<>(pageNo,pageSize);
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       pdSupplier.setDepartParentId(sysUser.getDepartParentId());
       pageList =pdSupplierService.selectList(pageList,pdSupplier);
       result.setSuccess(true);
       result.setResult(pageList);
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
        Result<Boolean> result = new Result<>();
        //如果此参数为false则程序发生异常
        result.setResult(true);
        result.setMessage("添加成功");
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdSupplier.setDepartParentId(sysUser.getDepartParentId());
        List<PdSupplier> obj = pdSupplierService.verify(pdSupplier);
        if (obj != null && obj.size()>0) {
            result.setSuccess(false);
            result.setMessage("供应商已存在");
            return result;
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
        Result<Boolean> result = new Result<>();
        //如果此参数为false则程序发生异常
        result.setResult(true);
        result.setMessage("编辑成功");
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        pdSupplier.setDepartParentId(sysUser.getDepartParentId());
        List<PdSupplier> obj = pdSupplierService.verify(pdSupplier);
        if (obj != null && obj.size()>0) {
            result.setSuccess(false);
            result.setMessage("供应商已存在");
            return result;
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
       return super.exportXls(request, pdSupplier, PdSupplier.class, "供应商");
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
       return super.importExcel(request, response, PdSupplier.class);
   }

}
