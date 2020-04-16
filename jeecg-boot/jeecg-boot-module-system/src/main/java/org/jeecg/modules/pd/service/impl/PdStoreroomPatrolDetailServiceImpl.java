package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdStoreroomPatrolDetail;
import org.jeecg.modules.pd.mapper.PdStoreroomPatrolDetailMapper;
import org.jeecg.modules.pd.service.IPdStoreroomPatrolDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: pd_storeroom_patrol_detail
 * @Author: jiangxz
 * @Date:   2020-04-07
 * @Version: V1.0
 */
@Service
public class PdStoreroomPatrolDetailServiceImpl extends ServiceImpl<PdStoreroomPatrolDetailMapper, PdStoreroomPatrolDetail> implements IPdStoreroomPatrolDetailService {
	
	@Autowired
	private PdStoreroomPatrolDetailMapper pdStoreroomPatrolDetailMapper;
	
	@Override
	public List<PdStoreroomPatrolDetail> selectByMainId(String mainId) {
		return pdStoreroomPatrolDetailMapper.selectByMainId(mainId);
	}
}
