package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.HForcerRfid;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: mcb
 * @Date:   2020-07-09
 * @Version: V1.0
 */
public interface HforcerRfidMapper extends BaseMapper<HForcerRfid> {

    public void deleteByforcerId(@Param("forcerId") String forcerId);

    public List<Map<String,Object>> queryHforcerRfidList(HForcerRfid face);

}
