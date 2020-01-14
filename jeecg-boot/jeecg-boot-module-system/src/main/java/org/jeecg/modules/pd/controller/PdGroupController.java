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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdGroup;
import org.jeecg.modules.pd.service.IPdGroupService;

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

 /**
 * @Description: 产品组别
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdGroup")
@Slf4j
public class PdGroupController extends JeecgController<PdGroup, IPdGroupService> {
	@Autowired
	private IPdGroupService pdGroupService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdGroup
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdGroup pdGroup,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PdGroup> queryWrapper = QueryGenerator.initQueryWrapper(pdGroup, req.getParameterMap());
		Page<PdGroup> page = new Page<PdGroup>(pageNo, pageSize);
		IPage<PdGroup> pageList = pdGroupService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdGroup
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdGroup pdGroup) {
		pdGroupService.save(pdGroup);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdGroup
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdGroup pdGroup) {
		pdGroupService.updateById(pdGroup);
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
		pdGroupService.removeById(id);
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
		this.pdGroupService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdGroup pdGroup = pdGroupService.getById(id);
		if(pdGroup==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdGroup);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdGroup
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdGroup pdGroup) {
        return super.exportXls(request, pdGroup, PdGroup.class, "产品组别");
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
        return super.importExcel(request, response, PdGroup.class);
    }

}
