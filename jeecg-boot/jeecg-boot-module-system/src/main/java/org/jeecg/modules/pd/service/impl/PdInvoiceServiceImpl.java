package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdInvoice;
import org.jeecg.modules.pd.entity.PdInvoiceDetail;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdInvoiceDetailMapper;
import org.jeecg.modules.pd.mapper.PdInvoiceMapper;
import org.jeecg.modules.pd.mapper.PdStockRecordDetailMapper;
import org.jeecg.modules.pd.service.IPdInvoiceDetailService;
import org.jeecg.modules.pd.service.IPdInvoiceService;
import org.jeecg.modules.pd.util.UUIDUtil;
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
	private PdStockRecordDetailMapper pdStockRecordDetailMapper;
	@Autowired
	private IPdInvoiceDetailService pdInvoiceDetailService;

	/**
	 * 此处修改为 一对一关系 modified by jiangxz 2020年7月7日 16:27:41
	 * @param pdInvoice
	 */
	@Override
	@Transactional
	public void saveMain(PdInvoice pdInvoice) {
//		pdInvoiceMapper.insert(pdInvoice);  此处修改为 一对一关系 modified by jiangxz 2020年7月7日 16:27:41
		List<String > billDetailIdList = pdInvoice.getBillDetailIdList();
		if(CollectionUtils.isEmpty(billDetailIdList)){
			throw new RuntimeException("参数不对，请重新选择明细维护发票！");
		}

		PdInvoiceDetail detail = new PdInvoiceDetail();
		detail.setBillDetailIdList(billDetailIdList);
		List<PdInvoiceDetail> detailList = pdInvoiceDetailService.selectByStockRecord(detail);
		if(CollectionUtils.isNotEmpty(detailList)) {
			for(PdInvoiceDetail entity : detailList) {
				pdInvoice.setId(null);
				pdInvoice.setInvoiceRegNo(UUIDUtil.generateOrderNoByType(PdConstant.ORDER_NO_FIRST_LETTER_FP));
				pdInvoiceMapper.insert(pdInvoice);
				//外键设置
				entity.setInvoiceId(pdInvoice.getId());
				pdInvoiceDetailMapper.insert(entity);

				// 发票号更新到入库明细
				PdStockRecordDetail recordDetail = new PdStockRecordDetail();
				recordDetail.setId(entity.getBillDetailId());
				recordDetail.setInvoiceNo(pdInvoice.getInvoiceNo());
				pdStockRecordDetailMapper.updateById(recordDetail);
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
		List<PdInvoiceDetail> detailList = pdInvoiceDetailMapper.selectByMainId(id);
		for(PdInvoiceDetail detail : detailList){
			if(PdConstant.INVOICE_STATUS_2.equals(detail.getStatus())){
				PdInvoice pdInvoice = pdInvoiceMapper.selectById(id);
				throw new RuntimeException("该发票登记号["+pdInvoice.getInvoiceRegNo()+"]下有已完成的明细，不能删除");
			}

			// 发票号更新到入库明细
			PdStockRecordDetail recordDetail = new PdStockRecordDetail();
			recordDetail.setId(detail.getBillDetailId());
			recordDetail.setInvoiceNo("");
			pdStockRecordDetailMapper.updateById(recordDetail);
		}

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
