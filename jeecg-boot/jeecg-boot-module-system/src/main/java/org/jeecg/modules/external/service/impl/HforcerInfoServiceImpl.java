package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.jeecg.modules.external.entity.HForcerInfo;
import org.jeecg.modules.external.mapper.HforcerInfoMapper;
import org.jeecg.modules.external.service.IHforcerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * @Description:
 * @Author: mcb
 * @Date:   2020-07-09
 * @Version: V1.0
 */
@Service
public class HforcerInfoServiceImpl extends ServiceImpl<HforcerInfoMapper, HForcerInfo> implements IHforcerInfoService {
    @Autowired
    private HforcerInfoMapper hforcerInfoMapper;

    /**
     * 判断是否已注册
     * @param macAddress
     * @return
     */
    @Override
    public boolean queryConsumables(String macAddress){
        boolean state=true;
        HForcerInfo hForcer=new HForcerInfo();
        hForcer.setMacAddress(macAddress);
        HForcerInfo forcer=  hforcerInfoMapper.queryConsumables(hForcer);
        if(forcer!=null && forcer.getId()!=null){
            state=false;
        }
        return state;
    }

    @Override
    @Transactional
    public void saveConsumables(Map<Object,Object> map) {
        //1.入SPD库
        HForcerInfo hForcer=new HForcerInfo();
        hForcer.setForcerNo("FN"+ System.currentTimeMillis());//柜子编号   自动生成
        hForcer.setForcerName(MapUtils.getString(map,"forcerName"));//柜子名称
        hForcer.setIsDisable("0");//默认未启用
        hForcer.setMacAddress(MapUtils.getString(map,"macAddress"));//设备地址
        hForcer.setKfId(MapUtils.getString(map,"kfId"));//管理库房id
        hForcer.setSjkfId(MapUtils.getString(map,"sjkfId"));//上一级库房id
        super.save(hForcer);
    }

    /**
     * 获取耗材柜相关信息
     * @param macAddress
     * @return
     */
    @Override
    public HForcerInfo queryForcerList(String macAddress){
        HForcerInfo hForcer=new HForcerInfo();
        hForcer.setMacAddress(macAddress);
        return  hforcerInfoMapper.queryConsumables(hForcer);
    }
}
