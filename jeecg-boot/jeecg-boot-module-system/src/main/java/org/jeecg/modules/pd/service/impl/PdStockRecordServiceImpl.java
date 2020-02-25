package org.jeecg.modules.pd.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdStockRecordDetailMapper;
import org.jeecg.modules.pd.mapper.PdStockRecordMapper;
import org.jeecg.modules.pd.service.IPdStockRecordService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
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
	
	@Override
	@Transactional
	public void saveMain(PdStockRecord pdStockRecord, List<PdStockRecordDetail> pdStockRecordDetailList) {
		pdStockRecord.setRecordType(PdConstant.RECODE_TYPE_1); // 入库
		pdStockRecord.setRecordState(PdConstant.RECODE_STATE_1); // 待提交
		pdStockRecordMapper.insert(pdStockRecord);
		if(CollectionUtils.isNotEmpty(pdStockRecordDetailList)) {
			for(PdStockRecordDetail entity : pdStockRecordDetailList) {
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
	
}
