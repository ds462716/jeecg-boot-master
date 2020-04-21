package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.jeecg.modules.pd.mapper.PdUsePackageDetailMapper;
import org.jeecg.modules.pd.service.IPdUsePackageDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 使用包明细
 * @Author: zxh
 * @Date:   2020年4月21日09:09:37
 * @Version: V1.0
 */
@Service
public class PdUsePackageDetailServiceImpl extends ServiceImpl<PdUsePackageDetailMapper, PdUsePackageDetail> implements IPdUsePackageDetailService {
	
	@Autowired
	private PdUsePackageDetailMapper pdUsePackageDetailMapper;
	
	@Override
	public List<PdUsePackageDetail> selectByMainId(String mainId) {
		return pdUsePackageDetailMapper.selectByMainId(mainId);
	}

	@Override
	public Page<PdUsePackageDetail> selectList(Page<PdUsePackageDetail> pageList, PdUsePackageDetail usePackageDetail) {
		return pageList.setRecords(pdUsePackageDetailMapper.selectList(usePackageDetail));

	}

	@Override
	public List<PdUsePackageDetail> queryPdUsePackageList(PdUsePackageDetail pdUsePackageDetail) {
		return pdUsePackageDetailMapper.selectList(pdUsePackageDetail);
	}

}
