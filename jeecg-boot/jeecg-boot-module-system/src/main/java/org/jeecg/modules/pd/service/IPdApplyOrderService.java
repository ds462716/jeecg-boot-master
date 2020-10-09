package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.vo.PdApplyOrderPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 * 用于申领单单弹出选择框
	 * @param applyOrderPage
	 * @return
	 */
	Page<PdApplyOrderPage> chooseApplyOrderList(Page<PdApplyOrderPage> pageList, PdApplyOrderPage applyOrderPage);

	/**
	 * 首页查询申领总数量
	 */
	Map<String,Object> queryApplyOrderCount(PdApplyOrder pdApplyOrder);

	/**
	 * 首页查询  根据范围统计每日的申领量
	 */
	List<HashMap> queryApplyOrderDateList(PdApplyOrderPage applyOrderPage);

	/**
	 * 首页查询  根据申领产品类区分统计申领金额及数量
	 */
	List<HashMap> queryApplyOrderTotalList(PdApplyOrderPage applyOrderPage);

	/**
	 * 自动生成申领单补货
	 * @param stockTotalList
	 */
	public void autoApplyOrderInfo(Set<PdProductStockTotal> stockTotalList,String departId) ;
}
