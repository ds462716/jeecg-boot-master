package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.message.util.PushMsgUtil;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdPurchaseDetailService;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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
	 @Autowired
	 private PushMsgUtil pushMsgUtil;
	 @Autowired
	 private IPdDepartService pdDepartService;



	 /**
	  * 分页列表查询
	  *
	  * @param pdPurchaseOrderPage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/list")
	 public Result<?> queryPageList(PdPurchaseOrderPage pdPurchaseOrderPage,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									HttpServletRequest req) {
		 Page<PdPurchaseOrder> page = new Page<PdPurchaseOrder>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdPurchaseOrderPage.setDepartId(sysUser.getCurrentDepartId());
		 pdPurchaseOrderPage.setDepartParentId(sysUser.getDepartParentId());
		 page = pdPurchaseOrderService.selectList(page, pdPurchaseOrderPage);
		 return Result.ok(page);
	 }

	 /**
	  * 分页列表查询(审核页面)
	  *
	  * @param pdPurchaseOrderPage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/auditList")
	 public Result<?> queryPageAuditList(PdPurchaseOrderPage pdPurchaseOrderPage,
										 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
										 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
										 HttpServletRequest req) {
		 Page<PdPurchaseOrder> page = new Page<PdPurchaseOrder>(pageNo, pageSize);
		 List<String> list = new ArrayList<>();
		 list.add(PdConstant.SUBMIT_STATE_2);//已提交
		 list.add(PdConstant.SUBMIT_STATE_3);//已撤回
		 pdPurchaseOrderPage.setSubmitStatusList(list);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdPurchaseOrderPage.setDepartParentId(sysUser.getDepartParentId());
		 page = pdPurchaseOrderService.selectList(page, pdPurchaseOrderPage);
		 return Result.ok(page);
	 }

	 /**
	  * 新增初始化操作
	  *
	  * @return
	  */
	 @GetMapping(value = "/purchaseInfo")
	 public Result<PdPurchaseOrderPage> purchaseInfo() {
		 Result<PdPurchaseOrderPage> result = new Result<>();
		 PdPurchaseOrderPage pdPurchaseOrder = new PdPurchaseOrderPage();
		 String orderNo = UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_SG);
		 pdPurchaseOrder.setOrderNo(orderNo);
		 pdPurchaseOrder.setOrderDate(new Date());
		 pdPurchaseOrder.setTotalNum(0.00);
		 pdPurchaseOrder.setTotalPrice(BigDecimal.ZERO);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 SysDepart sysDepart = sysDepartService.getDepartByOrgCode(sysUser.getOrgCode());
		 pdPurchaseOrder.setDepartId(sysDepart.getId());
		 pdPurchaseOrder.setDeptName(sysDepart.getDepartName());
		 pdPurchaseOrder.setPurchaseBy(sysUser.getId());
		 pdPurchaseOrder.setPurchaseName(sysUser.getRealname());
		 pdPurchaseOrder.setSubmitStatus(PdConstant.SUBMIT_STATE_1);
		 pdPurchaseOrder.setAuditStatus(PdConstant.AUDIT_STATE_1);
		 result.setResult(pdPurchaseOrder);
		 result.setSuccess(true);
		 return result;
	 }

	 /**
	  * 添加
	  *
	  * @param pdPurchaseOrderPage
	  * @return
	  */
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody PdPurchaseOrderPage pdPurchaseOrderPage) {
		 PdPurchaseOrder pdPurchaseOrder = new PdPurchaseOrder();
		 BeanUtils.copyProperties(pdPurchaseOrderPage, pdPurchaseOrder);
		 pdPurchaseOrder.setAuditStatus(PdConstant.AUDIT_STATE_1);//审核状态  1：待审核
		 pdPurchaseOrder.setDelFlag(PdConstant.DEL_FLAG_0);
		 pdPurchaseOrderService.saveMain(pdPurchaseOrder, pdPurchaseOrderPage.getPdPurchaseDetailList());
		 if (pdPurchaseOrderPage.getSubmitStatus().equals(PdConstant.SUBMIT_STATE_2)) {//如果是已提交
			 this.sendMsg(pdPurchaseOrderPage);//消息推送
		 }
		 return Result.ok("添加成功！");
	 }

	 /**
	  * 编辑
	  *
	  * @param pdPurchaseOrderPage
	  * @return
	  */
	 @PutMapping(value = "/edit")
	 public Result<?> edit(@RequestBody PdPurchaseOrderPage pdPurchaseOrderPage) {
		 PdPurchaseOrder pdPurchaseOrder = new PdPurchaseOrder();
		 BeanUtils.copyProperties(pdPurchaseOrderPage, pdPurchaseOrder);
		 PdPurchaseOrder pdPurchaseOrderEntity = pdPurchaseOrderService.getById(pdPurchaseOrder.getId());
		 if (pdPurchaseOrderEntity == null) {
			 return Result.error("未找到对应数据");
		 }
		 String orderStatus = pdPurchaseOrder.getAuditStatus();//审核状态
		 if ((PdConstant.AUDIT_STATE_2).equals(orderStatus) || (PdConstant.AUDIT_STATE_3).equals(orderStatus)) {
			 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			 pdPurchaseOrder.setAuditBy(sysUser.getId());
			 pdPurchaseOrder.setAuditDate(new Date());
		 }
		 pdPurchaseOrderService.updateMain(pdPurchaseOrder, pdPurchaseOrderPage.getPdPurchaseDetailList());
		 if (PdConstant.AUDIT_STATE_1.equals(orderStatus) && pdPurchaseOrderPage.getSubmitStatus().equals(PdConstant.SUBMIT_STATE_2)) {//如果是已提交
			 this.sendMsg(pdPurchaseOrderPage);//消息推送
		 }
		 return Result.ok("编辑成功!");
	 }

	 /**
	  * 通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		 pdPurchaseOrderService.removeById(id);
		 return Result.ok("删除成功!");
	 }

	 /**
	  * 批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		 this.pdPurchaseOrderService.removeByIds(Arrays.asList(ids.split(",")));
		 return Result.ok("批量删除成功！");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/queryById")
	 public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		 PdPurchaseOrder pdPurchaseOrder = pdPurchaseOrderService.getById(id);
		 if (pdPurchaseOrder == null) {
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
	 public Result<?> queryPdPurchaseDetail(@RequestParam(name = "orderNo", required = true) String orderNo) {
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
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("申购产品列表数据", "导出人:" + sysUser.getRealname(), "申购产品数据"));
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
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
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
		 Page<PdPurchaseOrderPage> page = new Page<PdPurchaseOrderPage>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdPurchaseOrderPage.setDepartParentId(sysUser.getDepartParentId());
		 IPage<PdPurchaseOrderPage> pageList = pdPurchaseOrderService.choosePurchaseOrderList(page, pdPurchaseOrderPage);
		 return Result.ok(pageList);
	 }

	 @GetMapping(value = "/choosePurchaseOrderDetailList")
	 public Result<?> choosePurchaseOrderDetailList(PdPurchaseOrderPage pdPurchaseOrderPage, HttpServletRequest req) {
		 List<PdProductPage> list = pdPurchaseOrderService.choosePurchaseOrderDetailList(pdPurchaseOrderPage);
		 return Result.ok(list);
	 }


	 /**
	  * 根据合并订单编号分页查询采购订单
	  *
	  * @param pdPurchaseOrderPage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/mergeList")
	 public Result<?> queryMergeList(PdPurchaseOrderPage pdPurchaseOrderPage,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									HttpServletRequest req) {
		 Page<PdPurchaseOrder> page = new Page<PdPurchaseOrder>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
 		 pdPurchaseOrderPage.setDepartParentId(sysUser.getDepartParentId());
		 page = pdPurchaseOrderService.selectList(page, pdPurchaseOrderPage);
		 return Result.ok(page);
	 }

	 /**
	  * 消息推送
	  * @param purchaseOrderPage
	  * @return
	  */
	 public boolean sendMsg(PdPurchaseOrderPage purchaseOrderPage) {
		 Map<String, Object> map = new HashMap<>();
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 List<String> userIdList =pdDepartService.findMenuUser(sysUser.getId(),PdConstant.AUDIT_MENU_3);
		 if (CollectionUtils.isNotEmpty(userIdList)) {
			 String userIds = String.join(",", userIdList);
			 Map<String, String> strMap = new HashMap<>();
			 //模板注入参数
			 strMap.put("userName", sysUser.getRealname());
			 strMap.put("orderNo", purchaseOrderPage.getOrderNo());
			 map.put("map", strMap);
			 //需要发送消息的用户id
			 map.put("userIds", userIds + ",");
			 //短信模板标识
			 map.put("templateCode", PdConstant.PURCHASE_SUBMIT_MSG);
			 return pushMsgUtil.newSendMessage(map);
		 }
		 return false;
	 }
 }
