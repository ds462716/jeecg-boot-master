package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdGoodsAllocation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.model.SysDepartTreeModel;

import java.util.List;
import java.util.Map;

/**
 * @Description: 货区货位表
 * @Author: jiangxz
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IPdGoodsAllocationService extends IService<PdGoodsAllocation> {
    /**
     * 查询所有货区货位,并分节点进行显示
     * @return
     */
    List<SysDepartTreeModel> queryTreeList(List<SysDepartTreeModel> departTreeList);

    List<PdGoodsAllocationPage> selectAllList(PdGoodsAllocation pdGoodsAllocation);

    boolean updatePdGoodsAllocation(PdGoodsAllocation pdGoodsAllocation);

    boolean savePdGoodsAllocation(PdGoodsAllocation pdGoodsAllocation);

    List<PdGoodsAllocationPage> getOptionsForSelect(PdGoodsAllocation pdGoodsAllocation);
}
