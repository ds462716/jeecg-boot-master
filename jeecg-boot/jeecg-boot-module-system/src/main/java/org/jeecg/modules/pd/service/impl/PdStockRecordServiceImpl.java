package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.mapper.*;
import org.jeecg.modules.pd.service.*;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Service
public class PdStockRecordServiceImpl extends ServiceImpl<PdStockRecordMapper, PdStockRecord> implements IPdStockRecordService {

	@Autowired
	private PdStockRecordMapper pdStockRecordMapper;
	@Autowired
	private PdStockRecordDetailMapper pdStockRecordDetailMapper;
	@Autowired
	private PdPurchaseDetailMapper pdPurchaseDetailMapper;
	@Autowired
	private PdApplyOrderMapper pdApplyOrderMapper;
	@Autowired
	private PdApplyDetailMapper pdApplyDetailMapper;
	@Autowired
	private PdAllocationRecordMapper pdAllocationRecordMapper;
	@Autowired
	private PdAllocationDetailMapper pdAllocationDetailMapper;
	@Autowired
	private IPdProductStockTotalService pdProductStockTotalService;
	@Autowired
	private IPdStockLogService pdStockLogService;
	@Autowired
	private IPdStockRecordDetailService pdStockRecordDetailService;
	@Autowired
	private ISysDepartService sysDepartService;
	@Autowired
	private IPdGoodsAllocationService pdGoodsAllocationService;
	@Autowired
	private ISysDictService sysDictService;
	@Autowired
	private IPdPurchaseDetailService pdPurchaseDetailService;
	@Autowired
	private IPdDepartService pdDepartService;
	@Autowired
	private IPdApplyDetailService pdApplyDetailService;
	@Autowired
	private IPdAllocationDetailService pdAllocationDetailService;

	@Override
	@Transactional
	public void saveMain(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String recordType) {
		if(PdConstant.RECODE_TYPE_1.equals(recordType)){
			this.saveInStockRecord(pdStockRecord, pdStockRecordDetailList, "");
		}else if(PdConstant.RECODE_TYPE_2.equals(recordType)){
			this.saveOutStockRecord(pdStockRecord, pdStockRecordDetailList);
		}
	}

