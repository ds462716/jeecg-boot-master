package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 申购订单主表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface PdPurchaseOrderMapper extends BaseMapper<PdPurchaseOrder> {

    List<PdPurchaseOrder> selectList(PdPurchaseOrder pdPurchaseOrder);

 }
