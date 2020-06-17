package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdStockRecord;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface PdStockRecordMapper extends BaseMapper<PdStockRecord> {


    List<PdStockRecord> selectList(@Param("entity") PdStockRecord pdStockRecord);

    IPage<PdStockRecord> selectList(Page<PdStockRecord> page, @Param("entity") PdStockRecord pdStockRecord);


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
    /**
     * 入库统计报表
     * @param pdStockRecord
     * @return
     */
    List<PdStockRecord> stockRecordReportQuery(PdStockRecord pdStockRecord);

    boolean deleteById(@Param("id") String id);

    /**
     * 假删除
     * @param pdStockRecord
     * @return
     */
    boolean deleteByDelFlag(PdStockRecord pdStockRecord);

    /*入库统计报表  根据产品查看每月入库量及金额*/
    List<HashMap> queryRecordView(PdStockRecord pdStockRecord);
}
