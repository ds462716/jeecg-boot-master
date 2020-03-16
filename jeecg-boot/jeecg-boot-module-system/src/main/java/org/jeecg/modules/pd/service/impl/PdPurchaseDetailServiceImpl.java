package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.mapper.PdPurchaseDetailMapper;
import org.jeecg.modules.pd.service.IPdPurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 申购单详细表
 * @Author: jeecg-boot
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Service
public class PdPurchaseDetailServiceImpl extends ServiceImpl<PdPurchaseDetailMapper, PdPurchaseDetail> implements IPdPurchaseDetailService {
	
	@Autowired
	private PdPurchaseDetailMapper pdPurchaseDetailMapper;
	
	@Override
	public List<PdPurchaseDetail> queryPdPurchaseDetail(PdPurchaseOrder purchaseOrder) {
		return pdPurchaseDetailMapper.queryPdPurchaseDetail(purchaseOrder);
	}
}
