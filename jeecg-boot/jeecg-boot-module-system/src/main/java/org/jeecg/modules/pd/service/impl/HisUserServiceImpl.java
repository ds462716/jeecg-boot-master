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
	private HisUserMapper hisUserMapper;
	@Autowired
	private HisDepartMapper hisDepartMapper;

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
			for(HisDepartInf deptInf: deptList){
				HisDepartInf inf=hisDepartMapper.queryHisDepart(deptInf.getFsfKsbh());
				if(inf==null){
				hisDepartMapper.insert(deptInf);
				}
			}
		}
		if(userList != null && userList.size() > 0){
			for(HisUserInf userInf: userList){
				hisUserMapper.insert(userInf);
			}
		}
	}
}
