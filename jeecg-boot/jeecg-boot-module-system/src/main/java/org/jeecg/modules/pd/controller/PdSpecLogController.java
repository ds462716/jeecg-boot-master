package org.jeecg.modules.pd.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.pd.entity.PdSpecLog;
import org.jeecg.modules.pd.service.IPdSpecLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 规格数量清零操作日志表
 * @Author: jiangxz
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdSpecLog")
@Slf4j
public class PdSpecLogController extends JeecgController<PdSpecLog, IPdSpecLogService> {
	@Autowired
	private IPdSpecLogService pdSpecLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdSpecLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdSpecLog pdSpecLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		 Page<PdSpecLog> page = new Page<PdSpecLog>(pageNo, pageSize);
		IPage<PdSpecLog> pageList =pdSpecLogService.selectList(page,pdSpecLog);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdSpecLog
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdSpecLog pdSpecLog) {
		pdSpecLogService.save(pdSpecLog);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdSpecLog
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdSpecLog pdSpecLog) {
		pdSpecLogService.updateById(pdSpecLog);
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
		pdSpecLogService.removeById(id);
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
		this.pdSpecLogService.removeByIds(Arrays.asList(ids.split(",")));
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
		PdSpecLog pdSpecLog = pdSpecLogService.getById(id);
		if(pdSpecLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdSpecLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdSpecLog
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdSpecLog pdSpecLog) {
        return super.exportXls(request, pdSpecLog, PdSpecLog.class, "规格数量清零操作日志表");
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
        return super.importExcel(request, response, PdSpecLog.class);
    }

}
