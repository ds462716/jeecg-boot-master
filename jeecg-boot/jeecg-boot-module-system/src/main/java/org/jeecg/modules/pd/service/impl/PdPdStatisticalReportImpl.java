package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.mapper.PdStatisticalReportMapper;
import org.jeecg.modules.pd.service.IPdStatisticalReportService;
import org.jeecg.modules.pd.vo.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 空实现
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
@Service
public class PdPdStatisticalReportImpl extends ServiceImpl<PdStatisticalReportMapper, PdStatisticalReport> implements IPdStatisticalReportService {

    /**
     *供应商用量使用统计
     * @param page
     * @param rpSupplierUseReportPage
     * @return
     */
    @Override
    public IPage<RpSupplierUseReportPage> supplierUseReport(Page<RpSupplierUseReportPage> page, RpSupplierUseReportPage rpSupplierUseReportPage) {
        return baseMapper.supplierUseReport(page,rpSupplierUseReportPage);
    }
    /**
     *供应商用量使用统计
     * @param rpSupplierUseReportPage
     * @return
     */
    @Override
    public List<RpSupplierUseReportPage> supplierUseReport(RpSupplierUseReportPage rpSupplierUseReportPage) {
        return baseMapper.supplierUseReport(rpSupplierUseReportPage);
    }

    /**
     * zxh供应商用量统计查询入库明细
     * @param inPageDetail
     * @param inDetail
     * @return
     */
    @Override
    public IPage<PdStockRecordDetail> supplierInDetailReport(Page<PdStockRecordDetail> inPageDetail, PdStockRecordDetail inDetail) {
        return baseMapper.supplierInDetailReport(inPageDetail,inDetail);
    }

    /**
     * zxh用量明细统计报表
     * @param usePageDetail
     * @param rpUseDetailReportPage
     * @return
     */
    @Override
    public IPage<RpUseDetailReportPage> rpUseDetailReport(Page<RpUseDetailReportPage> usePageDetail, RpUseDetailReportPage rpUseDetailReportPage) {
        return baseMapper.rpUseDetailReport(usePageDetail,rpUseDetailReportPage);
    }
    /**
     * zxh退货明细统计报表
     * @param rePageDetail
     * @param rpReDetailReportPage
     * @return
     */
    @Override
    public IPage<RpReDetailReportPage> rpReDetailReport(Page<RpReDetailReportPage> rePageDetail, RpReDetailReportPage rpReDetailReportPage) {
        return baseMapper.rpReDetailReport(rePageDetail,rpReDetailReportPage);
    }

    /**
     * 出入库统计报表查询——分页
     * add by jiangxz 2020年8月14日 09:59:53
     * @param page
     * @param entity
     * @return
     */
    @Override
    public IPage<RpInAndOutReportPage> rpInAndOutReport(Page<RpInAndOutReportPage> page, RpInAndOutReportPage entity) {
        return baseMapper.rpInAndOutReport(page,entity);
    }

    /**
     * 出入库统计报表查询
     * add by jiangxz 2020年8月14日 09:59:53
     * @param entity
     * @return
     */
    @Override
    public List<RpInAndOutReportPage> rpInAndOutReport(RpInAndOutReportPage entity) {
        return baseMapper.rpInAndOutReport(entity);
    }

    /**
     * 出入库统计报表明细查询——分页
     * add by jiangxz 2020年8月14日 09:59:53
     * @param page
     * @param entity
     * @return
     */
    @Override
    public IPage<RpInAndOutDetailReportPage> rpInAndOutDetailReport(Page<RpInAndOutDetailReportPage> page, RpInAndOutDetailReportPage entity) {
        return baseMapper.rpInAndOutDetailReport(page,entity);
    }

    /**
     * 出入库统计报表明细查询
     * add by jiangxz 2020年8月14日 09:59:53
     * @param entity
     * @return
     */
    @Override
    public List<RpInAndOutDetailReportPage> rpInAndOutDetailReport(RpInAndOutDetailReportPage entity) {
        return baseMapper.rpInAndOutDetailReport(entity);
    }

}
