package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.mapper.PdProductStockMapper;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 库存明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class PdProductStockServiceImpl extends ServiceImpl<PdProductStockMapper, PdProductStock> implements IPdProductStockService {
	
	@Autowired
	private PdProductStockMapper pdProductStockMapper;
	
	@Override
	public List<PdProductStock> selectByMainId(String mainId) {
		return pdProductStockMapper.selectByMainId(mainId);
	}
}
