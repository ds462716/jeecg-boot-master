package org.jeecg.modules.external.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.service.IExInspectionItemsService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Api(tags="检查项目表")
@RestController
@RequestMapping("/external/exInspectionItems")
@Slf4j
public class ExInspectionItemsController extends JeecgController<ExInspectionItems, IExInspectionItemsService> {
	@Autowired
	private IExInspectionItemsService exInspectionItemsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param exInspectionItems
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "检查项目表-分页列表查询")
	@ApiOperation(value="检查项目表-分页列表查询", notes="检查项目表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ExInspectionItems exInspectionItems,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ExInspectionItems> queryWrapper = QueryGenerator.initQueryWrapper(exInspectionItems, req.getParameterMap());
		Page<ExInspectionItems> page = new Page<ExInspectionItems>(pageNo, pageSize);
		IPage<ExInspectionItems> pageList = exInspectionItemsService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 @GetMapping(value = "/test")
	 public Result<?> test(ExInspectionItems exInspectionItems,
									HttpServletRequest req) {
		 List<ExInspectionItems> exInspectionItemss = exInspectionItemsService.selectList(exInspectionItems);
		 return Result.ok(exInspectionItemss);
	 }
	
	/**
	 *   添加
	 *
	 * @param exInspectionItems
	 * @return
	 */
	@AutoLog(value = "检查项目表-添加")
	@ApiOperation(value="检查项目表-添加", notes="检查项目表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ExInspectionItems exInspectionItems) {
		exInspectionItemsService.save(exInspectionItems);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param exInspectionItems
	 * @return
	 */
	@AutoLog(value = "检查项目表-编辑")
	@ApiOperation(value="检查项目表-编辑", notes="检查项目表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ExInspectionItems exInspectionItems) {
		exInspectionItemsService.updateById(exInspectionItems);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检查项目表-通过id删除")
	@ApiOperation(value="检查项目表-通过id删除", notes="检查项目表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		exInspectionItemsService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "检查项目表-批量删除")
	@ApiOperation(value="检查项目表-批量删除", notes="检查项目表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.exInspectionItemsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检查项目表-通过id查询")
	@ApiOperation(value="检查项目表-通过id查询", notes="检查项目表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ExInspectionItems exInspectionItems = exInspectionItemsService.getById(id);
		if(exInspectionItems==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(exInspectionItems);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param exInspectionItems
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExInspectionItems exInspectionItems) {
        return super.exportXls(request, exInspectionItems, ExInspectionItems.class, "检查项目表");
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
        return super.importExcel(request, response, ExInspectionItems.class);
    }

}
