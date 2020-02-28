package org.jeecg.modules.pd.mapper;

import java.util.List;
import org.jeecg.modules.pd.entity.PdProductStockCheckChild;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 盘点明细表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
public interface PdProductStockCheckChildMapper extends BaseMapper<PdProductStockCheckChild> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdProductStockCheckChild> selectByMainId(@Param("mainId") String mainId);
}
