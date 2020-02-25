package org.jeecg.modules.pd.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;

/**
 * @Description: 库存明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface PdProductStockMapper extends BaseMapper<PdProductStock> {


	List<PdProductStock> selectList(PdProductStockTotalPage stockTotalPage);

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdProductStock> selectByMainId(@Param("mainId") String mainId);

    List<PdProductStock> selectList(PdProductStock pdProductStock);

	public void updateProductStock(PdProductStock productStock);

	/**
	 * 库存明细锁表查询
	 * 注意：带事务方法调用时使用
	 * @param pdProductStock
	 */
	public List<PdProductStock> findForUpdate(PdProductStock pdProductStock);

	/**
	 * 更新库存明细库存数量
	 * @param pdProductStock
	 */
	public void updateStockNum(PdProductStock pdProductStock);
}
