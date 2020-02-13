package org.jeecg.modules.pd.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.service.IPdApplyDetailService;
import org.jeecg.modules.pd.service.IPdApplyOrderService;
import org.jeecg.modules.pd.vo.PdApplyOrderPage;
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
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
	/**
	 * 分页列表查询
	 *
	 * @param pdApplyOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdApplyOrder pdApplyOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PdApplyOrder> page = new Page<PdApplyOrder>(pageNo, pageSize);
		IPage<PdApplyOrder> pageList = pdApplyOrderService.selectList(page, pdApplyOrder);
		return Result.ok(pageList);
	}
	 /**
	  * 分页列表查询(审核页面)
	  *
	  * @param pdApplyOrder
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/auditList")
	 public Result<?> queryPageAuditList(PdApplyOrder pdApplyOrder,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Page<PdApplyOrder> page = new Page<PdApplyOrder>(pageNo, pageSize);
		 pdApplyOrder.setSubmitStart(PdConstant.SUBMIT_STATE_2);// 已提交
		 IPage<PdApplyOrder> pageList = pdApplyOrderService.selectList(page, pdApplyOrder);
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
		 pdApplyOrder.setApplyNo("SL"+System.currentTimeMillis());
		 pdApplyOrder.setApplyDate(new Date());
		 pdApplyOrder.setApplyNum(0.00);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 SysDepart sysDepart=sysDepartService.getDepartByOrgCode(sysUser.getOrgCode());
		 pdApplyOrder.setApplyBy(sysUser.getId());
		 pdApplyOrder.setRealName(sysUser.getRealname());
		 pdApplyOrder.setDeptId(sysDepart.getId());
		 pdApplyOrder.setDeptName(sysDepart.getDepartName());
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
		pdApplyOrder.setApplyStatus(PdConstant.ORDER_STATE_0);//审核状态  0：待审核
		pdApplyOrderService.saveMain(pdApplyOrder, pdApplyOrderPage.getPdApplyDetailList());
		return Result.ok("添加成功！");
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

		String applyStatus=pdApplyOrder.getApplyStatus();//审核状态
		if((PdConstant.ORDER_STATE_2).equals(applyStatus) || (PdConstant.ORDER_STATE_3).equals(applyStatus)){
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			pdApplyOrder.setAuditBy(sysUser.getId());
			pdApplyOrder.setAuditDate(new Date());
		}
		pdApplyOrderService.updateMain(pdApplyOrder, pdApplyOrderPage.getPdApplyDetailList());
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

}
