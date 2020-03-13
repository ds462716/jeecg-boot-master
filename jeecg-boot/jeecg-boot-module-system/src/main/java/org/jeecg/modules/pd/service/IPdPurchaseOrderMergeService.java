package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMerge;

/**
 * @Description: 合并申购单表
 * @Author: mcb
 * @Date:   2020-03-13
 * @Version: V1.0
 */
public interface IPdPurchaseOrderMergeService extends IService<PdPurchaseOrderMerge> {

    /**
     * 分页查询
     * @param pageList
     * @param purchaseOrderMerge
     * @return
     */
    Page<PdPurchaseOrderMerge> selectList(Page<PdPurchaseOrderMerge> pageList, PdPurchaseOrderMerge purchaseOrderMerge);


    public void saveMergeOrder(String orderNos, LoginUser sysUser);


   public void submitOrderInfo(PdPurchaseOrderMerge orderMerge,LoginUser sysUser);

}
