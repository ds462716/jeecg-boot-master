package org.jeecg.modules.pd.vo;

import java.util.List;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import lombok.Data;
import org.jeecg.modules.pd.entity.PdApplyOrder;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

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
	/** 提交状态集合 **/
	private List<String> submitStartList;
	@ExcelCollection(name="申领单明细表")
	private List<PdApplyDetail> pdApplyDetailList;
	
}
