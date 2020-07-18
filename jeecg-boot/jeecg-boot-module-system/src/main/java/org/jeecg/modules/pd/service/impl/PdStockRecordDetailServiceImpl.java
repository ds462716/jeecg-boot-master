package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdStockRecordDetailMapper;
import org.jeecg.modules.pd.service.IPdStockRecordDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
	public List<PdStockRecordDetail> selectByMainId(PdStockRecordDetail pdStockRecordDetail) {
		List<PdStockRecordDetail> list = pdStockRecordDetailMapper.selectByMainId(pdStockRecordDetail);
		return list;
	}

	/**
	 * 分页查询列表
	 * @param page
	 * @param pdStockRecordDetail
	 * @return
	 */
	@Override
	public IPage<PdStockRecordDetail> selectList(Page<PdStockRecordDetail> page, PdStockRecordDetail pdStockRecordDetail) {
		return pdStockRecordDetailMapper.selectList(page, pdStockRecordDetail);
	}

	/**
	 * 查询列表
	 * @param pdStockRecordDetail
	 * @return
	 */
	@Override
	public List<PdStockRecordDetail> selectList(PdStockRecordDetail pdStockRecordDetail) {
		return pdStockRecordDetailMapper.selectList(pdStockRecordDetail);
	}

	/**
	 * 查询明细
	 * @param pdStockRecordDetail
	 * @return
	 */
	@Override
	public List<PdStockRecordDetail> queryPdStockRecordDetail(PdStockRecordDetail pdStockRecordDetail) {
		return pdStockRecordDetailMapper.selectList(pdStockRecordDetail);
	}

	@Override
	public Map<String, Object> queryStockRecordCount(PdStockRecordDetail detail) {
		return pdStockRecordDetailMapper.queryStockRecordCount(detail);
	}

	@Override
	public List<PdStockRecordDetail> selectListForRefBarCodeCheck(PdStockRecordDetail pdStockRecordDetail) {
		return pdStockRecordDetailMapper.selectListForRefBarCodeCheck(pdStockRecordDetail);
	}

	@Override
	public List<PdStockRecordDetail> chooseStockRecordDetailList(PdStockRecord pdStockRecord) {
		return pdStockRecordDetailMapper.chooseStockRecordDetailList(pdStockRecord);
	}
}
