package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMerge;
import org.jeecg.modules.pd.vo.PdProductPage;
import org.jeecg.modules.pd.vo.PdPurchaseOrderPage;
import java.util.Map;
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

    List<PdPurchaseOrderMerge> choosePurchaseOrderList(PdPurchaseOrderPage pdPurchaseOrderPage);

    List<PdProductPage> choosePurchaseOrderDetailList(PdPurchaseOrderPage pdPurchaseOrderPage);

    /**
     * 批量更新审核状态
     */
    public int batchUpdateOrderStatus(HashMap<String, Object> map);

    /**
     * 批量更新合并订单号
     */
    public int batchUpdateMergeOrderNo(HashMap<String, Object> map);


    //根据申购单号查询数量和金额
    public PdPurchaseOrder getCountAndMoney(@Param("orderNos") List<String> orderNos);

     /*获取总采购数量及当日采购数量*/
    Map<String,Object> queryPurchaseOrderCount(PdPurchaseOrder pdPurchaseOrder);

     /*首页查询  根据范围统计每日的采购量*/
    List<HashMap> queryPurchaseOrderDateList(PdPurchaseOrderPage purchaseOrderPage);

}
