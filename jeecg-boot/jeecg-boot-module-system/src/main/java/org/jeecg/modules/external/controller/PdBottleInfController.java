package org.jeecg.modules.external.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.external.entity.PdBottleInf;
import org.jeecg.modules.external.service.IPdBottleInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

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
        return super.exportXls(request, pdBottleInf, PdBottleInf.class, "开闭瓶记录表");
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

}
