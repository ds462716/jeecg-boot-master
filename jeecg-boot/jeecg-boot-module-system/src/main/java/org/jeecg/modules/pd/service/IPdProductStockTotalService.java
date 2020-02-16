package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;
import java.util.List;

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
	public void updateProductStock(PdProductStockTotal stockTotal) ;
}
