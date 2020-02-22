package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;

import java.util.List;

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
	 * @param stockTotalPage
	 * @return
	 */
	Page<PdProductStock> selectList(Page<PdProductStock> pageList, PdProductStockTotalPage stockTotalPage);
	/**
	 * 查询不分页
	 */
	List<PdProductStock> findListForQuery(PdProductStockTotalPage stockTotalPage);


	public List<PdProductStock> selectByMainId(String mainId);

    List<PdProductStock> selectList(PdProductStock pdProductStock);
}
