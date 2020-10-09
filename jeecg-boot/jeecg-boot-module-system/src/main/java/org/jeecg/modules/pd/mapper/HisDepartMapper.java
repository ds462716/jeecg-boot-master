package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
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

	Page<HisDepartInf> selectListByPage(Page<HisDepartInf> page, @Param("entity") HisDepartInf entity);

	void deleteHisDepartInf();

	HisDepartInf queryHisDepart(@Param("fsfKsbh") String fsfKsbh);

}
