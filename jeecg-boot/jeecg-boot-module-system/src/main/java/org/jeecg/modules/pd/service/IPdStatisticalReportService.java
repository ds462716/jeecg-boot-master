package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.vo.RpReDetailReportPage;
import org.jeecg.modules.pd.vo.RpUseDetailReportPage;

/**
 * @Description: 空接口
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
public interface IPdStatisticalReportService extends IService<PdStatisticalReport> {

    IPage<PdStockRecordDetail> rpInDetailReport(Page<PdStockRecordDetail> inPageDetail, PdStockRecordDetail inDetail);

    IPage<RpUseDetailReportPage> rpUseDetailReport(Page<RpUseDetailReportPage> usePageDetail, RpUseDetailReportPage rpUseDetailReportPage);

    IPage<RpReDetailReportPage> rpReDetailReport(Page<RpReDetailReportPage> rePageDetail, RpReDetailReportPage rpReDetailReportPage);
}
