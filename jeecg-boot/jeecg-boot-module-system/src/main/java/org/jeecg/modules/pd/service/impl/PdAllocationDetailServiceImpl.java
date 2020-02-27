package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdAllocationDetail;
import org.jeecg.modules.pd.mapper.PdAllocationDetailMapper;
import org.jeecg.modules.pd.service.IPdAllocationDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

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
	public List<PdAllocationDetail> selectByMainId(String mainId) {
		return pdAllocationDetailMapper.selectByMainId(mainId);
	}
}
