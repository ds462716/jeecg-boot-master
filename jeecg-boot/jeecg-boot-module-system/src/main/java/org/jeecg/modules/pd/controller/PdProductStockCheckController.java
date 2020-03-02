package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStockCheck;
import org.jeecg.modules.pd.entity.PdProductStockCheckChild;
import org.jeecg.modules.pd.service.IPdProductStockCheckChildService;
import org.jeecg.modules.pd.service.IPdProductStockCheckService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
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
import java.util.*;
import java.util.stream.Collectors;

 /**
 * @Description: 盘点记录表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Api(tags="盘点记录表")
@RestController
@RequestMapping("/pd/pdProductStockCheck")
@Slf4j
public class PdProductStockCheckController {
	@Autowired
	private IPdProductStockCheckService pdProductStockCheckService;
	@Autowired
	private IPdProductStockCheckChildService pdProductStockCheckChildService;
	 @Autowired
	 private ISysDepartService sysDepartService;
	/**
	 * 分页列表查询
	 *
	 * @param pdProductStockCheck
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "盘点记录表-分页列表查询")
	@ApiOperation(value="盘点记录表-分页列表查询", notes="盘点记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdProductStockCheck pdProductStockCheck,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PdProductStockCheck> queryWrapper = QueryGenerator.initQueryWrapper(pdProductStockCheck, req.getParameterMap());
		Page<PdProductStockCheck> page = new Page<PdProductStockCheck>(pageNo, pageSize);
		IPage<PdProductStockCheck> pageList = pdProductStockCheckService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


	 /**
	  * 新增初始化操作
	  *
	  * @return
	  */
	 @GetMapping(value = "/checkInfo")
	 public Result<PdProductStockCheck> checkInfo() {
		 Result<PdProductStockCheck> result = new Result<>();
		 PdProductStockCheck productStockCheck = new PdProductStockCheck();
		 String checkNo = UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_PD);
		 productStockCheck.setCheckNo(checkNo);
		 productStockCheck.setCheckDate(new Date());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 SysDepart sysDepart = sysDepartService.getDepartByOrgCode(sysUser.getOrgCode());
		 productStockCheck.setDeptId(sysDepart.getId());
		 productStockCheck.setDeptName(sysDepart.getDepartName());
		 productStockCheck.setCheckBy(sysUser.getId());
		 productStockCheck.setCheckName(sysUser.getRealname());
		 productStockCheck.setCheckStatus(PdConstant.CHECK_STATE_1);
		 productStockCheck.setCheckCount(0.00);
		 productStockCheck.setProfitLossCount(0.00);
		 productStockCheck.setShouldCount(0.00);
		 result.setResult(productStockCheck);
		 result.setSuccess(true);
		 return result;
	 }
	/**
	 *   添加
	 *
	 * @param pdProductStockCheck
	 * @return
	 */
	@AutoLog(value = "盘点记录表-添加")
	@ApiOperation(value="盘点记录表-添加", notes="盘点记录表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdProductStockCheck pdProductStockCheck) {
		pdProductStockCheckService.saveMain(pdProductStockCheck, pdProductStockCheck.getPdProductStockCheckChildList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdProductStockCheck
	 * @return
	 */
	@AutoLog(value = "盘点记录表-编辑")
	@ApiOperation(value="盘点记录表-编辑", notes="盘点记录表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdProductStockCheck pdProductStockCheck) {
		PdProductStockCheck pdProductStockCheckEntity = pdProductStockCheckService.getById(pdProductStockCheck.getId());
		if(pdProductStockCheckEntity==null) {
			return Result.error("未找到对应数据");
		}
		pdProductStockCheckService.updateMain(pdProductStockCheck, pdProductStockCheck.getPdProductStockCheckChildList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "盘点记录表-通过id删除")
	@ApiOperation(value="盘点记录表-通过id删除", notes="盘点记录表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdProductStockCheckService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "盘点记录表-批量删除")
	@ApiOperation(value="盘点记录表-批量删除", notes="盘点记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pdProductStockCheckService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "盘点记录表-通过id查询")
	@ApiOperation(value="盘点记录表-通过id查询", notes="盘点记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdProductStockCheck pdProductStockCheck = pdProductStockCheckService.getById(id);
		if(pdProductStockCheck==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdProductStockCheck);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "盘点明细表集合-通过id查询")
	@ApiOperation(value="盘点明细表集合-通过id查询", notes="盘点明细表-通过id查询")
	@GetMapping(value = "/queryPdProductStockCheckChildByMainId")
	public Result<?> queryPdProductStockCheckChildListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PdProductStockCheckChild> pdProductStockCheckChildList = pdProductStockCheckChildService.selectByMainId(id);
		return Result.ok(pdProductStockCheckChildList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdProductStockCheck
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdProductStockCheck pdProductStockCheck) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PdProductStockCheck> queryWrapper = QueryGenerator.initQueryWrapper(pdProductStockCheck, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PdProductStockCheck> queryList = pdProductStockCheckService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PdProductStockCheck> pdProductStockCheckList = new ArrayList<PdProductStockCheck>();
      if(oConvertUtils.isEmpty(selections)) {
          pdProductStockCheckList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          pdProductStockCheckList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PdProductStockCheck> pageList = new ArrayList<PdProductStockCheck>();
      for (PdProductStockCheck main : pdProductStockCheckList) {
          List<PdProductStockCheckChild> pdProductStockCheckChildList = pdProductStockCheckChildService.selectByMainId(main.getId());
		  main.setPdProductStockCheckChildList(pdProductStockCheckChildList);
          pageList.add(main);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "盘点记录表列表");
      mv.addObject(NormalExcelConstants.CLASS, PdProductStockCheck.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("盘点记录表数据", "导出人:"+sysUser.getRealname(), "盘点记录表"));
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
              List<PdProductStockCheck> list = ExcelImportUtil.importExcel(file.getInputStream(), PdProductStockCheck.class, params);
              for (PdProductStockCheck page : list) {
                  PdProductStockCheck po = new PdProductStockCheck();
                  BeanUtils.copyProperties(page, po);
                  pdProductStockCheckService.saveMain(po, page.getPdProductStockCheckChildList());
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

}
