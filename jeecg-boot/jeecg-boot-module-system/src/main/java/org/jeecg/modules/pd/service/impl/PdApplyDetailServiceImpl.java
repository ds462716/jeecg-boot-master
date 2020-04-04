package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.mapper.PdApplyDetailMapper;
import org.jeecg.modules.pd.service.IPdApplyDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 申领单明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class PdApplyDetailServiceImpl extends ServiceImpl<PdApplyDetailMapper, PdApplyDetail> implements IPdApplyDetailService {
	
	@Autowired
	private PdApplyDetailMapper pdApplyDetailMapper;
	
	@Override
	public List<PdApplyDetail> selectByApplyNo(PdApplyDetail applyDetail) {
		return pdApplyDetailMapper.selectByApplyNo(applyDetail);
	}

	@Override
	public List<PdApplyDetail> queryApplyDetailPack(PdApplyDetail applyDetail) {
		return pdApplyDetailMapper.queryApplyDetailPack(applyDetail);
	}
}
