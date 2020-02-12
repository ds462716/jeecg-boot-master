package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.mapper.PdApplyDetailMapper;
import org.jeecg.modules.pd.mapper.PdApplyOrderMapper;
import org.jeecg.modules.pd.service.IPdApplyOrderService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class PdApplyOrderServiceImpl extends ServiceImpl<PdApplyOrderMapper, PdApplyOrder> implements IPdApplyOrderService {

	@Autowired
	private PdApplyOrderMapper pdApplyOrderMapper;
	@Autowired
	private PdApplyDetailMapper pdApplyDetailMapper;



	/**
	 * 查询列表
	 * @param page
	 * @param pdApplyOrder
	 * @return
	 */
	@Override
	public Page<PdApplyOrder> selectList(Page<PdApplyOrder> page, PdApplyOrder pdApplyOrder) {
		return page.setRecords(pdApplyOrderMapper.selectList(pdApplyOrder));
	}





	@Override
	@Transactional
	public void saveMain(PdApplyOrder pdApplyOrder, List<PdApplyDetail> pdApplyDetailList) {
		pdApplyOrderMapper.insert(pdApplyOrder);
		if(pdApplyDetailList!=null && pdApplyDetailList.size()>0) {
			for(PdApplyDetail entity:pdApplyDetailList) {
				//外键设置
				entity.setApplyNo(pdApplyOrder.getApplyNo());
				pdApplyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdApplyOrder pdApplyOrder,List<PdApplyDetail> pdApplyDetailList) {
		pdApplyOrderMapper.updateById(pdApplyOrder);
		//1.先删除子表数据
		pdApplyDetailMapper.deleteByApplyNo(pdApplyOrder.getApplyNo());
		//2.子表数据重新插入
		if(pdApplyDetailList!=null && pdApplyDetailList.size()>0) {
			for(PdApplyDetail entity:pdApplyDetailList) {
				//外键设置
				entity.setApplyNo(pdApplyOrder.getApplyNo());
				pdApplyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdApplyDetailMapper.deleteByMainId(id);
		pdApplyOrderMapper.deleteByMainId(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdApplyDetailMapper.deleteByMainId(id.toString());
			pdApplyOrderMapper.deleteByMainId(id.toString());
		}
	}
	
}
