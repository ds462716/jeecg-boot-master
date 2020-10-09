package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.vo.PdPurchaseSaleStockReportPage;

import java.util.List;

/**
 * @author jiangxz
 * @description 进销存报表
 * @date 2020-5-9
 */
public interface PdPurchaseSaleStockReportMapper extends BaseMapper<PdPurchaseSaleStockReportPage> {

    /**
     * 分页查询
     * @param page
     * @param vo
     * @return
     */
    Page<PdPurchaseSaleStockReportPage> selectList(Page<PdPurchaseSaleStockReportPage> page, @Param("entity") PdPurchaseSaleStockReportPage vo);

    List<PdPurchaseSaleStockReportPage> selectList(@Param("entity") PdPurchaseSaleStockReportPage vo);

}

