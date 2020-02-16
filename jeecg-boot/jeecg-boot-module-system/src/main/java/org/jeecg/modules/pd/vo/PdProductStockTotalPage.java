package org.jeecg.modules.pd.vo;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

/**
 * @Description: 库存总表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
public class PdProductStockTotalPage extends PdProductStockTotal {

	/**主键集合*/
	@TableField(exist = false)
	private String ids;
	/**过期数量*/
	@TableField(exist = false)
	private Double gCount;
	/**近效期数量*/
	@TableField(exist = false)
	private Double jCount;
	/**久存产品数量*/
	@TableField(exist = false)
	private Double isLcount;
	/**总数量*/
	@TableField(exist = false)
	private Double pCount;
	/**超出库房上下限产品数量*/
	@TableField(exist = false)
	private Double limtCount;
	@ExcelCollection(name="库存明细表")
	private List<PdProductStock> pdProductStockList;
	
}
