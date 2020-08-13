package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdStatisticalReport;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.vo.RpReDetailReportPage;
import org.jeecg.modules.pd.vo.RpUseDetailReportPage;

/**
 * @Description: 报表统计
 * @Author: zxh
 * @Date:   2020-01-14
 * @Version: V1.0
 */
public interface PdStatisticalReportMapper extends BaseMapper<PdStatisticalReport> {

    IPage<PdStockRecordDetail> rpInDetailReport(Page<PdStockRecordDetail> page, @Param("entity")PdStockRecordDetail inDetail);

    IPage<RpUseDetailReportPage> rpUseDetailReport(Page<RpUseDetailReportPage> usePageDetail, @Param("entity") RpUseDetailReportPage rpUseDetailReportPage);

    IPage<RpReDetailReportPage> rpReDetailReport(Page<RpReDetailReportPage> rePageDetail, @Param("entity")RpReDetailReportPage rpReDetailReportPage);
}
