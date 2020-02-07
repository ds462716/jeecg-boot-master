package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 申购订单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface IPdPurchaseOrderService extends IService<PdPurchaseOrder> {




	Page<PdPurchaseOrder> selectList(Page<PdPurchaseOrder> pageList,PdPurchaseOrder pdPurchaseOrder);

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PdPurchaseOrder pdPurchaseOrder, List<PdPurchaseDetail> pdPurchaseDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PdPurchaseOrder pdPurchaseOrder, List<PdPurchaseDetail> pdPurchaseDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
