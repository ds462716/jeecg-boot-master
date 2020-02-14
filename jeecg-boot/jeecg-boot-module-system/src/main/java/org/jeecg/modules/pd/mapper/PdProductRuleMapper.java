package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdProductRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: pd_product_rule
 * @Author: zxh
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface PdProductRuleMapper extends BaseMapper<PdProductRule> {
    void removeByProductId(PdProductRule pdProductRule);

    List<PdProductRule> selectList(PdProductRule pdProductRule);

    PdEncodingRule getByRuleId(String ruleId);
}
