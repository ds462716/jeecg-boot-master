package org.jeecg.modules.pd.service.impl;

import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdEncodingRuleDetail;
import org.jeecg.modules.pd.mapper.PdEncodingRuleMapper;
import org.jeecg.modules.pd.service.IPdEncodingRuleService;
import org.jeecg.modules.pd.util.UUIDUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @Description: 编码规则表
 * @Author: jeecg-boot
 * @Date:   2019-12-26
 * @Version: V1.0
 */
@Service
public class PdEncodingRuleServiceImpl extends ServiceImpl<PdEncodingRuleMapper, PdEncodingRule> implements IPdEncodingRuleService {

    @Override
    @Transactional
    public void savePdEncodingRule(PdEncodingRule pdEncodingRule) {
        if(pdEncodingRule!=null){
            String s = UUIDUtil.getUuid();
            pdEncodingRule.setId(s);
            List<PdEncodingRuleDetail> pdEncodingRuleDetails = pdEncodingRule.getPdEncodingRuleDetails();
            if(pdEncodingRuleDetails!=null && pdEncodingRuleDetails.size()>0){
                for(PdEncodingRuleDetail pdEncodingRuleDetail :pdEncodingRuleDetails){
                    pdEncodingRuleDetail.setIdentifier(pdEncodingRuleDetail.getId());
                    pdEncodingRuleDetail.setCodeId(pdEncodingRule.getId());
                    pdEncodingRuleDetail.setId(UUIDUtil.getUuid());
                }
            }
            this.save(pdEncodingRule);
        }
    }

}
