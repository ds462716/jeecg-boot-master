package org.jeecg.modules.pd.mapper;

import java.util.List;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 出入库明细表
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface PdStockRecordDetailMapper extends BaseMapper<PdStockRecordDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdStockRecordDetail> selectByMainId(PdStockRecordDetail pdStockRecordDetail);

	public List<PdStockRecordDetail> selectList(PdStockRecordDetail pdStockRecordDetail);

}
