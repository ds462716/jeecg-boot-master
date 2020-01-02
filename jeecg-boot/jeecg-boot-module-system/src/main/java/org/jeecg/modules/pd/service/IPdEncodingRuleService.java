package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdEncodingRule;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdEncodingRuleDetail;

import java.util.List;

/**
 * @Description: 编码规则表
 * @Author: jeecg-boot
 * @Date:   2019-12-26
 * @Version: V1.0
 */
public interface IPdEncodingRuleService extends IService<PdEncodingRule> {

    void savePdEncodingRule(PdEncodingRule pdEncodingRule);
}
