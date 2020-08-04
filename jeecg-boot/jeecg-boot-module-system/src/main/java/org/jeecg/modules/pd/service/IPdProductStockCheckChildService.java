package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pd.entity.PdProductStockCheckChild;

import java.util.List;

/**
 * @Description: 盘点明细表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
public interface IPdProductStockCheckChildService extends IService<PdProductStockCheckChild> {

	public List<PdProductStockCheckChild> selectByMainId(String mainId);

	public List<PdProductStockCheckChild> selectByCheckNo(String checkNo);

	Page<PdProductStockCheckChild> pdProductStockCheckQueryList(Page<PdProductStockCheckChild> page, PdProductStockCheckChild child);

	List<PdProductStockCheckChild> pdProductStockCheckQueryList(PdProductStockCheckChild child);
}
