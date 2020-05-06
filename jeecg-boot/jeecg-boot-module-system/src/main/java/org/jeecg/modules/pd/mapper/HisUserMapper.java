package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.HisUserInf;

import java.util.List;

/**
 * @Description: HIS用户信息
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface HisUserMapper extends BaseMapper<HisUserInf> {

	List<HisUserInf> selectList(HisUserInf hisUserInf);

	void deleteHisUserInf();
}
