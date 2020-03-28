package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecg.modules.pd.mapper.PdApplyDetailMapper;
import org.jeecg.modules.pd.mapper.PdApplyOrderMapper;
import org.jeecg.modules.pd.service.IPdApplyOrderService;
import org.jeecg.modules.pd.vo.PdApplyOrderPage;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class PdApplyOrderServiceImpl extends ServiceImpl<PdApplyOrderMapper, PdApplyOrder> implements IPdApplyOrderService {

	@Autowired
	private PdApplyOrderMapper pdApplyOrderMapper;
	@Autowired
	private PdApplyDetailMapper pdApplyDetailMapper;
	@Autowired
	private SqlSession sqlSession;


	/**
	 * 查询列表
	 * @param page
	 * @param pdApplyOrder
	 * @return
	 */
	@Override
	public Page<PdApplyOrder> selectList(Page<PdApplyOrder> page, PdApplyOrder pdApplyOrder) {
		return page.setRecords(pdApplyOrderMapper.selectList(pdApplyOrder));
	}





	@Override
	@Transactional
	public void saveMain(PdApplyOrder pdApplyOrder, List<PdApplyDetail> pdApplyDetailList) {
		pdApplyOrderMapper.insert(pdApplyOrder);
		PdApplyDetailMapper dao=sqlSession.getMapper(PdApplyDetailMapper.class);
		if(pdApplyDetailList!=null && pdApplyDetailList.size()>0) {
			for(PdApplyDetail entity:pdApplyDetailList) {
//------------------------
				/*if(StringUtils.isEmpty(entity.getPackageId())){
					entity.setApplyNo(pdApplyOrder.getApplyNo());
					entity.setProductId(pad.getProdId());
					entity.setNumber(pad.getProdNo());
					entity.setApplyCount(pad.getApplyCount());
					prodNum = prodNum + Integer.valueOf(pad.getApplyCount());
					tempArray.add(pad);
				}else{
					List<PdProductMPackage> pmpList = pdProductMPackageDao.getProdListByPackageId(UserUtils.getUser().getStoreroomId(),null, pad.getPackageId());
					for(PdProductMPackage ppmp : pmpList){
						PdApplyDetail pd = new PdApplyDetail();
						pd.preInsert();
						pd.setApplyNo(pdApplyOrder.getApplyNo());
						pd.setPackageId(pad.getPackageId());
						pd.setProdId(ppmp.getProductId());
						pd.setProdNo(ppmp.getPdProduct().getNumber());
						packProdNum = packProdNum + Integer.valueOf(pad.getPackageCount()) * ppmp.getProductCount();
						pd.setPackageCount(pad.getPackageCount());
						pd.setApplyCount(pad.getPackageCount() * ppmp.getProductCount());
						pd.setStockNum(Integer.valueOf(ppmp.getStockNum()));
						tempArray.add(pd);
					}
				}*/
//--------------------------
				//外键设置
				entity.setApplyNo(pdApplyOrder.getApplyNo());
				dao.insert(entity);
				//pdApplyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdApplyOrder pdApplyOrder,List<PdApplyDetail> pdApplyDetailList) {
		pdApplyOrderMapper.updateById(pdApplyOrder);
		//1.先删除子表数据
		pdApplyDetailMapper.deleteByApplyNo(pdApplyOrder.getApplyNo());
		//2.子表数据重新插入
		PdApplyDetailMapper dao=sqlSession.getMapper(PdApplyDetailMapper.class);
		if(pdApplyDetailList!=null && pdApplyDetailList.size()>0) {
			for(PdApplyDetail entity:pdApplyDetailList) {
				//外键设置
				entity.setApplyNo(pdApplyOrder.getApplyNo());
				dao.insert(entity);
				//pdApplyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	public Page<PdApplyOrderPage> chooseApplyOrderList(Page<PdApplyOrderPage> pageList, PdApplyOrderPage applyOrderPage) {
		List queryDate = applyOrderPage.getQueryDate();
		if(CollectionUtils.isNotEmpty(queryDate)){
			applyOrderPage.setQueryDateStart((String) queryDate.get(0));
			applyOrderPage.setQueryDateStart((String) queryDate.get(1));
		}
		return pageList.setRecords(pdApplyOrderMapper.chooseApplyOrderList(applyOrderPage));
	}


	@Override
	public Map<String,Object> queryApplyOrderCount(PdApplyOrder pdApplyOrder) {
		Map<String,Object> params = pdApplyOrderMapper.queryApplyOrderCount(pdApplyOrder);
		return params;
	}

	@Override
	public List<HashMap> queryApplyOrderDateList(PdApplyOrderPage applyOrderPage) {
		return pdApplyOrderMapper.queryApplyOrderDateList(applyOrderPage);
	}
}
