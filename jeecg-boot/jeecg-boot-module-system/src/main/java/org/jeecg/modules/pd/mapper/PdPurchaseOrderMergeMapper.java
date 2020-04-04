package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMerge;

import java.util.List;

/**
 * @Description: 合并申购单表
 * @Author: mcb
 * @Date:   2020-03-13
 * @Version: V1.0
 */
public interface PdPurchaseOrderMergeMapper extends BaseMapper<PdPurchaseOrderMerge> {


    List<PdPurchaseOrderMerge> selectList(PdPurchaseOrderMerge purchaseOrderMerge);

}
