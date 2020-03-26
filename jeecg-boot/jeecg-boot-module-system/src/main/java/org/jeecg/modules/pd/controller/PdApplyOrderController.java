package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.message.util.PushMsgUtil;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecg.modules.pd.service.IPdApplyDetailService;
import org.jeecg.modules.pd.service.IPdApplyOrderService;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdApplyOrderPage;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysPermissionService;
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
import java.util.*;

 /**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdApplyOrder")
@Slf4j
public class PdApplyOrderController {
	@Autowired
	private IPdApplyOrderService pdApplyOrderService;
	@Autowired
	private IPdApplyDetailService pdApplyDetailService;
	 @Autowired
	 private ISysDepartService sysDepartService;
	 @Autowired
	 private PushMsgUtil pushMsgUtil;
	 @Autowired
	 private IPdDepartService pdDepartService;
	 @Autowired
	 private IPdProductStockTotalService pdProductStockTotalService;
	 @Autowired
	 private ISysPermissionService sysPermissionService;
	/**
	 * 分页列表查询
	 *
	 * @param pdApplyOrderPage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdApplyOrderPage pdApplyOrderPage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PdApplyOrder> page = new Page<PdApplyOrder>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdApplyOrderPage.setDepartId(sysUser.getCurrentDepartId());
		pdApplyOrderPage.setDepartParentId(sysUser.getDepartParentId());
		IPage<PdApplyOrder> pageList = pdApplyOrderService.selectList(page, pdApplyOrderPage);
		return Result.ok(pageList);
	}
	 /**
	  * 分页列表查询(审核页面)
	  *
	  * @param pdApplyOrderPage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/auditList")
	 public Result<?> queryPageAuditList(PdApplyOrderPage pdApplyOrderPage,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Page<PdApplyOrder> page = new Page<PdApplyOrder>(pageNo, pageSize);
		 List<String> list=new ArrayList<>();
		 list.add(PdConstant.AUDIT_STATE_1);//待审核
		 list.add(PdConstant.AUDIT_STATE_2);//审核通过
		 list.add(PdConstant.AUDIT_STATE_3);//已驳回
		 pdApplyOrderPage.setAuditStatusList(list);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 SysDepart sysDepart=sysDepartService.queryDepartByOrgCode(sysUser.getOrgCode());

		 if(StringUtils.isEmpty(pdApplyOrderPage.getDepartId())){
			 //查询科室下所有下级科室的ID
			 SysDepart depart=new SysDepart();
			 List<String> departList=pdDepartService.selectListDepart(depart);
			 pdApplyOrderPage.setDepartIdList(departList);
		 }
		 pdApplyOrderPage.setOutDepartId(sysDepart.getId());
		 pdApplyOrderPage.setDepartParentId(sysUser.getDepartParentId());
		 IPage<PdApplyOrder> pageList = pdApplyOrderService.selectList(page, pdApplyOrderPage);
		 return Result.ok(pageList);
	 }

	 /**
	  * 新增初始化操作
	  *
	  * @return
	  */
	 @GetMapping(value = "/applyInfo")
	 public Result<PdApplyOrderPage> purchaseInfo() {
		 Result<PdApplyOrderPage> result = new Result<>();
		 PdApplyOrderPage pdApplyOrder=new PdApplyOrderPage();
		 String applyNo= UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_SL);
		 pdApplyOrder.setApplyNo(applyNo);
		 pdApplyOrder.setApplyDate(new Date());
		 pdApplyOrder.setTotalNum(0.00);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 SysDepart sysDepart=sysDepartService.queryDepartByOrgCode(sysUser.getOrgCode());
		 pdApplyOrder.setOutDepartId(sysDepart.getParentId());
		 pdApplyOrder.setApplyBy(sysUser.getId());
		 pdApplyOrder.setRealName(sysUser.getRealname());
		 pdApplyOrder.setDepartId(sysDepart.getId());
		 pdApplyOrder.setDeptName(sysDepart.getDepartName());
		 pdApplyOrder.setSubmitStatus(PdConstant.SUBMIT_STATE_1);
		 //pdApplyOrder.setAuditStatus(PdConstant.AUDIT_STATE_1);
		 result.setResult(pdApplyOrder);
		 result.setSuccess(true);
		 return result;
	 }

	/**
	 *   添加
	 *
	 * @param pdApplyOrderPage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdApplyOrderPage pdApplyOrderPage) {
		PdApplyOrder pdApplyOrder = new PdApplyOrder();
		BeanUtils.copyProperties(pdApplyOrderPage, pdApplyOrder);
		//pdApplyOrder.setAuditStatus(PdConstant.AUDIT_STATE_1);//审核状态  1：待审核
		pdApplyOrderService.saveMain(pdApplyOrder, pdApplyOrderPage.getPdApplyDetailList());
		if (pdApplyOrderPage.getSubmitStatus().equals(PdConstant.SUBMIT_STATE_2)) {//如果是已提交
			this.sendMsg(pdApplyOrderPage);//消息推送
		}
		return Result.ok("操作成功！");
	}

	/**
	 *  编辑
	 *
	 * @param pdApplyOrderPage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdApplyOrderPage pdApplyOrderPage) {
		PdApplyOrder pdApplyOrder = new PdApplyOrder();
		BeanUtils.copyProperties(pdApplyOrderPage, pdApplyOrder);
		PdApplyOrder pdApplyOrderEntity = pdApplyOrderService.getById(pdApplyOrder.getId());
		if(pdApplyOrderEntity==null) {
			return Result.error("未找到对应数据");
		}
		String applyStatus=pdApplyOrder.getAuditStatus();//审核状态
		if(StringUtils.isNotEmpty(applyStatus)) {
			if ((PdConstant.AUDIT_STATE_2).equals(applyStatus) || (PdConstant.AUDIT_STATE_3).equals(applyStatus)) {
				LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
				//判断申领数量不能大于出库科室的当前库存数量；
				Integer code = 200;
				Result result = null;
				if ((PdConstant.AUDIT_STATE_2).equals(applyStatus)) { //如果是审核通过，就查询当前科室下的
					String departId = sysUser.getCurrentDepartId();
					result = this.queryStock(departId, pdApplyOrderPage.getPdApplyDetailList());
					code = result.getCode();
				} else if ((PdConstant.AUDIT_STATE_1).equals(applyStatus)) {//如果是申领科室，就查询上级科室下的库存
					SysDepart sysDepart = sysDepartService.queryDepartByOrgCode(sysUser.getOrgCode());
					result = this.queryStock(sysDepart.getParentId(), pdApplyOrderPage.getPdApplyDetailList());
					code = result.getCode();
				}
				if (code != 200) {
					return Result.error(result.getMessage() + "库存数量不足");
				}
				pdApplyOrder.setAuditBy(sysUser.getId());
				pdApplyOrder.setAuditDate(new Date());
			}
		}
		pdApplyOrderService.updateMain(pdApplyOrder, pdApplyOrderPage.getPdApplyDetailList());
		if(StringUtils.isNotEmpty(applyStatus)) {
		if (PdConstant.AUDIT_STATE_1.equals(applyStatus) && pdApplyOrderPage.getSubmitStatus().equals(PdConstant.SUBMIT_STATE_2)) {//如果是已提交
			this.sendMsg(pdApplyOrderPage);//消息推送
		}
		}
		return Result.ok("操作成功!");
	}


	/*判断出库科室库存*/
	public Result<?> queryStock(String departId,List<PdApplyDetail> pdApplyDetailList){
		String prodNames="";
		if(pdApplyDetailList!=null && pdApplyDetailList.size()>0) {
			for(PdApplyDetail entity:pdApplyDetailList) {
				Double applyNum=entity.getApplyNum();
				PdProductStockTotalPage stockTotalPage=new PdProductStockTotalPage();
				stockTotalPage.setDepartId(departId);//上级科室ID;
				stockTotalPage.setProductId(entity.getProductId());
				List<PdProductStockTotalPage> aList = pdProductStockTotalService.findListForQuery(stockTotalPage);
				if(CollectionUtils.isNotEmpty(aList)){
					Double stockNum= aList.get(0).getStockNum();
					if(applyNum>stockNum){
						prodNames+=aList.get(0).getProductName();
 					}
				}
			}
		}
		 if(StringUtils.isNotEmpty(prodNames)){
			return	Result.error(prodNames);

		}
		return Result.ok(null);

	}


	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdApplyOrderService.removeById(id);
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
		this.pdApplyOrderService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdApplyOrder pdApplyOrder = pdApplyOrderService.getById(id);
		if(pdApplyOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdApplyOrder);

	}
	
	 /**
	  * 通过申领单号查询明细表
	  *
	  * @param applyNo
	  * @return
	  */
	 @GetMapping(value = "/queryApplyDetail")
	 public Result<?> queryApplyDetail(@RequestParam(name="applyNo",required=true) String applyNo) {
		 List<PdApplyDetail> pdApplyDetailList = pdApplyDetailService.selectByApplyNo(applyNo);
		 return Result.ok(pdApplyDetailList);
	 }

    /**
	 * 导出excel
	 *
	 * @param request
	 * @param pdApplyOrder
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, PdApplyOrder pdApplyOrder) {
		// Step.1 组装查询条件查询数据
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		//Step.2 获取导出数据
		List<PdApplyDetail> pdApplyDetailList = pdApplyDetailService.selectByApplyNo(pdApplyOrder.getApplyNo());
		// Step.3 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "申领明细产品列表");
		mv.addObject(NormalExcelConstants.CLASS, PdApplyDetail.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("申领明细列表数据", "导出人:"+sysUser.getRealname(), "申领产品数据"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pdApplyDetailList);
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
              List<PdApplyOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PdApplyOrderPage.class, params);
              for (PdApplyOrderPage page : list) {
                  PdApplyOrder po = new PdApplyOrder();
                  BeanUtils.copyProperties(page, po);
                  pdApplyOrderService.saveMain(po, page.getPdApplyDetailList());
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
	 /**
	  * 申领订单选择框
	  *
	  * @param pdApplyOrderPage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/chooseApplyOrderList")
	 public Result<?> chooseApplyOrderList(PdApplyOrderPage pdApplyOrderPage,
											  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
											  HttpServletRequest req) {
		 Page<PdApplyOrderPage> page = new Page<PdApplyOrderPage>(pageNo, pageSize);
		 IPage<PdApplyOrderPage> pageList = pdApplyOrderService.chooseApplyOrderList(page, pdApplyOrderPage);
		 return Result.ok(pageList);
	 }

	 /**
	  * 消息推送
	  * @param pdApplyOrderPage
	  * @return
	  */
	 public boolean sendMsg(PdApplyOrderPage pdApplyOrderPage) {
		 Map<String, Object> map = new HashMap<>();
		 //获取上级科室下的人员Id;
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 SysDepart sysDepart =  sysDepartService.queryDepartByOrgCode(sysUser.getOrgCode());
		 String departId=sysDepart.getParentId();
		 List<String> userIdList = pdDepartService.findDepartUserIds(departId);

		 String url = "";
		 QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<SysPermission>();
		 queryWrapper.eq("name",PdConstant.AUDIT_MENU_7);
		 List<SysPermission> permissionList = sysPermissionService.list(queryWrapper);
		 if(CollectionUtils.isNotEmpty(permissionList)){
			 url = permissionList.get(0).getUrl();
		 }
		 if (CollectionUtils.isNotEmpty(userIdList)) {
			 String userIds = String.join(",", userIdList);
			 Map<String, String> strMap = new HashMap<>();
			 //模板注入参数
			 strMap.put("userName", sysUser.getRealname());
			 strMap.put("applyNo", pdApplyOrderPage.getApplyNo());
			 strMap.put("url", url);
			 map.put("map", strMap);
			 //需要发送消息的用户id
			 map.put("userIds", userIds + ",");
			 //短信模板标识
			 map.put("templateCode", PdConstant.APPLY_SUBMIT_MSG);
			 return pushMsgUtil.newSendMessage(map);
		 }
		 return false;
	 }
}
