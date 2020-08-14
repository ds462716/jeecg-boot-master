package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.vo.*;

import java.util.List;

/**
 * @Description: 报表统计
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
public interface PdStatisticalReportMapper extends BaseMapper<PdStatisticalReport> {

    IPage<PdStockRecordDetail> supplierInDetailReport(Page<PdStockRecordDetail> page, @Param("entity")PdStockRecordDetail inDetail);

    IPage<RpUseDetailReportPage> rpUseDetailReport(Page<RpUseDetailReportPage> usePageDetail, @Param("entity") RpUseDetailReportPage rpUseDetailReportPage);

    IPage<RpReDetailReportPage> rpReDetailReport(Page<RpReDetailReportPage> rePageDetail, @Param("entity")RpReDetailReportPage rpReDetailReportPage);

    IPage<RpSupplierUseReportPage> supplierUseReport(Page<RpSupplierUseReportPage> page, @Param("entity")RpSupplierUseReportPage rpSupplierUseReportPage);

    List<RpSupplierUseReportPage> supplierUseReport(@Param("entity")RpSupplierUseReportPage rpSupplierUseReportPage);

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

}
