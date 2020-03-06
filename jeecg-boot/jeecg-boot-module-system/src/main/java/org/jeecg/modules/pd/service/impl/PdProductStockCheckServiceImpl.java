package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdProductStockCheck;
import org.jeecg.modules.pd.entity.PdProductStockCheckChild;
import org.jeecg.modules.pd.mapper.PdProductStockCheckChildMapper;
import org.jeecg.modules.pd.mapper.PdProductStockCheckMapper;
import org.jeecg.modules.pd.service.IPdProductStockCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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

	/**
	 * 查询列表
	 * @param page
	 * @param stockCheck
	 * @return
	 */
	@Override
	public Page<PdProductStockCheck> selectList(Page<PdProductStockCheck> page, PdProductStockCheck stockCheck) {
		return page.setRecords(pdProductStockCheckMapper.selectList(stockCheck));
	}

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
		pdProductStockCheckChildMapper.deleteByCheckNo(pdProductStockCheck.getCheckNo());

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
