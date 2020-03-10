package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.pd.entity.PdStockRecord;

import java.util.List;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface PdStockRecordMapper extends BaseMapper<PdStockRecord> {


    List<PdStockRecord> selectList(PdStockRecord pdStockRecord);

    /**
     * 获取一条记录
     * @param pdStockRecord
     * @return
     */
    PdStockRecord getOne(PdStockRecord pdStockRecord);

    /**
     * 调入明细查询
     * @param pdStockRecord
     * @return
     */
    List<PdStockRecord> selectTransferList(PdStockRecord pdStockRecord);

}
