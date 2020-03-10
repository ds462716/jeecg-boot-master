package org.jeecg.modules.pd.mapper;

import java.util.List;
import org.jeecg.modules.pd.entity.PdAllocationDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 调拨明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface PdAllocationDetailMapper extends BaseMapper<PdAllocationDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    

	public List<PdAllocationDetail> selectByAllocationNo(@Param("allocationNo") String allocationNo);    /**
	 * 增加发货数量 用于出库保存/提交 add by jiangxz 20200309
	 *
	 * @param detail
	 * @return
	 */
	void additionArrivalNum(PdAllocationDetail detail);

	/**
	 * 扣减发货数量 用于出库拒绝 add by jiangxz 20200309
	 *
	 * @param detail
	 * @return
	 */
	void subtractArrivalNum(PdAllocationDetail detail);
}
