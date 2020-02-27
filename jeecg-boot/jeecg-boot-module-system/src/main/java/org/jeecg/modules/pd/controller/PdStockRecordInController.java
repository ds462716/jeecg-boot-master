package org.jeecg.modules.pd.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
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
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/pd/pdStockRecordIn")
@Slf4j
public class PdStockRecordInController {
	@Autowired
	private IPdStockRecordService pdStockRecordService;
	@Autowired
	private IPdStockRecordDetailService pdStockRecordDetailService;
	@Autowired
	private ISysDepartService sysDepartService;
	@Autowired
	private IPdGoodsAllocationService pdGoodsAllocationService;
	@Autowired
	private ISysDictService sysDictService;
	@Autowired
	private IPdSupplierService pdSupplierService;
	@Autowired
	private IPdPurchaseDetailService pdPurchaseDetailService;


	 /**
	  * 初始化Modal页面
	  * @param req
	  * @return
	  */
	@GetMapping(value = "/initModal")
	public Result<?> initModal(@RequestParam(name="id") String id, HttpServletRequest req) {
		PdStockRecord pdStockRecord = new PdStockRecord();

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		SysDepart sysDepart = sysDepartService.getDepartByOrgCode(sysUser.getOrgCode());

		PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
		pdGoodsAllocation.setDepartId(sysDepart.getId());
		pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
		List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);

