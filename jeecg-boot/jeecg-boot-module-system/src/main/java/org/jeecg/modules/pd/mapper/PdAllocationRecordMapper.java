package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdAllocationRecord;

import java.util.List;

/**
 * @Description: 调拨记录表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface PdAllocationRecordMapper extends BaseMapper<PdAllocationRecord> {

    List<PdAllocationRecord> selectList(PdAllocationRecord allocationRecord);

    List<PdAllocationRecord> chooseAllocationList(PdAllocationRecord allocationRecord);
    /**
     * 增加发货数量 用于出库保存/提交 add by jiangxz 20200309
     *
     * @return
     */
    void additionArrivalCount(PdAllocationRecord record);

    /**
     * 扣减发货数量 用于出库拒绝 add by jiangxz 20200309
     *
     * @return
     */
    void subtractArrivalCount(PdAllocationRecord record);
}
