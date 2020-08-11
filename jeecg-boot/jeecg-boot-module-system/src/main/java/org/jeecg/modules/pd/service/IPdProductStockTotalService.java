package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.external.entity.ExInspectionItems;
import org.jeecg.modules.pd.entity.*;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;

import java.util.List;
import java.util.Map;

/**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IPdProductStockTotalService extends IService<PdProductStockTotal> {


    /**
     * 分页查询
     * @param pageList
     * @param stockTotal
     * @return
     */
    Page<PdProductStockTotalPage> selectList(Page<PdProductStockTotalPage> pageList, PdProductStockTotal stockTotal);

    /**
     * 查询不分页
     */
    List<PdProductStockTotalPage> findListForQuery(PdProductStockTotal stockTotal);
	/**
	 * 修改库存总表上下限数量
	 */
	public void updateProductStockTotal(PdProductStockTotal stockTotal) ;

    /**
     * 耗材入库更新库存信息
     * @param pdStockRecord
     * @return
     */
    public String updateInStock(PdStockRecord pdStockRecord);

    /**
     * 库存出库更新库存信息
     * @param pdStockRecord
     * @return
     */
    public String updateOutStock(PdStockRecord pdStockRecord);

    /**
     * 套包拆包加库存
     * @param pdPackageRecord
     * @return
     */
    public String updateInStockForPackage(PdPackageRecord pdPackageRecord);

    /**
     * 套包打包扣库存
     * @param pdPackageRecord
     * @return
     */
    public String updateOutStockForPackage(PdPackageRecord pdPackageRecord);

    /**
     * 院外退货更新库存信息
     * @param stockTotal
     * @return
     * */
    public Map<String,String> updateStockNumByProdIdAndDeptId(PdProductStockTotal stockTotal);


    /**
     * 获取待盘点产品总数量
     * @param stockTotal
     * @return
     */
    public Double queryCheckTotalNum(PdProductStockTotal stockTotal);

    String updateUseStock(String currentDepartId, List<PdDosageDetail> afterDealList);

    /**
     * 退货库存处理
     * @param pdRejected
     * @return
     */
    String updateRejectedStock(PdRejected pdRejected);

    /**
     * 库存移库更新库存信息
     * @param productStock
     * @return
     * */
    public String updateStockHuowei(PdProductStock productStock);

    /**
     * 用量退回更新库存
     * @param departId
     * @param afterDealList
     * @return
     */
    String updateRetunuseStock(String departId, List<PdDosageDetail> afterDealList);


    /**
     * 库存规格数量清零操作
     * @param productStock
     * @return
     * */
    public String updateStockSpecNum(PdProductStock productStock);

    /**
     *试剂耗材产品更新库存用量信息(重新扣减及批量扣减调用)
     * @param detailList
     * @return
     */
    public Map<String,Object> lisUpdateUseStock(ExInspectionItems items,String testDpeartId, List<PdUsePackageDetail> detailList);

    /**
     *试剂耗材产品更新库存用量信息(根据LIS系统检验项目数据)
     * @param detailList
     * @return
     */
    public Map<String,Object> lisUpdateUseStockLis(ExInspectionItems items,String testDpeartId, List<PdUsePackageDetail> detailList);

    /**
     * 检验项目手动扣减库存方法(检验包)
     * @param departId
     * @param usePackageDetailList
     * @return
     */
    public List<PdProductStock> jyUpdatePackageStockNum(String departId,List<PdUsePackageDetail> usePackageDetailList);


    /**
     * 查询库存总表（设置了库存下限且自动补货数量大于0的数据）
     */
    List<PdProductStockTotalPage> findListForAutoNum(PdProductStockTotal stockTotal);

    String jyUpdateProductStockNum(String departId, List<PdProductStock> exInspectionItemsUseDetails);


    public PdProductStock insertProdStock(PdProductStock productStock);

    public PdProductStock closeProdStock(PdProductStock productStock);

    /**
     * 盘点库存处理
     * @param pdProductStockCheck
     * @return
     */
    String updateCheckStock(PdProductStockCheck pdProductStockCheck);

    List<PdProductStockTotalPage> chooseStockTotalList(PdProductStockTotalPage stockTotal);
}
