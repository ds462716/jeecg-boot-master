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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStockCheckPermission;
import org.jeecg.modules.pd.service.IPdProductStockCheckPermissionService;

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
 * @Description: 盘点权限表
 * @Author: jiangxz
 * @Date:   2020-07-14
 * @Version: V1.0
 */
@Api(tags="盘点权限表")
@RestController
@RequestMapping("/pd/pdProductStockCheckPermission")
@Slf4j
public class PdProductStockCheckPermissionController extends JeecgController<PdProductStockCheckPermission, IPdProductStockCheckPermissionService> {
	@Autowired
	private IPdProductStockCheckPermissionService pdProductStockCheckPermissionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdProductStockCheckPermission
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "盘点权限表-分页列表查询")
	@ApiOperation(value="盘点权限表-分页列表查询", notes="盘点权限表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdProductStockCheckPermission pdProductStockCheckPermission,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PdProductStockCheckPermission> queryWrapper = QueryGenerator.initQueryWrapper(pdProductStockCheckPermission, req.getParameterMap());
		Page<PdProductStockCheckPermission> page = new Page<PdProductStockCheckPermission>(pageNo, pageSize);
		IPage<PdProductStockCheckPermission> pageList = pdProductStockCheckPermissionService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdProductStockCheckPermission
	 * @return
	 */
	@AutoLog(value = "盘点权限表-添加")
	@ApiOperation(value="盘点权限表-添加", notes="盘点权限表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdProductStockCheckPermission pdProductStockCheckPermission) {
		pdProductStockCheckPermissionService.save(pdProductStockCheckPermission);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdProductStockCheckPermission
	 * @return
	 */
	@AutoLog(value = "盘点权限表-编辑")
	@ApiOperation(value="盘点权限表-编辑", notes="盘点权限表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdProductStockCheckPermission pdProductStockCheckPermission) {
		pdProductStockCheckPermissionService.updateById(pdProductStockCheckPermission);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "盘点权限表-通过id删除")
	@ApiOperation(value="盘点权限表-通过id删除", notes="盘点权限表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdProductStockCheckPermissionService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "盘点权限表-批量删除")
	@ApiOperation(value="盘点权限表-批量删除", notes="盘点权限表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pdProductStockCheckPermissionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "盘点权限表-通过id查询")
	@ApiOperation(value="盘点权限表-通过id查询", notes="盘点权限表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdProductStockCheckPermission pdProductStockCheckPermission = pdProductStockCheckPermissionService.getById(id);
		if(pdProductStockCheckPermission==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdProductStockCheckPermission);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdProductStockCheckPermission
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdProductStockCheckPermission pdProductStockCheckPermission) {
        return super.exportXls(request, pdProductStockCheckPermission, PdProductStockCheckPermission.class, "盘点权限表");
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
        return super.importExcel(request, response, PdProductStockCheckPermission.class);
    }


	 /**
	  * 锁盘
	  * @param id
	  * @return
	  */
	 @RequestMapping(value = "/locking", method = RequestMethod.DELETE)
	 public Result<?> locking(@RequestParam(name="id",required=true) String id,@RequestParam(name="recordId",required=true) String recordId) {
		 Result<Object> resul = pdProductStockCheckPermissionService.locking(id,recordId);
		 return resul;
	 }

	 /**
	  * 解锁
	  * @param id
	  * @return
	  */
	 @RequestMapping(value = "/unlock", method = RequestMethod.DELETE)
	 public Result<?> unlock(@RequestParam(name="id",required=true) String id,@RequestParam(name="recordId",required=true) String recordId) {
		 Result<Object> resul = pdProductStockCheckPermissionService.unlock(id,recordId);
		 return resul;
	 }
}
