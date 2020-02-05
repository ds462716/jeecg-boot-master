package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdPackageDetail;
import org.jeecg.modules.pd.mapper.PdPackageDetailMapper;
import org.jeecg.modules.pd.service.IPdPackageDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 定数包明细
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
}
