package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.MessageConstant;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdPackageRecord;
import org.jeecg.modules.pd.entity.PdPackageRecordDetail;
import org.jeecg.modules.pd.mapper.PdPackageRecordDetailMapper;
import org.jeecg.modules.pd.mapper.PdPackageRecordMapper;
import org.jeecg.modules.pd.service.IPdPackageRecordService;
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
	
	@Override
	@Transactional
	public void saveMain(PdPackageRecord pdPackageRecord, List<PdPackageRecordDetail> pdPackageRecordDetailList) {
		pdPackageRecordMapper.insert(pdPackageRecord);
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
		pdPackageRecordDetailMapper.deleteByMainId(id);
		pdPackageRecordMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdPackageRecordDetailMapper.deleteByMainId(id.toString());
			pdPackageRecordMapper.deleteById(id);
		}
	}

	@Override
	public List<PdPackageRecord> queryList(PdPackageRecord pdPackageRecord) {
		return pdPackageRecordMapper.queryList(pdPackageRecord);
	}

	@Override
	public Page<PdPackageRecord> queryList(Page<PdPackageRecord> pageList, PdPackageRecord pdPackageRecord) {
		return pageList.setRecords(pdPackageRecordMapper.queryList(pdPackageRecord));
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
}
