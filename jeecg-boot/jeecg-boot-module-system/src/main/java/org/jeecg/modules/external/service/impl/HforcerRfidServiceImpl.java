package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.external.entity.HForcerRfid;
import org.jeecg.modules.external.mapper.HforcerRfidMapper;
import org.jeecg.modules.external.service.IHForcerRfidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: mcb
 * @Date:   2020-07-09
 * @Version: V1.0
 */
@Service
public class HforcerRfidServiceImpl extends ServiceImpl<HforcerRfidMapper, HForcerRfid> implements IHForcerRfidService {
    @Autowired
    private HforcerRfidMapper forcerRfidMapper;

    /**
     * 保存耗材柜rfid接口标签
     * @param param
     */
    @Override
    @Transactional
    public void saveList(Map<Object,Object> param){
        //先根据柜子ID删除对应柜子rfid数据
        String forcerId= MapUtils.getString(param,"forcerId");
        if(StringUtils.isNotEmpty(forcerId)){
        forcerRfidMapper.deleteByforcerId(forcerId);
        List<Map<String,Object>> list=(List<Map<String,Object>>)MapUtils.getObject(param,"List");
        if(list!=null && list.size()>0) {
            //1.入SPD库
            for (Map<String, Object> map : list) {
                HForcerRfid forcer = new HForcerRfid();
                forcer.setForcerId(MapUtils.getString(map, "ForcerId"));
                forcer.setForcerNumber(MapUtils.getString(map, "ForcerNumber"));
                forcer.setRfId(MapUtils.getString(map, "RfId"));
                forcerRfidMapper.insert(forcer);
            }
        }
      }
    }


    public List<Map<String,Object>> queryHforcerRfidList(HForcerRfid forcerRfid) {
        return forcerRfidMapper.queryHforcerRfidList(forcerRfid);
    }
}
