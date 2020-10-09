package org.jeecg.modules.pd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProductStockCheckChild;

import java.util.List;

/**
 * @Description: 盘点明细表
 * @Author: mcb
 * @Date:   2020-02-28
 * @Version: V1.0
 */
public interface PdProductStockCheckChildMapper extends BaseMapper<PdProductStockCheckChild> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public boolean deleteByCheckNo(@Param("checkNo") String checkNo);

	public List<PdProductStockCheckChild> selectByMainId(@Param("mainId") String mainId);

	public List<PdProductStockCheckChild> selectByCheckNo(@Param("checkNo") String checkNo);

	Page<PdProductStockCheckChild> pdProductStockCheckQueryList(Page<PdProductStockCheckChild> page, @Param("entity") PdProductStockCheckChild child);

	List<PdProductStockCheckChild> pdProductStockCheckQueryList(@Param("entity") PdProductStockCheckChild child);
}
