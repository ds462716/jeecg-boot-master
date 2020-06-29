package org.jeecg.modules.pd.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdInvoiceDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: pd_invoice_detail
 * @Author: jiangxz
 * @Date:   2020-06-24
 * @Version: V1.0
 */
public interface PdInvoiceDetailMapper extends BaseMapper<PdInvoiceDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdInvoiceDetail> selectByMainId(@Param("mainId") String mainId);

	IPage<PdInvoiceDetail> selectByStockRecord(Page<PdInvoiceDetail> page, @Param("entity") PdInvoiceDetail pdInvoiceDetail);

	List<PdInvoiceDetail> selectByStockRecord(@Param("entity") PdInvoiceDetail pdInvoiceDetail);

}
