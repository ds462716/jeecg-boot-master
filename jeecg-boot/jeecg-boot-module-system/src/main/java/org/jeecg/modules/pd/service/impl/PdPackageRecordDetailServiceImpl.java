package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdPackageRecordDetail;
import org.jeecg.modules.pd.mapper.PdPackageRecordDetailMapper;
import org.jeecg.modules.pd.service.IPdPackageRecordDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: pd_package_record_detail
 * @Author: jiangxz
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@Service
public class PdPackageRecordDetailServiceImpl extends ServiceImpl<PdPackageRecordDetailMapper, PdPackageRecordDetail> implements IPdPackageRecordDetailService {
	
	@Autowired
	private PdPackageRecordDetailMapper pdPackageRecordDetailMapper;
	
	@Override
	public List<PdPackageRecordDetail> selectByMainId(String mainId) {
		return pdPackageRecordDetailMapper.selectByMainId(mainId);
	}
}
