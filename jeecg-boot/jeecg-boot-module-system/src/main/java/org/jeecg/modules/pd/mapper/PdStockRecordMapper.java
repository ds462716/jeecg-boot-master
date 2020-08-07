package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.vo.RpInAndOutReportPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 出入库记录表
 * @Author: jiangxz
 * @Date: 2020-02-13
 * @Version: V1.0
 */
public interface PdStockRecordMapper extends BaseMapper<PdStockRecord> {


    List<PdStockRecord> selectList(@Param("entity") PdStockRecord pdStockRecord);

    IPage<PdStockRecord> selectList(Page<PdStockRecord> page, @Param("entity") PdStockRecord pdStockRecord);


    /**
     * 获取一条记录
     *
     * @param pdStockRecord
     * @return
     */
    PdStockRecord getOne(PdStockRecord pdStockRecord);

    /**
     * 调入明细查询
     *
     * @param pdStockRecord
     * @return
     */
    List<PdStockRecord> selectTransferList(PdStockRecord pdStockRecord);

    /**
     * 入库统计报表
     *
     * @param entity
     * @return
     */
    Page<PdStockRecord> stockRecordReportQuery(Page<PdStockRecord> page, @Param("entity") PdStockRecord entity);


    List<PdStockRecord> stockRecordReportQuery(@Param("entity") PdStockRecord entity);


    boolean deleteById(@Param("id") String id);

    /**
     * 假删除
     *
     * @param pdStockRecord
     * @return
     */
    boolean deleteByDelFlag(PdStockRecord pdStockRecord);

    /*入库统计报表  根据产品按月统计入库金额*/
    List<HashMap> queryRecordViewMoney(PdStockRecord pdStockRecord);

    /*入库统计报表  根据产品按月统计入库数量*/
    List<HashMap> queryRecordViewCount(PdStockRecord pdStockRecord);

    public List<Map<String, Object>> findOutQueryList(PdStockRecord pdStockRecord);


    IPage<PdStockRecord> querySupplierCountPageList(Page<PdStockRecord> page, @Param("entity") PdStockRecord pdStockRecord);

    List<PdStockRecord> querySupplierCountPageList(@Param("entity") PdStockRecord pdStockRecord);

    //根据供应商及产品ID统计出库数量及金额
    PdStockRecord queryApplyPriceList(PdStockRecord pdStockRecord);

    IPage<RpInAndOutReportPage> rpInAndOutReport(Page<RpInAndOutReportPage> page, @Param("entity") RpInAndOutReportPage entity);
    List<RpInAndOutReportPage> rpInAndOutReport(@Param("entity") RpInAndOutReportPage entity);

    /**
     * 获取入库产品总数 和 入库总金额
     * @param entity
     * @return
     */
    RpInAndOutReportPage getInTotalData(RpInAndOutReportPage entity);

    /**
     * 获取出库产品总数 和 出库总金额
     * @param entity
     * @return
     */
    RpInAndOutReportPage getOutTotalData(RpInAndOutReportPage entity);
}
