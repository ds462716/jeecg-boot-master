package org.jeecg.modules.pd.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdVender;
import org.jeecg.modules.pd.service.IPdVenderService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.pd.util.FileUploadUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

 /**
 * @Description: 生产厂家
 * @Author: zxh
 * @Date:   2020-01-09
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdVender")
@Slf4j
public class PdVenderController extends JeecgController<PdVender, IPdVenderService> {
	@Autowired
	private IPdVenderService pdVenderService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdVender
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdVender pdVender,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Result<Page<PdVender>> result = new Result<>();
		Page<PdVender> pageList = new Page<>(pageNo,pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdVender.setDepartParentId(sysUser.getDepartParentId());
		pageList =pdVenderService.selectList(pageList,pdVender);
		result.setSuccess(true);
		result.setResult(pageList);
		return Result.ok(pageList);
	}


	 /**
	  * 不分页列表查询
	  *
	  * @return
	  */
	 @GetMapping(value = "/getVenderList")
	 public Result<List<PdVender>> getVenderList(PdVender pdVender) {
		 long start = System.currentTimeMillis();
		 Result<List<PdVender>> result = new Result<>();
		 try {
			 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			 pdVender.setDepartParentId(sysUser.getDepartParentId());
			 List<PdVender> list = pdVenderService.selectList(pdVender);
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
	 * @param pdVender
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdVender pdVender) {
		pdVenderService.save(pdVender);
		return Result.ok("添加成功！");
	}

	 /**
	  * 包含文件上传功能
	  * @param pdVender
	  * @return
	  */
	 @PostMapping(value = "/save")
	 public Result<?> save(PdVender pdVender,@RequestParam MultipartFile[] licenceSiteUp0,
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
		 pdVender.setDepartParentId(sysUser.getDepartParentId());
		 List<PdVender> obj = pdVenderService.verify(pdVender);
         if (obj != null && obj.size()>0) {
             result.setSuccess(false);
             result.setMessage("生产厂家已存在");
             return result;
         }
		 //存入图片
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp0)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp0);
			 pdVender.setLicenceSite0(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp1)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp1);
			 pdVender.setLicenceSite1(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp2)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp2);
			 pdVender.setLicenceSite2(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp3)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp3);
			 pdVender.setLicenceSite3(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp4)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp4);
			 pdVender.setLicenceSite4(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp5)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp5);
			 pdVender.setLicenceSite5(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp6)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp6);
			 pdVender.setLicenceSite6(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp7)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp7);
			 pdVender.setLicenceSite7(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp8)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp8);
			 pdVender.setLicenceSite8(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp9)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp9);
			 pdVender.setLicenceSite9(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp10)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp10);
			 pdVender.setLicenceSite10(filePath);
		 }
		 if(!FileUploadUtil.isImgEmpty(licenceSiteUp11)){
			 String filePath = FileUploadUtil.upload(licenceSiteUp11);
			 pdVender.setLicenceSite11(filePath);
		 }
		 pdVenderService.save(pdVender);
		 return Result.ok("添加成功！");
	 }


     /**
      * 包含文件上传功能
      * @param pdVender
      * @return
      */
     @PostMapping(value = "/update")
     public Result<?> update(PdVender pdVender,@RequestParam MultipartFile[] licenceSiteUp0,
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
		 pdVender.setDepartParentId(sysUser.getDepartParentId());
         List<PdVender> obj = pdVenderService.verify(pdVender);
         if (obj != null && obj.size()>0) {
             result.setSuccess(false);
             result.setMessage("生产厂家已存在");
             return result;
         }
         //存入图片
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp0)){
             String filePath = FileUploadUtil.upload(licenceSiteUp0);
             if(pdVender.getLicenceSite0()!=null && !"".equals(pdVender.getLicenceSite0())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite0());
             }
             if(!"".equals(filePath)){
				 pdVender.setLicenceSite0(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp1)){
             String filePath = FileUploadUtil.upload(licenceSiteUp1);
             if(pdVender.getLicenceSite1()!=null && !"".equals(pdVender.getLicenceSite1())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite1());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite1(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp2)){
             String filePath = FileUploadUtil.upload(licenceSiteUp2);
             if(pdVender.getLicenceSite2()!=null && !"".equals(pdVender.getLicenceSite2())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite2());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite2(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp3)){
             String filePath = FileUploadUtil.upload(licenceSiteUp3);
             if(pdVender.getLicenceSite3()!=null && !"".equals(pdVender.getLicenceSite3())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite3());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite3(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp4)){
             String filePath = FileUploadUtil.upload(licenceSiteUp4);
             if(pdVender.getLicenceSite4()!=null && !"".equals(pdVender.getLicenceSite4())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite4());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite4(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp5)){
             String filePath = FileUploadUtil.upload(licenceSiteUp5);
             if(pdVender.getLicenceSite5()!=null && !"".equals(pdVender.getLicenceSite5())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite5());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite5(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp6)){
             String filePath = FileUploadUtil.upload(licenceSiteUp6);
             if(pdVender.getLicenceSite6()!=null && !"".equals(pdVender.getLicenceSite6())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite6());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite6(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp7)){
             String filePath = FileUploadUtil.upload(licenceSiteUp7);
             if(pdVender.getLicenceSite7()!=null && !"".equals(pdVender.getLicenceSite7())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite7());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite7(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp8)){
             String filePath = FileUploadUtil.upload(licenceSiteUp8);
             if(pdVender.getLicenceSite8()!=null && !"".equals(pdVender.getLicenceSite8())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite8());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite8(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp9)){
             String filePath = FileUploadUtil.upload(licenceSiteUp9);
             if(pdVender.getLicenceSite9()!=null && !"".equals(pdVender.getLicenceSite9())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite9());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite9(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp10)){
             String filePath = FileUploadUtil.upload(licenceSiteUp10);
             if(pdVender.getLicenceSite10()!=null && !"".equals(pdVender.getLicenceSite10())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite10());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite10(filePath);
			 }
         }
         if(!FileUploadUtil.isImgEmpty(licenceSiteUp11)){
             String filePath = FileUploadUtil.upload(licenceSiteUp11);
             if(pdVender.getLicenceSite11()!=null && !"".equals(pdVender.getLicenceSite11())){
                 //先删除再更新
                 FileUploadUtil.deleteFile(pdVender.getLicenceSite11());
             }
			 if(!"".equals(filePath)){
				 pdVender.setLicenceSite11(filePath);
			 }
         }
         pdVenderService.updateById(pdVender);
         return Result.ok("编辑成功！");
     }

	/**
	 *  编辑
	 *
	 * @param pdVender
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdVender pdVender) {
		pdVenderService.updateById(pdVender);
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
		Result<Object> resul = pdVenderService.deleteV(id);
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
		Result<Object> resul = pdVenderService.deleteBatchV(ids);
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
		PdVender pdVender = pdVenderService.getById(id);
		if(pdVender==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdVender);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdVender
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdVender pdVender) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdVender.setDepartParentId(sysUser.getDepartParentId());
		//Step.1 获取导出数据
		List<PdVender> pdProducts = pdVenderService.selectList(pdVender);
		// Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "生产厂家列表");
		mv.addObject(NormalExcelConstants.CLASS, PdVender.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("生产厂家列表数据", "导出人:" + sysUser.getRealname(), "产品数据"));
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
		Result<Object> resul = pdVenderService.importExcel(fileMap);
		return resul;
	}

}
