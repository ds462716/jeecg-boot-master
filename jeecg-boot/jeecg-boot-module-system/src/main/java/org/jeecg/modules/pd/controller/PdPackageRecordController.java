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
import org.jeecg.modules.pd.entity.PdPackageRecord;
import org.jeecg.modules.pd.service.IPdPackageRecordService;

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
 * @Description: pd_package_record
 * @Author: jiangxz
 * @Date:   2020-03-04
 * @Version: V1.0
 */
@Api(tags="pd_package_record")
@RestController
@RequestMapping("/pd/pdPackageRecord")
@Slf4j
public class PdPackageRecordController extends JeecgController<PdPackageRecord, IPdPackageRecordService> {
	@Autowired
	private IPdPackageRecordService pdPackageRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdPackageRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "pd_package_record-分页列表查询")
	@ApiOperation(value="pd_package_record-分页列表查询", notes="pd_package_record-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdPackageRecord pdPackageRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PdPackageRecord> queryWrapper = QueryGenerator.initQueryWrapper(pdPackageRecord, req.getParameterMap());
		Page<PdPackageRecord> page = new Page<PdPackageRecord>(pageNo, pageSize);
		IPage<PdPackageRecord> pageList = pdPackageRecordService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdPackageRecord
	 * @return
	 */
	@AutoLog(value = "pd_package_record-添加")
	@ApiOperation(value="pd_package_record-添加", notes="pd_package_record-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdPackageRecord pdPackageRecord) {
		pdPackageRecordService.save(pdPackageRecord);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdPackageRecord
	 * @return
	 */
	@AutoLog(value = "pd_package_record-编辑")
	@ApiOperation(value="pd_package_record-编辑", notes="pd_package_record-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdPackageRecord pdPackageRecord) {
		pdPackageRecordService.updateById(pdPackageRecord);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pd_package_record-通过id删除")
	@ApiOperation(value="pd_package_record-通过id删除", notes="pd_package_record-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdPackageRecordService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "pd_package_record-批量删除")
	@ApiOperation(value="pd_package_record-批量删除", notes="pd_package_record-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pdPackageRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pd_package_record-通过id查询")
	@ApiOperation(value="pd_package_record-通过id查询", notes="pd_package_record-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdPackageRecord pdPackageRecord = pdPackageRecordService.getById(id);
		if(pdPackageRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdPackageRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdPackageRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdPackageRecord pdPackageRecord) {
        return super.exportXls(request, pdPackageRecord, PdPackageRecord.class, "pd_package_record");
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
        return super.importExcel(request, response, PdPackageRecord.class);
    }

}