	private void saveInStockRecord(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList, String outType){
		if(oConvertUtils.isNotEmpty(outType)){
			if(PdConstant.OUT_TYPE_3.equals(outType)){
				pdStockRecord.setInType(PdConstant.IN_TYPE_3);
			}else{
				pdStockRecord.setInType(PdConstant.IN_TYPE_1);
			}
			pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_2);   // 审核通过
		}else{
			pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
		}
		pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1); // 入库
		pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); // 已提交
		pdStockRecordMapper.insert(pdStockRecord);

		if(CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
			for(PdStockRecordDetail entity : pdStockRecordDetailList) {
				String expDate = DateUtils.date2Str(entity.getExpDate(),DateUtils.yyMMdd.get());
				if(oConvertUtils.isEmpty(entity.getProductBarCode())){
					boolean bool = true;
					for(PdStockRecordDetail item : pdStockRecordDetailList) {
						String itemExpDate = DateUtils.date2Str(item.getExpDate(),DateUtils.yyMMdd.get());
						if(oConvertUtils.isNotEmpty(item.getProductBarCode())
								&& entity.getProductId().equals(item.getProductId())
								&& entity.getBatchNo().equals(item.getBatchNo()) && expDate.equals(itemExpDate)){
							entity.setProductBarCode(item.getProductBarCode());
							bool = false;
							break;
						}
					}
					if(bool){
						entity.setProductBarCode("01" + entity.getProductNumber() + "17" + expDate + "10" + entity.getBatchNo());
					}
				}

				if(oConvertUtils.isNotEmpty(entity.getOrderNo())){
					PdPurchaseDetail pdPurchaseDetail = new PdPurchaseDetail();
					pdPurchaseDetail.setOrderNo(entity.getOrderNo());
					pdPurchaseDetail.setProductId(entity.getProductId());
					pdPurchaseDetail.setArrivalNum(entity.getProductNum());
//					增加到货数量至采购订单明细表
					pdPurchaseDetailMapper.additionArrivalNum(pdPurchaseDetail);
				}
				entity.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
				entity.setRecordId(pdStockRecord.getId());//外键设置
				entity.setDelFlag(PdConstant.DEL_FLAG_0);
				if(PdConstant.OUT_TYPE_1.equals(outType)){
					entity.setImportNo(pdStockRecord.getApplyNo());
				}else if(PdConstant.OUT_TYPE_3.equals(outType)){
					entity.setImportNo(pdStockRecord.getAllocationNo());
				}
				pdStockRecordDetailMapper.insert(entity);
			}
		}
	}

	private void saveOutStockRecord(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList){
		pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_2); // 出库
		pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); // 待提交
		pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
		pdStockRecordMapper.insert(pdStockRecord);

		if(CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
			Double arrivalApplyCount = 0D;
			Double arrivalAllocationCount = 0D;
			for(PdStockRecordDetail entity : pdStockRecordDetailList) {
				//更新申领单发货数量
				if(oConvertUtils.isNotEmpty(pdStockRecord.getApplyNo())){
					PdApplyDetail pdApplyDetail = new PdApplyDetail();
					pdApplyDetail.setProductId(entity.getProductId());
					pdApplyDetail.setApplyNo(pdStockRecord.getApplyNo());
					pdApplyDetail.setArrivalNum(entity.getProductNum());
					pdApplyDetailMapper.additionArrivalNum(pdApplyDetail);
					arrivalApplyCount = arrivalApplyCount + entity.getProductNum();
				}

				//更新调拨单发货数量
				if(oConvertUtils.isNotEmpty(pdStockRecord.getAllocationNo())){
					PdAllocationDetail pdAllocationDetail = new PdAllocationDetail();
					pdAllocationDetail.setProductId(entity.getProductId());
					pdAllocationDetail.setAllocationNo(pdStockRecord.getAllocationNo());
					pdAllocationDetail.setArrivalNum(entity.getProductNum());
					pdAllocationDetailMapper.additionArrivalNum(pdAllocationDetail);
					arrivalAllocationCount = arrivalAllocationCount + entity.getProductNum();
				}

				entity.setId(null);//初始化ID (从前端传过来会自带页面列表行的ID)
				entity.setRecordId(pdStockRecord.getId());//外键设置
				entity.setDelFlag(PdConstant.DEL_FLAG_0);
				pdStockRecordDetailMapper.insert(entity);
			}

			//更新申领单发货总数量
			if(oConvertUtils.isNotEmpty(pdStockRecord.getApplyNo())){
				PdApplyOrder pdApplyOrder = new PdApplyOrder();
				pdApplyOrder.setApplyNo(pdStockRecord.getApplyNo());
				pdApplyOrder.setArrivalCount(arrivalApplyCount);
				pdApplyOrderMapper.additionArrivalCount(pdApplyOrder);
			}

			//更新调拨单发货总数量
			if(oConvertUtils.isNotEmpty(pdStockRecord.getAllocationNo())){
				PdAllocationRecord pdAllocationRecord = new PdAllocationRecord();
				pdAllocationRecord.setAllocationNo(pdStockRecord.getAllocationNo());
				pdAllocationRecord.setArrivalCount(arrivalAllocationCount);
				pdAllocationRecordMapper.additionArrivalCount(pdAllocationRecord);
			}

		}
	}

	@Override
	@Transactional
	public void updateMain(PdStockRecord pdStockRecord,List<PdStockRecordDetail> pdStockRecordDetailList) {
		pdStockRecordMapper.updateById(pdStockRecord);
		
		//1.先删除子表数据
		pdStockRecordDetailMapper.deleteByMainId(pdStockRecord.getId());
		
		//2.子表数据重新插入
		if(pdStockRecordDetailList!=null && pdStockRecordDetailList.size()>0) {
			for(PdStockRecordDetail entity:pdStockRecordDetailList) {
				//外键设置
				entity.setRecordId(pdStockRecord.getId());
				pdStockRecordDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdStockRecordDetailMapper.deleteByMainId(id);
		pdStockRecordMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdStockRecordDetailMapper.deleteByMainId(id.toString());
			pdStockRecordMapper.deleteById(id);
		}
	}

	@Override
	public List<PdStockRecord> queryList(PdStockRecord pdStockRecord) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdStockRecord.setDepartParentId(sysUser.getDepartParentId());
		return pdStockRecordMapper.selectList(pdStockRecord);
	}

	@Override
	public Page<PdStockRecord> queryList(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdStockRecord.setDepartParentId(sysUser.getDepartParentId());
		return pageList.setRecords(pdStockRecordMapper.selectList(pdStockRecord));
	}

	@Override
	public PdStockRecord getOne(PdStockRecord pdStockRecord) {
		return pdStockRecordMapper.getOne(pdStockRecord);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String,String> audit(PdStockRecord auditEntity,PdStockRecord pdStockRecord, String recordType) {
		Map<String,String> result = new HashMap<>();
		if(PdConstant.RECODE_TYPE_1.equals(recordType)){
			result = this.auditIn(auditEntity, pdStockRecord);
		}else if(PdConstant.RECODE_TYPE_2.equals(recordType)){
			result = this.auditOut(auditEntity, pdStockRecord);
		}
		return result;
	}

	/**
	 * 入库审核
	 * @param auditEntity
	 * @param pdStockRecord
	 * @return
	 */
	private Map<String,String> auditIn(PdStockRecord auditEntity, PdStockRecord pdStockRecord) {
		Map<String,String> result = new HashMap<>();

		if(PdConstant.AUDIT_STATE_2.equals(auditEntity.getAuditStatus())){
			//审核通过
			String inType = pdStockRecord.getInType();

			if(PdConstant.IN_TYPE_1.equals(inType)){  //正常入库
				// TODO 紧急产品处理逻辑

			}else if(PdConstant.IN_TYPE_2.equals(inType)){  //退货入库


			}else if(PdConstant.IN_TYPE_3.equals(inType)) {  //调拨入库

			}

			PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
			pdStockRecordDetail.setRecordId(pdStockRecord.getId());
			List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailMapper.selectByMainId(pdStockRecordDetail);
			pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);
			//处理库存
			String inStr = pdProductStockTotalService.updateInStock(pdStockRecord);

			if(PdConstant.TRUE.equals(inStr)){
				//保存出入库记录日志
				this.saveStockLog(pdStockRecord,inType,"");
				result.put("code",PdConstant.SUCCESS_200);
				result.put("message","审核成功！");
			}else{
				result.put("code",PdConstant.FAIL_500);
				result.put("message",inStr);
				throw new RuntimeException(inStr);
			}
		}else if(PdConstant.AUDIT_STATE_3.equals(auditEntity.getAuditStatus())){
			//驳回
			result.put("code",PdConstant.SUCCESS_200);
			result.put("message","驳回成功！");
		}

		// 变更审批意见 以及 审批状态
		auditEntity.setAuditDate(DateUtils.getDate());
		pdStockRecordMapper.updateById(auditEntity);

		return result;
	}

	/**
	 * 出库审核
	 * @param auditEntity
	 * @param pdStockRecord
	 * @return
	 */
	private Map<String,String> auditOut(PdStockRecord auditEntity,PdStockRecord pdStockRecord) {
		Map<String,String> result = new HashMap<>();

		if(PdConstant.AUDIT_STATE_2.equals(auditEntity.getAuditStatus())){      //审核通过

			String outType = pdStockRecord.getOutType(); //出库类型

			PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
			pdStockRecordDetail.setRecordId(pdStockRecord.getId());
			List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailMapper.selectByMainId(pdStockRecordDetail);
			pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);
			//1.处理出库库存
			String inStr = pdProductStockTotalService.updateOutStock(pdStockRecord);
			//2.保存出库日志记录
			this.saveStockLog(pdStockRecord,"",outType);

			//3.更新申领单 调拨单出库状态  这步暂时没有

			//4.生成入库单
			this.saveInStockRecord(pdStockRecord, pdStockRecordDetailList, outType);
			//5.处理入库库存
			String inStr2 = pdProductStockTotalService.updateInStock(pdStockRecord);
			//6.保存入库日志记录
			this.saveStockLog(pdStockRecord,PdConstant.IN_TYPE_1,"");

			if(PdConstant.TRUE.equals(inStr) && PdConstant.TRUE.equals(inStr2)){
				result.put("code",PdConstant.SUCCESS_200);
				result.put("message","审核成功！");
			}else{
				result.put("code",PdConstant.FAIL_500);
				result.put("message",inStr);
				throw new RuntimeException(inStr);
			}
		}else if(PdConstant.AUDIT_STATE_3.equals(auditEntity.getAuditStatus())){   //驳回

			result.put("code",PdConstant.SUCCESS_200);
			result.put("message","驳回成功！");
		}

		//6.变更审批意见 以及 审批状态
		auditEntity.setAuditDate(DateUtils.getDate());
		pdStockRecordMapper.updateById(auditEntity);

		return result;
	}

	@Override
	public PdStockRecord initInModal(String id) {
		PdStockRecord pdStockRecord = new PdStockRecord();

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
		pdGoodsAllocation.setDepartId(sysUser.getCurrentDepartId());
		pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
		List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);

		if(StringUtils.isNotEmpty(id)){ // 查看页面
			pdStockRecord = this.getById(id);

			//查入库单明细
			PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
			pdStockRecordDetail.setRecordId(id);
			List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
			BigDecimal totalPrice = new BigDecimal(0);//总金额	@TableField(exist = false)
			Double totalSum = new Double(0);//总数量
			for (PdStockRecordDetail item : pdStockRecordDetailList) {
				totalSum = totalSum + item.getProductNum();
				BigDecimal purchasePrice = item.getPurchasePrice() == null ? new BigDecimal(0) : item.getPurchasePrice();
				totalPrice = totalPrice.add(purchasePrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
			}
			pdStockRecord.setTotalSum(totalSum);
			pdStockRecord.setTotalPrice(totalPrice);
			pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);

			if(StringUtils.isNotEmpty(pdStockRecord.getOrderNo())){
				//查订单列表
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
			pdStockRecord.setInDepartId(sysUser.getCurrentDepartId());
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

		return pdStockRecord;
	}

	@Override
	public PdStockRecord initOutModal(String id) {
		PdStockRecord pdStockRecord = new PdStockRecord();

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		SysDepart sysDepart = pdDepartService.getById(sysUser.getCurrentDepartId());

		//部门列表
		SysDepart query = new SysDepart();
		query.setDepartParentId(sysUser.getDepartParentId());
		query.setDepartId(sysUser.getCurrentDepartId());
//		List<SysDepart> sysDepartList = pdDepartService.selectList(query);

		if (oConvertUtils.isNotEmpty(id)) { // 查看页面
			pdStockRecord = this.getById(id);

			//查入库单明细
			PdStockRecordDetail pdStockRecordDetail = new PdStockRecordDetail();
			pdStockRecordDetail.setRecordId(id);
			List<PdStockRecordDetail> pdStockRecordDetailList = pdStockRecordDetailService.selectByMainId(pdStockRecordDetail);
			BigDecimal totalPrice = new BigDecimal(0);//总金额
			Double totalSum = new Double(0);//总数量
			for (PdStockRecordDetail item : pdStockRecordDetailList) {
				totalSum = totalSum + item.getProductNum();
				BigDecimal sellingPrice = item.getSellingPrice() == null ? new BigDecimal(0) : item.getSellingPrice();
				totalPrice = totalPrice.add(sellingPrice.multiply(BigDecimal.valueOf(item.getProductNum())).setScale(4, BigDecimal.ROUND_HALF_UP));
			}
			pdStockRecord.setTotalSum(totalSum);
			pdStockRecord.setTotalPrice(totalPrice);
			pdStockRecord.setPdStockRecordDetailList(pdStockRecordDetailList);

			//查申领单列表
			if (oConvertUtils.isNotEmpty(pdStockRecord.getApplyNo())) {
				List<PdApplyDetail> pdApplyDetailList = pdApplyDetailService.selectByApplyNo(pdStockRecord.getApplyNo());
				pdStockRecord.setPdApplyDetailList(pdApplyDetailList);
			}
			//查调拨单列表
			if (oConvertUtils.isNotEmpty(pdStockRecord.getAllocationNo())) {
				List<PdAllocationDetail> pdAllocationDetailList = pdAllocationDetailService.selectByAllocationNo(pdStockRecord.getAllocationNo());
				pdStockRecord.setPdAllocationDetailList(pdAllocationDetailList);
			}

			//库区库位下拉框
			PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
			pdGoodsAllocation.setDepartId(pdStockRecord.getInDepartId());
			pdGoodsAllocation.setAreaType(PdConstant.GOODS_ALLCATION_AREA_TYPE_2);
			List<PdGoodsAllocationPage> goodsAllocationList = pdGoodsAllocationService.getOptionsForSelect(pdGoodsAllocation);
			pdStockRecord.setGoodsAllocationList(goodsAllocationList);
		} else {  // 新增页面


			pdStockRecord.setOutDepartId(sysDepart.getId());
			pdStockRecord.setOutDepartName(sysDepart.getDepartName());
			//获取出库单号
			pdStockRecord.setRecordNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_CK));
			//获取当前日期
			pdStockRecord.setSubmitDateStr(DateUtils.formatDate());
			pdStockRecord.setSubmitDate(DateUtils.getDate());
			//登录人姓名
			pdStockRecord.setSubmitBy(sysUser.getId());
			pdStockRecord.setSubmitByName(sysUser.getRealname());
			//默认入库类型
//			pdStockRecord.setInType(PdConstant.IN_TYPE_1);
		}

		//库区库位下拉框
