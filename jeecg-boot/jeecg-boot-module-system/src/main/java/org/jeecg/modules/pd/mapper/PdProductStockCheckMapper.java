package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProductStockCheck;

import java.util.List;

/**
 * @Description: 盘点记录表
 * @Author: mcb
 * @Date:   2020-02-28
 * @Version: V1.0
 */
public interface PdProductStockCheckMapper extends BaseMapper<PdProductStockCheck> {


    List<PdProductStockCheck> selectList(PdProductStockCheck stockCheck);

    Page<PdProductStockCheck> selectListByPage(Page<PdProductStockCheck> page, @Param("entity") PdProductStockCheck entity);

    void deleteByMainId(String id);

    PdProductStockCheck getByOne(PdProductStockCheck pdProductStockCheck);
}
