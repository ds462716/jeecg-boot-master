package org.jeecg.modules.pd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.pd.entity.PdProductStock;

import java.util.List;

/**
 * @Description: 库存明细表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface IPdProductStockService extends IService<PdProductStock> {

	public List<PdProductStock> selectByMainId(String mainId);
}
