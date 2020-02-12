package org.jeecg.modules.pd.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IPdApplyOrderService extends IService<PdApplyOrder> {

	/**
	 * 分页查询
	 * @param pageList
	 * @param pdApplyOrder
	 * @return
	 */
	Page<PdApplyOrder> selectList(Page<PdApplyOrder> pageList, PdApplyOrder pdApplyOrder);

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PdApplyOrder pdApplyOrder, List<PdApplyDetail> pdApplyDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PdApplyOrder pdApplyOrder, List<PdApplyDetail> pdApplyDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
