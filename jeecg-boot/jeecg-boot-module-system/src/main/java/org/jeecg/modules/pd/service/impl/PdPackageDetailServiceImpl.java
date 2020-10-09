package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdPackageDetail;
import org.jeecg.modules.pd.mapper.PdPackageDetailMapper;
import org.jeecg.modules.pd.service.IPdPackageDetailService;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 套包明细
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
@Service
public class PdPackageDetailServiceImpl extends ServiceImpl<PdPackageDetailMapper, PdPackageDetail> implements IPdPackageDetailService {
	
	@Autowired
	private PdPackageDetailMapper pdPackageDetailMapper;
	
	@Override
	public List<PdPackageDetail> selectByMainId(String mainId) {
		return pdPackageDetailMapper.selectByMainId(mainId);
	}

	@Override
	public Page<PdPackageDetail> selectList(Page<PdPackageDetail> pageList, PdPackageDetail packageDetail) {
		return pageList.setRecords(pdPackageDetailMapper.selectList(packageDetail));

	}

	@Override
	public List<PdPackageDetail> queryPdPackageList(PdPackageDetail packageDetail) {
		return pdPackageDetailMapper.selectList(packageDetail);
	}

}
