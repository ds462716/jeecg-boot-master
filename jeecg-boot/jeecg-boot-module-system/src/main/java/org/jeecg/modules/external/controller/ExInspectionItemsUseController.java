package org.jeecg.modules.external.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.external.entity.ExInspectionItemsUse;
import org.jeecg.modules.external.service.IExInspectionItemsUseService;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 检验项目使用表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
@Api(tags="检验项目使用表")
@RestController
@RequestMapping("/external/exInspectionItemsUse")
@Slf4j
public class ExInspectionItemsUseController extends JeecgController<ExInspectionItemsUse, IExInspectionItemsUseService> {
	@Autowired
	private IExInspectionItemsUseService exInspectionItemsUseService;
	@Autowired
	private IPdDepartService pdDepartService;
	/**
	 * 分页列表查询
	 *
	 * @param exInspectionItemsUse
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ExInspectionItemsUse exInspectionItemsUse,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<ExInspectionItemsUse> page = new Page<ExInspectionItemsUse>(pageNo,pageSize);

		if(oConvertUtils.isNotEmpty(exInspectionItemsUse.getDepartIds()) && !"undefined".equals(exInspectionItemsUse.getDepartIds())) {
			exInspectionItemsUse.setDepartIdList(Arrays.asList(exInspectionItemsUse.getDepartIds().split(",")));
		}else{
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			exInspectionItemsUse.setDepartIdList(departList);
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		exInspectionItemsUse.setDepartParentId(sysUser.getDepartParentId());
		IPage<ExInspectionItemsUse> pageList =exInspectionItemsUseService.selectList(page,exInspectionItemsUse);//
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param exInspectionItemsUse
	 * @return
	 */
	@AutoLog(value = "检验项目使用表-添加")
	@ApiOperation(value="检验项目使用表-添加", notes="检验项目使用表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ExInspectionItemsUse exInspectionItemsUse) {
		exInspectionItemsUseService.save(exInspectionItemsUse);
		return Result.ok("添加成功！");
	}
	 @AutoLog(value = "检验项目使用表-添加")
	 @ApiOperation(value="检验项目使用表-添加", notes="检验项目使用表-添加")
	 @PostMapping(value = "/submit")
	 public Result<?> submit(@RequestBody ExInspectionItemsUse exInspectionItemsUse) {
		 exInspectionItemsUseService.submit(exInspectionItemsUse);
		 return Result.ok("添加成功！");
	 }

	 @GetMapping(value = "/initModal")
	 public Result<?> initModal(@RequestParam(name = "id") String id, HttpServletRequest req) {
		 ExInspectionItemsUse exInspectionItemsUse = exInspectionItemsUseService.initOutModal(id);
		 return Result.ok(exInspectionItemsUse);
	 }

	
	/**
	 *  编辑
	 *
	 * @param exInspectionItemsUse
	 * @return
	 */
	@AutoLog(value = "检验项目使用表-编辑")
	@ApiOperation(value="检验项目使用表-编辑", notes="检验项目使用表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ExInspectionItemsUse exInspectionItemsUse) {
		exInspectionItemsUseService.updateById(exInspectionItemsUse);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检验项目使用表-通过id删除")
	@ApiOperation(value="检验项目使用表-通过id删除", notes="检验项目使用表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		exInspectionItemsUseService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "检验项目使用表-批量删除")
	@ApiOperation(value="检验项目使用表-批量删除", notes="检验项目使用表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.exInspectionItemsUseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检验项目使用表-通过id查询")
	@ApiOperation(value="检验项目使用表-通过id查询", notes="检验项目使用表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ExInspectionItemsUse exInspectionItemsUse = exInspectionItemsUseService.getById(id);
		if(exInspectionItemsUse==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(exInspectionItemsUse);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param exInspectionItemsUse
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExInspectionItemsUse exInspectionItemsUse) {
        return super.exportXls(request, exInspectionItemsUse, ExInspectionItemsUse.class, "检验项目使用表");
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
        return super.importExcel(request, response, ExInspectionItemsUse.class);
    }

}
