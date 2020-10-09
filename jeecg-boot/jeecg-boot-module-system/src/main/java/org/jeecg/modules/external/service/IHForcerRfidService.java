package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.HForcerRfid;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: MCB
 * @Date:   2020-07-09
 * @Version: V1.0
 */
public interface IHForcerRfidService extends IService<HForcerRfid> {

    /**
     * 保存耗材柜rfid接口标签
     */
    public void saveList(Map<Object,Object> param);


    public List<Map<String,Object>> queryHforcerRfidList(HForcerRfid forcerRfid);

}
