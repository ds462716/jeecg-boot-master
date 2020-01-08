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
import org.jeecg.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdEncodingIdentifier;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdEncodingRuleDetail;
import org.jeecg.modules.pd.service.IPdEncodingRuleService;
import org.apache.commons.lang.StringUtils;
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
 * @Description: 编码规则表
 * @Author: jeecg-boot
 * @Date:   2019-12-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdEncodingRule")
@Slf4j
public class PdEncodingRuleController extends JeecgController<PdEncodingRule, IPdEncodingRuleService> {
	@Autowired
	private IPdEncodingRuleService pdEncodingRuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdEncodingRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdEncodingRule pdEncodingRule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {

		Result<Page<PdEncodingRule>> result = new Result<Page<PdEncodingRule>>();
		Page<PdEncodingRule> pageList = new Page<PdEncodingRule>(pageNo,pageSize);
		pageList = pdEncodingRuleService.selectList(pageList,pdEncodingRule);//
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}


	 /**
	  * 不分页列表查询
	  *
	  * @return
	  */
	 @GetMapping(value = "/getEncodingRuleList")
	 public Result<List<PdEncodingRule>> getEncodingIdentifierList(PdEncodingRule pdEncodingRule) {
		 long start = System.currentTimeMillis();
		 Result<List<PdEncodingRule>> result = new Result<>();
		 try {
			 List<PdEncodingRule> list = pdEncodingRuleService.selectList(pdEncodingRule);
			 result.setResult(list);
			 result.setSuccess(true);
		 }catch(Exception e){
			 log.error(e.getMessage(), e);
		 }
		 log.info("======获取一级菜单数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		 return result;
	 }
	
	/**
	 *   添加
	 *
	 * @param pdEncodingRule
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdEncodingRule pdEncodingRule) {
		pdEncodingRuleService.save(pdEncodingRule);
		return Result.ok("添加成功！");
	}

	 /**
	  *   添加
	  *
	  * @param pdEncodingRule
	  * @return
	  */
	 @PostMapping(value = "/savePdEncodingRule")
	 public Result<?> savePdEncodingRule(@RequestBody PdEncodingRule pdEncodingRule) {
		 pdEncodingRuleService.savePdEncodingRule(pdEncodingRule);
		 return Result.ok("添加成功！");
	 }

	 /**
	  *   编辑
	  *
	  * @param pdEncodingRule
	  * @return
	  */
	 @PutMapping(value = "/updatePdEncodingRule")
	 public Result<?> updatePdEncodingRule(@RequestBody PdEncodingRule pdEncodingRule) {
		 pdEncodingRuleService.updatePdEncodingRule(pdEncodingRule);
		 return Result.ok("添加成功！");
	 }
	
	/**
	 *  编辑
	 *
	 * @param pdEncodingRule
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdEncodingRule pdEncodingRule) {
		pdEncodingRuleService.updateById(pdEncodingRule);
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
		pdEncodingRuleService.removeById(id);
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
		this.pdEncodingRuleService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdEncodingRule pdEncodingRule = pdEncodingRuleService.getById(id);
		if(pdEncodingRule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdEncodingRule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdEncodingRule
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdEncodingRule pdEncodingRule) {
        return super.exportXls(request, pdEncodingRule, PdEncodingRule.class, "编码规则表");
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
        return super.importExcel(request, response, PdEncodingRule.class);
    }

}
