package org.jeecg.modules.pd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 出入库记录表(市立医院供应室) 用于导出
 * @Author: mcb
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Data
public class PdStockRecordOutGYSPage {

	/**主键*/
	private String id;
	/**出入库单号*/
	@Excel(name = "出库单号", width = 15)
	private String recordNo;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Excel(name = "出库日期", width = 15, format = "yyyy-MM-dd")
	private Date auditDate;
	@Excel(name = "出库类型", width = 15,dicCode = "out_type")
	private String outType;
	@Excel(name = "入库科室", width = 15)
	private String inDepartName;//入库科室名称
	@Excel(name = "产品编号", width = 15)
	private String number;//产品编号
	@Excel(name = "产品名称", width = 15)
	private String productName;//产品编号
	@Excel(name = "规格", width = 15)
	private String spec;//产品规格
	@Excel(name = "批号", width = 15)
	private String batchNo;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	private Date expDate;
	@Excel(name = "出库数量", width = 15)
	private Double productNum;
	@Excel(name = "单位", width = 15)
	private String unitName;//单位名称
	@Excel(name = "入库单价", width = 15)
	private BigDecimal purchasePrice;
	@Excel(name = "出库单价", width = 15)
	private BigDecimal sellingPrice;
	@Excel(name = "操作人", width = 15)
	private String realname;
}
