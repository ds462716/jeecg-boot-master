package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdPackageRecord;
import org.jeecg.modules.pd.entity.PdPackageRecordDetail;
import org.jeecg.modules.pd.mapper.PdPackageRecordDetailMapper;
import org.jeecg.modules.pd.mapper.PdPackageRecordMapper;
import org.jeecg.modules.pd.service.IPdPackageRecordService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

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
}
