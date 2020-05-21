package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdSpecLog;

import java.util.List;

/**
 * @Description: 规格数量清零操作日志表
 * @Author: jiangxz
 * @Date:   2020-05-20
 * @Version: V1.0
 */
public interface PdSpecLogMapper extends BaseMapper<PdSpecLog> {


    List<PdSpecLog> selectList(PdSpecLog pdSpecLog);


}
