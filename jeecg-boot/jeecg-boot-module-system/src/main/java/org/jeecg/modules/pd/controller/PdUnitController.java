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

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdUnit;
import org.jeecg.modules.pd.service.IPdUnitService;

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

 /**
 * @Description: 产品单位表
 * @Author: jiangxz
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdUnit")
@Slf4j
public class PdUnitController extends JeecgController<PdUnit, IPdUnitService> {
	@Autowired
	private IPdUnitService pdUnitService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdUnit
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdUnit pdUnit,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PdUnit> page = new Page<PdUnit>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdUnit.setDepartParentId(sysUser.getDepartParentId());
		IPage<PdUnit> pageList = pdUnitService.queryList(page, pdUnit);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdUnit
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdUnit pdUnit,HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdUnit.setDepartParentId(sysUser.getDepartParentId());
		List<PdUnit> obj = pdUnitService.verify(pdUnit);
		if (obj != null && obj.size()>0) {
			return Result.error("单位已存在");
		}
		pdUnitService.save(pdUnit);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdUnit
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdUnit pdUnit) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdUnit.setDepartParentId(sysUser.getDepartParentId());
		List<PdUnit> obj = pdUnitService.verify(pdUnit);
		if (obj != null && obj.size()>0) {
			return Result.error("单位已存在");
		}
		pdUnitService.updateById(pdUnit);
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
		Result<Object> resul = pdUnitService.deleteV(id);
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
		Result<Object> resul = pdUnitService.deleteBatchV(ids);
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
		PdUnit pdUnit = pdUnitService.getById(id);
		if(pdUnit==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdUnit);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdUnit
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdUnit pdUnit) {
        return super.exportXls(request, pdUnit, PdUnit.class, "产品单位表");
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
        return super.importExcel(request, response, PdUnit.class);
    }

	 /**
	  * 不分页列表查询
	  *
	  * @return
	  */
	 @GetMapping(value = "/getUnitList")
	 public Result<List<PdUnit>> getUnitList(PdUnit pdUnit) {
		 long start = System.currentTimeMillis();
		 Result<List<PdUnit>> result = new Result<>();
		 try {
			 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			 pdUnit.setDepartParentId(sysUser.getDepartParentId());
			 List<PdUnit> list = pdUnitService.queryList(pdUnit);
			 result.setResult(list);
			 result.setSuccess(true);
		 }catch(Exception e){
			 log.error(e.getMessage(), e);
		 }
		 log.info("======获取单位数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		 return result;
	 }
}
