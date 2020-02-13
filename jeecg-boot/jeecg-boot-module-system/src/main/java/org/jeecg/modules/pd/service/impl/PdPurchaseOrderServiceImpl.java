package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.mapper.PdPurchaseDetailMapper;
import org.jeecg.modules.pd.mapper.PdPurchaseOrderMapper;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 申购订单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Service
public class PdPurchaseOrderServiceImpl extends ServiceImpl<PdPurchaseOrderMapper, PdPurchaseOrder> implements IPdPurchaseOrderService {

	@Autowired
	private PdPurchaseOrderMapper pdPurchaseOrderMapper;
	@Autowired
	private PdPurchaseDetailMapper pdPurchaseDetailMapper;



	/**
	 * 查询列表
	 * @param page
	 * @param pdPurchaseOrder
	 * @return
	 */
	@Override
	public Page<PdPurchaseOrder> selectList(Page<PdPurchaseOrder> page, PdPurchaseOrder pdPurchaseOrder) {
		return page.setRecords(pdPurchaseOrderMapper.selectList(pdPurchaseOrder));
	}

	@Override
	public List<PdPurchaseOrder> list(PdPurchaseOrder pdPurchaseOrder) {
		return pdPurchaseOrderMapper.selectList(pdPurchaseOrder);
	}


	@Override
	@Transactional
	public void saveMain(PdPurchaseOrder pdPurchaseOrder, List<PdPurchaseDetail> pdPurchaseDetailList) {
		pdPurchaseOrderMapper.insert(pdPurchaseOrder);
		if(pdPurchaseDetailList!=null && pdPurchaseDetailList.size()>0) {
			for(PdPurchaseDetail entity:pdPurchaseDetailList) {
				//外键设置
				entity.setOrderNo(pdPurchaseOrder.getOrderNo());
				pdPurchaseDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdPurchaseOrder pdPurchaseOrder,List<PdPurchaseDetail> pdPurchaseDetailList) {
		pdPurchaseOrderMapper.updateById(pdPurchaseOrder);
		
		//1.先删除子表数据
		pdPurchaseDetailMapper.deleteByOrderNo(pdPurchaseOrder.getOrderNo());
		
		//2.子表数据重新插入
		if(pdPurchaseDetailList!=null && pdPurchaseDetailList.size()>0) {
			for(PdPurchaseDetail entity:pdPurchaseDetailList) {
				//外键设置
				entity.setOrderNo(pdPurchaseOrder.getOrderNo());
				pdPurchaseDetailMapper.insert(entity);
			}
		}
	}

}
