package org.jeecg.modules.external.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.external.entity.ExInspectionInf;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.service.IExInspectionInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 试剂用量扣减表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Api(tags="试剂用量扣减表")
@RestController
@RequestMapping("/external/exInspectionInf")
@Slf4j
public class ExInspectionInfController extends JeecgController<ExInspectionInf, IExInspectionInfService> {
	@Autowired
	private IExInspectionInfService exInspectionInfService;
	/**
	 * 分页列表查询
	 *
	 * @param exInspectionInf
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ExInspectionInf exInspectionInf,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(StringUtils.isEmpty(exInspectionInf.getCode())) {
			return Result.error("检验项目代号不能为空");
		}
		exInspectionInf.setDepartParentId(sysUser.getDepartParentId());
		Page<ExInspectionInf> page = new Page<ExInspectionInf>(pageNo, pageSize);
		IPage<ExInspectionInf> pageList =exInspectionInfService.selectList(page, exInspectionInf);
		return Result.ok(pageList);
	}
	

	/**
	 *  编辑
	 *
	 * @param exInspectionItems
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ExInspectionItems exInspectionItems) {
	//	exInspectionItemsService.updateById(exInspectionItems);
		return Result.ok("编辑成功!");
	}
	

	

	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ExInspectionItems exInspectionItems = null;//exInspectionItemsService.getById(id);
		if(exInspectionItems==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(exInspectionItems);
	}
}
