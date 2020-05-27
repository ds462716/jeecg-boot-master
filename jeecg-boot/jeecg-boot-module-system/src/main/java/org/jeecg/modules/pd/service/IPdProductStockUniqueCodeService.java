package org.jeecg.modules.pd.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockUniqueCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 库存关联条码表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
public interface IPdProductStockUniqueCodeService extends IService<PdProductStockUniqueCode> {

    Result<List<PdProductStockUniqueCode>> uniqueCodeGeneration(PdProductStockUniqueCode pdProductStockUniqueCode);

    Result<List<PdProductStockUniqueCode>> batchCodeGeneration(List<String> asList);

    void deleteCode(String id);
}
