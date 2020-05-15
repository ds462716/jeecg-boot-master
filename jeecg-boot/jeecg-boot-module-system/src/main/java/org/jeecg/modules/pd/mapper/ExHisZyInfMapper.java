package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.vo.ExHisZyInfPage;

import java.util.List;

/**
 * @Description: HIs接口相关
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface ExHisZyInfMapper extends BaseMapper<ExHisZyInfPage> {


	int saveExHisZyInf(List<ExHisZyInfPage> exHisZyInf);
}
