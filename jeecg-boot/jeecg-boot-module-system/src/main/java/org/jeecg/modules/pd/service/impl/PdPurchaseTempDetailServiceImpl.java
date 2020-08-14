package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdPurchaseTempDetail;
import org.jeecg.modules.pd.mapper.PdPurchaseTempDetailMapper;
import org.jeecg.modules.pd.service.IPdPurchaseTempDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 申购模板详细表
 * @Author: jeecg-boot
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Service
public class PdPurchaseTempDetailServiceImpl extends ServiceImpl<PdPurchaseTempDetailMapper, PdPurchaseTempDetail> implements IPdPurchaseTempDetailService {
	
	@Autowired
	private PdPurchaseTempDetailMapper pdPurchaseTempDetailMapper;
	
	@Override
	public List<PdPurchaseTempDetail> queryPdPurchaseTempDetail(PdPurchaseTempDetail purchaseTempDetail) {
		return pdPurchaseTempDetailMapper.queryPdPurchaseTempDetail(purchaseTempDetail);
	}
}
