package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMerge;
import org.jeecg.modules.pd.mapper.PdPurchaseDetailMapper;
import org.jeecg.modules.pd.mapper.PdPurchaseOrderMapper;
import org.jeecg.modules.pd.service.IPdPurchaseOrderService;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 申购订单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Service
public class PdPurchaseOrderServiceImpl extends ServiceImpl<PdPurchaseOrderMapper, PdPurchaseOrder> implements IPdPurchaseOrderService {

	@Autowired
	private PdPurchaseOrderMapper pdPurchaseOrderMapper;
	@Autowired
	private PdPurchaseDetailMapper pdPurchaseDetailMapper;
	@Autowired
	private SqlSession sqlSession;


	/**
	 * 查询列表
	 * @param page
	 * @param pdPurchaseOrder
	 * @return
	 */
	@Override
	public Page<PdPurchaseOrder> selectList(Page<PdPurchaseOrder> page, PdPurchaseOrder pdPurchaseOrder) {
		return page.setRecords(pdPurchaseOrderMapper.selectList(pdPurchaseOrder));
	}

	@Override
	public List<PdPurchaseOrder> list(PdPurchaseOrder pdPurchaseOrder) {
		return pdPurchaseOrderMapper.selectList(pdPurchaseOrder);
	}


	@Override
	@Transactional
	public void saveMain(PdPurchaseOrder pdPurchaseOrder, List<PdPurchaseDetail> pdPurchaseDetailList) {
		pdPurchaseOrderMapper.insert(pdPurchaseOrder);
		PdPurchaseDetailMapper dao= sqlSession.getMapper(PdPurchaseDetailMapper.class);
		if(pdPurchaseDetailList!=null && pdPurchaseDetailList.size()>0) {
			for(PdPurchaseDetail entity:pdPurchaseDetailList) {
				//外键设置
				entity.setOrderNo(pdPurchaseOrder.getOrderNo());
				dao.insert(entity);
				//pdPurchaseDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdPurchaseOrder pdPurchaseOrder,List<PdPurchaseDetail> pdPurchaseDetailList) {
		pdPurchaseOrderMapper.updateById(pdPurchaseOrder);
		
		//1.先删除子表数据
		pdPurchaseDetailMapper.deleteByOrderNo(pdPurchaseOrder.getOrderNo());
		//2.子表数据重新插入
		PdPurchaseDetailMapper dao= sqlSession.getMapper(PdPurchaseDetailMapper.class);
		if(pdPurchaseDetailList!=null && pdPurchaseDetailList.size()>0) {
			for(PdPurchaseDetail entity:pdPurchaseDetailList) {
				//外键设置
				entity.setOrderNo(pdPurchaseOrder.getOrderNo());
				dao.insert(entity);
				//pdPurchaseDetailMapper.insert(entity);
			}
		}
	}

	@Override
	public Page<PdPurchaseOrderMerge> choosePurchaseOrderList(Page<PdPurchaseOrderMerge> pageList, PdPurchaseOrderPage pdPurchaseOrderPage) {
		List queryDate = pdPurchaseOrderPage.getQueryDate();
		if(CollectionUtils.isNotEmpty(queryDate)){
			pdPurchaseOrderPage.setQueryDateStart((String) queryDate.get(0));
			pdPurchaseOrderPage.setQueryDateStart((String) queryDate.get(1));
		}
		return pageList.setRecords(pdPurchaseOrderMapper.choosePurchaseOrderList(pdPurchaseOrderPage));
	}

	@Override
	public List<PdProductPage> choosePurchaseOrderDetailList(PdPurchaseOrderPage pdPurchaseOrderPage) {
		List<PdProductPage> list = pdPurchaseOrderMapper.choosePurchaseOrderDetailList(pdPurchaseOrderPage);
		if(CollectionUtils.isNotEmpty(list)){
			for (PdProductPage item : list) {
				item.setPrice(
						(item.getPurchasePrice() == null ? BigDecimal.ZERO:item.getPurchasePrice())
								.multiply(BigDecimal.valueOf(item.getOrderNum())));
			}
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public int audit(String orderNos, String auditStatus, String refuseReason,String submitStatus){
		if(StringUtils.isEmpty(orderNos) || StringUtils.isEmpty(auditStatus)){
			return 0;
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("auditStatus",auditStatus);
		map.put("orderNos",PdPurchaseOrder.dealStrData(orderNos));
		map.put("refuseReason",refuseReason);
		map.put("auditBy", sysUser.getId());
		map.put("auditDate", new Date());
		map.put("submitStatus",submitStatus);
		return pdPurchaseOrderMapper.batchUpdateOrderStatus(map);
	}
}
