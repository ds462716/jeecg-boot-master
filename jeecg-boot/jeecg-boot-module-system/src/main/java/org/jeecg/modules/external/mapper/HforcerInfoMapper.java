package org.jeecg.modules.external.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.external.entity.HForcerInfo;

/**
 * @Description:
 * @Author: mcb
 * @Date:   2020-07-09
 * @Version: V1.0
 */
public interface HforcerInfoMapper extends BaseMapper<HForcerInfo> {

    Page<HForcerInfo> selectList(Page<HForcerInfo> page, @Param("entity") HForcerInfo entity);

    //根据设备mac地址获取数据
    public HForcerInfo  queryConsumables(HForcerInfo hForcer);

}
