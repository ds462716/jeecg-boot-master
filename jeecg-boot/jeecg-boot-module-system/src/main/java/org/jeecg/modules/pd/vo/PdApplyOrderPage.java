package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.List;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
public class PdApplyOrderPage extends PdApplyOrder {
	
	/**操作人名称 */
	private String realName;

	/**审核人名称 */
	private String auditByName;
	/**产品名称，用于查询条件*/
	private String productName;
	/**产品编号，用于查询条件*/
	private String number;
	/**科室名称 */
	private String deptName;
	/** 提交状态集合 **/
	private List<String> submitStatusList;
	/** 审核状态集合 **/
	private List<String> auditStatusList;
	/** 查询日期范围 **/
	private List<Object> queryDate;

	/** 查询日期起始 **/
	private String queryDateStart;

	/** 查询日期结束 **/
	private String queryDateEnd;
	@ExcelCollection(name="申领单明细表")
	private List<PdApplyDetail> pdApplyDetailList;
	
}
