package org.jeecg.modules.pd.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.jeecg.modules.pd.service.IPdPurchaseDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 申购订单主表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdPurchaseOrder")
@Slf4j
public class PdPurchaseOrderController {
	@Autowired
	private IPdPurchaseOrderService pdPurchaseOrderService;
	@Autowired
	private IPdPurchaseDetailService pdPurchaseDetailService;
	 @Autowired
	 private ISysDepartService sysDepartService;

	/**
	 * 分页列表查询
	 *
	 * @param pdPurchaseOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdPurchaseOrder pdPurchaseOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PdPurchaseOrder> page = new Page<PdPurchaseOrder>(pageNo, pageSize);
		page = pdPurchaseOrderService.selectList(page,pdPurchaseOrder);
		return Result.ok(page);
	}



	 /**
	  * 新增初始化操作
	  *
	  * @return
	  */
	 @GetMapping(value = "/purchaseInfo")
	 public Result<PdPurchaseOrder> purchaseInfo() {
		 Result<PdPurchaseOrder> result = new Result<>();
		 PdPurchaseOrder pdPurchaseOrder=new PdPurchaseOrder();
		 pdPurchaseOrder.setOrderNo("SG"+System.currentTimeMillis());
		 pdPurchaseOrder.setOrderDate(new Date());
		 pdPurchaseOrder.setAmountCount(0);
		 pdPurchaseOrder.setAmountMoney(BigDecimal.ZERO);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 SysDepart sysDepart=sysDepartService.getDepartByOrgCode(sysUser.getOrgCode());
		 pdPurchaseOrder.setStoreroomId(sysDepart.getId());
		 pdPurchaseOrder.setStoreroomName(sysDepart.getDepartName());
		 pdPurchaseOrder.setPurchaseBy(sysUser.getId());
		 pdPurchaseOrder.setPurchaseName(sysUser.getRealname());
		 result.setResult(pdPurchaseOrder);
		 result.setSuccess(true);
		 return result;
	 }



	/**
	 *   添加
	 *
	 * @param pdPurchaseOrderPage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdPurchaseOrderPage pdPurchaseOrderPage) {
		PdPurchaseOrder pdPurchaseOrder = new PdPurchaseOrder();
		BeanUtils.copyProperties(pdPurchaseOrderPage, pdPurchaseOrder);
		pdPurchaseOrder.setOrderStatus("0");//审核状态  0：待审核
		pdPurchaseOrder.setDelFlag("0");
		pdPurchaseOrderService.saveMain(pdPurchaseOrder, pdPurchaseOrderPage.getPdPurchaseDetailList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdPurchaseOrderPage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdPurchaseOrderPage pdPurchaseOrderPage) {
		PdPurchaseOrder pdPurchaseOrder = new PdPurchaseOrder();
		BeanUtils.copyProperties(pdPurchaseOrderPage, pdPurchaseOrder);
		PdPurchaseOrder pdPurchaseOrderEntity = pdPurchaseOrderService.getById(pdPurchaseOrder.getId());
		if(pdPurchaseOrderEntity==null) {
			return Result.error("未找到对应数据");
		}
		pdPurchaseOrderService.updateMain(pdPurchaseOrder, pdPurchaseOrderPage.getPdPurchaseDetailList());
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
		pdPurchaseOrderService.delMain(id);
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
		this.pdPurchaseOrderService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdPurchaseOrder pdPurchaseOrder = pdPurchaseOrderService.getById(id);
		if(pdPurchaseOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdPurchaseOrder);

	}
	
	/**
	 * 通过申购单号查询明细表
	 *
	 * @param orderNo
	 * @return
	 */
	@GetMapping(value = "/queryPdPurchaseDetail")
	public Result<?> queryPdPurchaseDetail(@RequestParam(name="orderNo",required=true) String orderNo) {
		List<PdPurchaseDetail> pdPurchaseDetailList = pdPurchaseDetailService.selectByOrderNo(orderNo);
		return Result.ok(pdPurchaseDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdPurchaseOrder
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdPurchaseOrder pdPurchaseOrder) {
      // Step.1 组装查询条件查询数据
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
      //Step.2 获取导出数据
      List<PdPurchaseDetail> pdPurchaseDetailList = pdPurchaseDetailService.selectByOrderNo(pdPurchaseOrder.getOrderNo());
      // Step.3 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "申购产品列表");
      mv.addObject(NormalExcelConstants.CLASS, PdPurchaseDetail.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("申购产品列表数据", "导出人:"+sysUser.getRealname(), "申购产品数据"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pdPurchaseDetailList);
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<PdPurchaseOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PdPurchaseOrderPage.class, params);
              for (PdPurchaseOrderPage page : list) {
                  PdPurchaseOrder po = new PdPurchaseOrder();
                  BeanUtils.copyProperties(page, po);
                  pdPurchaseOrderService.saveMain(po, page.getPdPurchaseDetailList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

}
