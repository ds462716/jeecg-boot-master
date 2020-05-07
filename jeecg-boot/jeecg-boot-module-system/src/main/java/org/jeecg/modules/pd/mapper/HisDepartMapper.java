package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.HisDepartInf;

import java.util.List;

/**
 * @Description: HIS科室信息
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface HisDepartMapper extends BaseMapper<HisDepartInf> {

	List<HisDepartInf> selectList(HisDepartInf hisDepartInf);

	void deleteHisDepartInf();

	HisDepartInf queryHisDepart(String fsfKsbh);
}
