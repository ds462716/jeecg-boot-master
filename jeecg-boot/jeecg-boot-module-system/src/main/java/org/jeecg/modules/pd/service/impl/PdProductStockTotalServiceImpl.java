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
	public void updateProductStock(PdProductStockTotal stockTotal) {
		pdProductStockTotalMapper.updateProductStock(stockTotal);
	}
}
