package org.jeecg.modules.external.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.external.service.IExInspectionItemsService;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.service.IPdUsePackageDetailService;
import org.jeecg.modules.pd.service.IPdUsePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 检查项目表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Api(tags="检查项目表")
@RestController
@RequestMapping("/external/exInspectionItems")
@Slf4j
public class ExInspectionItemsController extends JeecgController<ExInspectionItems, IExInspectionItemsService> {
	@Autowired
	private IExInspectionItemsService exInspectionItemsService;

	@Autowired
	private IPdUsePackageService pdUsePackageService;

	@Autowired
	private IPdUsePackageDetailService pdUsePackageDetailService;

	@Autowired
	private IPdProductStockTotalService pdProductStockTotalService;
	/**
	 * 分页列表查询
	 *
	 * @param exInspectionItems
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "检查项目表-分页列表查询")
	@ApiOperation(value="检查项目表-分页列表查询", notes="检查项目表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ExInspectionItems exInspectionItems,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		Page<ExInspectionItems> page = new Page<ExInspectionItems>(pageNo, pageSize);
		IPage<ExInspectionItems> pageList = exInspectionItemsService.selectList(page, exInspectionItems);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param exInspectionItems
	 * @return
	 */
	@AutoLog(value = "检查项目表-添加")
	@ApiOperation(value="检查项目表-添加", notes="检查项目表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ExInspectionItems exInspectionItems) {
		exInspectionItemsService.save(exInspectionItems);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param exInspectionItems
	 * @return
	 */
	@AutoLog(value = "检查项目表-编辑")
	@ApiOperation(value="检查项目表-编辑", notes="检查项目表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ExInspectionItems exInspectionItems) {
		exInspectionItemsService.updateById(exInspectionItems);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检查项目表-通过id删除")
	@ApiOperation(value="检查项目表-通过id删除", notes="检查项目表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		exInspectionItemsService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "检查项目表-批量删除")
	@ApiOperation(value="检查项目表-批量删除", notes="检查项目表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.exInspectionItemsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检查项目表-通过id查询")
	@ApiOperation(value="检查项目表-通过id查询", notes="检查项目表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ExInspectionItems exInspectionItems = exInspectionItemsService.getById(id);
		if(exInspectionItems==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(exInspectionItems);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param exInspectionItems
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExInspectionItems exInspectionItems) {
        return super.exportXls(request, exInspectionItems, ExInspectionItems.class, "检查项目表");
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
        return super.importExcel(request, response, ExInspectionItems.class);
    }



	 /**
	  *  重新扣减检验用量
	  *
	  * @param items
	  * @return
	  */
	 @PostMapping(value = "/editUsePackage")
	 public Result<?> editUsePackage(@RequestBody ExInspectionItems items) {
		 LambdaQueryWrapper<PdUsePackage> query = new LambdaQueryWrapper<>();
		 query.eq(PdUsePackage::getCode, items.getTestItemCode());
		 query.eq(PdUsePackage::getName,items.getTestItemName());
		 PdUsePackage pdUsePackage = pdUsePackageService.getOne(query);
		 String testDpeartId=pdUsePackage.getTestDepartId();
		 //不存在或沒有配置檢驗用量明細
		 if(pdUsePackage!=null){
			 if(StringUtils.isEmpty(testDpeartId)){
				 items.setRemarks("未配置检验科室");
				 items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);// 0：已扣减  1：未配置检验用量  2:未扣减
			 }else {
				 PdUsePackageDetail detail = new PdUsePackageDetail();
				 detail.setPackageId(pdUsePackage.getId());
				 List<PdUsePackageDetail> pdUsePackageDetails = pdUsePackageDetailService.queryPdUsePackageList(detail);
				 if (pdUsePackageDetails != null && pdUsePackageDetails.size() > 0) {
					 try {
						 pdProductStockTotalService.lisUpdateUseStock(items,testDpeartId,pdUsePackageDetails);
						 items.setRemarks("");
						 items.setAcceptStatus(PdConstant.ACCEPT_STATUS_0);//已扣减
					 } catch (Exception e) {
						 e.getMessage();
						 log.error("扣減用量失敗:" + e.getMessage());
						 items.setRemarks(e.getMessage());
						 items.setAcceptStatus(PdConstant.ACCEPT_STATUS_2);
						 exInspectionItemsService.updateById(items);
						 return Result.error(e.getMessage());
					 }
				 } else {
					 items.setRemarks("检验用量未配置:"+pdUsePackage.getRemarks());
					 items.setAcceptStatus(PdConstant.ACCEPT_STATUS_3);// 0：已扣减  1：未配置检验用量  2:未扣减
					 return Result.error("扣減用量失敗:检验项目用量未配置");
				 }
			 }
		 }else{
			 items.setRemarks("检验项目未配置");
			 items.setAcceptStatus(PdConstant.ACCEPT_STATUS_1);// 0：已扣减  1：未配置检验用量  2:未扣减
			 return Result.error("扣減用量失敗:检验项目未配置");
		 }
	 	 exInspectionItemsService.updateById(items);
		 return Result.ok("操作成功!");
	 }



	/**
	 *  批量扣减
	 *
	 * @param ids
	 * @return
	 */
	@PostMapping(value = "/batchUsePackageDetail")
	public Result<?> batchUsePackageDetail(@RequestParam(name="ids",required=true) String ids) {
		try {
			exInspectionItemsService.batchUsePackageDetail(ids);
		}catch (Exception e){
			e.getMessage();
			Result.error("操作失败!");
		}
		return Result.ok("操作成功!");
	}

}
