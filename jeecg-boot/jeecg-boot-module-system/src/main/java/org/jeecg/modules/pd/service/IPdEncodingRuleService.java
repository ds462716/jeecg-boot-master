package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdEncodingRuleDetail;

import java.util.List;

/**
 * @Description: 编码规则表
 * @Author: zxh
 * @Date:   2019-12-26
 * @Version: V1.0
 */
public interface IPdEncodingRuleService extends IService<PdEncodingRule> {

    void savePdEncodingRule(PdEncodingRule pdEncodingRule);

    Page<PdEncodingRule> selectList(Page<PdEncodingRule> pageList,PdEncodingRule pdEncodingRule);

    List<PdEncodingRule> selectList(PdEncodingRule pdEncodingRule);

    void updatePdEncodingRule(PdEncodingRule pdEncodingRule);

    Result<Object> deleteV(String id);

    Result<Object> deleteBatchV(String ids);
}
