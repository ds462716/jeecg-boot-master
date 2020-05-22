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
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdStockLog;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdStockLogService;

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
 * @Description: pd_stock_log
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Api(tags="pd_stock_log")
@RestController
@RequestMapping("/pd/pdStockLog")
@Slf4j
public class PdStockLogController extends JeecgController<PdStockLog, IPdStockLogService> {
	@Autowired
	private IPdStockLogService pdStockLogService;
	 @Autowired
	 private IPdProductStockService pdProductStockService;

	/**
	 * 分页列表查询
	 *
	 * @param pdStockLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "pd_stock_log-分页列表查询")
	@ApiOperation(value="pd_stock_log-分页列表查询", notes="pd_stock_log-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdStockLog pdStockLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PdStockLog> queryWrapper = QueryGenerator.initQueryWrapper(pdStockLog, req.getParameterMap());
		Page<PdStockLog> page = new Page<PdStockLog>(pageNo, pageSize);
		IPage<PdStockLog> pageList = pdStockLogService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 院内物流追溯
	  * @param pdProductStock
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/getByOriginalProduct")
	 public Result<?> getByOriginalProduct(PdProductStock pdProductStock,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdProductStock.setDepartParentId(sysUser.getDepartParentId());
		 pdProductStock.setProductFlag(PdConstant.PRODUCT_FLAG_0);
		 List<PdProductStock> pdProductStocks  = pdProductStockService.getByOriginalProduct(pdProductStock);
		 return Result.ok(pdProductStocks);
	 }

	 /**
	  * 获取产品追溯信息
	  * @param pdStockLog
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @PostMapping(value = "/getProdFlowInfo")
	 public Result<?> getProdFlowInfo(PdStockLog pdStockLog,
										   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										   HttpServletRequest req) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdStockLog.setDepartParentId(sysUser.getDepartParentId());
		 pdStockLog.setDepartId(sysUser.getCurrentDepartId());
		 List<PdStockLog> pdStockLogs  = pdStockLogService.list(new QueryWrapper<PdStockLog>().eq("product_id",pdStockLog.getProductId())
				 .eq("batch_no",pdStockLog.getBatchNo())
				 .eq("product_bar_code",pdStockLog.getProductBarCode())
				 .eq("exp_date",pdStockLog.getExpDate()).orderByAsc("record_time"));
		 return Result.ok(pdStockLogs);
	 }

	/**
	 *   添加
	 *
	 * @param pdStockLog
	 * @return
	 */
	@AutoLog(value = "pd_stock_log-添加")
	@ApiOperation(value="pd_stock_log-添加", notes="pd_stock_log-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdStockLog pdStockLog) {
		pdStockLogService.save(pdStockLog);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdStockLog
	 * @return
	 */
	@AutoLog(value = "pd_stock_log-编辑")
	@ApiOperation(value="pd_stock_log-编辑", notes="pd_stock_log-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdStockLog pdStockLog) {
		pdStockLogService.updateById(pdStockLog);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pd_stock_log-通过id删除")
	@ApiOperation(value="pd_stock_log-通过id删除", notes="pd_stock_log-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdStockLogService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "pd_stock_log-批量删除")
	@ApiOperation(value="pd_stock_log-批量删除", notes="pd_stock_log-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pdStockLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pd_stock_log-通过id查询")
	@ApiOperation(value="pd_stock_log-通过id查询", notes="pd_stock_log-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdStockLog pdStockLog = pdStockLogService.getById(id);
		if(pdStockLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdStockLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdStockLog
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdStockLog pdStockLog) {
        return super.exportXls(request, pdStockLog, PdStockLog.class, "pd_stock_log");
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
        return super.importExcel(request, response, PdStockLog.class);
    }

}
