package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdStockRecordDetailMapper;
import org.jeecg.modules.pd.service.IPdStockRecordDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 出入库明细表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Service
public class PdStockRecordDetailServiceImpl extends ServiceImpl<PdStockRecordDetailMapper, PdStockRecordDetail> implements IPdStockRecordDetailService {
	
	@Autowired
	private PdStockRecordDetailMapper pdStockRecordDetailMapper;
	
	@Override
	public List<PdStockRecordDetail> selectByMainId(String mainId) {
		return pdStockRecordDetailMapper.selectByMainId(mainId);
	}
}
