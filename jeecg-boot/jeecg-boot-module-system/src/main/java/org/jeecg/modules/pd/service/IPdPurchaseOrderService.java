package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMerge;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	Page<PdPurchaseOrderMerge> choosePurchaseOrderList(Page<PdPurchaseOrderMerge> pageList, PdPurchaseOrderPage pdPurchaseOrderPage);

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

	/**
	 * 首页查询采购总数量
	 */
	Map<String,Object> queryPurchaseOrderCount(PdPurchaseOrder pdPurchaseOrder);

	/**
	 * 首页查询  根据范围统计每日的采购量
	 */
	List<HashMap> queryPurchaseOrderDateList(PdPurchaseOrderPage purchaseOrderPage);

}