		if(StringUtils.isNotEmpty(id)){ // 查看页面
			pdStockRecord = pdStockRecordService.getById(id);
			if(StringUtils.isNotEmpty(pdStockRecord.getOrderNo())){
				List<PdPurchaseDetail> pdPurchaseDetailList = pdPurchaseDetailService.selectByOrderNo(pdStockRecord.getOrderNo());
				pdStockRecord.setPdPurchaseDetailList(pdPurchaseDetailList);
			}
		}else{  // 新增页面
			//开关-是否允许入库量大于订单量   1-允许入库量大于订单量；0-不允许入库量大于订单量
			List<DictModel> allowInMoreOrder = sysDictService.queryDictItemsByCode(PdConstant.ON_OFF_ALLOW_IN_MORE_ORDER);
			//开关-是否允许入库非订单产品     1-允许非订单产品；0-不允许非订单产品
			List<DictModel> allowNotOrderProduct = sysDictService.queryDictItemsByCode(PdConstant.ON_OFF_ALLOW_NOT_ORDER_PRODUCT);
			if(CollectionUtils.isNotEmpty(allowInMoreOrder)){
				pdStockRecord.setAllowInMoreOrder(allowInMoreOrder.get(0).getValue());
			}
			if(CollectionUtils.isNotEmpty(allowNotOrderProduct)){
				pdStockRecord.setAllowNotOrderProduct(allowNotOrderProduct.get(0).getValue());
			}

			//部门id
			pdStockRecord.setInDepartId(sysDepart.getId());
			//获取入库单号
			pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_RK));
			//获取当前日期
			pdStockRecord.setSubmitDateStr(DateUtils.formatDate());
			pdStockRecord.setSubmitDate(DateUtils.getDate());
			//登录人姓名
			pdStockRecord.setSubmitBy(sysUser.getId());
			pdStockRecord.setSubmitByName(sysUser.getRealname());
			//默认入库类型
			pdStockRecord.setInType(PdConstant.IN_TYPE_1);

		}

		//库区库位下拉框
		pdStockRecord.setGoodsAllocationList(goodsAllocationList);
		return Result.ok(pdStockRecord);
	}
	/**
	 * 分页列表查询
	 *
	 * @param pdStockRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PdStockRecord pdStockRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<PdStockRecord> queryWrapper = QueryGenerator.initQueryWrapper(pdStockRecord, req.getParameterMap());
		Page<PdStockRecord> page = new Page<PdStockRecord>(pageNo, pageSize);
//		IPage<PdStockRecord> pageList = pdStockRecordService.page(page, queryWrapper);
		pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1);
		IPage<PdStockRecord> pageList = pdStockRecordService.queryList(page, pdStockRecord);
		return Result.ok(pageList);
	}
	
	/**
	 *   保存
	 *
	 * @param PdStockRecord
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PdStockRecord PdStockRecord) {
		PdStockRecord pdStockRecord = new PdStockRecord();
		BeanUtils.copyProperties(PdStockRecord, pdStockRecord);
		pdStockRecordService.saveMain(pdStockRecord, PdStockRecord.getPdStockRecordDetailList());
		return Result.ok("添加成功！");
	}

	 /**
	  *   提交
	  *
	  * @param PdStockRecord
	  * @return
	  */
	 @PostMapping(value = "/submit")
	 public Result<?> submit(@RequestBody PdStockRecord PdStockRecord) {
		 PdStockRecord pdStockRecord = new PdStockRecord();
		 BeanUtils.copyProperties(PdStockRecord, pdStockRecord);
		 pdStockRecordService.saveMain(pdStockRecord, PdStockRecord.getPdStockRecordDetailList());
		 return Result.ok("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param PdStockRecord
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PdStockRecord PdStockRecord) {
		PdStockRecord pdStockRecord = new PdStockRecord();
		BeanUtils.copyProperties(PdStockRecord, pdStockRecord);
		PdStockRecord pdStockRecordEntity = pdStockRecordService.getById(pdStockRecord.getId());
		if(pdStockRecordEntity==null) {
			return Result.error("未找到对应数据");
		}
		pdStockRecordService.updateMain(pdStockRecord, PdStockRecord.getPdStockRecordDetailList());
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
		pdStockRecordService.delMain(id);
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
		this.pdStockRecordService.delBatchMain(Arrays.asList(ids.split(",")));
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
		PdStockRecord pdStockRecord = pdStockRecordService.getById(id);
		if(pdStockRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pdStockRecord);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryPdStockRecordDetailByMainId")
	public Result<?> queryPdStockRecordDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
		pdStockRecordDetail.setRecordId(id);
		List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
		return Result.ok(pdStockRecordDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pdStockRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PdStockRecord pdStockRecord) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PdStockRecord> queryWrapper = QueryGenerator.initQueryWrapper(pdStockRecord, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PdStockRecord> queryList = pdStockRecordService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PdStockRecord> pdStockRecordList = new ArrayList<PdStockRecord>();
      if(oConvertUtils.isEmpty(selections)) {
          pdStockRecordList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          pdStockRecordList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PdStockRecord> pageList = new ArrayList<PdStockRecord>();
      for (PdStockRecord main : pdStockRecordList) {
          PdStockRecord vo = new PdStockRecord();
		  PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
		  pdStockRecordDetail.setRecordId(main.getId());
          BeanUtils.copyProperties(main, vo);
          List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
          vo.setPdStockRecordDetailList(pdStockRecordDetailList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "出入库记录表列表");
      mv.addObject(NormalExcelConstants.CLASS, PdStockRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("出入库记录表数据", "导出人:"+sysUser.getRealname(), "出入库记录表"));
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
              List<PdStockRecord> list = ExcelImportUtil.importExcel(file.getInputStream(), PdStockRecord.class, params);
              for (PdStockRecord page : list) {
                  PdStockRecord po = new PdStockRecord();
                  BeanUtils.copyProperties(page, po);
                  pdStockRecordService.saveMain(po, page.getPdStockRecordDetailList());
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


	 /**
	  * 查询入库明细  mcb  --20200224 用于统计查询
	  * @param pdStockRecordDetail
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/queryPdStockRecordInList")
	 public Result<?> queryPdStockRecordInList(PdStockRecordDetail pdStockRecordDetail,
													 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
													 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
													 HttpServletRequest req) {

		 Page<PdStockRecordDetail> page = new Page<PdStockRecordDetail>(pageNo, pageSize);
		 pdStockRecordDetail.setRecordType(PdConstant.RECODE_TYPE_1);
		 page = pdStockRecordDetailService.selectList(page,pdStockRecordDetail);
		 return Result.ok(page);
	 }

	 /**
	  * 查询出库明细  mcb  --20200224 用于统计查询
	  * @param pdStockRecordDetail
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/queryPdStockRecordOutList")
	 public Result<?> queryPdStockRecordOutList(PdStockRecordDetail pdStockRecordDetail,
											   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
											   HttpServletRequest req) {

		 Page<PdStockRecordDetail> page = new Page<PdStockRecordDetail>(pageNo, pageSize);
		 pdStockRecordDetail.setRecordType(PdConstant.RECODE_TYPE_2);
 		 page = pdStockRecordDetailService.selectList(page,pdStockRecordDetail);
		 return Result.ok(page);
	 }
 }
