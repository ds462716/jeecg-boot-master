package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMergeDetail;
import org.jeecg.modules.pd.mapper.PdPurchaseOrderMergeDetailMapper;
import org.jeecg.modules.pd.service.IPdPurchaseOrderMergeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 合并申购明细表
 * @Author: mcb
 * @Date:   2020-03-20
 * @Version: V1.0
 */
@Service
public class PdPurchaseOrderMergeDetailServiceImpl extends ServiceImpl<PdPurchaseOrderMergeDetailMapper, PdPurchaseOrderMergeDetail> implements IPdPurchaseOrderMergeDetailService {
	
	@Autowired
	private PdPurchaseOrderMergeDetailMapper pdPurchaseOrderMergeDetailMapper;
	
	@Override
	public List<PdPurchaseOrderMergeDetail> selectByMainId(String mainId) {
		return pdPurchaseOrderMergeDetailMapper.selectByMainId(mainId);
	}

	@Override
	public List<PdPurchaseOrderMergeDetail> queryPdPurchaseMergeDetail(PdPurchaseOrderMergeDetail orderMergeDetail) {
		return pdPurchaseOrderMergeDetailMapper.queryPdPurchaseMergeDetail(orderMergeDetail);
	}
}
