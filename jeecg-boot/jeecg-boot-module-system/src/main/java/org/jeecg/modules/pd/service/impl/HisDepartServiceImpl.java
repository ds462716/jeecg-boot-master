package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.HisDepartInf;
import org.jeecg.modules.pd.mapper.HisDepartMapper;
import org.jeecg.modules.pd.service.IHisDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Description:
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
@Service
	public class HisDepartServiceImpl extends ServiceImpl<HisDepartMapper, HisDepartInf> implements IHisDepartService {

	@Autowired
	HisDepartMapper hisDepartMapper;

	/**
	 * 查询列表
	 * @param page
	 * @param hisDepartInf
	 * @return
	 */
	@Override
	public Page<HisDepartInf> selectList(Page<HisDepartInf> page, HisDepartInf hisDepartInf) {
		return page.setRecords(hisDepartMapper.selectList(hisDepartInf));
	}


	@Override
	public List<HisDepartInf> selectHisDepartInf(HisDepartInf hisDepartInf) {
		return hisDepartMapper.selectList(hisDepartInf);
	}

	@Override
	@Transactional
	public void synUpdateDept(List<HisDepartInf> deptList) {
		if (deptList != null && deptList.size() > 0) {
			for(HisDepartInf deptInf: deptList){
				String fsfKsbh=deptInf.getFsfKsbh();
				HisDepartInf inf=hisDepartMapper.queryHisDepart(fsfKsbh);
				if(inf==null){
					hisDepartMapper.insert(deptInf);
				}
			}
		}
	}
}
