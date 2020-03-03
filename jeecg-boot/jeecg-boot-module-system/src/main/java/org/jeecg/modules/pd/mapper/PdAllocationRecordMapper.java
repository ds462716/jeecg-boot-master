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
}
