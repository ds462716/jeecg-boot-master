package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.jeecg.modules.pd.entity.PdProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.vo.PdProductPage;

/**
 * @Description: pd_product
 * @Author: zxh
 * @Date:   2020-02-05
 * @Version: V1.0
 */
public interface PdProductMapper extends BaseMapper<PdProduct> {

    List<PdProductPage> chooseProductList(PdProduct pdProduct);

}
