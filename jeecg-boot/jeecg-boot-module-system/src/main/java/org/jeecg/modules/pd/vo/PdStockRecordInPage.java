package org.jeecg.modules.pd.vo;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import org.jeecg.modules.pd.entity.PdStockRecord;
import org.jeecg.modules.pd.entity.PdStockRecordDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 出入库记录表 用于导出
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Data
public class PdStockRecordInPage {

	/**主键*/
	private String id;
	/**出入库单号*/
	@Excel(name = "入库单号", width = 15)
	private String recordNo;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Excel(name = "入库日期", width = 15, format = "yyyy-MM-dd")
	private Date auditDate;
	@Excel(name = "入库科室", width = 15)
	private String inDepartName;//入库科室名称
	@Excel(name = "产品编号", width = 15)
	private String number;//产品编号
	@Excel(name = "产品名称", width = 15)
	private String productNumber;//产品编号
	@Excel(name = "产品条码", width = 15)
	private String productBarCode;
	@Excel(name = "规格", width = 15)
	private String spec;//产品规格
	@Excel(name = "型号", width = 15)
	private String version;//产品型号
	@Excel(name = "批号", width = 15)
	private String batchNo;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
	private Date produceDate;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	private Date expDate;
	@Excel(name = "入库数量", width = 15)
	private Double productNum;
	@Excel(name = "单位", width = 15)
	private String unitName;//单位名称
	@Excel(name = "入库单价", width = 15)
	private BigDecimal purchasePrice;
	@Excel(name = "入库金额", width = 15)
	private BigDecimal inTotalPrice;
	@Excel(name = "生产厂家", width = 15)
	private String venderName;//生产厂家名称
	@Excel(name = "供应商", width = 15)
	private String supplierName;//供应商名称
	@Excel(name = "注册证号", width = 15)
	private String registration;//注册证号
	@Excel(name = "备注", width = 15)
	private String remarks;
	@Excel(name = "入库类型", width = 15,dicCode = "in_type")
	private String inType;
	@Excel(name = "JDE编号", width = 15)
	private String jdeCode;
	@Excel(name = "操作人", width = 15)
	private String realname;
}
