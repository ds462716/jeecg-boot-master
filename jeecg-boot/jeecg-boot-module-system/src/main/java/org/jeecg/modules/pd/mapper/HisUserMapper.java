package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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

	Page<HisUserInf> selectListByPage(Page<HisUserInf> page, @Param("entity") HisUserInf entity);

	void deleteHisUserInf();
}
