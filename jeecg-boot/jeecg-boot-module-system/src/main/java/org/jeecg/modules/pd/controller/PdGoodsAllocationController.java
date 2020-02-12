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

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdGoodsAllocation;
import org.jeecg.modules.pd.service.IPdGoodsAllocationService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
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

 /**
 * @Description: 货区货位表
 * @Author: jiangxz
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdGoodsAllocation")
@Slf4j
public class PdGoodsAllocationController extends JeecgController<PdGoodsAllocation, IPdGoodsAllocationService> {
	@Autowired
	private IPdGoodsAllocationService pdGoodsAllocationService;

	 @Autowired
	private ISysDepartService sysDepartService;

	@RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	public Result<List<SysDepartTreeModel>> queryTreeList() {
		Result<List<SysDepartTreeModel>> result = new Result<>();
		try {
			List<SysDepartTreeModel> departTreeList = sysDepartService.queryTreeList();
			//查询货区货位
			List<SysDepartTreeModel> list = pdGoodsAllocationService.queryTreeList(departTreeList);
			result.setResult(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}

	/**
	 * 分页列表查询
	 *
	 * @param pdGoodsAllocation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdGoodsAllocation pdGoodsAllocation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PdGoodsAllocation> queryWrapper = QueryGenerator.initQueryWrapper(pdGoodsAllocation, req.getParameterMap());
		Page<PdGoodsAllocation> page = new Page<PdGoodsAllocation>(pageNo, pageSize);
		IPage<PdGoodsAllocation> pageList = pdGoodsAllocationService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdGoodsAllocation
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdGoodsAllocation pdGoodsAllocation) {
		pdGoodsAllocationService.save(pdGoodsAllocation);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdGoodsAllocation
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdGoodsAllocation pdGoodsAllocation) {
		pdGoodsAllocationService.updateById(pdGoodsAllocation);
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
		pdGoodsAllocationService.removeById(id);
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
		this.pdGoodsAllocationService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdGoodsAllocation pdGoodsAllocation = pdGoodsAllocationService.getById(id);
		if(pdGoodsAllocation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdGoodsAllocation);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdGoodsAllocation
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdGoodsAllocation pdGoodsAllocation) {
        return super.exportXls(request, pdGoodsAllocation, PdGoodsAllocation.class, "货区货位表");
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
        return super.importExcel(request, response, PdGoodsAllocation.class);
    }

}
