package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.service.IPdStockRecordDetailService;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
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
	 private IPdStockRecordDetailService pdStockRecordDetailService;
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
		 stockTotalPage.setDepartParentId(sysUser.getDepartParentId());
         page = pdProductStockTotalService.selectList(page,stockTotalPage);
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
		 map.put("records",page);
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
			 PdProductStockTotal stockTotal = new PdProductStockTotal();
			 stockTotal.setId(id);
			 if (ObjectUtils.isNotEmpty(downNum)) {
				 stockTotal.setLimitDown(downNum);
			 }
			 if (ObjectUtils.isNotEmpty(upNum)) {
				 stockTotal.setLimitUp(upNum);
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
		 productStock.setDepartId(sysUser.getCurrentDepartId());
		 page = pdProductStockService.selectList(page,productStock);
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
		 page = pdStockRecordDetailService.selectList(page,pdStockRecordDetail);
		 Double productTotNum = 0.00;//入库总数量
		 Double productOutTotNum = 0.00;//出库总数量
		 BigDecimal inPrice = new BigDecimal(0);//入库总金额
		 BigDecimal outPrice = new BigDecimal(0);//出库总金额
		 List<PdStockRecordDetail> list = pdStockRecordDetailService.queryPdStockRecordDetail(pdStockRecordDetail);
		 if(CollectionUtils.isNotEmpty(list) && list.size() > 0){
			   for (PdStockRecordDetail item : list) {
				   outPrice=outPrice.add(item.getOutTotalPrice());
				   inPrice=inPrice.add(item.getInTotalPrice());
				   if(PdConstant.RECODE_TYPE_1.equals(item.getRecordType())){//入库
					   productTotNum+=item.getProductNum();
				   }else if(PdConstant.RECODE_TYPE_2.equals(item.getRecordType())){//出库
					   productOutTotNum+=item.getProductNum();
				   }
			  }
		 }

		 Map map=new HashMap();
		 map.put("page",page);
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
		 productStock.setDepartId(sysUser.getCurrentDepartId());
		 page = pdProductStockService.selectList(page,productStock);
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
		 mv.addObject(NormalExcelConstants.CLASS, PdProductStockTotal.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("库存列表数据", "导出人:"+sysUser.getRealname(), "库存产品数据"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, aList);
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
		 if(stockTotalPage.getCode().equals("2")){//如果是申领单过来的话
			String deptId= sysUser.getCurrentDepartId();//当前登录科室
			 //获取父级部门ID//暂时给器械科ID
			 stockTotalPage.setDepartId("226bedba2af745e097d4ac8873b9178e");
		  }
		 page = pdProductStockTotalService.selectList(page,stockTotalPage);
		 return Result.ok(page);
	 }

}
