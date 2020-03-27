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
import org.jeecg.modules.pd.entity.PdAllocationDetail;
import org.jeecg.modules.pd.entity.PdAllocationRecord;
import org.jeecg.modules.pd.service.IPdAllocationDetailService;
import org.jeecg.modules.pd.service.IPdAllocationRecordService;
import org.jeecg.modules.pd.service.IPdDepartService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.util.UUIDUtil;
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
 * @Description: 调拨记录表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdAllocationRecord")
@Slf4j
public class PdAllocationRecordController {
	@Autowired
	private IPdAllocationRecordService pdAllocationRecordService;
	@Autowired
	private IPdAllocationDetailService pdAllocationDetailService;
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
	 * @param pdAllocationRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdAllocationRecord pdAllocationRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
 		Page<PdAllocationRecord> page = new Page<PdAllocationRecord>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(StringUtils.isEmpty(pdAllocationRecord.getOutDeptId())){
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			pdAllocationRecord.setOutDeptIdList(departList);
		}
 		pdAllocationRecord.setDepartParentId(sysUser.getDepartParentId());
		IPage<PdAllocationRecord> pageList =pdAllocationRecordService.selectList(page, pdAllocationRecord);
		return Result.ok(pageList);
	}


	 /**
	  * 分页列表查询(审核页面)
	  *
	  * @param allocationRecord
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/auditList")
	 public Result<?> queryPageAuditList(PdAllocationRecord allocationRecord,
										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {
		 Page<PdAllocationRecord> page = new Page<PdAllocationRecord>(pageNo, pageSize);
		 List<String> list=new ArrayList<>();
		 list.add(PdConstant.AUDIT_STATE_1);//待审核
		 list.add(PdConstant.AUDIT_STATE_2);//审核通过
		 list.add(PdConstant.AUDIT_STATE_3);//已驳回
		 allocationRecord.setAuditStatusList(list);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 if(StringUtils.isEmpty(allocationRecord.getInDeptId())){
			 //查询科室下所有下级科室的ID
			 SysDepart sysDepart=new SysDepart();
			 List<String> departList=pdDepartService.selectListDepart(sysDepart);
			 allocationRecord.setInDeptIdList(departList);
		 }
		 allocationRecord.setDepartParentId(sysUser.getDepartParentId());
		 IPage<PdAllocationRecord> pageList = pdAllocationRecordService.selectList(page, allocationRecord);
		 return Result.ok(pageList);
	 }


	 /**
	  * 新增初始化操作
	  *
	  * @return
	  */
	 @GetMapping(value = "/allocationInfo")
	 public Result<PdAllocationRecord> purchaseInfo() {
		 Result<PdAllocationRecord> result = new Result<>();
		 PdAllocationRecord allocationRecord=new PdAllocationRecord();
		 String allocationNo= UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_DB);
		 allocationRecord.setAllocationNo(allocationNo);
		 allocationRecord.setAllocationDate(new Date());
		 allocationRecord.setTotalNum(0.00);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 SysDepart sysDepart=sysDepartService.getDepartByOrgCode(sysUser.getOrgCode());
		 allocationRecord.setAllocationBy(sysUser.getId());
		 allocationRecord.setRealName(sysUser.getRealname());
		 allocationRecord.setInDeptId(sysDepart.getId());//入库科室
		 allocationRecord.setInDeptName(sysDepart.getDepartName());
		 allocationRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_1);
		 result.setResult(allocationRecord);
		 result.setSuccess(true);
		 return result;
	 }

	/**
	 *   添加
	 *
	 * @param pdAllocationRecord
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdAllocationRecord pdAllocationRecord) {
		pdAllocationRecordService.saveMain(pdAllocationRecord, pdAllocationRecord.getPdAllocationDetailList());
		if (pdAllocationRecord.getSubmitStatus().equals(PdConstant.SUBMIT_STATE_2)) {//如果是已提交
			this.sendMsg(pdAllocationRecord);//消息推送
		}
		return Result.ok("操作成功！");
	}

	/**
	 *  编辑
	 *
	 * @param pdAllocationRecord
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdAllocationRecord pdAllocationRecord) {
		PdAllocationRecord pdAllocationRecordEntity = pdAllocationRecordService.getById(pdAllocationRecord.getId());
		if(pdAllocationRecordEntity==null) {
			return Result.error("未找到对应数据");
		}
		String auditStatus=pdAllocationRecord.getAuditStatus();//审核状态
		if(StringUtils.isNotEmpty(auditStatus)) {
			if ((PdConstant.AUDIT_STATE_2).equals(auditStatus) || (PdConstant.AUDIT_STATE_3).equals(auditStatus)) {
				LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
				if ((PdConstant.AUDIT_STATE_2).equals(auditStatus)) {
					//判断调拨数量不能大于出库科室的当前库存数量；
					boolean bool = true;
					String prodNames = "";
					List<PdAllocationDetail> list = pdAllocationRecord.getPdAllocationDetailList();
					if (list != null && list.size() > 0) {
						for (PdAllocationDetail entity : list) {
							Double allocationNum = entity.getAllocationNum();
							PdProductStockTotalPage stockTotalPage = new PdProductStockTotalPage();
							stockTotalPage.setDepartId(pdAllocationRecord.getOutDeptId());//出库科室ID;
							stockTotalPage.setProductId(entity.getProductId());
							List<PdProductStockTotalPage> aList = pdProductStockTotalService.findListForQuery(stockTotalPage);
							if (CollectionUtils.isNotEmpty(aList)) {
								Double stockNum = aList.get(0).getStockNum();
								if (allocationNum > stockNum) {
									prodNames += aList.get(0).getProductName();
									bool = false;
								}
							}
						}
					}
					if (!bool) {
						return Result.error(prodNames + "库存数量不足");
					}
				}
				pdAllocationRecord.setAuditBy(sysUser.getId());
				pdAllocationRecord.setAuditDate(new Date());
			}
		}
		pdAllocationRecordService.updateMain(pdAllocationRecord, pdAllocationRecord.getPdAllocationDetailList());
		if(StringUtils.isNotEmpty(auditStatus)) {
			if (PdConstant.AUDIT_STATE_1.equals(auditStatus) && pdAllocationRecord.getSubmitStatus().equals(PdConstant.SUBMIT_STATE_2)) {//如果是已提交
				this.sendMsg(pdAllocationRecord);//消息推送
			}
		}
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
		 pdAllocationRecordService.removeById(id);
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
		 this.pdAllocationRecordService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdAllocationRecord pdAllocationRecord = pdAllocationRecordService.getById(id);
		if(pdAllocationRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdAllocationRecord);

	}
	

	 /**
	  * 通过调拨单号查询明细表
	  *
	  * @param allocationNo
	  * @return
	  */
	 @GetMapping(value = "/queryPdAllocationDetailList")
	 public Result<?> queryPdAllocationDetailList(@RequestParam(name="allocationNo",required=true) String allocationNo) {
		 List<PdAllocationDetail> pdAllocationDetailList = pdAllocationDetailService.selectByAllocationNo(allocationNo);
		 return Result.ok(pdAllocationDetailList);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param pdAllocationRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdAllocationRecord pdAllocationRecord) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		//Step.1 获取导出数据
		List<PdAllocationDetail> pdAllocationDetailList = pdAllocationDetailService.selectByAllocationNo(pdAllocationRecord.getAllocationNo());
		// Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "调拨产品列表");
		mv.addObject(NormalExcelConstants.CLASS, PdAllocationDetail.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("调拨产品列表数据", "导出人:" + sysUser.getRealname(), "调拨产品数据"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pdAllocationDetailList);
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
              List<PdAllocationRecord> list = ExcelImportUtil.importExcel(file.getInputStream(), PdAllocationRecord.class, params);
              for (PdAllocationRecord page : list) {
                  PdAllocationRecord po = new PdAllocationRecord();
                  BeanUtils.copyProperties(page, po);
                  pdAllocationRecordService.saveMain(po, page.getPdAllocationDetailList());
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
	  * 调拨订单选择框
	  *
	  * @param allocationRecord
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/chooseAllocationList")
	 public Result<?> chooseAllocationList(PdAllocationRecord allocationRecord,
										   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
										   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
										   HttpServletRequest req) {
		 Page<PdAllocationRecord> page = new Page<PdAllocationRecord>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 allocationRecord.setDepartParentId(sysUser.getDepartParentId());
		 IPage<PdAllocationRecord> pageList = pdAllocationRecordService.chooseAllocationList(page, allocationRecord);
		 return Result.ok(pageList);
	 }


	 /**
	  * 消息推送
	  * @param allocationRecord
	  * @return
	  */
	 public boolean sendMsg(PdAllocationRecord allocationRecord) {
		 Map<String, Object> map = new HashMap<>();
		 //获取出库科室下的人员Id;
		 String departId=allocationRecord.getOutDeptId();
		 List<String> userIdList = pdDepartService.findDepartUserIds(departId);
		 String url = "";
		 QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<SysPermission>();
		 queryWrapper.eq("name",PdConstant.AUDIT_MENU_8);
		 List<SysPermission> permissionList = sysPermissionService.list(queryWrapper);
		 if(CollectionUtils.isNotEmpty(permissionList)){
			 url = permissionList.get(0).getUrl();
		 }
		 if (CollectionUtils.isNotEmpty(userIdList)) {
			 String userIds = String.join(",", userIdList);
			 Map<String, String> strMap = new HashMap<>();
			 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			 //模板注入参数
			 strMap.put("userName", sysUser.getRealname());
			 strMap.put("allocationNo", allocationRecord.getAllocationNo());
			 strMap.put("url", url);
			 map.put("map", strMap);
			 //需要发送消息的用户id
			 map.put("userIds", userIds + ",");
			 //短信模板标识
			 map.put("templateCode", PdConstant.ALLOCATION_MSG);
			 return pushMsgUtil.newSendMessage(map);
		 }
		 return false;
	 }

 }
