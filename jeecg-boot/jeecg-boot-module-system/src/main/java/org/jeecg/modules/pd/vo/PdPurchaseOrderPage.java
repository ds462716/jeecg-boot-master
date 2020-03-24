package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.List;

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

	/**审核人姓名*/
	private String auditByName;

	/**采购订单id*/
	private String purchaseId;

	/**供应商ID，用于查询条件*/
	private String supplierId;

	/**产品编号，用于查询条件*/
	private String productNumber;

	/**产品名称，用于查询条件*/
	private String productName;

	/** 查询日期范围 **/
	private List<Object> queryDate;

	/** 查询日期起始 **/
	private String queryDateStart;

	/** 查询日期结束 **/
	private String queryDateEnd;
	/**申购科室名称*/
	@Excel(name = "申购科室名称", width = 15)
	private String deptName;
	/** 提交状态集合 **/
	private List<String> submitStatusList;
	/** 提交状态集合 **/
	private List<String> auditStatusList;

	@ExcelCollection(name="申购单详细表")
	private List<PdPurchaseDetail> pdPurchaseDetailList;	
}
