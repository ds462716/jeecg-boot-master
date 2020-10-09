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
import org.jeecg.modules.external.entity.ExInspectionItemsUseDetail;
import org.jeecg.modules.external.service.IExInspectionItemsUseDetailService;

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
 * @Description: 检验项目使用详情表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
@Api(tags="检验项目使用详情表")
@RestController
@RequestMapping("/external/exInspectionItemsUseDetail")
@Slf4j
public class ExInspectionItemsUseDetailController extends JeecgController<ExInspectionItemsUseDetail, IExInspectionItemsUseDetailService> {
	@Autowired
	private IExInspectionItemsUseDetailService exInspectionItemsUseDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param exInspectionItemsUseDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "检验项目使用详情表-分页列表查询")
	@ApiOperation(value="检验项目使用详情表-分页列表查询", notes="检验项目使用详情表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ExInspectionItemsUseDetail exInspectionItemsUseDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ExInspectionItemsUseDetail> queryWrapper = QueryGenerator.initQueryWrapper(exInspectionItemsUseDetail, req.getParameterMap());
		Page<ExInspectionItemsUseDetail> page = new Page<ExInspectionItemsUseDetail>(pageNo, pageSize);
		IPage<ExInspectionItemsUseDetail> pageList = exInspectionItemsUseDetailService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param exInspectionItemsUseDetail
	 * @return
	 */
	@AutoLog(value = "检验项目使用详情表-添加")
	@ApiOperation(value="检验项目使用详情表-添加", notes="检验项目使用详情表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ExInspectionItemsUseDetail exInspectionItemsUseDetail) {
		exInspectionItemsUseDetailService.save(exInspectionItemsUseDetail);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param exInspectionItemsUseDetail
	 * @return
	 */
	@AutoLog(value = "检验项目使用详情表-编辑")
	@ApiOperation(value="检验项目使用详情表-编辑", notes="检验项目使用详情表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ExInspectionItemsUseDetail exInspectionItemsUseDetail) {
		exInspectionItemsUseDetailService.updateById(exInspectionItemsUseDetail);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检验项目使用详情表-通过id删除")
	@ApiOperation(value="检验项目使用详情表-通过id删除", notes="检验项目使用详情表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		exInspectionItemsUseDetailService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "检验项目使用详情表-批量删除")
	@ApiOperation(value="检验项目使用详情表-批量删除", notes="检验项目使用详情表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.exInspectionItemsUseDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检验项目使用详情表-通过id查询")
	@ApiOperation(value="检验项目使用详情表-通过id查询", notes="检验项目使用详情表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ExInspectionItemsUseDetail exInspectionItemsUseDetail = exInspectionItemsUseDetailService.getById(id);
		if(exInspectionItemsUseDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(exInspectionItemsUseDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param exInspectionItemsUseDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExInspectionItemsUseDetail exInspectionItemsUseDetail) {
        return super.exportXls(request, exInspectionItemsUseDetail, ExInspectionItemsUseDetail.class, "检验项目使用详情表");
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
        return super.importExcel(request, response, ExInspectionItemsUseDetail.class);
    }

}
