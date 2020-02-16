package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;

import java.util.List;

/**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface PdProductStockTotalMapper extends BaseMapper<PdProductStockTotal> {

    List<PdProductStockTotalPage> selectList(PdProductStockTotal stockTotal);

    public void updateProductStock(PdProductStockTotal stockTotal);

}
