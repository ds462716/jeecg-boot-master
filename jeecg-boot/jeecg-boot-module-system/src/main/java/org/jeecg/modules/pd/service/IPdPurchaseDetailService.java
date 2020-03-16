package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;

import java.util.List;

/**
 * @Description: 申购单详细表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface IPdPurchaseDetailService extends IService<PdPurchaseDetail> {

	public List<PdPurchaseDetail> queryPdPurchaseDetail(PdPurchaseOrder purchaseOrder);
}
