package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.pd.entity.PdAllocationDetail;
import org.jeecg.modules.pd.entity.PdAllocationRecord;
import org.jeecg.modules.pd.mapper.PdAllocationDetailMapper;
import org.jeecg.modules.pd.mapper.PdAllocationRecordMapper;
import org.jeecg.modules.pd.service.IPdAllocationRecordService;
import org.jeecg.modules.pd.vo.PdApplyOrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 调拨记录表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Service
public class PdAllocationRecordServiceImpl extends ServiceImpl<PdAllocationRecordMapper, PdAllocationRecord> implements IPdAllocationRecordService {

	@Autowired
	private PdAllocationRecordMapper pdAllocationRecordMapper;
	@Autowired
	private PdAllocationDetailMapper pdAllocationDetailMapper;



	/**
	 * 查询列表
	 * @param page
	 * @param allocationRecord
	 * @return
	 */
	@Override
	public Page<PdAllocationRecord> selectList(Page<PdAllocationRecord> page, PdAllocationRecord allocationRecord) {
		return page.setRecords(pdAllocationRecordMapper.selectList(allocationRecord));
	}
	@Override
	@Transactional
	public void saveMain(PdAllocationRecord pdAllocationRecord, List<PdAllocationDetail> pdAllocationDetailList) {
		pdAllocationRecordMapper.insert(pdAllocationRecord);
		if(pdAllocationDetailList!=null && pdAllocationDetailList.size()>0) {
			for(PdAllocationDetail entity:pdAllocationDetailList) {
				//外键设置
				entity.setAllocationNo(pdAllocationRecord.getAllocationNo());
				pdAllocationDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdAllocationRecord pdAllocationRecord,List<PdAllocationDetail> pdAllocationDetailList) {
		pdAllocationRecordMapper.updateById(pdAllocationRecord);
		//1.先删除子表数据
		pdAllocationDetailMapper.deleteByMainId(pdAllocationRecord.getAllocationNo());
		//2.子表数据重新插入
		if(pdAllocationDetailList!=null && pdAllocationDetailList.size()>0) {
			for(PdAllocationDetail entity:pdAllocationDetailList) {
				//外键设置
				entity.setAllocationNo(pdAllocationRecord.getAllocationNo());
				pdAllocationDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdAllocationDetailMapper.deleteByMainId(id);
		pdAllocationRecordMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdAllocationDetailMapper.deleteByMainId(id.toString());
			pdAllocationRecordMapper.deleteById(id);
		}
	}
	@Override
	public Page<PdAllocationRecord> chooseAllocationList(Page<PdAllocationRecord> pageList, PdAllocationRecord allocationRecord) {
		List queryDate = allocationRecord.getQueryDate();
		if(CollectionUtils.isNotEmpty(queryDate)){
			allocationRecord.setQueryDateStart((String) queryDate.get(0));
			allocationRecord.setQueryDateStart((String) queryDate.get(1));
		}
		return pageList.setRecords(pdAllocationRecordMapper.chooseAllocationList(allocationRecord));
	}
}
