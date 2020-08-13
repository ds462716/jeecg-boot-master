package org.jeecg.modules.external.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.vo.PdNumericalInfHcExlce;
import org.jeecg.modules.external.vo.PdNumericalInfSjExlce;
import org.jeecg.modules.pd.entity.PdNumericalInf;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdNumericalInfService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 开闭瓶记录表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Api(tags="耗材/试剂使用统计报表")
@RestController
@RequestMapping("/external/pdNumericalInf")
@Slf4j
public class PdNumericalInfController extends JeecgController<PdNumericalInf, IPdNumericalInfService> {

	@Autowired
	private IPdNumericalInfService pdNumericalInfService;
	@Autowired
	private IPdDepartService pdDepartService;
	/**
	 * 分页列表查询
	 *
	 * @param pdNumericalInf
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdNumericalInf pdNumericalInf,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PdNumericalInf> page = new Page<PdNumericalInf>(pageNo, pageSize);
		if(oConvertUtils.isNotEmpty(pdNumericalInf.getDepartIds()) && !"undefined".equals(pdNumericalInf.getDepartIds())) {
			pdNumericalInf.setDepartIdList(Arrays.asList(pdNumericalInf.getDepartIds().split(",")));
		}else{
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			pdNumericalInf.setDepartIdList(departList);
		}
		IPage<PdNumericalInf> pageList = pdNumericalInfService.selectListByPage(page, pdNumericalInf);
		return Result.ok(pageList);
	}

	/**
	 * 导出excel(月费用统计报表)
	 * @param request
	 * @param pdNumericalInf
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, PdNumericalInf pdNumericalInf) {

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(oConvertUtils.isNotEmpty(pdNumericalInf.getDepartIds()) && !"undefined".equals(pdNumericalInf.getDepartIds())) {
			pdNumericalInf.setDepartIdList(Arrays.asList(pdNumericalInf.getDepartIds().split(",")));
		}else{
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			pdNumericalInf.setDepartIdList(departList);
		}
		List<PdNumericalInf> list = pdNumericalInfService.selectList(pdNumericalInf);//
		String tjType=pdNumericalInf.getTjType();
		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<PdNumericalInfHcExlce> exportHcList =new ArrayList<>();
		List<PdNumericalInfSjExlce> exportSjList =new ArrayList<>();
		if("0".equals(tjType)){ //试剂统计导出
			exportSjList = JSON.parseArray(JSON.toJSONString(list), PdNumericalInfSjExlce.class);
			mv.addObject(NormalExcelConstants.FILE_NAME, "试剂月统计报表");
			mv.addObject(NormalExcelConstants.CLASS, PdNumericalInfSjExlce.class);
			mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("试剂月统计报表", "导出人:" + sysUser.getRealname(), "试剂月统计报表"));
			mv.addObject(NormalExcelConstants.DATA_LIST, exportSjList);
		}else{ //耗材
			exportHcList = JSON.parseArray(JSON.toJSONString(list), PdNumericalInfHcExlce.class);
			mv.addObject(NormalExcelConstants.FILE_NAME, "耗材月统计报表");
			mv.addObject(NormalExcelConstants.CLASS, PdNumericalInfHcExlce.class);
			mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("耗材月统计报表", "导出人:" + sysUser.getRealname(), "耗材月统计报表"));
			mv.addObject(NormalExcelConstants.DATA_LIST, exportHcList);
		}
		return mv;
	}
}
