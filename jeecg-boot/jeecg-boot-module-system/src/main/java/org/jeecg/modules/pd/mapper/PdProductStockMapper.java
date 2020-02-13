package org.jeecg.modules.pd.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProductStock;

/**
 * @Description: 库存明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface PdProductStockMapper extends BaseMapper<PdProductStock> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdProductStock> selectByMainId(@Param("mainId") String mainId);
}
