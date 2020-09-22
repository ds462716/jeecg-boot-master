package org.jeecg.modules.external.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.entity.PdBottleInf;
import org.jeecg.modules.external.service.IExInspectionItemsService;
import org.jeecg.modules.external.service.IPdBottleInfService;
import org.jeecg.modules.external.vo.PdBottleInfExlce;
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
import java.math.BigDecimal;
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
	@Autowired
	private IExInspectionItemsService exInspectionItemsService;
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
	@GetMapping(value = "/bottleInfReportQuery")
	public Result<?> bottleInfReportQuery(PdBottleInf pdBottleInf,
											@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdBottleInf.setDepartParentId(sysUser.getDepartParentId());
		Page<PdBottleInf> page = new Page<PdBottleInf>(pageNo, pageSize);
		IPage<PdBottleInf> pageList = pdBottleInfService.bottleInfReportQuery(page, pdBottleInf);//
		List<PdBottleInf> list = pdBottleInfService.bottleInfReportQuery(pdBottleInf);//
		//计算总消耗数量，总消耗金额
		//**************************
		Double totalCount=0.00;//总消耗数量
		BigDecimal totalPrice=BigDecimal.ZERO;//总消耗金额
		if(CollectionUtils.isNotEmpty(list)){
			for (PdBottleInf inf : list) {
				totalCount+=inf.getNum();
				totalPrice=totalPrice.add(inf.getTotalPrice());
			}
		}
		Map map=new HashMap();
		map.put("records",pageList);
		map.put("totalCount",totalCount);//总消耗数量
		map.put("totalPrice",totalPrice);//总消耗金额
		return Result.ok(map);
		//return Result.ok(pageList);
	}
	/**
	 * 入库统计视图  mcb  --20200617 用于统计查询  入库统计报表视图
	 */
	@GetMapping(value = "queryRecordView")
	public Result<?> queryRecordView(PdBottleInf pdBottleInf){
		Map map=new HashMap();
		List<HashMap> orderMoney=new ArrayList<HashMap>();//金额
		List<HashMap> orderCount=new ArrayList<HashMap>();//数量
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdBottleInf.setDepartParentId(sysUser.getDepartParentId());
		//根据产品按月统计试剂消耗金额
		orderMoney=pdBottleInfService.queryRecordViewMoney(pdBottleInf);
		//根据产品按月统计试剂消耗数量
		orderCount=pdBottleInfService.queryRecordViewCount(pdBottleInf);
		map.put("orderMoney",orderMoney);
		map.put("orderCount",orderCount);
		return Result.ok(map);
	}


	/**
	 * 导出excel(试剂消耗统计报表导出)
	 *
	 * @param request
	 * @param pdBottleInf
	 */
	@RequestMapping(value = "/ReportQueryExportXls")
	public ModelAndView ReportQueryExportXls(HttpServletRequest request, PdBottleInf pdBottleInf) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdBottleInf.setDepartParentId(sysUser.getDepartParentId());
		 List<PdBottleInf> detailList =  pdBottleInfService.bottleInfReportQuery(pdBottleInf);
		List<PdBottleInfExlce> exportList = JSON.parseArray(JSON.toJSONString(detailList), PdBottleInfExlce.class);
		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "试剂消耗报表");
		mv.addObject(NormalExcelConstants.CLASS, PdBottleInfExlce.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("试剂消耗报表数据", "导出人:" + sysUser.getRealname(), "试剂消耗报表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		return mv;
	}



	/**
	 * 统计查询  -- 月检验项目收费金额统计
	 */
	@GetMapping(value = "inspectionMonthQuery")
	public Result<?> inspectionMonthQuery(PdBottleInf pdBottleInf,
										  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
										  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdBottleInf.setDepartParentId(sysUser.getDepartParentId());
		Page<PdBottleInf> page = new Page<PdBottleInf>(pageNo, pageSize);
		IPage<PdBottleInf> pageList = pdBottleInfService.inspectionMonthQuery(page, pdBottleInf);//
		 List<PdBottleInf> list=  pageList.getRecords();
		 if(oConvertUtils.isNotEmpty(list)){
			 for(PdBottleInf  inf:  list ){
			 String month=inf.getMonth();
				 ExInspectionItems items=new ExInspectionItems();
				 items.setMonth(month);
				 items= exInspectionItemsService.inspectionMonthQuery(items);
				 if(oConvertUtils.isNotEmpty(items)){
					 inf.setItemNum(items.getItemNum());
					 inf.setItemPrice(items.getItemPrice());
				 }
			 }
		 }
 		return Result.ok(pageList);
	}


	/**
	 *  统计查询 --试剂消耗明细（月份统计）
	 */
	@GetMapping(value = "selectBottleInfMonth")
	public Result<?> selectBottleInfMonth(PdBottleInf pdBottleInf,
										  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
										  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdBottleInf.setDepartParentId(sysUser.getDepartParentId());
		Page<PdBottleInf> page = new Page<PdBottleInf>(pageNo, pageSize);
		IPage<PdBottleInf> pageList = pdBottleInfService.selectBottleInfMonth(page, pdBottleInf);//
		return Result.ok(pageList);
	}
}
