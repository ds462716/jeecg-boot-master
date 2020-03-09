package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStockCheck;
import org.jeecg.modules.pd.entity.PdProductStockCheckChild;

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
	 * 分页查询
	 * @param pageList
	 * @param stockCheck
	 * @return
	 */
	Page<PdProductStockCheck> selectList(Page<PdProductStockCheck> pageList, PdProductStockCheck stockCheck);

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