//		pdStockRecord.setGoodsAllocationList(goodsAllocationList);
		//部门下拉框
//		pdStockRecord.setSysDepartList(sysDepartList);

		return pdStockRecord;
	}

	/**
	 * 保存出入库记录日志
	 * @param pdStockRecord
	 */
	private void saveStockLog(PdStockRecord pdStockRecord, String inType, String outType){
		//日志
		List<PdStockRecordDetail> detail = pdStockRecord.getPdStockRecordDetailList();
		List<PdStockLog> logList = new ArrayList<PdStockLog>();
		PdStockLog stockLog;
		for(PdStockRecordDetail psd : detail){
			stockLog = new PdStockLog();

			stockLog.setInvoiceNo(pdStockRecord.getRecordNo());
			stockLog.setProductId(psd.getProductId());
			stockLog.setProductBarCode(psd.getProductBarCode());
			stockLog.setBatchNo(psd.getBatchNo());
			stockLog.setProductNum(psd.getProductNum());
			if(StringUtils.isNotEmpty(pdStockRecord.getSupplierId())){
				stockLog.setInFrom(pdStockRecord.getSupplierName());
			}else{
				stockLog.setInFrom(pdStockRecord.getOutDepartName());
			}
			stockLog.setOutTo(pdStockRecord.getInDepartName());
			if(oConvertUtils.isNotEmpty(inType)){ // 入库
				if(PdConstant.IN_TYPE_2.equals(inType)){
					stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_5);
				}else{
					stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_1);
				}
			}else if(oConvertUtils.isNotEmpty(outType)){ //出库
				stockLog.setLogType(PdConstant.STOCK_LOG_TYPE_2);
			}
			stockLog.setRecordTime(DateUtils.getDate());
			logList.add(stockLog);
		}
		pdStockLogService.saveBatch(logList);
	}

	@Override
	public Page<PdStockRecord> selectTransferList(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord) {
 		return pageList.setRecords(pdStockRecordMapper.selectTransferList(pdStockRecord));
	}
}
