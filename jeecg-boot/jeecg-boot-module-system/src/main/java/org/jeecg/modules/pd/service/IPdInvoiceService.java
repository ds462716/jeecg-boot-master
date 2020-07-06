package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdInvoiceDetail;
import org.jeecg.modules.pd.entity.PdInvoice;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: pd_invoice
 * @Author: jiangxz
 * @Date:   2020-06-24
 * @Version: V1.0
 */
public interface IPdInvoiceService extends IService<PdInvoice> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PdInvoice pdInvoice) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PdInvoice pdInvoice, List<PdInvoiceDetail> pdInvoiceDetailList);

	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

}
