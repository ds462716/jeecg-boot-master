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
import org.jeecg.modules.pd.entity.PdOnOff;
import org.jeecg.modules.pd.service.IPdOnOffService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.pd.util.UUIDUtil;
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
 * @Description: pd_on_off
 * @Author: jiangxz
 * @Date:   2020-04-01
 * @Version: V1.0
 */
@Api(tags="pd_on_off")
@RestController
@RequestMapping("/pd/pdOnOff")
@Slf4j
public class PdOnOffController extends JeecgController<PdOnOff, IPdOnOffService> {
	@Autowired
	private IPdOnOffService pdOnOffService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdOnOff
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "pd_on_off-分页列表查询")
	@ApiOperation(value="pd_on_off-分页列表查询", notes="pd_on_off-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdOnOff pdOnOff,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdOnOff.setDepartParentId(sysUser.getDepartParentId());
		QueryWrapper<PdOnOff> queryWrapper = QueryGenerator.initQueryWrapper(pdOnOff, req.getParameterMap());
		Page<PdOnOff> page = new Page<PdOnOff>(pageNo, pageSize);
		IPage<PdOnOff> pageList = pdOnOffService.page(page, queryWrapper);

		if(pageList.getRecords()==null || pageList.getRecords().size()==0){
			//如果没有就查询默认的
			pdOnOff.setDepartParentId("");
			queryWrapper = QueryGenerator.initQueryWrapper(pdOnOff, req.getParameterMap());
			List<PdOnOff> pdOnOffList = pdOnOffService.list(queryWrapper);

			for(PdOnOff oo :pdOnOffList){
				oo.setDepartParentId(sysUser.getDepartParentId());
				oo.setDepartId(sysUser.getCurrentDepartId());
				oo.setId(UUIDUtil.getUuid());
			}
			pdOnOffService.saveBatch(pdOnOffList);
			page.setRecords(pdOnOffList);
			return Result.ok(page);
		}
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdOnOff
	 * @return
	 */
	@AutoLog(value = "pd_on_off-添加")
	@ApiOperation(value="pd_on_off-添加", notes="pd_on_off-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdOnOff pdOnOff) {
		pdOnOffService.save(pdOnOff);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdOnOff
	 * @return
	 */
	@AutoLog(value = "pd_on_off-编辑")
	@ApiOperation(value="pd_on_off-编辑", notes="pd_on_off-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdOnOff pdOnOff) {
		pdOnOffService.updateById(pdOnOff);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pd_on_off-通过id删除")
	@ApiOperation(value="pd_on_off-通过id删除", notes="pd_on_off-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdOnOffService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "pd_on_off-批量删除")
	@ApiOperation(value="pd_on_off-批量删除", notes="pd_on_off-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pdOnOffService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "pd_on_off-通过id查询")
	@ApiOperation(value="pd_on_off-通过id查询", notes="pd_on_off-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdOnOff pdOnOff = pdOnOffService.getById(id);
		if(pdOnOff==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdOnOff);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdOnOff
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdOnOff pdOnOff) {
        return super.exportXls(request, pdOnOff, PdOnOff.class, "pd_on_off");
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
        return super.importExcel(request, response, PdOnOff.class);
    }

}
