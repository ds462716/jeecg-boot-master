package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdPurchaseTemp;

/**
 * @Description: 申购模板主表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface PdPurchaseTempMapper extends BaseMapper<PdPurchaseTemp> {
    Page<PdPurchaseTemp> selectListByPage(Page<PdPurchaseTemp> page, @Param("entity") PdPurchaseTemp entity);


}
