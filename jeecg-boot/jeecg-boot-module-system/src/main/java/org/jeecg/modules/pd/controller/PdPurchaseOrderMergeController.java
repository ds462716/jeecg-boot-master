package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMerge;
import org.jeecg.modules.pd.service.IPdPurchaseOrderMergeService;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 合并申购单表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdPurchaseOrderMerge")
@Slf4j
public class PdPurchaseOrderMergeController extends JeecgController<PdPurchaseOrderMerge, IPdPurchaseOrderMergeService> {
	@Autowired
	private IPdPurchaseOrderMergeService pdPurchaseOrderMergeService;
	@Autowired
	private IPdPurchaseOrderService pdPurchaseOrderService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdPurchaseOrderMerge
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdPurchaseOrderMerge pdPurchaseOrderMerge,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		//QueryWrapper<PdPurchaseOrderMerge> queryWrapper = QueryGenerator.initQueryWrapper(pdPurchaseOrderMerge, req.getParameterMap());
		//Page<PdPurchaseOrderMerge> page = new Page<PdPurchaseOrderMerge>(pageNo, pageSize);
		//IPage<PdPurchaseOrderMerge> pageList = pdPurchaseOrderMergeService.page(page, queryWrapper);
		Page<PdPurchaseOrderMerge> page = new Page<PdPurchaseOrderMerge>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdPurchaseOrderMerge.setDepartParentId(sysUser.getDepartParentId());
		page = pdPurchaseOrderMergeService.selectList(page, pdPurchaseOrderMerge);
		return Result.ok(page);
	}
	
	/**
	 *   添加
	 *
	 * @param pdPurchaseOrderMerge
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdPurchaseOrderMerge pdPurchaseOrderMerge) {
		pdPurchaseOrderMergeService.save(pdPurchaseOrderMerge);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  采购审核
	 *
	 * @param pdPurchaseOrderMerge
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdPurchaseOrderMerge pdPurchaseOrderMerge) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdPurchaseOrderMergeService.submitOrderInfo(pdPurchaseOrderMerge,sysUser);
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
		pdPurchaseOrderMergeService.removeById(id);
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
		this.pdPurchaseOrderMergeService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdPurchaseOrderMerge pdPurchaseOrderMerge = pdPurchaseOrderMergeService.getById(id);
		if(pdPurchaseOrderMerge==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdPurchaseOrderMerge);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdPurchaseOrderMerge
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdPurchaseOrderMerge pdPurchaseOrderMerge) {
        return super.exportXls(request, pdPurchaseOrderMerge, PdPurchaseOrderMerge.class, "合并申购单表");
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
        return super.importExcel(request, response, PdPurchaseOrderMerge.class);
    }



	/**
	 * 采购订单选择框
	 *
	 * @param pdPurchaseOrderPage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/choosePurchaseOrderList")
	public Result<?> choosePurchaseOrderList(PdPurchaseOrderPage pdPurchaseOrderPage,
											 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
											 HttpServletRequest req) {
		Page<PdPurchaseOrderMerge> page = new Page<PdPurchaseOrderMerge>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdPurchaseOrderPage.setDepartParentId(sysUser.getDepartParentId());
		IPage<PdPurchaseOrderMerge> pageList = pdPurchaseOrderService.choosePurchaseOrderList(page, pdPurchaseOrderPage);
		return Result.ok(pageList);
	}
}
