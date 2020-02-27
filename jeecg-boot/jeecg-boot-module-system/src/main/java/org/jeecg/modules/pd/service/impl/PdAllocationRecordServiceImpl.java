package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdAllocationRecord;
import org.jeecg.modules.pd.entity.PdAllocationDetail;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecg.modules.pd.mapper.PdAllocationDetailMapper;
import org.jeecg.modules.pd.mapper.PdAllocationRecordMapper;
import org.jeecg.modules.pd.service.IPdAllocationRecordService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

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
	 * @param pdApplyOrder
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
		pdAllocationDetailMapper.deleteByMainId(pdAllocationRecord.getId());
		
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
	
}
