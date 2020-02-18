package org.jeecg.modules.pd.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.constant.PdConstant;
import org.jeecg.modules.pd.entity.PdGoodsAllocation;
import org.jeecg.modules.pd.mapper.PdGoodsAllocationMapper;
import org.jeecg.modules.pd.service.IPdGoodsAllocationService;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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

    @Override
    public List<SysDepartTreeModel> queryTreeList(List<SysDepartTreeModel> departTreeList) {
        if(CollectionUtils.isNotEmpty(departTreeList)){
            for (SysDepartTreeModel departItem : departTreeList) { //循环一级机构
                List<SysDepartTreeModel> departChildrenList = departItem.getChildren();

                if(CollectionUtils.isNotEmpty(departChildrenList)){
                    for (SysDepartTreeModel childDepartItem : departChildrenList) {//循环二级机构
                        PdGoodsAllocation pdGoodsAllocation = new PdGoodsAllocation();
                        pdGoodsAllocation.setDepartId(childDepartItem.getId());
                        List<SysDepartTreeModel> goodsParentList = pdGoodsAllocationMapper.selectParentList(pdGoodsAllocation);

                        if(CollectionUtils.isNotEmpty(goodsParentList)){
                            childDepartItem.setIsLeaf(false);
                            childDepartItem.setChildren(goodsParentList);
                            for (SysDepartTreeModel goodsParentItem : goodsParentList) {//循环货区
                                goodsParentItem.setIsLeaf(true);
                                goodsParentItem.setOrgType(PdConstant.GOODS_ALLCATION_FLAG_1); //货位标识
                                PdGoodsAllocation pdGoodsAllocation2 = new PdGoodsAllocation();
                                pdGoodsAllocation2.setParentId(goodsParentItem.getId());
                                List<SysDepartTreeModel> goodsChildList = pdGoodsAllocationMapper.selectChildList(pdGoodsAllocation2);
                                if(CollectionUtils.isNotEmpty(goodsChildList)){
                                    goodsParentItem.setIsLeaf(false);
                                    goodsParentItem.setChildren(goodsChildList);
                                    for (SysDepartTreeModel goodsChildItem : goodsChildList) {//循环货位
                                        goodsChildItem.setIsLeaf(true);
                                        goodsChildItem.setOrgType(PdConstant.GOODS_ALLCATION_FLAG_2);//货位标识
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return departTreeList;
    }

    @Override
    public List<PdGoodsAllocationPage> selectAllList(PdGoodsAllocation pdGoodsAllocation) {
        return pdGoodsAllocationMapper.selectAllList(pdGoodsAllocation);
    }

    @Override
    public boolean updatePdGoodsAllocation(PdGoodsAllocation pdGoodsAllocation) {
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

}
