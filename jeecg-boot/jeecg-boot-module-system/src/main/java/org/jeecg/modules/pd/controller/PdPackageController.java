package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdPackage;
import org.jeecg.modules.pd.entity.PdPackageDetail;
import org.jeecg.modules.pd.service.IPdPackageDetailService;
import org.jeecg.modules.pd.service.IPdPackageService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.vo.PdPackagePage;
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
 * @Description: 定数包
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdPackage")
@Slf4j
public class PdPackageController {
	@Autowired
	private IPdPackageService pdPackageService;
	@Autowired
	private IPdPackageDetailService pdPackageDetailService;
	@Autowired
	private IPdProductStockTotalService pdProductStockTotalService;

	 /**
	 * 分页列表查询
	 *
	 * @param pdPackage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdPackage pdPackage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<PdPackage> queryWrapper = QueryGenerator.initQueryWrapper(pdPackage, req.getParameterMap());
		Page<PdPackage> page = new Page<PdPackage>(pageNo, pageSize);
//		IPage<PdPackage> pageList = pdPackageService.page(page, queryWrapper);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdPackage.setDepartParentId(sysUser.getDepartParentId());
		IPage<PdPackage> pageList = pdPackageService.queryList(page, pdPackage);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdPackagePage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdPackagePage pdPackagePage) {
		PdPackage pdPackage = new PdPackage();
		BeanUtils.copyProperties(pdPackagePage, pdPackage);
		pdPackageService.saveMain(pdPackage, pdPackagePage.getPdPackageDetailList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdPackagePage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdPackagePage pdPackagePage) {
		PdPackage pdPackage = new PdPackage();
		BeanUtils.copyProperties(pdPackagePage, pdPackage);
		PdPackage pdPackageEntity = pdPackageService.getById(pdPackage.getId());
		if(pdPackageEntity==null) {
			return Result.error("未找到对应数据");
		}
		pdPackageService.updateMain(pdPackage, pdPackagePage.getPdPackageDetailList());
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
		pdPackageService.delMain(id);
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
		this.pdPackageService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdPackage pdPackage = pdPackageService.getById(id);
		if(pdPackage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdPackage);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryPdPackageDetailByMainId")
	public Result<?> queryPdPackageDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PdPackageDetail> pdPackageDetailList = pdPackageDetailService.selectByMainId(id);
		return Result.ok(pdPackageDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdPackage
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdPackage pdPackage) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PdPackage> queryWrapper = QueryGenerator.initQueryWrapper(pdPackage, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PdPackage> queryList = pdPackageService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PdPackage> pdPackageList = new ArrayList<PdPackage>();
      if(oConvertUtils.isEmpty(selections)) {
          pdPackageList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          pdPackageList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PdPackagePage> pageList = new ArrayList<PdPackagePage>();
      for (PdPackage main : pdPackageList) {
          PdPackagePage vo = new PdPackagePage();
          BeanUtils.copyProperties(main, vo);
          List<PdPackageDetail> pdPackageDetailList = pdPackageDetailService.selectByMainId(main.getId());
          vo.setPdPackageDetailList(pdPackageDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "定数包列表");
      mv.addObject(NormalExcelConstants.CLASS, PdPackagePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("定数包数据", "导出人:"+sysUser.getRealname(), "定数包"));
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
              List<PdPackagePage> list = ExcelImportUtil.importExcel(file.getInputStream(), PdPackagePage.class, params);
              for (PdPackagePage page : list) {
                  PdPackage po = new PdPackage();
                  BeanUtils.copyProperties(page, po);
                  pdPackageService.saveMain(po, page.getPdPackageDetailList());
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
	  * 定数包选择器用
	  *
	  * @param pdPackage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/queryPackgeList")
	 public Result<?> queryPackgeList(PdPackage pdPackage,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Page<PdPackage> page = new Page<PdPackage>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdPackage.setDepartParentId(sysUser.getDepartParentId());
		 IPage<PdPackage> pageList = pdPackageService.queryList(page, pdPackage);
		 return Result.ok(pageList);
	 }
	 /**
	  *  查询定数包明细   申领单及调拨单用
	  * @param pdPackageDetail
	  * @return
	  */
	 @GetMapping(value = "queryPdPackageDetailList")
	 public Result<?> queryPdPackageList(PdPackageDetail pdPackageDetail,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {

		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 List<PdPackageDetail> detailList = pdPackageDetailService.queryPdPackageList(pdPackageDetail);
		    if(CollectionUtils.isNotEmpty(detailList)){
                  for(PdPackageDetail detail:detailList){
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
