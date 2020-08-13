package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
	 * 分页 用于库存选择器
	 *
	 * @param page
	 * @param pdProductStock
	 * @return
	 */
	@Override
	public Page<PdProductStock> chooseProductStockList(Page<PdProductStock> page, PdProductStock pdProductStock) {
		return pdProductStockMapper.chooseProductStockList(page,pdProductStock);
	}

	/**
	 * 不分页，用于套包自动选择库存
	 *
	 * @param pdProductStock
	 * @return
	 */
	@Override
	public List<PdProductStock> chooseProductStockList(PdProductStock pdProductStock) {
		return pdProductStockMapper.chooseProductStockList(pdProductStock);
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

	/**
	 * 唯一码扫码
	 * @param pdProductStock
	 * @return
	 */
	@Override
	public List<PdProductStock> queryUniqueProductStockList(PdProductStock pdProductStock) {
		return pdProductStockMapper.queryUniqueProductStockList(pdProductStock);
	}

	@Override
	public Page<PdProductStock> queryPrintList(Page<PdProductStock> page, PdProductStock productStock) {
		return baseMapper.queryPrintList(page,productStock);
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
	public Page<PdProductStock> getByOriginalProduct(Page<PdProductStock> page, PdProductStock pdProductStock){
		return pdProductStockMapper.getByOriginalProduct(page,pdProductStock);
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
		return  pdProductStockMapper.queryListByPage(page,productStock);
	}

	@Override
	public List<PdProductStock> queryStockList( PdProductStock productStock) {
		return  pdProductStockMapper.queryList(productStock);
	}


	@Override
	@Transactional
	public void updateStockBarCodeType(PdProductStock productStock) {
		pdProductStockMapper.updateStockBarCodeType(productStock);
	}

	public List<Map<String, Object>> queryPdProductStockList(PdProductStock productStock){
		return pdProductStockMapper.queryPdProductStockList(productStock);
	}

}
