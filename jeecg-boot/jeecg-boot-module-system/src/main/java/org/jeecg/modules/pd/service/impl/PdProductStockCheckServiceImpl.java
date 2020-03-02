package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdProductStockCheck;
import org.jeecg.modules.pd.entity.PdProductStockCheckChild;
import org.jeecg.modules.pd.mapper.PdProductStockCheckChildMapper;
import org.jeecg.modules.pd.mapper.PdProductStockCheckMapper;
import org.jeecg.modules.pd.service.IPdProductStockCheckService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 盘点记录表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Service
public class PdProductStockCheckServiceImpl extends ServiceImpl<PdProductStockCheckMapper, PdProductStockCheck> implements IPdProductStockCheckService {

	@Autowired
	private PdProductStockCheckMapper pdProductStockCheckMapper;
	@Autowired
	private PdProductStockCheckChildMapper pdProductStockCheckChildMapper;
	
	@Override
	@Transactional
	public void saveMain(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> pdProductStockCheckChildList) {
		pdProductStockCheckMapper.insert(pdProductStockCheck);
		if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0) {
			for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
				//外键设置
				entity.setCheckNo(pdProductStockCheck.getCheckNo());
				pdProductStockCheckChildMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdProductStockCheck pdProductStockCheck,List<PdProductStockCheckChild> pdProductStockCheckChildList) {
		pdProductStockCheckMapper.updateById(pdProductStockCheck);
		
		//1.先删除子表数据
		pdProductStockCheckChildMapper.deleteByMainId(pdProductStockCheck.getId());
		
		//2.子表数据重新插入
		if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0) {
			for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
				//外键设置
				entity.setCheckNo(pdProductStockCheck.getCheckNo());
				pdProductStockCheckChildMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdProductStockCheckChildMapper.deleteByMainId(id);
		pdProductStockCheckMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdProductStockCheckChildMapper.deleteByMainId(id.toString());
			pdProductStockCheckMapper.deleteById(id);
		}
	}
	
}
