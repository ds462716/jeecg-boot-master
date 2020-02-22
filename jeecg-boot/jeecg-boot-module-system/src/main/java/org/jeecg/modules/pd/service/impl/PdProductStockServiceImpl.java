package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.mapper.PdProductStockMapper;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

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


	/**
	 * 查询列表
	 * @param page
	 * @param stockTotalPage
	 * @return
	 */
	@Override
	public Page<PdProductStock> selectList(Page<PdProductStock> page, PdProductStockTotalPage stockTotalPage) {
		return page.setRecords(pdProductStockMapper.selectList(stockTotalPage));
	}

	@Override
	public List<PdProductStock> findListForQuery(PdProductStockTotalPage stockTotalPage) {
		return pdProductStockMapper.selectList(stockTotalPage);
	}

	@Override
	public List<PdProductStock> selectByMainId(String mainId) {
		return pdProductStockMapper.selectByMainId(mainId);
	}

	/**
	 * 根据各种条件查询库存
	 * @param pdProductStock
	 * @return
	 */
	@Override
	public List<PdProductStock> selectList(PdProductStock pdProductStock) {
		return pdProductStockMapper.selectList(pdProductStock);
	}


	@Override
	@Transactional
	public void updateProductStock(PdProductStock productStock) {
		pdProductStockMapper.updateProductStock(productStock);
	}
}
