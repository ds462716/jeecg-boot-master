package org.jeecg.modules.pd.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.pd.vo.PdProductAllocationExcel;
import org.jeecg.modules.pd.vo.PdProductStockExcel;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdProductStockTotal")
@Slf4j
public class PdProductStockTotalController {
	 @Autowired
	 private IPdProductStockTotalService pdProductStockTotalService;
	 @Autowired
	 private IPdProductStockService pdProductStockService;
	 @Autowired
	 private ISysDepartService sysDepartService;
	 @Autowired
	 private IPdDepartService pdDepartService;
	 @Autowired
	 private IPdStockRecordDetailService pdStockRecordDetailService;
	 @Autowired
	 private IPdGoodsAllocationService pdGoodsAllocationService;
	 @Autowired
	 private IPdProductStockUniqueCodeService productStockUniqueCodeService;

	 /**
	  * 分页列表查询
	  *
	  * @param stockTotalPage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/list")
	 public Result<?> queryPageList(PdProductStockTotalPage stockTotalPage,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									HttpServletRequest req) {

         Page<PdProductStockTotalPage> page = new Page<PdProductStockTotalPage>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 if(oConvertUtils.isNotEmpty(stockTotalPage.getDepartIds()) && !"undefined".equals(stockTotalPage.getDepartIds())) {
			 stockTotalPage.setDepartIdList(Arrays.asList(stockTotalPage.getDepartIds().split(",")));
		 }else{
			 //查询科室下所有下级科室的ID
			 SysDepart sysDepart=new SysDepart();
			 List<String> departList=pdDepartService.selectListDepart(sysDepart);
			 stockTotalPage.setDepartIdList(departList);
		 }
		 stockTotalPage.setDepartParentId(sysUser.getDepartParentId());
		 stockTotalPage.setFilterType("1");//有值的话，则过滤库存数量为0的数据
 		 IPage<PdProductStockTotalPage> pageList = pdProductStockTotalService.selectList(page, stockTotalPage);//
		 //计算总库存数量，近效期数量，过期数量等值；
         //**************************
         List<PdProductStockTotalPage> aList = pdProductStockTotalService.findListForQuery(stockTotalPage);
         Double gCount=0.00;//过期数量
         Double jCount=0.00;//近效期数量
         Double isLcount=0.00;//久存产品数量
         Double pCount=0.00;//总数量
         Double limtCount=0.00;//超出库房上下限产品数量
         if(CollectionUtils.isNotEmpty(aList)){
             for (PdProductStockTotal p : aList) {
                 if(PdConstant.PD_STATE_2.equals(p.getExpStatus())){
					 gCount+=p.getStockNum();
                     //gCount++;
                 }else if(PdConstant.PD_STATE_1.equals(p.getExpStatus())){
                     //jCount++;
					 jCount+=p.getStockNum();
                 }
                 if(PdConstant.IS_LONG_1.equals(p.getIsLong())){
					 isLcount+=p.getStockNum();
                    // isLcount++;
                 }
                 if(ObjectUtils.isNotEmpty(p.getLimitUp()) && ObjectUtils.isEmpty(p.getLimitDown())){
                     Double up=p.getLimitUp();
                     if(p.getStockNum()>up){
						 limtCount+=p.getStockNum();
                     	//limtCount++;
					 }
                 }
                 if(ObjectUtils.isNotEmpty(p.getLimitDown())&&ObjectUtils.isEmpty(p.getLimitUp())){
                     Double down =  p.getLimitDown();
                     if(p.getStockNum()<down){
						 limtCount+=p.getStockNum();
                     	//limtCount++;
					 }
                 }
                 if(ObjectUtils.isNotEmpty(p.getLimitDown())&&ObjectUtils.isNotEmpty(p.getLimitUp())){
                     Double up= p.getLimitUp();
                     Double down =p.getLimitDown();
                     if(p.getStockNum()>up||p.getStockNum()<down){
                         //limtCount++;
						 limtCount+=p.getStockNum();
                     }
                 }
                 pCount+=p.getStockNum();
             }
         }
		Map map=new HashMap();
		 map.put("records",pageList);
		 map.put("gCount",gCount);//过期数量
		 map.put("jCount",jCount);//近效期数量
		 map.put("isLcount",isLcount);//久存产品数量
		 map.put("pCount",pCount);//总数量
		 map.put("limtCount",limtCount);//超出库房上下限产品数量
		 return Result.ok(map);
	 }
	 /**
	  * 设置库存上下线数值
	  *
	  * @param pdProductStockTotalPage
	  * @return
	  */
	 @PutMapping(value = "/updateProductStock")
	 public Result<?> updateProductStock(@RequestBody PdProductStockTotalPage pdProductStockTotalPage) {
		 String ids = pdProductStockTotalPage.getIds();
		 Double upNum=pdProductStockTotalPage.getLimitUp();
		 Double downNum=pdProductStockTotalPage.getLimitDown();
		 List arr = Arrays.asList(ids.split(","));
		 for (int i = 0; i < arr.size(); i++) {
			 String id=(String)arr.get(i);
			 PdProductStockTotal stockTotal =new PdProductStockTotal();
			 stockTotal.setId(id);
			 //先查询库存上限或下限数量，比较设置的上限值不能小于当前下限值；
			 PdProductStockTotal stock_total = pdProductStockTotalService.getById(id);
			 if (ObjectUtils.isNotEmpty(downNum) ) {//下限
				Double  limitUp= stock_total.getLimitUp();
			 	if(ObjectUtils.isNotEmpty(limitUp)){
			 		      if(limitUp > downNum){
							  stockTotal.setLimitDown(downNum);
						  }else{
							  return Result.error("库存下限不能大于库存上限");
						  }
				  }else{
					stockTotal.setLimitDown(downNum);
					stockTotal.setAutoNum(pdProductStockTotalPage.getAutoNum());
				}

			 }
			 if (ObjectUtils.isNotEmpty(upNum)) {//上限
				 Double  limitDown= stock_total.getLimitDown();
                            if(ObjectUtils.isNotEmpty(limitDown)){
								if(upNum > limitDown){
									stockTotal.setLimitUp(upNum);
								}else{
									return Result.error("库存上限必须大于库存下限");
								}
							}else{
								stockTotal.setLimitUp(upNum);
							}
			 }
			 pdProductStockTotalService.updateProductStockTotal(stockTotal);
		 }
		 return Result.ok("设置成功!");
	 }


	

	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdProductStockTotal pdProductStockTotal = pdProductStockTotalService.getById(id);
		if(pdProductStockTotal==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdProductStockTotal);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryPdProductStockByMainId")
	public Result<?> queryPdProductStockListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PdProductStock> pdProductStockList = pdProductStockService.selectByMainId(id);
		return Result.ok(pdProductStockList);
	}



	 /**
	  * 分页查询库存明细信息  mcb  --20200217   用于库存管理查询库存明细信息
	  *
	  * @param productStock
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/chooseProductStockList")
	 public Result<?> chooseProductStockList(PdProductStock productStock,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									HttpServletRequest req) {

		 Page<PdProductStock> page = new Page<PdProductStock>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 productStock.setDepartParentId(sysUser.getDepartParentId());
		 //page = pdProductStockService.selectList(page,productStock);
		 page =pdProductStockService.queryProductStockList(page,productStock);
		 //计算总库存数量，近效期数量，过期数量等值；
		 //**************************
		 List<PdProductStock> aList = pdProductStockService.selectList(productStock);
		 Double gCount=0.00;//过期数量
		 Double jCount=0.00;//近效期数量
		 Double pCount=0.00;//总数量
		 if(CollectionUtils.isNotEmpty(aList)){
			 for (PdProductStock p : aList) {
				 if(PdConstant.PD_STATE_2.equals(p.getExpStatus())){
					 gCount+=p.getStockNum();
					 //gCount++;
				 }else if(PdConstant.PD_STATE_1.equals(p.getExpStatus())){
					 jCount+=p.getStockNum();
				 	//jCount++;
				 }
				 pCount+=p.getStockNum();
			 }
		 }
		 Map map=new HashMap();
		 map.put("records",page);
		 map.put("gCount",gCount);//过期数量
		 map.put("jCount",jCount);//近效期数量
		 map.put("pCount",pCount);//总数量
		 return Result.ok(map);
	 }




	 /**
	  * 查询出入库明细  mcb  --20200217 用于库存管理查询出入库明细
	  * @param stockTotalPage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/stockInAndOutRecordDetailQuery")
	 public Result<?> stockInAndOutRecordDetailQuery(PdProductStockTotalPage stockTotalPage,
											 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
											 HttpServletRequest req) {

		 Page<PdStockRecordDetail> page = new Page<PdStockRecordDetail>(pageNo, pageSize);
		 PdStockRecordDetail pdStockRecordDetail=new PdStockRecordDetail();
		 pdStockRecordDetail.setProductId(stockTotalPage.getProductId());
		 pdStockRecordDetail.setAuditStatus(PdConstant.AUDIT_STATE_2);
		 pdStockRecordDetail.setDeptId(stockTotalPage.getDepartId());
		 pdStockRecordDetail.setBatchNo(stockTotalPage.getBatchNo());
		 pdStockRecordDetail.setVenderId(stockTotalPage.getVenderId());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdStockRecordDetail.setDepartParentId(sysUser.getDepartParentId());
		 IPage<PdStockRecordDetail> pageList = pdStockRecordDetailService.selectStockRecordListPage(page,pdStockRecordDetail);
		 Double productTotNum = 0.00;//入库总数量
		 Double productOutTotNum = 0.00;//出库总数量
		 BigDecimal inPrice = new BigDecimal(0);//入库总金额
		 BigDecimal outPrice = new BigDecimal(0);//出库总金额
		 List<PdStockRecordDetail> list = pdStockRecordDetailService.selectStockRecordList(pdStockRecordDetail);
		 if(CollectionUtils.isNotEmpty(list) && list.size() > 0){
			   for (PdStockRecordDetail item : list) {
				   if(PdConstant.RECODE_TYPE_1.equals(item.getRecordType())){//入库
					   inPrice=inPrice.add(item.getInTotalPrice());
					   productTotNum+=item.getProductNum();
				   }else if(PdConstant.RECODE_TYPE_2.equals(item.getRecordType())){//出库
					   productOutTotNum+=item.getProductNum();
					   outPrice=outPrice.add(item.getOutTotalPrice());
				   }
			  }
		 }

		 Map map=new HashMap();
		 map.put("page",pageList);
		 map.put("productTotNum",productTotNum);//入库总数量
		 map.put("productOutTotNum",productOutTotNum);//出库总数量
		 map.put("inPrice",inPrice);//入库总金额
		 map.put("outPrice",outPrice);//出库总金额
		 return Result.ok(map);
	 }


	 /**
	  * 分页查询库存明细信息  mcb  --20200217   用于统计查询库存明细信息
	  *
	  * @param productStock
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/queryList")
	 public Result<?> queryList(PdProductStock productStock,
											 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
											 HttpServletRequest req) {
		 Page<PdProductStock> page = new Page<PdProductStock>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
 			 if(oConvertUtils.isNotEmpty(productStock.getDepartIds()) && !"undefined".equals(productStock.getDepartIds())) {
				 productStock.setDepartIdList(Arrays.asList(productStock.getDepartIds().split(",")));
			 }else{
				 //查询科室下所有下级科室的ID
			 SysDepart sysDepart=new SysDepart();
			 List<String> departList=pdDepartService.selectListDepart(sysDepart);
			 productStock.setDepartIdList(departList);
		 }
		 productStock.setDepartParentId(sysUser.getDepartParentId());
		 IPage<PdProductStock> pageList = pdProductStockService.queryList(page, productStock);//
		 List<PdProductStock> aList= pageList.getRecords();
		 if(aList !=null && aList.size()>0){
			 for(PdProductStock stock : aList){
				 PdProductStockUniqueCode stockUniqueCode=new PdProductStockUniqueCode();
				 stockUniqueCode.setDepartParentId(sysUser.getDepartParentId());
				 stockUniqueCode.setCodeState(PdConstant.CODE_PRINT_STATE_0);
				 stockUniqueCode.setProductStockId(stock.getId());
				 String uniqueCode=productStockUniqueCodeService.queryUniqueCode(stockUniqueCode);
				 stock.setRefBarCodes(uniqueCode);
			 }
		 }
		 return Result.ok(pageList);
	 }

	/**
	 * 条码打印查询库存
	 * @param productStock
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/queryPrintList")
	public Result<?> queryPrintList(PdProductStock productStock,
							   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
							   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
							   HttpServletRequest req) {
		Page<PdProductStock> page = new Page<PdProductStock>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(oConvertUtils.isNotEmpty(productStock.getDepartIds()) && !"undefined".equals(productStock.getDepartIds())) {
			productStock.setDepartIdList(Arrays.asList(productStock.getDepartIds().split(",")));
		}else{
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			productStock.setDepartIdList(departList);
		}
		productStock.setDepartParentId(sysUser.getDepartParentId());
		page = pdProductStockService.queryPrintList(page,productStock);
		return Result.ok(page);
	}




	 /**
	  * 导出excel
	  *
	  * @param request
	  * @param stockTotalPage
	  */
	 @RequestMapping(value = "/exportXls")
	 public ModelAndView exportXls(HttpServletRequest request, PdProductStockTotalPage stockTotalPage) {
		 // Step.1 组装查询条件查询数据
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 //Step.2 获取导出数据
		 List<PdProductStockTotalPage> aList = pdProductStockTotalService.findListForQuery(stockTotalPage);
		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "库存产品列表");
		 mv.addObject(NormalExcelConstants.CLASS, PdProductStockTotalPage.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("库存列表数据", "导出人:"+sysUser.getRealname(), "库存产品数据"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, aList);
		 return mv;
	 }



	/**
	 * 库存明细导出excel
	 *
	 * @param request
	 * @param productStock
	 */
	@RequestMapping(value = "/stockExportXls")
	public ModelAndView stockExportXls(HttpServletRequest request, PdProductStock productStock) {
		// Step.1 组装查询条件查询数据
		String exportType=productStock.getExportType();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(oConvertUtils.isNotEmpty(productStock.getDepartIds()) && !"undefined".equals(productStock.getDepartIds())) {
			productStock.setDepartIdList(Arrays.asList(productStock.getDepartIds().split(",")));
		}else{
			//查询科室下所有下级科室的ID
			SysDepart sysDepart=new SysDepart();
			List<String> departList=pdDepartService.selectListDepart(sysDepart);
			productStock.setDepartIdList(departList);
		}
		//Step.2 获取导出数据
		if(exportType.equals("2")){
			productStock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
		}
		List<PdProductStock> aList =pdProductStockService.queryStockList(productStock);
		if(aList !=null && aList.size()>0){
			for(PdProductStock stock : aList){
				PdProductStockUniqueCode stockUniqueCode=new PdProductStockUniqueCode();
				stockUniqueCode.setDepartParentId(sysUser.getDepartParentId());
				stockUniqueCode.setProductStockId(stock.getId());
				stockUniqueCode.setCodeState(PdConstant.CODE_PRINT_STATE_0);
				String uniqueCode=productStockUniqueCodeService.queryUniqueCode(stockUniqueCode);
				stock.setRefBarCodes(uniqueCode);
			}
		}
        // Step.3 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		if(exportType.equals("2")){
			List<PdProductAllocationExcel>  exportList = JSON.parseArray(JSON.toJSONString(aList), PdProductAllocationExcel.class);
			mv.addObject(NormalExcelConstants.CLASS, PdProductAllocationExcel.class);
			mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		}else{
			List<PdProductStockExcel>  exportList = JSON.parseArray(JSON.toJSONString(aList), PdProductStockExcel.class);
			mv.addObject(NormalExcelConstants.CLASS, PdProductStockExcel.class);
			mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		}
		mv.addObject(NormalExcelConstants.FILE_NAME, "库存明细列表");
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("库存明细列表", "导出人:"+sysUser.getRealname(), "库存明细列表"));

		return mv;
	}

	 /**
	  * 查询盘点库存明细信息
	  *
	  * @param productStock
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/queryCheckStockList")
	 public Result<?> queryCheckStockList(PdProductStock productStock,
								@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
								HttpServletRequest req) {
		 Page<PdProductStock> page = new Page<PdProductStock>(pageNo, pageSize);
		 page = pdProductStockService.selectList(page,productStock);
		 return Result.ok(page);
	 }



	 /**
	  * 申领单/调拨单库存产品选择器用（分页列表查询）
	  *
	  * @param stockTotalPage
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/queryProductStockList")
	 public Result<?> queryProductStockList(PdProductStockTotalPage stockTotalPage,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									HttpServletRequest req) {
		 Page<PdProductStockTotalPage> page = new Page<PdProductStockTotalPage>(pageNo, pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 stockTotalPage.setDepartParentId(sysUser.getDepartParentId());
		 stockTotalPage.setCurrentDepartId(sysUser.getCurrentDepartId());
		 stockTotalPage.setFilterType("1");//如果传值，则过滤库存数量为0的
		String code= stockTotalPage.getCode();
		 if(StringUtils.isNotEmpty(code) && stockTotalPage.getCode().equals("2")){ //如果是申领单过来的话
			 //获取父级部门ID
			 SysDepart sysDepart=sysDepartService.queryDepartByOrgCode(sysUser.getOrgCode());
			 stockTotalPage.setDepartId(sysDepart.getParentId());
		  }
		 page = pdProductStockTotalService.selectList(page,stockTotalPage);
		 return Result.ok(page);
	 }



	 /**
	  * 修改库存明细信息（暂时修改产品生产日期用）
	  *
	  * @param productStock
	  * @return
	  */
	 @PutMapping(value = "/updateStock")
	 public Result<?> updateStock(@RequestBody PdProductStock productStock) {
		 pdProductStockService.updateById(productStock);
		 return Result.ok("编辑成功!");
	 }

	 /**
	  * 获取科室下所有货位信息
	  */

	@GetMapping(value = "/queryHuoWei")
	public Result<List<PdGoodsAllocationPage>> queryHuoWei() {
		Result<List<PdGoodsAllocationPage>> result = new Result<>();
		PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdGoodsAllocation.setDepartId(sysUser.getCurrentDepartId());
		pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
		List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);
		result.setResult(goodsAllocationList);
		result.setSuccess(true);
		return result;
	}
	 /**
	  *库存明细移库操作
	  *
	  * @param productStock
	  * @return
	  */
	 @PutMapping(value = "/updateStockHuowei")
	 public Result<?> updateStockHuowei(@RequestBody PdProductStock productStock) {
		 pdProductStockTotalService.updateStockHuowei(productStock);
		 return Result.ok("操作成功!");
	 }



	/**
	 * 出库产品选择器用查询库存明细信息
	 *
	 * @param productStock
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/selectProductStockList")
	public Result<?> selectProductStockList(PdProductStock productStock,
											@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
											HttpServletRequest req) {
		Page<PdProductStock> page = new Page<PdProductStock>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(oConvertUtils.isNotEmpty(productStock.getDepartId())){
			productStock.setDepartId(productStock.getDepartId());
		}else{
			productStock.setDepartId(sysUser.getCurrentDepartId());
		}
		productStock.setDepartParentId(sysUser.getDepartParentId());
//		productStock.setNestatStatus(PdConstant.STOCK_NESTAT_STATUS_1);
//		page = pdProductStockService.selectList(page,productStock);
		if(oConvertUtils.isNotEmpty(productStock.getProductIds())){
			productStock.setProductIdList(Arrays.asList(productStock.getProductIds().split(",")));
		}
		page = pdProductStockService.chooseProductStockList(page,productStock);

		return Result.ok(page);
	}

	/**
	 *库存规格数量清零
	 *
	 * @param productStock
	 * @return
	 */
	@PutMapping(value = "/updateStockSpecNum")
	public Result<?> updateStockSpecNum(@RequestBody PdProductStock productStock) {
		pdProductStockTotalService.updateStockSpecNum(productStock);
		return Result.ok("操作成功!");
	}


	/**
	 * 自动补货查询库存明细(根据库存上下限查询)
	 * @param stockTotal

	 * @return
	 */
	@GetMapping(value = "/chooseStockTotalList")
	public Result<?> chooseStockTotalList(PdProductStockTotalPage stockTotal) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		stockTotal.setDepartId(sysUser.getCurrentDepartId());
		stockTotal.setDepartParentId(sysUser.getDepartParentId());
		stockTotal.setStatus(PdConstant.DISABLE_ENABLE_STATUS_0);
		//stockTotal.setProductFlag(PdConstant.PRODUCT_FLAG_0);//只查产品
		List<PdProductStockTotalPage>  list=pdProductStockTotalService.chooseStockTotalList(stockTotal);
		return Result.ok(list);
	}


}
