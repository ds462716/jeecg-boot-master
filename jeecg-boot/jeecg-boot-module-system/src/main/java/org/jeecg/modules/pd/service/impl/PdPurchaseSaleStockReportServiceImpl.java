package org.jeecg.modules.pd.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pd.mapper.PdPurchaseSaleStockReportMapper;
import org.jeecg.modules.pd.service.IPdPurchaseSaleStockReportService;
import org.jeecg.modules.pd.vo.PdPurchaseSaleStockReportPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangxz
 * @description 进销存报表
 * @date 2020-5-9
 */
@Service
public class PdPurchaseSaleStockReportServiceImpl extends ServiceImpl<PdPurchaseSaleStockReportMapper, PdPurchaseSaleStockReportPage> implements IPdPurchaseSaleStockReportService {

    @Autowired
    private PdPurchaseSaleStockReportMapper pdPurchaseSaleStockReportMapper;

    @Override
    public IPage<PdPurchaseSaleStockReportPage> selectList(Page<PdPurchaseSaleStockReportPage> pageList, PdPurchaseSaleStockReportPage vo) {
        return pdPurchaseSaleStockReportMapper.selectListByPage(pageList,vo);
    }
}
