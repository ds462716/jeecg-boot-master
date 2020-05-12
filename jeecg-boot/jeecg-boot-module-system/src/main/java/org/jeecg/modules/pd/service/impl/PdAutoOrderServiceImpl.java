package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdAutoOrderInf;
import org.jeecg.modules.pd.mapper.PdAutoOrderMapper;
import org.jeecg.modules.pd.service.IPdAutoOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class PdAutoOrderServiceImpl extends ServiceImpl<PdAutoOrderMapper, PdAutoOrderInf> implements IPdAutoOrderService {

	@Autowired
	private PdAutoOrderMapper pdAutoOrderMapper;




	/**
	 *  查询列表
	 * @param autoOrderInf
	 * @return
	 */
	@Override
	public List<PdAutoOrderInf> queryList(PdAutoOrderInf autoOrderInf) {
		return pdAutoOrderMapper.queryList(autoOrderInf);
	}



}
