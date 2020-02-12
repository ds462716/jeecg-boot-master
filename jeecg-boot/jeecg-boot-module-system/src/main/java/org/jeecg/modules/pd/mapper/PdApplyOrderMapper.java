package org.jeecg.modules.pd.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface PdApplyOrderMapper extends BaseMapper<PdApplyOrder> {

    List<PdApplyOrder> selectList(PdApplyOrder pdApplyOrder);

    public boolean deleteByMainId(@Param("id") String id);
}
