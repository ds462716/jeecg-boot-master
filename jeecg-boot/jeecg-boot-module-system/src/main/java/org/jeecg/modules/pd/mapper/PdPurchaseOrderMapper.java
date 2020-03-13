package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;

import java.util.HashMap;
import java.util.List;

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

    /**
     * 批量更新审核状态
     */
    public int batchUpdateOrderStatus(HashMap<String, Object> map);

    /**
     * 批量更新合并订单号
     */
    public int batchUpdateMergeOrderNo(HashMap<String, Object> map);


 }
