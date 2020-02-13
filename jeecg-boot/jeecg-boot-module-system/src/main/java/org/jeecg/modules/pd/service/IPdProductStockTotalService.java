package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IPdProductStockTotalService extends IService<PdProductStockTotal> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PdProductStockTotal pdProductStockTotal, List<PdProductStock> pdProductStockList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PdProductStockTotal pdProductStockTotal, List<PdProductStock> pdProductStockList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
