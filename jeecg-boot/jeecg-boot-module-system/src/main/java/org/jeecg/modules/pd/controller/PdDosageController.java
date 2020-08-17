package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdDosage;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdDosageService;
import org.jeecg.modules.pd.vo.PdDosagePage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用量表
 * @Author: zxh
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Api(tags="用量表")
@RestController
@RequestMapping("/pd/pdDosage")
@Slf4j
public class PdDosageController extends JeecgController<PdDosage, IPdDosageService> {
	@Autowired
	private IPdDosageService pdDosageService;
	@Autowired
	private IPdDepartService pdDepartService;

	
	/**
	 * 分页列表查询
	 *
	 * @param pdDosage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用量表-分页列表查询")
	@ApiOperation(value="用量表-分页列表查询", notes="用量表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdDosage pdDosage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PdDosage> page = new Page<PdDosage>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();



		if(oConvertUtils.isNotEmpty(pdDosage.getDepartIds()) && !"undefined".equals(pdDosage.getDepartIds())) {
			pdDosage.setDepartIdList(Arrays.asList(pdDosage.getDepartIds().split(",")));
		}else{
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			pdDosage.setDepartIdList(departList);
		}
		//pdProductStockCheck.setDepartParentId(sysUser.getDepartParentId());
		//pdProductStockCheck.setDepartId(sysUser.getCurrentDepartId());


		pdDosage.setDepartParentId(sysUser.getDepartParentId());
		//pdDosage.setDepartId(sysUser.getCurrentDepartId());
		IPage<PdDosage> pageList = pdDosageService.queryList(page, pdDosage);

		Map<String,Object> map = new HashMap<>();
		map.put("hospitalCode",sysUser.getHospitalCode());
		map.put("pageList",pageList);

		return Result.ok(map);
	}
	
	/**
	 *   添加
	 *
	 * @param pdDosage
	 * @return
	 */
	@AutoLog(value = "用量表-添加")
	@ApiOperation(value="用量表-添加", notes="用量表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdDosage pdDosage) {
		pdDosageService.save(pdDosage);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdDosage
	 * @return
	 */
	@AutoLog(value = "用量表-编辑")
	@ApiOperation(value="用量表-编辑", notes="用量表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdDosage pdDosage) {
		pdDosageService.updateById(pdDosage);
		return Result.ok("编辑成功!");
	}

	 /**
	  * 初始化Modal页面
	  *
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/initModal")
	 public Result<?> initModal(@RequestParam(name = "id") String id, HttpServletRequest req) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 PdDosage pdDosage = pdDosageService.initModal(id);
		 pdDosage.setHospitalCode(sysUser.getHospitalCode());
		 return Result.ok(pdDosage);
	 }

	 /**
	  * 统计查询-用量明细
	  * @param pdDosage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/queryPdDosageList")
	 public Result<?> queryPdDosageList(PdDosage pdDosage,
											   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
											   HttpServletRequest req) {

		 Page<PdDosage> page = new Page<>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdDosage.setDepartParentId(sysUser.getDepartParentId());

		 if(oConvertUtils.isNotEmpty(pdDosage.getDepartIds()) && !"undefined".equals(pdDosage.getDepartIds())){
			 pdDosage.setDepartIdList(Arrays.asList(pdDosage.getDepartIds().split(",")));
		 }else{
			 //查询科室下所有下级科室的ID
			 SysDepart sysDepart=new SysDepart();
			 List<String> departList=pdDepartService.selectListDepart(sysDepart);
			 pdDosage.setDepartIdList(departList);
		 }
		 pdDosage.setHyChargedList(Arrays.asList("0,1".split(",")));
		 page = pdDosageService.queryPdDosageList(page, pdDosage);
		 return Result.ok(page);
	 }

	 /**
	  * 提交
	  *
	  * @param pdDosage
	  * @return
	  */
	 @PostMapping(value = "/submit")
	 public Result<?> submit(@RequestBody PdDosage pdDosage) {
	 	//不收费
		 pdDosageService.saveMain(pdDosage, PdConstant.IS_CHARGE_FLAG_1);
		 return Result.ok("添加成功！");
	 }

	 /**
	  * 用量退回
	  * @param pdDosage
	  * @return
	  */
	 @PostMapping(value = "/dosageReturned")
	 public Result<?> dosageReturned(@RequestBody PdDosage pdDosage) {
		 //不收费
		 pdDosageService.dosageReturned(pdDosage);
		 return Result.ok("退回成功！");
	 }

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用量表-通过id删除")
	@ApiOperation(value="用量表-通过id删除", notes="用量表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdDosageService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用量表-批量删除")
	@ApiOperation(value="用量表-批量删除", notes="用量表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pdDosageService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用量表-通过id查询")
	@ApiOperation(value="用量表-通过id查询", notes="用量表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdDosage pdDosage = pdDosageService.getById(id);
		if(pdDosage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdDosage);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdDosage
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdDosage pdDosage) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdDosage.setDepartParentId(sysUser.getDepartParentId());

		if(oConvertUtils.isNotEmpty(pdDosage.getDepartIds()) && !"undefined".equals(pdDosage.getDepartIds())){
			pdDosage.setDepartIdList(Arrays.asList(pdDosage.getDepartIds().split(",")));
		}else{
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			pdDosage.setDepartIdList(departList);
		}
		pdDosage.setHyChargedList(Arrays.asList("0,1".split(",")));
		List<PdDosage> detailList = pdDosageService.queryPdDosageList(pdDosage);
		List<PdDosagePage> exportList = JSON.parseArray(JSON.toJSONString(detailList), PdDosagePage.class);
		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "用量");
		mv.addObject(NormalExcelConstants.CLASS, PdDosagePage.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用量数据", "导出人:" + sysUser.getRealname(), "用量表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
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
        return super.importExcel(request, response, PdDosage.class);
    }

}
