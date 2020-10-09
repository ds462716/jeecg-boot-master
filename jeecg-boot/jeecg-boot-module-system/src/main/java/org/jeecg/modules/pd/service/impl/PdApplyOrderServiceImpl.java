package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecg.modules.pd.entity.PdPackageDetail;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.mapper.PdApplyDetailMapper;
import org.jeecg.modules.pd.mapper.PdApplyOrderMapper;
import org.jeecg.modules.pd.service.IPdApplyOrderService;
import org.jeecg.modules.pd.service.IPdPackageDetailService;
import org.jeecg.modules.pd.service.IPdProductStockTotalService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.jeecg.modules.pd.vo.PdApplyOrderPage;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
	@Autowired
	private IPdPackageDetailService packageDetailService;
	@Autowired
	private SysDepartMapper sysDepartMapper;
	@Autowired
	private IPdProductStockTotalService pdProductStockTotalService;

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
				if(StringUtils.isNotEmpty(entity.getPackageId())){
					List<PdPackageDetail> pmpList = packageDetailService.selectByMainId(entity.getPackageId());
 					for(PdPackageDetail ppmp : pmpList){
						PdApplyDetail pd = new PdApplyDetail();
						pd.setApplyNo(pdApplyOrder.getApplyNo());
						pd.setPackageRecordId(entity.getPackageRecordId());
						pd.setPackageId(entity.getPackageId());
						pd.setPackageCode(entity.getPackageCode());
						pd.setPackageName(entity.getPackageName());
						pd.setProductId(ppmp.getProductId());
					    Double packProdNum = entity.getApplyNum() * ppmp.getCount();
						pd.setApplyNum(packProdNum);
						pd.setStockNum( ppmp.getStockNum());
						pd.setProductAttr(PdConstant.PROD_ATTR_2);
						dao.insert(pd);
				    }
				}else{
					//外键设置
					entity.setApplyNo(pdApplyOrder.getApplyNo());
					entity.setProductAttr(PdConstant.PROD_ATTR_1);
					entity.setId(null);
					dao.insert(entity);
				}
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
				if(StringUtils.isNotEmpty(entity.getPackageId())){
					List<PdPackageDetail> pmpList = packageDetailService.selectByMainId(entity.getPackageId());
					for(PdPackageDetail ppmp : pmpList){
						PdApplyDetail pd = new PdApplyDetail();
						pd.setApplyNo(pdApplyOrder.getApplyNo());
						pd.setPackageRecordId(entity.getPackageRecordId());
						pd.setPackageId(entity.getPackageId());
						pd.setPackageCode(entity.getPackageCode());
						pd.setPackageName(entity.getPackageName());
						pd.setProductId(ppmp.getProductId());
						Double packProdNum = entity.getApplyNum() * ppmp.getCount();
						pd.setApplyNum(packProdNum);
						pd.setStockNum( ppmp.getStockNum());
						pd.setProductAttr(PdConstant.PROD_ATTR_2);
						dao.insert(pd);
					}
				}else{
					//外键设置
					entity.setApplyNo(pdApplyOrder.getApplyNo());
					entity.setProductAttr(PdConstant.PROD_ATTR_1);
					entity.setId(null);
					dao.insert(entity);
				}
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


	@Override
	public List<HashMap> queryApplyOrderTotalList(PdApplyOrderPage applyOrderPage) {
		return pdApplyOrderMapper.queryApplyOrderTotalList(applyOrderPage);
	}

	/**
	 * 生成自动申领申请单
	 */
	@Transactional
    @Override
	public void autoApplyOrderInfo(Set<PdProductStockTotal> stockTotalList,String departId) {
		PdApplyDetailMapper dao=sqlSession.getMapper(PdApplyDetailMapper.class);
		 String applyNo= UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_SL);
		String orgCode="";
		Double totalNum=0.00;
		String createBy="";
		Double stockNum=0.00;
		if(CollectionUtils.isNotEmpty(stockTotalList)) {
				for (PdProductStockTotal stockTotal : stockTotalList) {
					orgCode = stockTotal.getSysOrgCode();
					createBy=stockTotal.getCreateBy();
					PdApplyDetail pdApplyDetail = new PdApplyDetail();
					pdApplyDetail.setApplyNo(applyNo);//申领单号;
					pdApplyDetail.setProductId(stockTotal.getProductId());//产品ID
					pdApplyDetail.setCreateBy(stockTotal.getCreateBy());
					pdApplyDetail.setSysOrgCode(stockTotal.getSysOrgCode());
					//获取出库科室库存数量
					PdProductStockTotalPage total=new PdProductStockTotalPage();
					SysDepart sysDepart = sysDepartMapper.queryDepartByOrgCode(orgCode);
					total.setDepartId(sysDepart.getParentId());
					total.setProductId(stockTotal.getProductId());
					total.setFilterType("1");
					List<PdProductStockTotalPage> list=pdProductStockTotalService.findListForQuery(total);
					if(CollectionUtils.isNotEmpty(list)){
						for(PdProductStockTotal stockTotal1:  list){
							stockNum+=stockTotal1.getStockNum();
						}
					}
					pdApplyDetail.setStockNum(stockNum);//出库科室库存数量
					pdApplyDetail.setApplyNum(stockTotal.getAutoNum());//申领数量=自动补货数量
					pdApplyDetail.setProductAttr(PdConstant.PROD_ATTR_1);//产品属性
					pdApplyDetail.setCurrentStockNum(stockTotal.getStockNum());
					dao.insert(pdApplyDetail);
					totalNum += stockTotal.getAutoNum();
				}
				PdApplyOrder pdApplyOrder = new PdApplyOrder();
				pdApplyOrder.setApplyNo(applyNo);//申领单号;
			    pdApplyOrder.setCreateBy(createBy);
			    pdApplyOrder.setUpdateBy(createBy);
				pdApplyOrder.setApplyBy(createBy);//申领人
				pdApplyOrder.setApplyDate(new Date());//申领日期
			    pdApplyOrder.setSysOrgCode(orgCode);
				pdApplyOrder.setRemarks("自动生成申领补货单");
				pdApplyOrder.setAuditStatus(PdConstant.AUDIT_STATE_1);//审核状态
				pdApplyOrder.setSubmitStatus(PdConstant.SUBMIT_STATE_2);//提交状态
				pdApplyOrder.setTotalNum(totalNum);//申领总数
				pdApplyOrder.setDepartId(departId);//申领科室ID
 				SysDepart sysDepart = sysDepartMapper.queryDepartByOrgCode(orgCode);
				pdApplyOrder.setOutDepartId(sysDepart.getParentId());//出库科室ID
			    pdApplyOrder.setDepartParentId(sysDepart.getDepartParentId());
				pdApplyOrderMapper.insert(pdApplyOrder);
		}
	}
}
