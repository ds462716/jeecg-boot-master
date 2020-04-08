package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdStoreroomPatrol;
import org.jeecg.modules.pd.entity.PdStoreroomPatrolDetail;
import org.jeecg.modules.pd.mapper.PdStoreroomPatrolDetailMapper;
import org.jeecg.modules.pd.mapper.PdStoreroomPatrolMapper;
import org.jeecg.modules.pd.service.IPdStoreroomPatrolService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: pd_storeroom_patrol
 * @Author: jiangxz
 * @Date:   2020-04-07
 * @Version: V1.0
 */
@Service
public class PdStoreroomPatrolServiceImpl extends ServiceImpl<PdStoreroomPatrolMapper, PdStoreroomPatrol> implements IPdStoreroomPatrolService {

	@Autowired
	private PdStoreroomPatrolMapper pdStoreroomPatrolMapper;
	@Autowired
	private PdStoreroomPatrolDetailMapper pdStoreroomPatrolDetailMapper;

	@Override
	public List<PdStoreroomPatrol> selectList(PdStoreroomPatrol pdStoreroomPatrol) {
		return pdStoreroomPatrolMapper.selectList(pdStoreroomPatrol);
	}

	@Override
	public Page<PdStoreroomPatrol> selectList(Page<PdStoreroomPatrol> pageList, PdStoreroomPatrol pdStoreroomPatrol) {
		return pageList.setRecords(pdStoreroomPatrolMapper.selectList(pdStoreroomPatrol));
	}

	@Override
	@Transactional
	public void saveMain(PdStoreroomPatrol pdStoreroomPatrol, List<PdStoreroomPatrolDetail> pdStoreroomPatrolDetailList) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		Double patrolCount = 0D; //巡查数量
		Double passCount = 0D;   //合格数量
		Double failCount = 0D;   //不合格数量
		if(pdStoreroomPatrolDetailList!=null && pdStoreroomPatrolDetailList.size()>0) {
			for(PdStoreroomPatrolDetail entity : pdStoreroomPatrolDetailList) {
				patrolCount = patrolCount + entity.getStockNum();
				if(PdConstant.RESULT_QUALIFIED.equals(entity.getPatrolResult())){
					passCount = passCount + entity.getStockNum();
				}else if(PdConstant.RESULT_UNQUALIFIED.equals(entity.getPatrolResult())){
					failCount = failCount + entity.getStockNum();
				}

				//外键设置
				entity.setStoreroomPatrolId(pdStoreroomPatrol.getId());
				entity.setId(null);
				pdStoreroomPatrolDetailMapper.insert(entity);
			}
		}
		pdStoreroomPatrol.setPatrolMan(sysUser.getRealname());
		pdStoreroomPatrol.setPatrolCount(patrolCount);
		pdStoreroomPatrol.setPassCount(passCount);
		pdStoreroomPatrol.setFailCount(failCount);
		pdStoreroomPatrolMapper.insert(pdStoreroomPatrol);
	}

	@Override
	@Transactional
	public void updateMain(PdStoreroomPatrol pdStoreroomPatrol,List<PdStoreroomPatrolDetail> pdStoreroomPatrolDetailList) {
		pdStoreroomPatrolMapper.updateById(pdStoreroomPatrol);
		
		//1.先删除子表数据
		pdStoreroomPatrolDetailMapper.deleteByMainId(pdStoreroomPatrol.getId());
		
		//2.子表数据重新插入
		if(pdStoreroomPatrolDetailList!=null && pdStoreroomPatrolDetailList.size()>0) {
			for(PdStoreroomPatrolDetail entity:pdStoreroomPatrolDetailList) {
				//外键设置
				entity.setStoreroomPatrolId(pdStoreroomPatrol.getId());
				pdStoreroomPatrolDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdStoreroomPatrolDetailMapper.deleteByMainId(id);
		pdStoreroomPatrolMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdStoreroomPatrolDetailMapper.deleteByMainId(id.toString());
			pdStoreroomPatrolMapper.deleteById(id);
		}
	}
	
}
