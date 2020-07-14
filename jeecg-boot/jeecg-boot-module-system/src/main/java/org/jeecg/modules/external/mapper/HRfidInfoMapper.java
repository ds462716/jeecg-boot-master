package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.external.entity.HRfidInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: mcb
 * @Date:   2020-07-09
 * @Version: V1.0
 */
public interface HRfidInfoMapper extends BaseMapper<HRfidInfo> {

    public List<Map<String,Object>> queryHrfidList(HRfidInfo hrfid);

    public List<Map<String,Object>> queryHrfid(HRfidInfo hrfid);


}
