package org.jeecg.modules.pd.vo;

import java.util.List;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

/**
 * @Description: 申购订单主表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Data
public class PdPurchaseOrderPage extends PdPurchaseOrder{

	@Excel(name = "申购人名称", width = 15)
	private String purchaseName;

	@ExcelCollection(name="申购单详细表")
	private List<PdPurchaseDetail> pdPurchaseDetailList;	
}
