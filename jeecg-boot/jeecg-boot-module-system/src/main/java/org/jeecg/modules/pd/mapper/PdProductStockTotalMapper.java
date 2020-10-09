package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;

import java.util.List;

/**
 * @Description: 库存总表
 * @Author: mcb
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface PdProductStockTotalMapper extends BaseMapper<PdProductStockTotal> {

    Page<PdProductStockTotalPage> selectListByPage(Page<PdProductStockTotalPage> page, @Param("entity") PdProductStockTotal entity);

    List<PdProductStockTotalPage> selectList(PdProductStockTotal stockTotal);

    public void updateProductStockTotal(PdProductStockTotal stockTotal);

    /**
     * 库存总表加锁查询，有事务时使用
     * @param  stockTotal
     * @return List<PdProductStockTotal>
     */
    public List<PdProductStockTotal> findForUpdate(PdProductStockTotal stockTotal);

    /**
     * 更新库存总表库存数量
     * @param stockTotal
     */
    public void updateStockNum(PdProductStockTotal stockTotal);

    public void updateForDosagert(PdProductStockTotal stockTotal);

    public Double queryCheckTotalNum(PdProductStockTotal stockTotal);

    //加库存
    public void addStock(PdProductStockTotal pdProductStockTotal);

    /**
     * 查询库存总表（设置了库存下限且自动补货数量大于0的数据）
     */
    List<PdProductStockTotalPage> findListForAutoNum(PdProductStockTotal stockTotal);

    List<PdProductStockTotalPage> chooseStockTotalList(PdProductStockTotalPage stockTotal);

}
