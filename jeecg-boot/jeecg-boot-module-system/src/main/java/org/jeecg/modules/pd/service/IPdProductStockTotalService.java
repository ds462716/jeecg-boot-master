package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import org.springframework.transaction.annotation.Transactional;

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
     * @param inDeptId
     * @param supplierId
     * @param stockRecordDetails
     * @return
     */
    public String updateInStock(String inDeptId, String supplierId, List<PdStockRecordDetail> stockRecordDetails);

    /**
     * 库存出库更新库存信息
     * @param outDeptId
     * @param inDeptId
     * @param stockRecordDetails
     * @return
     */
    public Map updateOutStock(String outDeptId, String inDeptId, List<PdStockRecordDetail> stockRecordDetails);

    /**
     * 院外退货更新库存信息
     * @param stockTotal
     * @return
     * */
    public Map<String,String> updateStockNumByProdIdAndDeptId(PdProductStockTotal stockTotal);

    }
