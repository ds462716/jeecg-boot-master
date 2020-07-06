package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdInvoiceDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: pd_invoice_detail
 * @Author: jiangxz
 * @Date:   2020-06-24
 * @Version: V1.0
 */
public interface IPdInvoiceDetailService extends IService<PdInvoiceDetail> {

	public List<PdInvoiceDetail> selectByMainId(String mainId);

	/**
	 * 根据出入库单记录查询
	 * @param page
	 * @param pdInvoiceDetail
	 * @return
	 */
	IPage<PdInvoiceDetail> selectByStockRecord(Page<PdInvoiceDetail> page, PdInvoiceDetail pdInvoiceDetail);

	/**
	 * 根据出入库单记录查询
	 * @param pdInvoiceDetail
	 * @return
	 */
	List<PdInvoiceDetail> selectByStockRecord(PdInvoiceDetail pdInvoiceDetail);

	/**
	 * 发票明细查询
	 * @param page
	 * @param pdInvoiceDetail
	 * @return
	 */
	IPage<PdInvoiceDetail> selectInvoiceDetailList(Page<PdInvoiceDetail> page, PdInvoiceDetail pdInvoiceDetail);

	/**
	 * 发票明细查询
	 * @param pdInvoiceDetail
	 * @return
	 */
	List<PdInvoiceDetail> selectInvoiceDetailList(PdInvoiceDetail pdInvoiceDetail);
}
