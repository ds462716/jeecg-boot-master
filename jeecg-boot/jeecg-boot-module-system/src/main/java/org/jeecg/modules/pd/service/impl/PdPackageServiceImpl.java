package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.pd.entity.PdPackage;
import org.jeecg.modules.pd.entity.PdPackageDetail;
import org.jeecg.modules.pd.mapper.PdPackageDetailMapper;
import org.jeecg.modules.pd.mapper.PdPackageMapper;
import org.jeecg.modules.pd.service.IPdPackageService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 定数包
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
@Service
public class PdPackageServiceImpl extends ServiceImpl<PdPackageMapper, PdPackage> implements IPdPackageService {

	@Autowired
	private PdPackageMapper pdPackageMapper;
	@Autowired
	private PdPackageDetailMapper pdPackageDetailMapper;
	
	@Override
	@Transactional
	public void saveMain(PdPackage pdPackage, List<PdPackageDetail> pdPackageDetailList) {
		pdPackageMapper.insert(pdPackage);
		if(pdPackageDetailList!=null && pdPackageDetailList.size()>0) {
			for(PdPackageDetail entity:pdPackageDetailList) {
				//外键设置
				entity.setPackageId(pdPackage.getId());
				entity.setDelFlag("0");
				entity.setId(null);
				pdPackageDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PdPackage pdPackage,List<PdPackageDetail> pdPackageDetailList) {
		pdPackageMapper.updateById(pdPackage);
		
		//1.先删除子表数据
		pdPackageDetailMapper.deleteByMainId(pdPackage.getId());
		
		//2.子表数据重新插入
		if(pdPackageDetailList!=null && pdPackageDetailList.size()>0) {
			for(PdPackageDetail entity:pdPackageDetailList) {
				//外键设置
				entity.setPackageId(pdPackage.getId());
				entity.setId(null);
				entity.setDelFlag("0");
				pdPackageDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		pdPackageDetailMapper.deleteByMainId(id);
		pdPackageMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pdPackageDetailMapper.deleteByMainId(id.toString());
			pdPackageMapper.deleteById(id);
		}
	}

	@Override
	public List<PdPackage> queryList(PdPackage pdPackage) {
		return pdPackageMapper.queryList(pdPackage);
	}

	@Override
	public Page<PdPackage> queryList(Page<PdPackage> pageList, PdPackage pdPackage) {
		return pageList.setRecords(pdPackageMapper.queryList(pdPackage));
	}

}
