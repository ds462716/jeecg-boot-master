package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdPurchaseTempDetail;

import java.util.List;

/**
 * @Description: 申购模板详细表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
public interface PdPurchaseTempDetailMapper extends BaseMapper<PdPurchaseTempDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public boolean deleteByTempNo(@Param("tempNo") String tempNo);

	public List<PdPurchaseTempDetail> queryPdPurchaseTempDetail(PdPurchaseTempDetail purchaseTempDetail);



}
