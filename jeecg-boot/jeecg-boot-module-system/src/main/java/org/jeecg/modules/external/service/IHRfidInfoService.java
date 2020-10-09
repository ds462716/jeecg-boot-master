package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.HRfidInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: MCB
 * @Date:   2020-07-09
 * @Version: V1.0
 */
public interface IHRfidInfoService extends IService<HRfidInfo> {

    /**
     * rfid条码信息保存
     * @param map
     */
    public void saveHrfid(Map<Object,Object> map);

    public List<Map<String,Object>> queryHrfidList(Map param);

}
