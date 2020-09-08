package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.vo.RpDepartUseReportPage;
import org.jeecg.modules.pd.vo.RpReDetailReportPage;
import org.jeecg.modules.pd.vo.RpSupplierUseReportPage;
import org.jeecg.modules.pd.vo.RpUseDetailReportPage;
import org.jeecg.modules.pd.vo.*;

import java.util.List;

/**
 * @Description: 空接口
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
public interface IPdStatisticalReportService extends IService<PdStatisticalReport> {

    IPage<RpSupplierUseReportPage> supplierUseReport(Page<RpSupplierUseReportPage> page, RpSupplierUseReportPage rpSupplierUseReportPage);

    List<RpSupplierUseReportPage> supplierUseReport(RpSupplierUseReportPage rpSupplierUseReportPage);

    IPage<PdStockRecordDetail> supplierInDetailReport(Page<PdStockRecordDetail> inPageDetail, PdStockRecordDetail inDetail);

    IPage<RpUseDetailReportPage> rpUseDetailReport(Page<RpUseDetailReportPage> usePageDetail, RpUseDetailReportPage rpUseDetailReportPage);

    IPage<RpReDetailReportPage> rpReDetailReport(Page<RpReDetailReportPage> rePageDetail, RpReDetailReportPage rpReDetailReportPage);

    /**
     * 出入库统计报表查询——分页
     * add by jiangxz 2020年8月14日 09:59:53
     * @param page
     * @param entity
     * @return
     */
    IPage<RpInAndOutReportPage> rpInAndOutReport(Page<RpInAndOutReportPage> page, RpInAndOutReportPage entity);

    /**
     * 出入库统计报表查询
     * add by jiangxz 2020年8月14日 09:59:53
     * @param entity
     * @return
     */
    List<RpInAndOutReportPage> rpInAndOutReport(RpInAndOutReportPage entity);

    /**
     * 出入库统计报表明细查询——分页
     * add by jiangxz 2020年8月14日 09:59:53
     * @param page
     * @param entity
     * @return
     */
    IPage<RpInAndOutDetailReportPage> rpInAndOutDetailReport(Page<RpInAndOutDetailReportPage> page, RpInAndOutDetailReportPage entity);

    /**
     * 出入库统计报表明细查询
     * add by jiangxz 2020年8月14日 09:59:53
     * @param entity
     * @return
     */
    List<RpInAndOutDetailReportPage> rpInAndOutDetailReport(RpInAndOutDetailReportPage entity);

    IPage<RpDepartUseReportPage> departUseReport(Page<RpDepartUseReportPage> page, RpDepartUseReportPage rpDepartUseReportPage);

    List<RpDepartUseReportPage> departUseReport(RpDepartUseReportPage rpDepartUseReportPage);

    IPage<RpDepartUseDetailReportPage> rpDepartUseDetailReport(Page<RpDepartUseDetailReportPage> usePageDetail, RpDepartUseDetailReportPage rpDepartUseDetailReportPage);

    IPage<RpDepartStockReportPage> departStockReport(Page<RpDepartStockReportPage> page, RpDepartStockReportPage rpDepartStockReportPage);

    List<RpDepartStockReportPage> departStockReport(RpDepartStockReportPage rpDepartStockReportPage);

    IPage<RpDepartStockDetailReportPage> rpDepartStockDetailReport(Page<RpDepartStockDetailReportPage> stockPageDetail, RpDepartStockDetailReportPage rpDepartStockDetailReportPage);

    IPage<RpSupplierUseReportPage> supplierReagentUseReport(Page<RpSupplierUseReportPage> page, RpSupplierUseReportPage rpSupplierUseReportPage);

    IPage<RpReagentUseDetailReportPage> rpReagentUseDetailReport(Page<RpReagentUseDetailReportPage> usePageDetail, RpReagentUseDetailReportPage rpReagentUseDetailReportPage);

    List<RpPurchaseUseReportPage> queryPurchaseCountView(RpPurchaseUseReportPage entity);

}
