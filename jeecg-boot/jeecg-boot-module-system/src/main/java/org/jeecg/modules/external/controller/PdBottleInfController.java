package org.jeecg.modules.external.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.entity.PdBottleInf;
import org.jeecg.modules.external.service.IPdBottleInfService;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 开闭瓶记录表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Api(tags="开闭瓶记录表")
@RestController
@RequestMapping("/pd/pdBottleInf")
@Slf4j
public class PdBottleInfController extends JeecgController<PdBottleInf, IPdBottleInfService> {
	@Autowired
	private IPdBottleInfService pdBottleInfService;
	@Autowired
	private IPdDepartService pdDepartService;
	/**
	 * 分页列表查询
	 *
	 * @param pdBottleInf
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdBottleInf pdBottleInf,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PdBottleInf> page = new Page<PdBottleInf>(pageNo, pageSize);
		if(oConvertUtils.isNotEmpty(pdBottleInf.getDepartIds()) && !"undefined".equals(pdBottleInf.getDepartIds())) {
			pdBottleInf.setDepartIdList(Arrays.asList(pdBottleInf.getDepartIds().split(",")));
		}else{
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			pdBottleInf.setDepartIdList(departList);
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdBottleInf.setDepartParentId(sysUser.getDepartParentId());
		IPage<PdBottleInf> pageList = pdBottleInfService.selectList(page, pdBottleInf);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdBottleInf
	 * @return
	 */
	@PostMapping(value = "/submitPdBottleInf")
	public Result<?> submitPdBottleInf(@RequestBody PdBottleInf pdBottleInf) {
		pdBottleInfService.save(pdBottleInf);
		return Result.ok("操作成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdBottleInf
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdBottleInf pdBottleInf) {
		pdBottleInfService.updateById(pdBottleInf);
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
		pdBottleInfService.removeById(id);
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
		this.pdBottleInfService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdBottleInf pdBottleInf = pdBottleInfService.getById(id);
		if(pdBottleInf==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdBottleInf);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdBottleInf
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdBottleInf pdBottleInf) {
		// Step.1 组装查询条件查询数据
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		//Step.2 获取导出数据
		if(oConvertUtils.isNotEmpty(pdBottleInf.getDepartIds()) && !"undefined".equals(pdBottleInf.getDepartIds())) {
			pdBottleInf.setDepartIdList(Arrays.asList(pdBottleInf.getDepartIds().split(",")));
		}else{
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			pdBottleInf.setDepartIdList(departList);
		}
		pdBottleInf.setDepartParentId(sysUser.getDepartParentId());
		List<PdBottleInf> bottleInfList=pdBottleInfService.selectList(pdBottleInf);
		// Step.3 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "试剂用量明细");
		mv.addObject(NormalExcelConstants.CLASS, PdBottleInf.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("试剂用量明细", "导出人:"+sysUser.getRealname(), "试剂用量明细数据"));
		mv.addObject(NormalExcelConstants.DATA_LIST, bottleInfList);
		return mv;



        //return super.exportXls(request, pdBottleInf, PdBottleInf.class, "开闭瓶记录表");
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
        return super.importExcel(request, response, PdBottleInf.class);
    }

	/**
	 * 试剂消耗报表  mcb  --20200616 用于统计查询  试剂消耗报表
	 */
	@GetMapping(value = "bottleInfReportQuery")
	public Result<?> bottleInfReportQuery(PdBottleInf pdBottleInf,
											@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdBottleInf.setDepartParentId(sysUser.getDepartParentId());
		Page<PdBottleInf> page = new Page<PdBottleInf>(pageNo, pageSize);
		page = pdBottleInfService.bottleInfReportQuery(page, pdBottleInf);
		return Result.ok(page);
	}
	/**
	 * 入库统计视图  mcb  --20200617 用于统计查询  入库统计报表视图
	 */
	@GetMapping(value = "queryRecordView")
	public Result<?> queryRecordView(PdBottleInf pdBottleInf){
		Map map=new HashMap();
		List<HashMap> orderDate=new ArrayList<HashMap>();//根据月份数
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdBottleInf.setDepartParentId(sysUser.getDepartParentId());
		//根据日期统计每日的采购量
		//orderDate=pdStockRecordService.queryRecordView(pdBottleInf);
		map.put("orderDate",orderDate);
		return Result.ok(map);
	}
}
