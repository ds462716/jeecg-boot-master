package org.jeecg.modules.pd.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecg.modules.pd.vo.PdProductStockTotalPage;

/**
 * @Description: 库存明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface PdProductStockMapper extends BaseMapper<PdProductStock> {


	List<PdProductStock> selectList(PdProductStockTotalPage stockTotalPage);

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PdProductStock> selectByMainId(@Param("mainId") String mainId);

    List<PdProductStock> selectList(PdProductStock pdProductStock);
}
