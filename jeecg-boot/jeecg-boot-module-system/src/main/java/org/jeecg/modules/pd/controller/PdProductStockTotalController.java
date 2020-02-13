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

import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
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

 /**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@RestController
@RequestMapping("/stock/pdProductStockTotal")
@Slf4j
public class PdProductStockTotalController {
	@Autowired
	private IPdProductStockTotalService pdProductStockTotalService;
	@Autowired
	private IPdProductStockService pdProductStockService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdProductStockTotal
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdProductStockTotal pdProductStockTotal,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PdProductStockTotal> queryWrapper = QueryGenerator.initQueryWrapper(pdProductStockTotal, req.getParameterMap());
		Page<PdProductStockTotal> page = new Page<PdProductStockTotal>(pageNo, pageSize);
		IPage<PdProductStockTotal> pageList = pdProductStockTotalService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdProductStockTotalPage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdProductStockTotalPage pdProductStockTotalPage) {
		PdProductStockTotal pdProductStockTotal = new PdProductStockTotal();
		BeanUtils.copyProperties(pdProductStockTotalPage, pdProductStockTotal);
		pdProductStockTotalService.saveMain(pdProductStockTotal, pdProductStockTotalPage.getPdProductStockList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdProductStockTotalPage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdProductStockTotalPage pdProductStockTotalPage) {
		PdProductStockTotal pdProductStockTotal = new PdProductStockTotal();
		BeanUtils.copyProperties(pdProductStockTotalPage, pdProductStockTotal);
		PdProductStockTotal pdProductStockTotalEntity = pdProductStockTotalService.getById(pdProductStockTotal.getId());
		if(pdProductStockTotalEntity==null) {
			return Result.error("未找到对应数据");
		}
		pdProductStockTotalService.updateMain(pdProductStockTotal, pdProductStockTotalPage.getPdProductStockList());
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
		pdProductStockTotalService.delMain(id);
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
		this.pdProductStockTotalService.delBatchMain(Arrays.asList(ids.split(",")));
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
		PdProductStockTotal pdProductStockTotal = pdProductStockTotalService.getById(id);
		if(pdProductStockTotal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdProductStockTotal);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryPdProductStockByMainId")
	public Result<?> queryPdProductStockListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PdProductStock> pdProductStockList = pdProductStockService.selectByMainId(id);
		return Result.ok(pdProductStockList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdProductStockTotal
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdProductStockTotal pdProductStockTotal) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PdProductStockTotal> queryWrapper = QueryGenerator.initQueryWrapper(pdProductStockTotal, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PdProductStockTotal> queryList = pdProductStockTotalService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PdProductStockTotal> pdProductStockTotalList = new ArrayList<PdProductStockTotal>();
      if(oConvertUtils.isEmpty(selections)) {
          pdProductStockTotalList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          pdProductStockTotalList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PdProductStockTotalPage> pageList = new ArrayList<PdProductStockTotalPage>();
      for (PdProductStockTotal main : pdProductStockTotalList) {
          PdProductStockTotalPage vo = new PdProductStockTotalPage();
          BeanUtils.copyProperties(main, vo);
          List<PdProductStock> pdProductStockList = pdProductStockService.selectByMainId(main.getId());
          vo.setPdProductStockList(pdProductStockList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "库存总表列表");
      mv.addObject(NormalExcelConstants.CLASS, PdProductStockTotalPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("库存总表数据", "导出人:"+sysUser.getRealname(), "库存总表"));
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
              List<PdProductStockTotalPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PdProductStockTotalPage.class, params);
              for (PdProductStockTotalPage page : list) {
                  PdProductStockTotal po = new PdProductStockTotal();
                  BeanUtils.copyProperties(page, po);
                  pdProductStockTotalService.saveMain(po, page.getPdProductStockList());
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
