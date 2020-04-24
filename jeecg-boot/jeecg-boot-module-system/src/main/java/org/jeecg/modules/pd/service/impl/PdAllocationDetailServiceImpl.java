package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdAllocationDetail;
import org.jeecg.modules.pd.mapper.PdAllocationDetailMapper;
import org.jeecg.modules.pd.service.IPdAllocationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 调拨明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Service
public class PdAllocationDetailServiceImpl extends ServiceImpl<PdAllocationDetailMapper, PdAllocationDetail> implements IPdAllocationDetailService {
	
	@Autowired
	private PdAllocationDetailMapper pdAllocationDetailMapper;
	

	@Override
	public List<PdAllocationDetail> selectByAllocationNo(PdAllocationDetail allocationDetail) {
		return pdAllocationDetailMapper.selectByAllocationNo(allocationDetail);
	}

	@Override
	public List<PdAllocationDetail> queryAllocationDetailPack(PdAllocationDetail allocationDetail) {
		return pdAllocationDetailMapper.queryAllocationDetailPack(allocationDetail);
	}

}
