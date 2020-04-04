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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdEncodingIdentifier;
import org.jeecg.modules.pd.service.IPdEncodingIdentifierService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysPermission;
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
 * @Description: 应用标识符表
 * @Author: zxh
 * @Date:   2019-12-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdEncodingIdentifier")
@Slf4j
public class PdEncodingIdentifierController extends JeecgController<PdEncodingIdentifier, IPdEncodingIdentifierService> {
	@Autowired
	private IPdEncodingIdentifierService pdEncodingIdentifierService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdEncodingIdentifier
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdEncodingIdentifier pdEncodingIdentifier,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdEncodingIdentifier.setDepartParentId(sysUser.getDepartParentId());
		QueryWrapper<PdEncodingIdentifier> queryWrapper = QueryGenerator.initQueryWrapper(pdEncodingIdentifier, req.getParameterMap());
		Page<PdEncodingIdentifier> page = new Page<PdEncodingIdentifier>(pageNo, pageSize);
		IPage<PdEncodingIdentifier> pageList = pdEncodingIdentifierService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


	 /**
	  * 不分页列表查询
	  *
	  * @return
	  */
	 @GetMapping(value = "/getEncodingIdentifierList")
	 public Result<List<PdEncodingIdentifier>> getEncodingIdentifierList(PdEncodingIdentifier pdEncodingIdentifier) {
		 long start = System.currentTimeMillis();
		 Result<List<PdEncodingIdentifier>> result = new Result<>();
		 try {
			 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			 LambdaQueryWrapper<PdEncodingIdentifier> query = new LambdaQueryWrapper<PdEncodingIdentifier>();
			 query.eq(PdEncodingIdentifier::getDepartParentId, sysUser.getDepartParentId());
			 if(!oConvertUtils.isEmpty(pdEncodingIdentifier.getValue())){
				 query.eq(PdEncodingIdentifier::getValue, pdEncodingIdentifier.getValue());
			 }
			 if(!oConvertUtils.isEmpty(pdEncodingIdentifier.getMeaning())){
				 query.eq(PdEncodingIdentifier::getMeaning,pdEncodingIdentifier.getMeaning());
			 }
			 query.orderByAsc(PdEncodingIdentifier::getUpdateTime);
			 List<PdEncodingIdentifier> list = pdEncodingIdentifierService.list(query);
			 result.setResult(list);
			 result.setSuccess(true);
		 }catch(Exception e){
			 log.error(e.getMessage(), e);
		 }
		 log.info("======获取一级菜单数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		 return result;
	 }

	/**
	 *   添加
	 *
	 * @param pdEncodingIdentifier
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdEncodingIdentifier pdEncodingIdentifier) {
		pdEncodingIdentifierService.save(pdEncodingIdentifier);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdEncodingIdentifier
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdEncodingIdentifier pdEncodingIdentifier) {
		pdEncodingIdentifierService.updateById(pdEncodingIdentifier);
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
		pdEncodingIdentifierService.removeById(id);
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
		this.pdEncodingIdentifierService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdEncodingIdentifier pdEncodingIdentifier = pdEncodingIdentifierService.getById(id);
		if(pdEncodingIdentifier==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdEncodingIdentifier);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdEncodingIdentifier
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdEncodingIdentifier pdEncodingIdentifier) {
        return super.exportXls(request, pdEncodingIdentifier, PdEncodingIdentifier.class, "应用标识符表");
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
        return super.importExcel(request, response, PdEncodingIdentifier.class);
    }

}
