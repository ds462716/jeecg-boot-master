package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.jeecg.modules.pd.entity.PdPurchaseTemp;
import org.jeecg.modules.pd.entity.PdPurchaseTempDetail;
import org.jeecg.modules.pd.mapper.PdPurchaseTempDetailMapper;
import org.jeecg.modules.pd.mapper.PdPurchaseTempMapper;
import org.jeecg.modules.pd.service.IPdPurchaseTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 申购订单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Service
public class PdPurchaseTempServiceImpl extends ServiceImpl<PdPurchaseTempMapper, PdPurchaseTemp> implements IPdPurchaseTempService {

	@Autowired
	private PdPurchaseTempMapper pdPurchaseTempMapper;
	@Autowired
	private PdPurchaseTempDetailMapper pdPurchaseTempDetailMapper;
	@Autowired
	private SqlSession sqlSession;

	/**
	 * 查询列表
	 * @param page
	 * @param pdPurchaseTemp
	 * @return
	 */
	@Override
	public Page<PdPurchaseTemp> selectList(Page<PdPurchaseTemp> page, PdPurchaseTemp pdPurchaseTemp) {
		return   pdPurchaseTempMapper.selectListByPage(page,pdPurchaseTemp);
	}



	@Override
	@Transactional
	public void saveMain(PdPurchaseTemp pdPurchaseTemp, List<PdPurchaseTempDetail> pdPurchaseTempDetailList) {
		pdPurchaseTempMapper.insert(pdPurchaseTemp);
		PdPurchaseTempDetailMapper dao= sqlSession.getMapper(PdPurchaseTempDetailMapper.class);
		if(pdPurchaseTempDetailList!=null && pdPurchaseTempDetailList.size()>0) {
			for(PdPurchaseTempDetail entity:pdPurchaseTempDetailList) {
				//外键设置
				entity.setTempNo(pdPurchaseTemp.getTempNo());
				dao.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdPurchaseTemp pdPurchaseTemp, List<PdPurchaseTempDetail> pdPurchaseTempDetailList) {
		pdPurchaseTempMapper.updateById(pdPurchaseTemp);
		//1.先删除子表数据
		pdPurchaseTempDetailMapper.deleteByTempNo(pdPurchaseTemp.getTempNo());
		//2.子表数据重新插入
		PdPurchaseTempDetailMapper dao= sqlSession.getMapper(PdPurchaseTempDetailMapper.class);
		if(pdPurchaseTempDetailList!=null && pdPurchaseTempDetailList.size()>0) {
			for(PdPurchaseTempDetail entity:pdPurchaseTempDetailList) {
				//外键设置
				entity.setTempNo(pdPurchaseTemp.getTempNo());
				dao.insert(entity);
			}
		}
	}
}
