package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
