package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;

/**
 * @Description: 申购订单主表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface PdPurchaseOrderMapper extends BaseMapper<PdPurchaseOrder> {

    List<PdPurchaseOrder> selectList(PdPurchaseOrder pdPurchaseOrder);

    List<PdPurchaseOrderPage> choosePurchaseOrderList(PdPurchaseOrderPage pdPurchaseOrderPage);

    List<PdProductPage> choosePurchaseOrderDetailList(PdPurchaseOrderPage pdPurchaseOrderPage);

 }
