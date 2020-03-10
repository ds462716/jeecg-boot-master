package org.jeecg.modules.pd.mapper;

import java.util.List;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 申领单明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface PdApplyDetailMapper extends BaseMapper<PdApplyDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public boolean deleteByApplyNo(@Param("applyNo") String applyNo);

	public List<PdApplyDetail> selectByApplyNo(@Param("applyNo") String applyNo);

	/**
	 * 增加发货数量 用于出库保存/提交 add by jiangxz 20200309
	 *
	 * @param detail
	 * @return
	 */
	void additionArrivalNum(PdApplyDetail detail);

	/**
	 * 扣减发货数量 用于出库拒绝 add by jiangxz 20200309
	 *
	 * @param detail
	 * @return
	 */
	void subtractArrivalNum(PdApplyDetail detail);
}
