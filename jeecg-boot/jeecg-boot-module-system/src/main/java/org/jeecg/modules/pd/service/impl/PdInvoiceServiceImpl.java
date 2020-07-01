package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdInvoice;
import org.jeecg.modules.pd.entity.PdInvoiceDetail;
import org.jeecg.modules.pd.mapper.PdInvoiceDetailMapper;
import org.jeecg.modules.pd.mapper.PdInvoiceMapper;
import org.jeecg.modules.pd.service.IPdInvoiceDetailService;
import org.jeecg.modules.pd.service.IPdInvoiceService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

/**
 * @Description: pd_invoice
 * @Author: jiangxz
 * @Date:   2020-06-24
 * @Version: V1.0
 */
@Service
public class PdInvoiceServiceImpl extends ServiceImpl<PdInvoiceMapper, PdInvoice> implements IPdInvoiceService {

	@Autowired
	private PdInvoiceMapper pdInvoiceMapper;
	@Autowired
	private PdInvoiceDetailMapper pdInvoiceDetailMapper;
	@Autowired
	private IPdInvoiceDetailService pdInvoiceDetailService;
	
	@Override
	@Transactional
	public void saveMain(PdInvoice pdInvoice) {
		pdInvoiceMapper.insert(pdInvoice);
		List<String > billDetailIdList = pdInvoice.getBillDetailIdList();
		if(CollectionUtils.isEmpty(billDetailIdList)){
			throw new RuntimeException("参数不对，请重新选择明细维护发票！");
		}

		PdInvoiceDetail detail = new PdInvoiceDetail();
		detail.setBillDetailIdList(billDetailIdList);
		List<PdInvoiceDetail> detailList = pdInvoiceDetailService.selectByStockRecord(detail);
		if(CollectionUtils.isNotEmpty(detailList)) {
			for(PdInvoiceDetail entity : detailList) {
				//外键设置
				entity.setInvoiceId(pdInvoice.getId());
				pdInvoiceDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdInvoice pdInvoice,List<PdInvoiceDetail> pdInvoiceDetailList) {
		pdInvoiceMapper.updateById(pdInvoice);
		
		//1.先删除子表数据
		pdInvoiceDetailMapper.deleteByMainId(pdInvoice.getId());
		
		//2.子表数据重新插入
		if(pdInvoiceDetailList!=null && pdInvoiceDetailList.size()>0) {
			for(PdInvoiceDetail entity:pdInvoiceDetailList) {
				//外键设置
				entity.setInvoiceId(pdInvoice.getId());
				pdInvoiceDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdInvoiceDetailMapper.deleteByMainId(id);
		pdInvoiceMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdInvoiceDetailMapper.deleteByMainId(id.toString());
			pdInvoiceMapper.deleteById(id);
		}
	}

}
