package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.mapper.PdApplyDetailMapper;
import org.jeecg.modules.pd.service.IPdApplyDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

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
	public List<PdApplyDetail> selectByApplyNo(String applyNo) {
		return pdApplyDetailMapper.selectByApplyNo(applyNo);
	}
}
