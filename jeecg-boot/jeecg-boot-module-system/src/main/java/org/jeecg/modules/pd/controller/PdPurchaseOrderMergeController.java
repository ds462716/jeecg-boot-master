package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMerge;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMergeDetail;
import org.jeecg.modules.pd.service.IPdPurchaseOrderMergeDetailService;
import org.jeecg.modules.pd.service.IPdPurchaseOrderMergeService;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

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
	private IPdPurchaseOrderMergeDetailService pdPurchaseOrderMergeDetailService;
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
		Page<PdPurchaseOrderMerge> page = new Page<PdPurchaseOrderMerge>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdPurchaseOrderMerge.setDepartParentId(sysUser.getDepartParentId());
		IPage<PdPurchaseOrderMerge> pageList = pdPurchaseOrderMergeService.selectList(page, pdPurchaseOrderMerge);//
		return Result.ok(pageList);
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
		return Result.ok("操作成功!");
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
	 * 查询明细表
	 *
	 * @param orderMergeDetail
	 * @return
	 */
	@GetMapping(value = "/queryPdPurchaseMergeDetail")
	public Result<?> queryPdPurchaseDetail(PdPurchaseOrderMergeDetail orderMergeDetail) {
		List<PdPurchaseOrderMergeDetail> pdPurchaseMergeDetailList = pdPurchaseOrderMergeDetailService.queryPdPurchaseMergeDetail(orderMergeDetail);
		return Result.ok(pdPurchaseMergeDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdPurchaseOrderMerge
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdPurchaseOrderMerge pdPurchaseOrderMerge) {
			// Step.1 组装查询条件查询数据
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			//Step.2 获取导出数据
		PdPurchaseOrderMergeDetail orderMergeDetail=new PdPurchaseOrderMergeDetail();
		orderMergeDetail.setMergeOrderNo(pdPurchaseOrderMerge.getMergeOrderNo());
		List<PdPurchaseOrderMergeDetail> i_orderMergeDetail = pdPurchaseOrderMergeDetailService.queryPdPurchaseMergeDetail(orderMergeDetail);
			// Step.3 AutoPoi 导出Excel
			ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
			mv.addObject(NormalExcelConstants.FILE_NAME, "申购订单明细列表");
			mv.addObject(NormalExcelConstants.CLASS, PdPurchaseOrderMergeDetail.class);
			mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("申购订单产品列表数据", "导出人:" + sysUser.getRealname(), "申购订单产品数据"));
			mv.addObject(NormalExcelConstants.DATA_LIST, i_orderMergeDetail);
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

	/**
	 * 查询合并订单明细（选择器用）
	 * @param pdPurchaseOrderPage
	 * @param req
	 * @return
	 */
	/*@GetMapping(value = "/choosePurchaseOrderMergeDetailList")
	public Result<?> choosePurchaseOrderMergeDetailList(PdPurchaseOrderPage pdPurchaseOrderPage, HttpServletRequest req) {
		List<PdPurchaseOrderMergeDetail> list = pdPurchaseOrderMergeDetailService.queryPdPurchaseMergeDetail(orderMergeDetail);
		//List<PdProductPage> list = pdPurchaseOrderService.choosePurchaseOrderDetailList(pdPurchaseOrderPage);
		return Result.ok(list);
	}*/
}
