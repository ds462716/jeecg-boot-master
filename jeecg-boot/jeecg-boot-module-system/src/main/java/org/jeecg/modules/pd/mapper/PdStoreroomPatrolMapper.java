package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdStoreroomPatrol;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: pd_storeroom_patrol
 * @Author: jiangxz
 * @Date:   2020-04-07
 * @Version: V1.0
 */
public interface PdStoreroomPatrolMapper extends BaseMapper<PdStoreroomPatrol> {

    List<PdStoreroomPatrol> selectList(PdStoreroomPatrol pdStoreroomPatrol);
}
