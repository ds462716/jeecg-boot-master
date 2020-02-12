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
    public List<PdGoodsAllocationPage> selectList(PdGoodsAllocation pdGoodsAllocation) {
        // TODO 组装 childList
        return pdGoodsAllocationMapper.selectList(pdGoodsAllocation);
    }


}
