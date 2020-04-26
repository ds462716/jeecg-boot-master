package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdDepartConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 部门个性化配置
 * @Author: zxh
 * @Date:   2020-01-19
 * @Version: V1.0
 */
public interface IPdDepartConfigService extends IService<PdDepartConfig> {

    String findPdDepartConfig(String reminder_type,String departParentId);

}
