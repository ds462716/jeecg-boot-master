package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.mapper.PdProductStockMapper;
import org.jeecg.modules.pd.mapper.PdProductStockTotalMapper;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class PdProductStockTotalServiceImpl extends ServiceImpl<PdProductStockTotalMapper, PdProductStockTotal> implements IPdProductStockTotalService {

	@Autowired
	private PdProductStockTotalMapper pdProductStockTotalMapper;
	@Autowired
	private PdProductStockMapper pdProductStockMapper;
	
	@Override
	@Transactional
	public void saveMain(PdProductStockTotal pdProductStockTotal, List<PdProductStock> pdProductStockList) {
		pdProductStockTotalMapper.insert(pdProductStockTotal);
		if(pdProductStockList!=null && pdProductStockList.size()>0) {
			for(PdProductStock entity:pdProductStockList) {
				//外键设置
				entity.setStoreroomId(pdProductStockTotal.getId());
				pdProductStockMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdProductStockTotal pdProductStockTotal,List<PdProductStock> pdProductStockList) {
		pdProductStockTotalMapper.updateById(pdProductStockTotal);
		
		//1.先删除子表数据
		pdProductStockMapper.deleteByMainId(pdProductStockTotal.getId());
		
		//2.子表数据重新插入
		if(pdProductStockList!=null && pdProductStockList.size()>0) {
			for(PdProductStock entity:pdProductStockList) {
				//外键设置
				entity.setStoreroomId(pdProductStockTotal.getId());
				pdProductStockMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdProductStockMapper.deleteByMainId(id);
		pdProductStockTotalMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdProductStockMapper.deleteByMainId(id.toString());
			pdProductStockTotalMapper.deleteById(id);
		}
	}
	
}
