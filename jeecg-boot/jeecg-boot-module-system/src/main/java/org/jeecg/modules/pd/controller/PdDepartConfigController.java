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
import org.jeecg.modules.pd.entity.PdDepartConfig;
import org.jeecg.modules.pd.service.IPdDepartConfigService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.pd.util.UUIDUtil;
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
 * @Description: 部门个性化配置
 * @Author: zxh
 * @Date:   2020-01-19
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdDepartConfig")
@Slf4j
public class PdDepartConfigController extends JeecgController<PdDepartConfig, IPdDepartConfigService> {
	@Autowired
	private IPdDepartConfigService pdDepartConfigService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdDepartConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdDepartConfig pdDepartConfig,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		try{
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			pdDepartConfig.setDepartParentId(sysUser.getDepartParentId());
			QueryWrapper<PdDepartConfig> queryWrapper = QueryGenerator.initQueryWrapper(pdDepartConfig, req.getParameterMap());
			Page<PdDepartConfig> page = new Page<>(pageNo, pageSize);
			IPage<PdDepartConfig> pageList = pdDepartConfigService.page(page, queryWrapper);
			if(pageList.getRecords()==null || pageList.getRecords().size()==0){
				//如果没有就查询默认的
				pdDepartConfig.setDepartParentId("");
				pdDepartConfig.setIsDefault(PdConstant.IS_DEFAULT_0);
				queryWrapper = QueryGenerator.initQueryWrapper(pdDepartConfig, req.getParameterMap());
				List<PdDepartConfig> pdDepartConfigs = pdDepartConfigService.list(queryWrapper);
				for(PdDepartConfig pc :pdDepartConfigs){
					pc.setDepartParentId(sysUser.getDepartParentId());
					pc.setDepartId(sysUser.getCurrentDepartId());
					pc.setId(UUIDUtil.getUuid());
					pc.setIsDefault(PdConstant.IS_DEFAULT_1);
				}
				pdDepartConfigService.saveBatch(pdDepartConfigs);
				page.setRecords(pdDepartConfigs);
				return Result.ok(page);
			}
			return Result.ok(pageList);
		}catch (Exception e){
			e.printStackTrace();
			return Result.error("系统异常");
		}
	}
	
	/**
	 *   添加
	 *
	 * @param pdDepartConfig
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdDepartConfig pdDepartConfig) {
		pdDepartConfigService.save(pdDepartConfig);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdDepartConfig
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdDepartConfig pdDepartConfig) {
		pdDepartConfigService.updateById(pdDepartConfig);
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
		pdDepartConfigService.removeById(id);
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
		this.pdDepartConfigService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdDepartConfig pdDepartConfig = pdDepartConfigService.getById(id);
		if(pdDepartConfig==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdDepartConfig);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdDepartConfig
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdDepartConfig pdDepartConfig) {
        return super.exportXls(request, pdDepartConfig, PdDepartConfig.class, "部门个性化配置");
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
        return super.importExcel(request, response, PdDepartConfig.class);
    }

}
