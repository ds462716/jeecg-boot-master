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
import org.jeecg.modules.external.entity.ExLabInstrInf;
import org.jeecg.modules.external.service.IExLabInstrInfService;
import org.jeecg.modules.pd.service.IHisChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 设备仪器表
 * @Author: zxh
 * @Date:   2020-06-09
 * @Version: V1.0
 */
@Api(tags="ex_lab_instr_inf")
@RestController
@RequestMapping("/ex/exLabInstrInf")
@Slf4j
public class ExLabInstrInfController extends JeecgController<ExLabInstrInf, IExLabInstrInfService> {
	@Autowired
	private IExLabInstrInfService exLabInstrInfService;
	 @Autowired
	 private IHisChargeService hisChargeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param exLabInstrInf
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "ex_lab_instr_inf-分页列表查询")
	@ApiOperation(value="ex_lab_instr_inf-分页列表查询", notes="ex_lab_instr_inf-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ExLabInstrInf exLabInstrInf,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<ExLabInstrInf> page = new Page<ExLabInstrInf>(pageNo, pageSize);
		IPage<ExLabInstrInf> pageList = exLabInstrInfService.selectList(page, exLabInstrInf);
		return Result.ok(pageList);
	}

     /**
      * 不分页列表查询
      *
      * @return
      */
     @GetMapping(value = "/getExLabInstrInf")
     public Result<List<ExLabInstrInf>> getExLabInstrInf(ExLabInstrInf exLabInstrInf) {
         long start = System.currentTimeMillis();
         Result<List<ExLabInstrInf>> result = new Result<>();
         try {
             LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			 exLabInstrInf.setTestDepartId(sysUser.getCurrentDepartId());
             exLabInstrInf.setDepartParentId(sysUser.getDepartParentId());
             List<ExLabInstrInf> list = exLabInstrInfService.getExLabInstrInf(exLabInstrInf);
             result.setResult(list);
             result.setSuccess(true);
         }catch(Exception e){
             log.error(e.getMessage(), e);
         }
         log.info("======获取产品数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
         return result;
     }

	/**
	 *   添加
	 *
	 * @param exLabInstrInf
	 * @return
	 */
	@AutoLog(value = "ex_lab_instr_inf-添加")
	@ApiOperation(value="ex_lab_instr_inf-添加", notes="ex_lab_instr_inf-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ExLabInstrInf exLabInstrInf) {
		exLabInstrInfService.save(exLabInstrInf);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param exLabInstrInf
	 * @return
	 */
	@AutoLog(value = "ex_lab_instr_inf-编辑")
	@ApiOperation(value="ex_lab_instr_inf-编辑", notes="ex_lab_instr_inf-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ExLabInstrInf exLabInstrInf) {
		exLabInstrInfService.updateById(exLabInstrInf);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ex_lab_instr_inf-通过id删除")
	@ApiOperation(value="ex_lab_instr_inf-通过id删除", notes="ex_lab_instr_inf-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		exLabInstrInfService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ex_lab_instr_inf-批量删除")
	@ApiOperation(value="ex_lab_instr_inf-批量删除", notes="ex_lab_instr_inf-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.exLabInstrInfService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ex_lab_instr_inf-通过id查询")
	@ApiOperation(value="ex_lab_instr_inf-通过id查询", notes="ex_lab_instr_inf-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ExLabInstrInf exLabInstrInf = exLabInstrInfService.getById(id);
		if(exLabInstrInf==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(exLabInstrInf);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param exLabInstrInf
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExLabInstrInf exLabInstrInf) {
        return super.exportXls(request, exLabInstrInf, ExLabInstrInf.class, "ex_lab_instr_inf");
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
        return super.importExcel(request, response, ExLabInstrInf.class);
    }


	 /**
	  *   同步更新设备仪器信息
	  * @param exLabInstrInf
	  * @return
	  */
	 @PostMapping(value = "/synUpdateInstrInf")
	 public Result<?> synUpdateDeptOrUser(@RequestBody ExLabInstrInf exLabInstrInf) {
		  List<ExLabInstrInf> labInstrInfList= hisChargeService.selectExLabInstrInf();//查询HIS科室信息
		   exLabInstrInfService.synUpdateInstrInf(labInstrInfList);
		 return Result.ok("操作成功！");
	 }

}
