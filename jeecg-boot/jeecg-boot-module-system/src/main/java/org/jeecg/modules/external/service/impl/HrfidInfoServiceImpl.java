package org.jeecg.modules.external.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.external.entity.HRfidInfo;
import org.jeecg.modules.external.mapper.HRfidInfoMapper;
import org.jeecg.modules.external.service.IHRfidInfoService;
import org.jeecg.modules.pd.entity.PdProductStockUniqueCode;
import org.jeecg.modules.pd.service.IPdProductStockUniqueCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: mcb
 * @Date:   2020-07-09
 * @Version: V1.0
 */
@Service
public class HrfidInfoServiceImpl extends ServiceImpl<HRfidInfoMapper, HRfidInfo> implements IHRfidInfoService {
    @Autowired
    private HRfidInfoMapper rfidInfoMapper;
    @Autowired
    private IPdProductStockUniqueCodeService productStockUniqueCodeService;

    /**
     * rfid条码信息保存
     * @param map
     */
    @Override
    @Transactional
    public void saveHrfid(Map<Object,Object> map) {
        //1.入SPD库
        HRfidInfo rfid = new HRfidInfo();
        rfid.setRfId(MapUtils.getString(map, "rfId"));//Rfid
        //先判断该rfid标签是否已经存在
        List<Map<String, Object>> list = rfidInfoMapper.queryHrfid(rfid);
        if (list == null || list.size() == 0) {
            rfid.setProductId(MapUtils.getString(map, "productId"));//产品ID
            rfid.setUserId(MapUtils.getString(map, "userId"));
            rfid.setStockId(MapUtils.getString(map, "productStockId"));//库存明细ID
            rfid.setRkmxId(MapUtils.getString(map, "rkmxId"));//入库明细ID
            rfid.setDepartParentId(MapUtils.getString(map, "departParentId"));
            rfid.setProductId(MapUtils.getString(map, "productId"));//产品ID
            rfid.setProductNo(MapUtils.getString(map, "productNo"));//产品编码
            rfid.setIsDisable(PdConstant.DISABLE_ENABLE_STATUS_0);//启用标识   0：默认为启用
            rfid.setBatchNo(MapUtils.getString(map, "batchNo"));//批号
            String validDate = MapUtils.getString(map, "validDate");
            if (!StringUtils.isEmpty(validDate)) {
                rfid.setValidDate(DateUtils.str2Date(validDate,new SimpleDateFormat("yyyy-MM-dd")));//有效期
            }
            rfidInfoMapper.insert(rfid);
            PdProductStockUniqueCode psc=new PdProductStockUniqueCode();
            psc.setProductStockId(rfid.getStockId());
            psc.setId(rfid.getRfId());
            psc.setUniqueCodeOrder(MapUtils.getInteger(map, "uniqueCodeOrder"));
            psc.setDepartId(MapUtils.getString(map, "departId"));
            psc.setDepartParentId(MapUtils.getString(map, "departParentId"));
            productStockUniqueCodeService.foreignSaveUniqueCode(psc);
        }
    }


    /**
     * 根据rfid标签获取库存信息接口
     * @param param
     * @return
     */
    public List<Map<String,Object>> queryHrfidList(Map param) {
        List<Map<String,Object>> list= new ArrayList<>();
        String kfId=MapUtils.getString(param,"kfId");
        List<Map<String,Object>> lists=(List<Map<String,Object>>)MapUtils.getObject(param,"List");
        List<String> rfIds =new ArrayList<>();
        List<String> batchNos =new ArrayList<>();
        for(Map<String,Object> map:lists){
            String rfId=MapUtils.getString(map,"rfId");
            String batchNo=MapUtils.getString(map,"batchNo");
            rfIds.add(rfId);
            if(StringUtils.isNotEmpty(batchNo)){
                batchNos.add(batchNo);
            }
        }
        HRfidInfo rfIdInfo=new HRfidInfo();
        rfIdInfo.setKfId(kfId);
        rfIdInfo.setBatchNos(batchNos);
        rfIdInfo.setRfIds(rfIds);
        List<Map<String,Object>> rfidList=rfidInfoMapper.queryHrfidList(rfIdInfo);
        if(rfidList!=null && rfidList.size()>0){
            for(Map<String,Object> maps:rfidList){
                int czNum=0;//操作数量
                String stockId= MapUtils.getString(maps,"productStockId");
                String productId= MapUtils.getString(maps,"productId");
                //String productNo= MapUtils.getString(maps,"productNo");
                //String bacthNo= MapUtils.getString(maps,"batchNo");
                HRfidInfo hrfidInfo=new HRfidInfo();
                hrfidInfo.setProductId(productId);
                //hrfidInfo.setProductNo(productNo);
                //hrfidInfo.setBatchNo(bacthNo);
                hrfidInfo.setStockId(stockId);
                hrfidInfo.setRfIds(rfIds);
                List<Map<String,Object>> hfidList= rfidInfoMapper.queryHrfid(hrfidInfo);
                if(hfidList!=null && hfidList.size()>0){
                    czNum=czNum+hfidList.size();
                }
                maps.put("czNum",czNum);
            }
        }
        return rfidList;
    }
}
