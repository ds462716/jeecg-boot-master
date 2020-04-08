package org.jeecg.modules.pd.mapper;

import java.util.List;
import org.jeecg.modules.pd.entity.PdStoreroomPatrolDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: pd_storeroom_patrol_detail
 * @Author: jiangxz
 * @Date:   2020-04-07
 * @Version: V1.0
 */
public interface PdStoreroomPatrolDetailMapper extends BaseMapper<PdStoreroomPatrolDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdStoreroomPatrolDetail> selectByMainId(@Param("mainId") String mainId);
}
