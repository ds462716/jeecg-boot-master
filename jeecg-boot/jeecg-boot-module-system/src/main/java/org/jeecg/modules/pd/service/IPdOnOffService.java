package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdOnOff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: pd_on_off
 * @Author: jiangxz
 * @Date:   2020-04-01
 * @Version: V1.0
 */
public interface IPdOnOffService extends IService<PdOnOff> {

    PdOnOff getOne(PdOnOff pdOnOff);

    List<PdOnOff> selectOriginalList(PdOnOff pdOnOff);

}
