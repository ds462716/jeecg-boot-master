package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.vo.RpDepartUseReportPage;
import org.jeecg.modules.pd.vo.RpReDetailReportPage;
import org.jeecg.modules.pd.vo.RpSupplierUseReportPage;
import org.jeecg.modules.pd.vo.RpUseDetailReportPage;
import org.jeecg.modules.pd.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: 报表统计
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
public interface PdStatisticalReportMapper extends BaseMapper<PdStatisticalReport> {

    IPage<PdStockRecordDetail> supplierInDetailReport(Page<PdStockRecordDetail> page, @Param("entity") PdStockRecordDetail inDetail);

    IPage<RpUseDetailReportPage> rpUseDetailReport(Page<RpUseDetailReportPage> usePageDetail, @Param("entity") RpUseDetailReportPage rpUseDetailReportPage);

    IPage<RpReDetailReportPage> rpReDetailReport(Page<RpReDetailReportPage> rePageDetail, @Param("entity") RpReDetailReportPage rpReDetailReportPage);

    IPage<RpSupplierUseReportPage> supplierUseReport(Page<RpSupplierUseReportPage> page, @Param("entity") RpSupplierUseReportPage rpSupplierUseReportPage);

    List<RpSupplierUseReportPage> supplierUseReport(@Param("entity") RpSupplierUseReportPage rpSupplierUseReportPage);

    IPage<RpDepartUseReportPage> departUseReport(Page<RpDepartUseReportPage> page, @Param("entity") RpDepartUseReportPage rpDepartUseReportPage);

    List<RpDepartUseReportPage> departUseReport(@Param("entity") RpDepartUseReportPage rpDepartUseReportPage);

    /**
     * 出入库统计报表查询——分页
     * add by jiangxz 2020年8月14日 09:59:53
     * @param page
     * @param entity
     * @return
     */
    IPage<RpInAndOutReportPage> rpInAndOutReport(Page<RpInAndOutReportPage> page, @Param("entity") RpInAndOutReportPage entity);
    /**
     * 出入库统计报表查询
     * add by jiangxz 2020年8月14日 09:59:53
     * @param entity
     * @return
     */
    List<RpInAndOutReportPage> rpInAndOutReport(@Param("entity") RpInAndOutReportPage entity);

    /**
     * 出入库统计报表明细查询——分页
     * add by jiangxz 2020年8月14日 09:59:53
     * @param page
     * @param entity
     * @return
     */
    IPage<RpInAndOutDetailReportPage> rpInAndOutDetailReport(Page<RpInAndOutDetailReportPage> page, @Param("entity") RpInAndOutDetailReportPage entity);

    /**
     * 出入库统计报表明细查询
     * add by jiangxz 2020年8月14日 09:59:53
     * @param entity
     * @return
     */
    List<RpInAndOutDetailReportPage> rpInAndOutDetailReport(@Param("entity") RpInAndOutDetailReportPage entity);

    IPage<RpDepartUseDetailReportPage> rpDepartUseDetailReport(Page<RpDepartUseDetailReportPage> usePageDetail, @Param("entity") RpDepartUseDetailReportPage rpDepartUseDetailReportPage);

    IPage<RpDepartStockReportPage> departStockReport(Page<RpDepartStockReportPage> page, @Param("entity") RpDepartStockReportPage rpDepartStockReportPage);

    List<RpDepartStockReportPage> departStockReport(@Param("entity") RpDepartStockReportPage rpDepartStockReportPage);

    IPage<RpDepartStockDetailReportPage> rpDepartStockDetailReport(Page<RpDepartStockDetailReportPage> stockPageDetail, @Param("entity") RpDepartStockDetailReportPage rpDepartStockDetailReportPage);

    IPage<RpSupplierUseReportPage> supplierReagentUseReport(Page<RpSupplierUseReportPage> page, @Param("entity") RpSupplierUseReportPage rpSupplierUseReportPage);

    IPage<RpReagentUseDetailReportPage> rpReagentUseDetailReport(Page<RpReagentUseDetailReportPage> usePageDetail, @Param("entity") RpReagentUseDetailReportPage rpReagentUseDetailReportPage);

    List<RpPurchaseUseReportPage> queryPurchaseCountView(@Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);

    List<RpPurchaseUseReportPage> queryConsumptionView(@Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);

    List<RpPurchaseUseReportPage> queryDepartContionView(@Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);

    List<RpPurchaseUseReportPage> queryDepartChargeView(@Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);

    List<RpPurchaseUseReportPage> queryDepartPurchaseCountView(@Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);

    List<RpPurchaseUseReportPage> queryPurchaseAmountMomTableView(@Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);

    List<RpPurchaseUseReportPage> queryItemMoneyCountView(@Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);

    IPage<RpPurchaseUseReportPage> queryMoOnMoView(Page<RpPurchaseUseReportPage> page, @Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);
    /*不分页*/
    List<RpPurchaseUseReportPage> queryMoOnMoView(@Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);

    List<RpPurchaseUseReportPage> getYearMonth(@Param("entity") RpPurchaseUseReportPage rpPurchaseUseReportPage);

    IPage<RpPurchaseMoneyReportPage> queryMonthMoneyList(Page<RpPurchaseMoneyReportPage> page, @Param("entity") RpPurchaseMoneyReportPage purchaseMoneyReportPage);

    List<RpPurchaseMoneyReportPage> queryMonthMoneyList(@Param("entity") RpPurchaseMoneyReportPage purchaseMoneyReportPage);

    IPage<RpDepartApplyPage> departApplyUseReportPage(Page<RpDepartApplyPage> page, @Param("entity") RpDepartApplyPage departApplyPage);

    List<RpDepartApplyPage> departApplyUseReportPage(@Param("entity") RpDepartApplyPage departApplyPage);

    List<RpPurchaseUseReportPage> queryConsolidatedDataView(@Param("entity")RpPurchaseUseReportPage purchaseUseReportPage);
}
