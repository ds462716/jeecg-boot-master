package org.jeecg.modules.pd.mapper;

import java.util.List;

import org.jeecg.modules.pd.entity.PdGoodsAllocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.vo.PdGoodsAllocationPage;
import org.jeecg.modules.system.model.SysDepartTreeModel;

/**
 * @Description: 货区货位表
 * @Author: jiangxz
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface PdGoodsAllocationMapper extends BaseMapper<PdGoodsAllocation> {

    List<SysDepartTreeModel> selectParentList(PdGoodsAllocation pdGoodsAllocation);

    List<SysDepartTreeModel> selectChildList(PdGoodsAllocation pdGoodsAllocation);

    List<PdGoodsAllocationPage> selectList(PdGoodsAllocation pdGoodsAllocation);
}
