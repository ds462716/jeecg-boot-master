package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdEncodingRuleDetail;
import org.jeecg.modules.pd.mapper.PdEncodingRuleDetailMapper;
import org.jeecg.modules.pd.service.IPdEncodingRuleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 编码规则详情表
 * @Author: jeecg-boot
 * @Date:   2019-12-26
 * @Version: V1.0
 */
@Service
public class PdEncodingRuleDetailServiceImpl extends ServiceImpl<PdEncodingRuleDetailMapper, PdEncodingRuleDetail> implements IPdEncodingRuleDetailService {

    @Autowired
    private PdEncodingRuleDetailMapper pdEncodingRuleDetailMapper;

    @Override
    public void removeByCodeId(PdEncodingRule pdEncodingRule) {
        pdEncodingRuleDetailMapper.removeByCodeId(pdEncodingRule);
    }
}
