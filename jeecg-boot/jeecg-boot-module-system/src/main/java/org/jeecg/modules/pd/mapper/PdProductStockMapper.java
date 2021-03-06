package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 库存明细表
 * @Author: jeecg-boot
 * @Date: 2020-02-11
 * @Version: V1.0
 */
public interface PdProductStockMapper extends BaseMapper<PdProductStock> {

    Page<PdProductStock> queryListByPage(Page<PdProductStock> page, @Param("entity") PdProductStock entity);

    PdProductStock  queryStockCount(@Param("entity") PdProductStock entity);

    List<PdProductStock> selectList(PdProductStockTotalPage stockTotalPage);

    /*试剂查询专用*/
    List<PdProductStock> queryProductStockList(PdProductStock pdProductStock);


    public boolean deleteByMainId(@Param("mainId") String mainId);

    public List<PdProductStock> selectByMainId(@Param("mainId") String mainId);

    List<PdProductStock> selectList(PdProductStock pdProductStock);

    /**
     * 分页 用于库存选择器
     *
     * @param page
     * @param pdProductStock
     * @return
     */
    Page<PdProductStock> chooseProductStockList(Page<PdProductStock> page, @Param("entity") PdProductStock pdProductStock);

    /**
     * 不分页，用于套包自动选择库存
     *
     * @param pdProductStock
     * @return
     */
    List<PdProductStock> chooseProductStockList(@Param("entity") PdProductStock pdProductStock);

    PdProductStock getOne(PdProductStock pdProductStock);

    public void updateProductStock(PdProductStock productStock);

    /**
     * 库存明细锁表查询
     * 注意：带事务方法调用时使用
     *
     * @param pdProductStock
     */
    public List<PdProductStock> findForUpdate(PdProductStock pdProductStock);
    /**
     * 入库时调用
     * 库存明细锁表查询
     * 注意：带事务方法调用时使用
     *
     * @param pdProductStock
     */
    public List<PdProductStock> findForInsert(PdProductStock pdProductStock);

    /**
     * 更新库存明细库存数量
     *
     * @param pdProductStock
     */
    public void updateStockNum(PdProductStock pdProductStock);

    void updateHuoweiCode(PdProductStock detail);

    /**
     * 物流追溯产品列表
     *
     * @param pdProductStock
     * @return
     */
    Page<PdProductStock> getByOriginalProduct(Page<PdProductStock> page, @Param("entity") PdProductStock pdProductStock);

    /*获取总库存数量及当日入库存数量*/
    Map<String, Object> queryProductStockCount(PdProductStock pdProductStock);

    /*首页查询  根据范围统计每日入库的库存数量*/
    List<HashMap> queryStockDateList(PdProductStock pdProductStock);

    /*  首页查询  根据采购产品类区分统计采购金额*/
    List<HashMap> queryStockTotalList(PdProductStock pdProductStock);


    List<PdProductStock> queryList(PdProductStock pdProductStock);

    //加库存
    public void addStock(PdProductStock pdProductStock);

    /**
     * 更新库存明细规格数量
     *
     * @param pdProductStock
     */
    public void updateStockSpecNum(PdProductStock pdProductStock);

    /**
     * 修改条码类型
     *
     * @param pdProductStock
     */
    public void updateStockBarCodeType(PdProductStock pdProductStock);

    /**
     * 库存明细查询,根据有效期排序查询
     * 注意：带事务方法调用时使用
     *
     * @param pdProductStock
     */
    public List<PdProductStock> selectOrExpDate(PdProductStock pdProductStock);

    List<PdProductStock> queryUniqueProductStockList(PdProductStock pdProductStock);

    Page<PdProductStock> queryPrintList(Page<PdProductStock> page, @Param("entity") PdProductStock entity);

    List<Map<String, Object>> queryPdProductStockList(PdProductStock productStock);


}
