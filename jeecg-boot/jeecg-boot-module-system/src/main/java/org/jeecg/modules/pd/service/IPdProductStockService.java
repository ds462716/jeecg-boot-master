package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 库存明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IPdProductStockService extends IService<PdProductStock> {


	/**
	 * 分页查询
	 * @param pageList
	 * @param productStock
	 * @return
	 */
	Page<PdProductStock> selectList(Page<PdProductStock> pageList, PdProductStock productStock);

	public List<PdProductStock> selectByMainId(String mainId);

    List<PdProductStock> selectList(PdProductStock pdProductStock);

	/**
	 * 获取一条记录
	 * @param pdProductStock
	 * @return
	 */
	PdProductStock getOne(PdProductStock pdProductStock);
	/**
	 * 更新库存明细表过期状态
	 */
	public void updateProductStock(PdProductStock productStock);

	List<PdProductStock> getByOriginalProduct(PdProductStock pdProductStock);

    /**
     * 首页查询库存总数量
     */
    Map<String,Object> queryProductStockCount(PdProductStock pdProductStock);

	/**
	 * 首页查询  统计前一周的入库库存数量
	 */
	List<HashMap> queryStockDateList(PdProductStock pdProductStock);
	/**
	 * 首页查询  根据库存产品类区分统计库存金额
	 */
	List<HashMap> queryStockTotalList(PdProductStock PdProductStock);

	Page<PdProductStock> queryList(Page<PdProductStock> pageList, PdProductStock productStock);

	/**
	 * 更新条码类型
	 */
	public void updateStockBarCodeType(PdProductStock productStock);
}
