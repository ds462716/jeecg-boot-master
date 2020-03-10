package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdDepartConfig;
import org.jeecg.modules.pd.mapper.PdDepartConfigMapper;
import org.jeecg.modules.pd.service.IPdDepartConfigService;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 部门个性化配置
 * @Author: zxh
 * @Date:   2020-01-19
 * @Version: V1.0
 */
@Service
public class PdDepartConfigServiceImpl extends ServiceImpl<PdDepartConfigMapper, PdDepartConfig> implements IPdDepartConfigService {

    @Override
    public PdDepartConfig findPdDepartConfig(PdDepartConfig pdDepartConfig){
        LambdaQueryWrapper<PdDepartConfig> query = new LambdaQueryWrapper<>();
        query.eq(PdDepartConfig::getDepartParentId,pdDepartConfig.getDepartParentId());
        query.eq(PdDepartConfig::getDepartId,pdDepartConfig.getDepartId());
        query.eq(PdDepartConfig::getType,pdDepartConfig.getType());
        query.eq(PdDepartConfig::getIsDefault, PdConstant.IS_DEFAULT_1);
        pdDepartConfig = this.getOne(query);
        return pdDepartConfig;
    }
}
