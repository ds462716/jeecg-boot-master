package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdPurchaseOrderMergeDetail;

import java.util.List;

/**
 * @Description: 合并申购明细表
 * @Author: mcb
 * @Date:   2020-03-20
 * @Version: V1.0
 */
public interface PdPurchaseOrderMergeDetailMapper extends BaseMapper<PdPurchaseOrderMergeDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdPurchaseOrderMergeDetail> selectByMainId(@Param("mainId") String mainId);

	public List<PdPurchaseOrderMergeDetail> queryPdPurchaseMergeDetail(PdPurchaseOrderMergeDetail orderMergeDetail);

}
