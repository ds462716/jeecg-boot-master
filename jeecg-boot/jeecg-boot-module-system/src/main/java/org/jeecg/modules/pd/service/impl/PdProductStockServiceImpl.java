package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.mapper.PdProductStockMapper;
import org.jeecg.modules.pd.service.IPdProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		if(oConvertUtils.isNotEmpty(productStock.getProductIds())){
			productStock.setProductIdList(Arrays.asList(productStock.getProductIds().split(",")));
		}
		//productStock.setBarCodeType(PdConstant.CODE_PRINT_TYPE_0);//批量打印的条码类型
		return page.setRecords(pdProductStockMapper.selectList(productStock));
	}

	/**
	 * 以前查询库存的方法
	 * 该方法只查询耗材  过滤试剂，且只能查非唯一码类型
	 * @param page
	 * @param productStock
	 * @return
	 */
	@Override
	public Page<PdProductStock> queryProductStockList(Page<PdProductStock> page, PdProductStock productStock) {
		if(oConvertUtils.isNotEmpty(productStock.getProductIds())){
			productStock.setProductIdList(Arrays.asList(productStock.getProductIds().split(",")));
		}
		return page.setRecords(pdProductStockMapper.queryProductStockList(productStock));
	}

	/**
	 * 以前查询库存的方法
	 * 该方法只查询耗材  过滤试剂，且只能查非唯一码类型
	 * @param pdProductStock
	 * @return
	 */
	@Override
	public List<PdProductStock> queryProductStockList(PdProductStock pdProductStock) {
		return pdProductStockMapper.queryProductStockList(pdProductStock);
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
	public  PdProductStock getOne(PdProductStock pdProductStock) {
		return pdProductStockMapper.getOne(pdProductStock);
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

	@Override
	public Map<String,Object> queryProductStockCount(PdProductStock pdProductStock) {
		Map<String,Object> params = pdProductStockMapper.queryProductStockCount(pdProductStock);
		return params;
	}

	@Override
	public List<HashMap> queryStockDateList(PdProductStock pdProductStock) {
		return pdProductStockMapper.queryStockDateList(pdProductStock);
	}
	@Override
	public List<HashMap> queryStockTotalList(PdProductStock pdProductStock) {
		return pdProductStockMapper.queryStockTotalList(pdProductStock);
	}

	@Override
	public Page<PdProductStock> queryList(Page<PdProductStock> page, PdProductStock productStock) {
		return page.setRecords(pdProductStockMapper.queryList(productStock));
	}


	@Override
	@Transactional
	public void updateStockBarCodeType(PdProductStock productStock) {
		pdProductStockMapper.updateStockBarCodeType(productStock);
	}

}
