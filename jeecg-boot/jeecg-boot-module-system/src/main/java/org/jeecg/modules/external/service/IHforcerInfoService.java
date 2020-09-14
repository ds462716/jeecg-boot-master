package org.jeecg.modules.external.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.HForcerInfo;

import java.util.Map;

/**
 * @Description:
 * @Author: MCB
 * @Date:   2020-07-09
 * @Version: V1.0
 */
public interface IHforcerInfoService extends IService<HForcerInfo> {

    /**
     * 分页查询
     * @param pageList
     * @param forcerInfo
     * @return
     */
    IPage<HForcerInfo> selectList(Page<HForcerInfo> pageList, HForcerInfo forcerInfo);
    /**
     * 判断是否已注册
     * @param macAddress
     * @return
     */
    public boolean queryConsumables(String macAddress);

    /*注册**/
    public void saveConsumables(Map<Object,Object> map);

    /**
     * 获取耗材柜相关信息
     * @param macAddress
     * @return
     */
    public HForcerInfo queryForcerList(String macAddress);

}
