package org.jeecg.modules.pd.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockUniqueCode;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.service.IPdProductStockUniqueCodeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 库存关联条码表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Api(tags="库存关联条码表")
@RestController
@RequestMapping("/pd/pdProductStockUniqueCode")
@Slf4j
public class PdProductStockUniqueCodeController extends JeecgController<PdProductStockUniqueCode, IPdProductStockUniqueCodeService> {
	@Autowired
	private IPdProductStockUniqueCodeService pdProductStockUniqueCodeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdProductStockUniqueCode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "库存关联条码表-分页列表查询")
	@ApiOperation(value="库存关联条码表-分页列表查询", notes="库存关联条码表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdProductStockUniqueCode pdProductStockUniqueCode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PdProductStockUniqueCode> page = new Page<>(pageNo,pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdProductStockUniqueCode.setDepartParentId(sysUser.getDepartParentId());
		//因为打了批量码以后该条码并不清除所以只查唯一码
		pdProductStockUniqueCode.setPrintType(PdConstant.CODE_PRINT_TYPE_1);
		IPage<PdProductStockUniqueCode> pageList =pdProductStockUniqueCodeService.selectList(page,pdProductStockUniqueCode);//
		return Result.ok(pageList);
	}

	 @AutoLog(value = "库存关联条码表-分页列表查询")
	 @ApiOperation(value="库存关联条码表-分页列表查询", notes="库存关联条码表-分页列表查询")
	 @GetMapping(value = "/findList")
	 public Result<?> findList(PdProductStockUniqueCode pdProductStockUniqueCode,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Page<PdProductStockUniqueCode> page = new Page<>(pageNo,pageSize);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 pdProductStockUniqueCode.setDepartParentId(sysUser.getDepartParentId());
		 IPage<PdProductStockUniqueCode> pageList =pdProductStockUniqueCodeService.findList(page,pdProductStockUniqueCode);//
		 return Result.ok(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param pdProductStockUniqueCode
	 * @return
	 */
	@AutoLog(value = "库存关联条码表-添加")
	@ApiOperation(value="库存关联条码表-添加", notes="库存关联条码表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdProductStockUniqueCode pdProductStockUniqueCode) {
		pdProductStockUniqueCodeService.save(pdProductStockUniqueCode);
		return Result.ok("添加成功！");
	}

	 @AutoLog(value = "批量修改打印次数")
	 @ApiOperation(value="批量修改打印次数", notes="批量修改打印次数")
	 @PostMapping(value = "/updatePrintNum")
	 public Result<?> updatePrintNum(@RequestBody List<PdProductStockUniqueCode> pdProductStockUniqueCodes) {
		 pdProductStockUniqueCodeService.updatePrintNum(pdProductStockUniqueCodes);
		 return Result.ok("添加成功！");
	 }
	
	/**
	 *  编辑
	 *
	 * @param pdProductStockUniqueCode
	 * @return
	 */
	@AutoLog(value = "库存关联条码表-编辑")
	@ApiOperation(value="库存关联条码表-编辑", notes="库存关联条码表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdProductStockUniqueCode pdProductStockUniqueCode) {
		pdProductStockUniqueCodeService.updateById(pdProductStockUniqueCode);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存关联条码表-通过id删除")
	@ApiOperation(value="库存关联条码表-通过id删除", notes="库存关联条码表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdProductStockUniqueCodeService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存关联条码表-批量删除")
	@ApiOperation(value="库存关联条码表-批量删除", notes="库存关联条码表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pdProductStockUniqueCodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存关联条码表-通过id查询")
	@ApiOperation(value="库存关联条码表-通过id查询", notes="库存关联条码表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdProductStockUniqueCode pdProductStockUniqueCode = pdProductStockUniqueCodeService.getById(id);
		if(pdProductStockUniqueCode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdProductStockUniqueCode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdProductStockUniqueCode
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdProductStockUniqueCode pdProductStockUniqueCode) {
        return super.exportXls(request, pdProductStockUniqueCode, PdProductStockUniqueCode.class, "库存关联条码表");
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
        return super.importExcel(request, response, PdProductStockUniqueCode.class);
    }


	 /**
	  * 生成唯一码并保存
	  * @param pdProductStockUniqueCode
	  * @param req
	  * @return
	  */
	@PostMapping(value = "/uniqueCodeGeneration")
	public Result<?> uniqueCodeGeneration(PdProductStockUniqueCode pdProductStockUniqueCode,
										  HttpServletRequest req) {
		Result<List<PdProductStockUniqueCode>> result = pdProductStockUniqueCodeService.uniqueCodeGeneration(pdProductStockUniqueCode);
		return result;
	}

	 /**
	  * 批量生成条码
	  * @param ids
	  * @return
	  */
	 @PostMapping(value = "/batchCodeGeneration")
	 public Result<?> batchCodeGeneration(String ids) {
		 Result<List<PdProductStockUniqueCode>> result = pdProductStockUniqueCodeService.batchCodeGeneration(Arrays.asList(ids.split(",")));
		 return result;
	 }

     /**
      * 删除条码
      * @param id
      * @return
      */
     @AutoLog(value = "库存关联条码表-批量删除")
     @ApiOperation(value="库存关联条码表-批量删除", notes="库存关联条码表-批量删除")
     @DeleteMapping(value = "/deleteCode")
     public Result<?> deleteCode(@RequestParam(name="id",required=true) String id) {
		 Result<Object> resul = pdProductStockUniqueCodeService.deleteCode(id);
		 return resul;
     }
}
