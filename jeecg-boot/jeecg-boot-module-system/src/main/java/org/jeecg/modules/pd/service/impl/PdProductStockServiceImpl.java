package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.mapper.PdProductStockMapper;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
	 * @param productStock
	 * @return
	 */
	@Override
	public Page<PdProductStock> selectList(Page<PdProductStock> page, PdProductStock productStock) {
		return page.setRecords(pdProductStockMapper.selectList(productStock));
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

	/**
	 * 物流追溯产品列表
	 * @param pdProductStock
	 * @return
	 */
	@Override
	public List<PdProductStock> getByOriginalProduct(PdProductStock pdProductStock){
		return pdProductStockMapper.getByOriginalProduct(pdProductStock);
	}

}
