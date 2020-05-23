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
import org.jeecg.modules.external.entity.ExDeductuinDosage;
import org.jeecg.modules.external.service.IExDeductuinDosageService;
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
 * @Description: 试剂用量扣减记录表
 * @Author: jiangxz
 * @Date:   2020-05-22
 * @Version: V1.0
 */
@Api(tags="试剂用量扣减记录表")
@RestController
@RequestMapping("/pd/exDeductuinDosage")
@Slf4j
public class ExDeductuinDosageController extends JeecgController<ExDeductuinDosage, IExDeductuinDosageService> {
	@Autowired
	private IExDeductuinDosageService exDeductuinDosageService;
    @Autowired
    private IPdDepartService pdDepartService;
	/**
	 * 分页列表查询
	 *
	 * @param exDeductuinDosage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ExDeductuinDosage exDeductuinDosage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<ExDeductuinDosage> page = new Page<ExDeductuinDosage>(pageNo, pageSize);
		//IPage<ExDeductuinDosage> pageList = exDeductuinDosageService.page(page, queryWrapper);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		exDeductuinDosage.setDepartParentId(sysUser.getDepartParentId());

        if(oConvertUtils.isNotEmpty(exDeductuinDosage.getDepartIds()) && !"undefined".equals(exDeductuinDosage.getDepartIds())){
            exDeductuinDosage.setDepartIdList(Arrays.asList(exDeductuinDosage.getDepartIds().split(",")));
        }else{
            //查询科室下所有下级科室的ID
            SysDepart sysDepart=new SysDepart();
            List<String> departList=pdDepartService.selectListDepart(sysDepart);
            exDeductuinDosage.setDepartIdList(departList);
        }
		IPage<ExDeductuinDosage> pageList =exDeductuinDosageService.selectList(page,exDeductuinDosage);//
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param exDeductuinDosage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ExDeductuinDosage exDeductuinDosage) {
		exDeductuinDosageService.save(exDeductuinDosage);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param exDeductuinDosage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ExDeductuinDosage exDeductuinDosage) {
		exDeductuinDosageService.updateById(exDeductuinDosage);
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
		exDeductuinDosageService.removeById(id);
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
		this.exDeductuinDosageService.removeByIds(Arrays.asList(ids.split(",")));
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
		ExDeductuinDosage exDeductuinDosage = exDeductuinDosageService.getById(id);
		if(exDeductuinDosage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(exDeductuinDosage);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param exDeductuinDosage
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExDeductuinDosage exDeductuinDosage) {
        return super.exportXls(request, exDeductuinDosage, ExDeductuinDosage.class, "试剂用量扣减记录表");
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
        return super.importExcel(request, response, ExDeductuinDosage.class);
    }

}
