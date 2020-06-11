package org.jeecg.modules.pd.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockUniqueCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 库存关联条码表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
public interface PdProductStockUniqueCodeMapper extends BaseMapper<PdProductStockUniqueCode> {

    Page<PdProductStockUniqueCode> selectListByPage(Page<PdProductStockUniqueCode> page, @Param("entity") PdProductStockUniqueCode entity);

    List<PdProductStockUniqueCode> selectListOne(PdProductStockUniqueCode pdProductStockUniqueCode);

    List<PdProductStock> selectListByGroup(PdProductStockUniqueCode pdProductStockUniqueCode);

    void updatePrintNum(List<String> ids);
}
