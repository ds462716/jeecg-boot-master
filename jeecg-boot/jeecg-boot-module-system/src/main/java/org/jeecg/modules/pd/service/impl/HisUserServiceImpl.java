package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.HisDepartInf;
import org.jeecg.modules.pd.entity.HisUserInf;
import org.jeecg.modules.pd.mapper.HisDepartMapper;
import org.jeecg.modules.pd.mapper.HisUserMapper;
import org.jeecg.modules.pd.service.IHisUserService;
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
public class HisUserServiceImpl extends ServiceImpl<HisUserMapper, HisUserInf> implements IHisUserService {
	@Autowired
	HisUserMapper hisUserMapper;
	@Autowired
	HisDepartMapper hisDepartMapper;

	/**
	 * 查询列表
	 * @param page
	 * @param hisUserInf
	 * @return
	 */
	@Override
	public Page<HisUserInf> selectList(Page<HisUserInf> page, HisUserInf hisUserInf) {
		return page.setRecords(hisUserMapper.selectList(hisUserInf));
	}


	@Override
	@Transactional
	public void synUpdateDeptOrUser(List<HisDepartInf> deptList, List<HisUserInf> userList) {
		if (deptList != null && deptList.size() > 0) {
			hisUserMapper.deleteHisUserInf();
			hisDepartMapper.deleteHisDepartInf();
			for(HisDepartInf deptInf: deptList){
				hisDepartMapper.insert(deptInf);
			}
			for(HisUserInf userInf: userList){
				hisUserMapper.insert(userInf);
			}
		}
	}
}
