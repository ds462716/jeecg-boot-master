package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdProductStockCheckChild;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 盘点明细表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
public interface IPdProductStockCheckChildService extends IService<PdProductStockCheckChild> {

	public List<PdProductStockCheckChild> selectByMainId(String mainId);
}
