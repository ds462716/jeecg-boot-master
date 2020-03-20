package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;

import java.util.List;

/**
 * @Description: 申购单详细表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface PdPurchaseDetailMapper extends BaseMapper<PdPurchaseDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public boolean deleteByOrderNo(@Param("orderNo") String orderNo);

	public List<PdPurchaseDetail> queryPdPurchaseDetail(PdPurchaseDetail purchaseDetail);

	/**
	 * 增加到货数量 用于入库保存/提交 add by jiangxz 20200227
	 * @param detail
	 * @return
	 */
	public void additionArrivalNum(PdPurchaseDetail detail);

	/**
	 * 扣减到货数量 用于入库拒绝 add by jiangxz 20200227
	 * @param detail
	 * @return
	 */
	public void subtractArrivalNum(PdPurchaseDetail detail);
}
