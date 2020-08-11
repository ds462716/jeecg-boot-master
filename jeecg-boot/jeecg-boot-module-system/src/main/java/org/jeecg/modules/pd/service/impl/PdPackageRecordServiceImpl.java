package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.MessageConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdPackageRecord;
import org.jeecg.modules.pd.entity.PdPackageRecordDetail;
import org.jeecg.modules.pd.entity.PdStockLog;
import org.jeecg.modules.pd.mapper.PdPackageRecordDetailMapper;
import org.jeecg.modules.pd.mapper.PdPackageRecordMapper;
import org.jeecg.modules.pd.service.IPdPackageRecordService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.service.IPdStockLogService;
import org.jeecg.modules.pd.util.BarCodeUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.*;

/**
 * @Description: pd_package_record
 * @Author: jiangxz
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@Service
public class PdPackageRecordServiceImpl extends ServiceImpl<PdPackageRecordMapper, PdPackageRecord> implements IPdPackageRecordService {

	@Autowired
	private PdPackageRecordMapper pdPackageRecordMapper;
	@Autowired
	private PdPackageRecordDetailMapper pdPackageRecordDetailMapper;
	@Autowired
	private IPdProductStockTotalService pdProductStockTotalService;
	@Autowired
	private IPdStockLogService pdStockLogService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> saveMain(PdPackageRecord pdPackageRecord, List<PdPackageRecordDetail> pdPackageRecordDetailList) {
		Map<String, String> result = new HashMap<>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdPackageRecord.setDepartId(sysUser.getCurrentDepartId());
		pdPackageRecord.setDepartParentId(sysUser.getDepartParentId());
		pdPackageRecord.setStatus(PdConstant.PACKAGE_RECORD_STATUS_1);
		pdPackageRecordMapper.insert(pdPackageRecord);
		if(pdPackageRecordDetailList!=null && pdPackageRecordDetailList.size()>0) {
			for(PdPackageRecordDetail entity:pdPackageRecordDetailList) {
				//外键设置
				entity.setRecordId(pdPackageRecord.getId());
				pdPackageRecordDetailMapper.insert(entity);
			}
		}

		// 扣减库存
		String retStr = pdProductStockTotalService.updateOutStockForPackage(pdPackageRecord);

		if (PdConstant.TRUE.equals(retStr)) {
			//保存出入库记录日志
			this.saveStockLog(pdPackageRecord, PdConstant.STOCK_LOG_TYPE_8);
			result.put("code", PdConstant.SUCCESS_200);
			result.put("message", "审打包成功！");
		} else {
			result.put("code", PdConstant.FAIL_500);
			result.put("message", retStr);
			throw new RuntimeException(retStr);
		}

		return result;
	}

	@Override
	@Transactional
	public void updateMain(PdPackageRecord pdPackageRecord,List<PdPackageRecordDetail> pdPackageRecordDetailList) {
		pdPackageRecordMapper.updateById(pdPackageRecord);
		
		//1.先删除子表数据
		pdPackageRecordDetailMapper.deleteByMainId(pdPackageRecord.getId());
		
		//2.子表数据重新插入
		if(pdPackageRecordDetailList!=null && pdPackageRecordDetailList.size()>0) {
			for(PdPackageRecordDetail entity:pdPackageRecordDetailList) {
				//外键设置
				entity.setRecordId(pdPackageRecord.getId());
				pdPackageRecordDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		PdPackageRecord pdPackageRecord = pdPackageRecordMapper.selectById(id);
		List<PdPackageRecordDetail> pdPackageRecordDetailList = pdPackageRecordDetailMapper.selectByMainId(id);
		pdPackageRecord.setPdPackageRecordDetailList(pdPackageRecordDetailList);
		// 拆包 库存还回
		pdProductStockTotalService.updateInStockForPackage(pdPackageRecord);

		pdPackageRecordMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
//			pdPackageRecordDetailMapper.deleteByMainId(id.toString());
			pdPackageRecordMapper.deleteById(id);
		}
	}

	@Override
	public List<PdPackageRecord> queryList(PdPackageRecord pdPackageRecord) {
		return pdPackageRecordMapper.queryList(pdPackageRecord);
	}

	@Override
	public IPage<PdPackageRecord> queryList(Page<PdPackageRecord> pageList, PdPackageRecord pdPackageRecord) {
		return pdPackageRecordMapper.queryList(pageList, pdPackageRecord);
	}

	@Override
	public Result<Map> packageRecordScanCode(String Barcode1, Result<Map> result) {

		Map<String,Object> resultMap = new HashMap<>();
		//去空格转为大写
		Barcode1 = BarCodeUtil.trimStr(Barcode1.toUpperCase());
		if(oConvertUtils.isNotEmpty(Barcode1)){
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			PdPackageRecord pdPackageRecord = new PdPackageRecord();
			pdPackageRecord.setDepartParentId(sysUser.getDepartParentId());
			pdPackageRecord.setStatus(PdConstant.PACKAGE_RECORD_STATUS_1);
			pdPackageRecord.setPackageBarCode(Barcode1);
			List<PdPackageRecord> packageRecordList = this.queryList(pdPackageRecord);

			if(CollectionUtils.isEmpty(packageRecordList)){
				resultMap.put("code",MessageConstant.ICODE_STATE_500);
				resultMap.put("msg",MessageConstant.PACKAGE_CODE_MESSAGE_4);
				result.setResult(resultMap);
				return  result;
			}

			if(packageRecordList.size() > 1){
				resultMap.put("code",MessageConstant.ICODE_STATE_500);
				resultMap.put("msg",MessageConstant.PACKAGE_CODE_MESSAGE_3);
				result.setResult(resultMap);
				return  result;
			}

			if(packageRecordList.size() == 1){
				PdPackageRecord record = packageRecordList.get(0);
				List<PdPackageRecordDetail> detailList = pdPackageRecordDetailMapper.selectByMainId(record.getId());
				record.setPdPackageRecordDetailList(detailList);
				resultMap.put("pdPackageRecord",record);
				resultMap.put("code",MessageConstant.ICODE_STATE_200);
				resultMap.put("msg",MessageConstant.PACKAGE_CODE_MESSAGE_2);

				for (PdPackageRecordDetail detail : detailList) {
					if(detail.getExpDate() != null){
						String expDate = DateUtils.date2Str(detail.getExpDate(),DateUtils.yyyy_MM_dd.get());
						long difDays = BarCodeUtil.dateDiff(expDate);
						//效期提醒时间
						if (difDays < PdConstant.REMINDER_TIME){
							if (difDays < 0) {
								//产品过期
								resultMap.put("code",MessageConstant.CODE_STATE_201);
								resultMap.put("msg", MessageConstant.PACKAGE_CODE_MESSAGE_6);
								break;
							}else{
								//近有效期
								resultMap.put("code",MessageConstant.ICODE_STATE_203);
								resultMap.put("msg",MessageConstant.PACKAGE_CODE_MESSAGE_7);
								break;
							}
						}
					}
				}
			}
		}else{
			result.setCode(MessageConstant.ICODE_STATE_500);
			result.setMessage(MessageConstant.PACKAGE_CODE_MESSAGE_5);
		}
		result.setResult(resultMap);
		return  result;
	}

	/**
	 * 保存套包打包日志
	 * @param pdPackageRecord
	 * @param type
	 */
	private void saveStockLog(PdPackageRecord pdPackageRecord, String type) {
		//日志
		List<PdPackageRecordDetail> detail = pdPackageRecord.getPdPackageRecordDetailList();
		List<PdStockLog> logList = new ArrayList<PdStockLog>();
		PdStockLog stockLog;
		for (PdPackageRecordDetail psd : detail) {
			stockLog = new PdStockLog();

			stockLog.setInvoiceNo(pdPackageRecord.getPackageBarCode());
			stockLog.setProductId(psd.getProductId());
			stockLog.setProductBarCode(psd.getProductBarCode());
			stockLog.setBatchNo(psd.getBatchNo());
			stockLog.setProductNum(psd.getProductNum());
			stockLog.setExpDate(psd.getExpDate());
			stockLog.setInFrom("");
			stockLog.setOutTo("");
			stockLog.setLogType(type);
			stockLog.setRecordTime(DateUtils.getDate());
			logList.add(stockLog);
		}
		pdStockLogService.saveBatch(logList);
	}
}
