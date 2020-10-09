package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdInvoiceDetail;
import org.jeecg.modules.pd.mapper.PdInvoiceDetailMapper;
import org.jeecg.modules.pd.service.IPdInvoiceDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: pd_invoice_detail
 * @Author: jiangxz
 * @Date:   2020-06-24
 * @Version: V1.0
 */
@Service
public class PdInvoiceDetailServiceImpl extends ServiceImpl<PdInvoiceDetailMapper, PdInvoiceDetail> implements IPdInvoiceDetailService {
	
	@Autowired
	private PdInvoiceDetailMapper pdInvoiceDetailMapper;
	
	@Override
	public List<PdInvoiceDetail> selectByMainId(String mainId) {
		return pdInvoiceDetailMapper.selectByMainId(mainId);
	}

	@Override
	public IPage<PdInvoiceDetail> selectByStockRecord(Page<PdInvoiceDetail> page, PdInvoiceDetail pdInvoiceDetail) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdInvoiceDetail.setDepartParentId(sysUser.getDepartParentId());
		return pdInvoiceDetailMapper.selectByStockRecord(page,pdInvoiceDetail);
	}

	@Override
	public List<PdInvoiceDetail> selectByStockRecord(PdInvoiceDetail pdInvoiceDetail) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdInvoiceDetail.setDepartParentId(sysUser.getDepartParentId());
		return pdInvoiceDetailMapper.selectByStockRecord(pdInvoiceDetail);
	}

	@Override
	public IPage<PdInvoiceDetail> selectInvoiceDetailList(Page<PdInvoiceDetail> page, PdInvoiceDetail pdInvoiceDetail) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdInvoiceDetail.setDepartParentId(sysUser.getDepartParentId());
		return pdInvoiceDetailMapper.selectInvoiceDetailList(page,pdInvoiceDetail);
	}

	@Override
	public List<PdInvoiceDetail> selectInvoiceDetailList(PdInvoiceDetail pdInvoiceDetail) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pdInvoiceDetail.setDepartParentId(sysUser.getDepartParentId());
		return pdInvoiceDetailMapper.selectInvoiceDetailList(pdInvoiceDetail);
	}
}
