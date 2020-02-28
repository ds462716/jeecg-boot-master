package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdProductStockCheckChild;
import org.jeecg.modules.pd.mapper.PdProductStockCheckChildMapper;
import org.jeecg.modules.pd.service.IPdProductStockCheckChildService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 盘点明细表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Service
public class PdProductStockCheckChildServiceImpl extends ServiceImpl<PdProductStockCheckChildMapper, PdProductStockCheckChild> implements IPdProductStockCheckChildService {
	
	@Autowired
	private PdProductStockCheckChildMapper pdProductStockCheckChildMapper;
	
	@Override
	public List<PdProductStockCheckChild> selectByMainId(String mainId) {
		return pdProductStockCheckChildMapper.selectByMainId(mainId);
	}
}
