package org.jeecg.modules.pd.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecg.modules.pd.entity.PdProductStockTotal;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.List;

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
	/**申购科室名称*/
	@Excel(name = "申购科室名称", width = 15)
	private String deptName;
	@ExcelCollection(name="库存明细表")
	private List<PdProductStock> pdProductStockList;

    /*区分是申领单还是调拨单产品选择器**/
	@TableField(exist = false)
	private String code;  //1:调拨单   2:申领单
	
}
