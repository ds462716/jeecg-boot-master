package org.jeecg.modules.pd.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdAllocationDetail;
import org.jeecg.modules.pd.entity.PdAllocationRecord;
import org.jeecg.modules.pd.service.IPdAllocationRecordService;
import org.jeecg.modules.pd.service.IPdAllocationDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 调拨记录表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pds/pdAllocationRecord")
@Slf4j
public class PdAllocationRecordController {
	@Autowired
	private IPdAllocationRecordService pdAllocationRecordService;
	@Autowired
	private IPdAllocationDetailService pdAllocationDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pdAllocationRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdAllocationRecord pdAllocationRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
 		Page<PdAllocationRecord> page = new Page<PdAllocationRecord>(pageNo, pageSize);
		IPage<PdAllocationRecord> pageList =pdAllocationRecordService.selectList(page, pdAllocationRecord);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pdAllocationRecord
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdAllocationRecord pdAllocationRecord) {
 		pdAllocationRecordService.saveMain(pdAllocationRecord, pdAllocationRecord.getPdAllocationDetailList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pdAllocationRecord
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdAllocationRecord pdAllocationRecord) {
		PdAllocationRecord pdAllocationRecordEntity = pdAllocationRecordService.getById(pdAllocationRecord.getId());
		if(pdAllocationRecordEntity==null) {
			return Result.error("未找到对应数据");
		}
		pdAllocationRecordService.updateMain(pdAllocationRecord, pdAllocationRecord.getPdAllocationDetailList());
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
		pdAllocationRecordService.delMain(id);
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
		this.pdAllocationRecordService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PdAllocationRecord pdAllocationRecord = pdAllocationRecordService.getById(id);
		if(pdAllocationRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdAllocationRecord);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryPdAllocationDetailByMainId")
	public Result<?> queryPdAllocationDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PdAllocationDetail> pdAllocationDetailList = pdAllocationDetailService.selectByMainId(id);
		return Result.ok(pdAllocationDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdAllocationRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdAllocationRecord pdAllocationRecord) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PdAllocationRecord> queryWrapper = QueryGenerator.initQueryWrapper(pdAllocationRecord, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PdAllocationRecord> queryList = pdAllocationRecordService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PdAllocationRecord> pdAllocationRecordList = new ArrayList<PdAllocationRecord>();
      if(oConvertUtils.isEmpty(selections)) {
          pdAllocationRecordList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          pdAllocationRecordList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PdAllocationRecord> pageList = new ArrayList<PdAllocationRecord>();
      for (PdAllocationRecord main : pdAllocationRecordList) {
          PdAllocationRecord vo = new PdAllocationRecord();
          BeanUtils.copyProperties(main, vo);
          List<PdAllocationDetail> pdAllocationDetailList = pdAllocationDetailService.selectByMainId(main.getId());
          vo.setPdAllocationDetailList(pdAllocationDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "调拨记录表列表");
      mv.addObject(NormalExcelConstants.CLASS, PdAllocationRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("调拨记录表数据", "导出人:"+sysUser.getRealname(), "调拨记录表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<PdAllocationRecord> list = ExcelImportUtil.importExcel(file.getInputStream(), PdAllocationRecord.class, params);
              for (PdAllocationRecord page : list) {
                  PdAllocationRecord po = new PdAllocationRecord();
                  BeanUtils.copyProperties(page, po);
                  pdAllocationRecordService.saveMain(po, page.getPdAllocationDetailList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

}
