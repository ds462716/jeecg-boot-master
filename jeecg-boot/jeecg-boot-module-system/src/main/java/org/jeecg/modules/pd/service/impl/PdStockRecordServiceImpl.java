package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdPurchaseDetailMapper;
import org.jeecg.modules.pd.mapper.PdStockRecordDetailMapper;
import org.jeecg.modules.pd.mapper.PdStockRecordMapper;
import org.jeecg.modules.pd.service.IPdStockRecordService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

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
	
	@Override
	@Transactional
	public void saveMain(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList) {
		pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1); // 入库
		pdStockRecord.setSubmitStatus(PdConstant.SUBMIT_STATE_2); // 待提交
		pdStockRecord.setAuditStatus(PdConstant.AUDIT_STATE_1);   // 待审核
		pdStockRecordMapper.insert(pdStockRecord);
		List<PdStockRecordDetail> newList = new ArrayList<>();

		if(CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
			for(PdStockRecordDetail entity : pdStockRecordDetailList) {
				entity.setProductBarCode("01" + entity.getProductNumber() + "17" + DateUtils.date2Str(entity.getLimitDate(),DateUtils.yyMMdd.get()) + "10" + entity.getBatchNo());
				if(StringUtils.isNotEmpty(entity.getOrderNo())){
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
				pdStockRecordDetailMapper.insert(entity);
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
		return pdStockRecordMapper.selectList(pdStockRecord);
	}

	@Override
	public Page<PdStockRecord> queryList(Page<PdStockRecord> pageList, PdStockRecord pdStockRecord) {
		return pageList.setRecords(pdStockRecordMapper.selectList(pdStockRecord));
	}

}
