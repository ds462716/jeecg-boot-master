package org.jeecg.modules.pd.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.pd.entity.PdDosageDetail;
import org.jeecg.modules.pd.service.IPdDosageDetailService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 用量详情表
 * @Author: jiangxz
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Api(tags="用量详情表")
@RestController
@RequestMapping("/pd/pdDosageDetail")
@Slf4j
public class PdDosageDetailController extends JeecgController<PdDosageDetail, IPdDosageDetailService> {
	@Autowired
	private IPdDosageDetailService pdDosageDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdDosageDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用量详情表-分页列表查询")
	@ApiOperation(value="用量详情表-分页列表查询", notes="用量详情表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdDosageDetail pdDosageDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PdDosageDetail> queryWrapper = QueryGenerator.initQueryWrapper(pdDosageDetail, req.getParameterMap());
		Page<PdDosageDetail> page = new Page<PdDosageDetail>(pageNo, pageSize);
		IPage<PdDosageDetail> pageList = pdDosageDetailService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdDosageDetail
	 * @return
	 */
	@AutoLog(value = "用量详情表-添加")
	@ApiOperation(value="用量详情表-添加", notes="用量详情表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdDosageDetail pdDosageDetail) {
		pdDosageDetailService.save(pdDosageDetail);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdDosageDetail
	 * @return
	 */
	@AutoLog(value = "用量详情表-编辑")
	@ApiOperation(value="用量详情表-编辑", notes="用量详情表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdDosageDetail pdDosageDetail) {
		pdDosageDetailService.updateById(pdDosageDetail);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用量详情表-通过id删除")
	@ApiOperation(value="用量详情表-通过id删除", notes="用量详情表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pdDosageDetailService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用量详情表-批量删除")
	@ApiOperation(value="用量详情表-批量删除", notes="用量详情表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pdDosageDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用量详情表-通过id查询")
	@ApiOperation(value="用量详情表-通过id查询", notes="用量详情表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdDosageDetail pdDosageDetail = pdDosageDetailService.getById(id);
		if(pdDosageDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdDosageDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdDosageDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdDosageDetail pdDosageDetail) {
        return super.exportXls(request, pdDosageDetail, PdDosageDetail.class, "用量详情表");
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
        return super.importExcel(request, response, PdDosageDetail.class);
    }

}
