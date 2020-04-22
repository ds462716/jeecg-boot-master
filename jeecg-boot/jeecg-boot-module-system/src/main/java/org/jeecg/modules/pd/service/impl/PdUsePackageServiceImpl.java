package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdUsePackage;
import org.jeecg.modules.pd.entity.PdUsePackageDetail;
import org.jeecg.modules.pd.mapper.PdUsePackageDetailMapper;
import org.jeecg.modules.pd.mapper.PdUsePackageMapper;
import org.jeecg.modules.pd.service.IPdUsePackageDetailService;
import org.jeecg.modules.pd.service.IPdUsePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 检验项目
 * @Author: zxh
 * @Date:   2020年4月21日08:56:20
 * @Version: V1.0
 */
@Service
public class PdUsePackageServiceImpl extends ServiceImpl<PdUsePackageMapper, PdUsePackage> implements IPdUsePackageService {

	@Autowired
	private PdUsePackageMapper pdUsePackageMapper;

	@Autowired
	private IPdUsePackageDetailService pdUsePackageDetailService;

	@Autowired
	private PdUsePackageDetailMapper pdUsePackageDetailMapper;
	
	@Override
	@Transactional
	public void saveMain(PdUsePackage PdUsePackage, List<PdUsePackageDetail> pdUsePackageDetailList) {
		pdUsePackageMapper.insert(PdUsePackage);
		if(pdUsePackageDetailList!=null && pdUsePackageDetailList.size()>0) {
			for(PdUsePackageDetail entity:pdUsePackageDetailList) {
				//外键设置
				entity.setPackageId(PdUsePackage.getId());
				entity.setId(null);
			}
			pdUsePackageDetailService.saveBatch(pdUsePackageDetailList);
		}
	}

	@Override
	@Transactional
	public void updateMain(PdUsePackage pdUsePackage,List<PdUsePackageDetail> pdUsePackageDetailList) {
		pdUsePackageMapper.updateById(pdUsePackage);
		
		//1.先删除子表数据
		pdUsePackageDetailMapper.deleteByMainId(pdUsePackage.getId());
		
		//2.子表数据重新插入
		if(pdUsePackageDetailList!=null && pdUsePackageDetailList.size()>0) {
			for(PdUsePackageDetail entity:pdUsePackageDetailList) {
				//外键设置
				entity.setPackageId(pdUsePackage.getId());
				entity.setId(null);
			}
			pdUsePackageDetailService.saveBatch(pdUsePackageDetailList);
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdUsePackageDetailMapper.deleteByMainId(id);
		pdUsePackageMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdUsePackageDetailMapper.deleteByMainId(id.toString());
			pdUsePackageMapper.deleteById(id);
		}
	}

	@Override
	public List<PdUsePackage> queryList(PdUsePackage pdUsePackage) {
		return pdUsePackageMapper.queryList(pdUsePackage);
	}

	@Override
	public Page<PdUsePackage> queryList(Page<PdUsePackage> pageList, PdUsePackage pdUsePackage) {
		return pageList.setRecords(pdUsePackageMapper.queryList(pdUsePackage));
	}

	@Override
	public List<PdUsePackage> verify(PdUsePackage pdUsePackage) {
		return pdUsePackageMapper.verify(pdUsePackage);
	}

}
