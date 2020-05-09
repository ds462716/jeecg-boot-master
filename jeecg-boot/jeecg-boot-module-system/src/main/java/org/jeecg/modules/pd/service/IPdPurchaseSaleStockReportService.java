package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.vo.PdPurchaseSaleStockReportPage;

/**
 * @author jiangxz
 * @description 进销存报表
 * @date 2020-5-9
 */
public interface IPdPurchaseSaleStockReportService extends IService<PdPurchaseSaleStockReportPage> {

    /**
     * 分页查询
     * @param pageList
     * @param entity
     * @return
     */
    IPage<PdPurchaseSaleStockReportPage> selectList(Page<PdPurchaseSaleStockReportPage> pageList, PdPurchaseSaleStockReportPage vo);

}
