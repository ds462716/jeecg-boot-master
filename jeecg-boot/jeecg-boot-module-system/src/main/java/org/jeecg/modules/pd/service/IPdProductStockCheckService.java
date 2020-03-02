package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdProductStockCheckChild;
import org.jeecg.modules.pd.entity.PdProductStockCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 盘点记录表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
public interface IPdProductStockCheckService extends IService<PdProductStockCheck> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> pdProductStockCheckChildList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> pdProductStockCheckChildList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
