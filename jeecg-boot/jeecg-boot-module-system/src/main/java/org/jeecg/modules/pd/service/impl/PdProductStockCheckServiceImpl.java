package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdProductStockCheck;
import org.jeecg.modules.pd.entity.PdProductStockCheckChild;
import org.jeecg.modules.pd.mapper.PdProductStockCheckChildMapper;
import org.jeecg.modules.pd.mapper.PdProductStockCheckMapper;
import org.jeecg.modules.pd.service.IPdProductStockCheckChildService;
import org.jeecg.modules.pd.service.IPdProductStockCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 盘点记录表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Service
public class PdProductStockCheckServiceImpl extends ServiceImpl<PdProductStockCheckMapper, PdProductStockCheck> implements IPdProductStockCheckService {

	@Autowired
	private PdProductStockCheckMapper pdProductStockCheckMapper;
	@Autowired
	private PdProductStockCheckChildMapper pdProductStockCheckChildMapper;

	@Autowired
	private IPdProductStockCheckChildService pdProductStockCheckChildService;
	@Autowired
	private SqlSession sqlSession;
	/**
	 * 查询列表
	 * @param page
	 * @param stockCheck
	 * @return
	 */
	@Override
	public Page<PdProductStockCheck> selectList(Page<PdProductStockCheck> page, PdProductStockCheck stockCheck) {
		return baseMapper.selectListByPage(page,stockCheck);
	}

	@Override
	@Transactional
	public void saveMain(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> pdProductStockCheckChildList) {
		pdProductStockCheckMapper.insert(pdProductStockCheck);
		PdProductStockCheckChildMapper dao=sqlSession.getMapper(PdProductStockCheckChildMapper.class);
		if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0) {
			for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
				//外键设置
				entity.setCheckNo(pdProductStockCheck.getCheckNo());
				dao.insert(entity);
				//pdProductStockCheckChildMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdProductStockCheck pdProductStockCheck,List<PdProductStockCheckChild> pdProductStockCheckChildList) {
		pdProductStockCheckMapper.updateById(pdProductStockCheck);
		
		//1.先删除子表数据
		pdProductStockCheckChildMapper.deleteByCheckNo(pdProductStockCheck.getCheckNo());
		//2.子表数据重新插入
		PdProductStockCheckChildMapper dao=sqlSession.getMapper(PdProductStockCheckChildMapper.class);
		if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0) {
			for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
				//外键设置
				entity.setCheckNo(pdProductStockCheck.getCheckNo());
				dao.insert(entity);
				//pdProductStockCheckChildMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdProductStockCheckChildMapper.deleteByMainId(id);
		pdProductStockCheckMapper.deleteByMainId(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		PdProductStockCheckChildMapper childDao=sqlSession.getMapper(PdProductStockCheckChildMapper.class);
		PdProductStockCheckMapper dao=sqlSession.getMapper(PdProductStockCheckMapper.class);
		for(Serializable id:idList) {
			childDao.deleteByMainId(id.toString());
			dao.deleteById(id);
			//pdProductStockCheckChildMapper.deleteByMainId(id.toString());
			//pdProductStockCheckMapper.deleteById(id);
		}
	}

	@Transactional
	@Override
	public String submit(PdProductStockCheck pdProductStockCheck, List<PdProductStockCheckChild> pdProductStockCheckChildList) {
		// 修改前先删除数据
		if (oConvertUtils.isNotEmpty(pdProductStockCheck.getId())) {
			this.delMain(pdProductStockCheck.getCheckNo());
		}
		pdProductStockCheckMapper.insert(pdProductStockCheck);
		if(pdProductStockCheckChildList!=null && pdProductStockCheckChildList.size()>0) {
			for(PdProductStockCheckChild entity:pdProductStockCheckChildList) {
				//外键设置
				entity.setCheckNo(pdProductStockCheck.getCheckNo());
			}
			pdProductStockCheckChildService.saveBatch(pdProductStockCheckChildList);
		}
		return pdProductStockCheck.getId();
	}

	@Override
	public PdProductStockCheck getByOne(PdProductStockCheck pdProductStockCheck) {
		return baseMapper.getByOne(pdProductStockCheck);
	}

}
