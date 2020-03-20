package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMergeDetail;

import java.util.List;

/**
 * @Description: 合并申购明细表
 * @Author: mcb
 * @Date:   2020-03-20
 * @Version: V1.0
 */
public interface IPdPurchaseOrderMergeDetailService extends IService<PdPurchaseOrderMergeDetail> {

	public List<PdPurchaseOrderMergeDetail> selectByMainId(String mainId);

	public List<PdPurchaseOrderMergeDetail> queryPdPurchaseMergeDetail(PdPurchaseOrderMergeDetail orderMergeDetail);

}
