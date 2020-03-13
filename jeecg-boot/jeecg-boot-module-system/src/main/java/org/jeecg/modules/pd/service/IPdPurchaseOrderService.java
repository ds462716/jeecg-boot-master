package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;

import java.util.List;

/**
 * @Description: 申购订单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface IPdPurchaseOrderService extends IService<PdPurchaseOrder> {

	/**
	 * 分页查询
	 * @param pageList
	 * @param pdPurchaseOrder
	 * @return
	 */
	Page<PdPurchaseOrder> selectList(Page<PdPurchaseOrder> pageList,PdPurchaseOrder pdPurchaseOrder);

	/**
	 * 列表查询不分页
	 */
	List<PdPurchaseOrder> list(PdPurchaseOrder pdPurchaseOrder);

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
	 * 用于采购单弹出选择框
	 * @param pdPurchaseOrderPage
	 * @return
	 */
	Page<PdPurchaseOrderPage> choosePurchaseOrderList(Page<PdPurchaseOrderPage> pageList, PdPurchaseOrderPage pdPurchaseOrderPage);

	/**
	 * 用于采购单弹出选择框
	 * @param pdPurchaseOrderPage
	 * @return
	 */
	List<PdProductPage> choosePurchaseOrderDetailList(PdPurchaseOrderPage pdPurchaseOrderPage);

	/**
	 * 批量修改订单状态
	 */
	public int audit(String orderNos, String auditStatus, String refuseReason,String submitStatus) ;
}
