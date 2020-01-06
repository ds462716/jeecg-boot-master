package org.jeecg.modules.pd.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdEncodingRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 编码规则表
 * @Author: jeecg-boot
 * @Date:   2019-12-26
 * @Version: V1.0
 */
public interface PdEncodingRuleMapper extends BaseMapper<PdEncodingRule> {

    List<PdEncodingRule> selectList(PdEncodingRule pdEncodingRule);
}
