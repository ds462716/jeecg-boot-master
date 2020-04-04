package org.jeecg.modules.pd.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.pd.entity.PdGoodsAllocation;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdGoodsAllocationMapper;
import org.jeecg.modules.pd.mapper.PdProductStockMapper;
import org.jeecg.modules.pd.mapper.PdStockRecordDetailMapper;
import org.jeecg.modules.pd.service.IPdGoodsAllocationService;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: 货区货位表
 * @Author: jiangxz
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
public class PdGoodsAllocationServiceImpl extends ServiceImpl<PdGoodsAllocationMapper, PdGoodsAllocation> implements IPdGoodsAllocationService {

    @Autowired
    private PdGoodsAllocationMapper pdGoodsAllocationMapper;
    @Autowired
    private PdStockRecordDetailMapper pdStockRecordDetailMapper;
    @Autowired
    private PdProductStockMapper pdProductStockMapper;

    @Override
    public List<PdGoodsAllocationPage> queryTreeList(String departId) {
        List<PdGoodsAllocationPage> treeList = null;
        if(oConvertUtils.isNotEmpty(departId)) {
            PdGoodsAllocation query1 = new PdGoodsAllocation();
            query1.setDepartId(departId);
            treeList = pdGoodsAllocationMapper.selectHuoquList(query1);

            if(CollectionUtils.isNotEmpty(treeList)){
                for (PdGoodsAllocationPage huoqu : treeList) {//循环货区
                    PdGoodsAllocation query2 = new PdGoodsAllocation();
                    query2.setParentId(huoqu.getId());
                    query2.setDepartId(departId);
                    List<PdGoodsAllocationPage> huoweiList = pdGoodsAllocationMapper.selectHuoweiList(query2);
                    if(CollectionUtils.isNotEmpty(huoweiList)){
                        huoqu.setChildren(huoweiList);
                    }
                }
            }
        }
        return treeList;
    }

    @Override
    public List<PdGoodsAllocationPage> selectAllList(PdGoodsAllocation pdGoodsAllocation) {
        return pdGoodsAllocationMapper.selectAllList(pdGoodsAllocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePdGoodsAllocation(PdGoodsAllocation pdGoodsAllocation) {
        PdGoodsAllocation oldItem = super.getById(pdGoodsAllocation.getId());
        Boolean bool = super.updateById(pdGoodsAllocation);

        if(bool && PdConstant.GOODS_ALLCATION_AREA_TYPE_2.equals(pdGoodsAllocation.getAreaType())){
            //货位更新 需要变更货区的数量
            PdGoodsAllocation queryItem = new PdGoodsAllocation();
            queryItem.setParentId(pdGoodsAllocation.getParentId());
            List<PdGoodsAllocationPage> list = this.selectAllList(queryItem);
            if(CollectionUtils.isNotEmpty(list)){
                Integer sum = 0;
                for (PdGoodsAllocationPage item : list) {
                    sum = sum + (item.getSubNum() == null ? 0 : item.getSubNum());
                }

                PdGoodsAllocation parentItem = new PdGoodsAllocation();
                parentItem.setId(pdGoodsAllocation.getParentId());
                parentItem.setSubNum(sum);
                super.updateById(parentItem);
            }
            if(!oldItem.getCode().equals(pdGoodsAllocation.getCode())){
                // 货位编号变更 则变更出入库记录以及库存中的货位编号
                PdStockRecordDetail detail = new PdStockRecordDetail();
                detail.setInHuoweiCode(pdGoodsAllocation.getCode());
                detail.setOldInHuoweiCode(oldItem.getCode());
                detail.setOutHuoweiCode(pdGoodsAllocation.getCode());
                detail.setOldOutHuoweiCode(oldItem.getCode());
                pdStockRecordDetailMapper.updateInHuoweiCode(detail);
                pdStockRecordDetailMapper.updateOutHuoweiCode(detail);

                PdProductStock stock = new PdProductStock();
                stock.setHuoweiCode(pdGoodsAllocation.getCode());
                stock.setOldHuoweiCode(oldItem.getCode());
                pdProductStockMapper.updateHuoweiCode(stock);
            }
        }
        return bool;
    }

    @Override
    public boolean savePdGoodsAllocation(PdGoodsAllocation pdGoodsAllocation) {
        Boolean bool = super.save(pdGoodsAllocation);

        if(bool && PdConstant.GOODS_ALLCATION_AREA_TYPE_2.equals(pdGoodsAllocation.getAreaType())){
            //货位新增 需要变更货区的数量
            PdGoodsAllocation queryItem = new PdGoodsAllocation();
            queryItem.setParentId(pdGoodsAllocation.getParentId());
            List<PdGoodsAllocationPage> list = this.selectAllList(queryItem);
            if(CollectionUtils.isNotEmpty(list)){
                Integer sum = 0;
                for (PdGoodsAllocationPage item : list) {
                    sum = sum + (item.getSubNum() == null ? 0 : item.getSubNum());
                }

                PdGoodsAllocation parentItem = new PdGoodsAllocation();
                parentItem.setId(pdGoodsAllocation.getParentId());
                parentItem.setSubNum(sum);
                super.updateById(parentItem);
            }
        }

        return bool;
    }

    @Override
    public List<PdGoodsAllocationPage> getOptionsForSelect(PdGoodsAllocation pdGoodsAllocation) {
        return pdGoodsAllocationMapper.getOptionsForSelect(pdGoodsAllocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteBatch(List<String> ids) {
        String message = "";

        for (String id : ids) {
            PdGoodsAllocation pdGoodsAllocation = pdGoodsAllocationMapper.selectById(id);
            if(pdGoodsAllocation != null){
                if(pdGoodsAllocation.getAreaType().equals(PdConstant.GOODS_ALLCATION_AREA_TYPE_1)){
                    PdGoodsAllocation query = new PdGoodsAllocation();
                    query.setParentId(id);
                    List<PdGoodsAllocationPage> huoweiList = pdGoodsAllocationMapper.selectHuoweiList(query);
                    for(PdGoodsAllocationPage huowei : huoweiList){
                        PdProductStock pdProductStock = new PdProductStock();
                        pdProductStock.setHuoweiCode(huowei.getCode());
                        List<PdProductStock> stockList = pdProductStockMapper.selectList(pdProductStock);
                        if(CollectionUtils.isNotEmpty(stockList)){
                            message = message + huowei.getName() + " ";
                        }else{
                            pdGoodsAllocationMapper.deleteById(huowei.getId());
                        }
                    }
                    if(oConvertUtils.isEmpty(message)){
                        // 货区下货位全部删除后，则删除该货区
                        pdGoodsAllocationMapper.deleteById(id);
                    }
                }else{
                    PdProductStock pdProductStock = new PdProductStock();
                    pdProductStock.setHuoweiCode(pdGoodsAllocation.getCode());
                    List<PdProductStock> stockList = pdProductStockMapper.selectList(pdProductStock);
                    if(CollectionUtils.isNotEmpty(stockList)){
                        message = message + pdGoodsAllocation.getName() + " ";
                    }else{
                        pdGoodsAllocationMapper.deleteById(id);
                    }
                }
            }
        }

        if(oConvertUtils.isNotEmpty(message)){
            return "货位 "+message+"中有库存，请移库后再进行删除操作！";
        }
        return PdConstant.TRUE;
    }

}
