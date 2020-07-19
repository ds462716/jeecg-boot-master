package org.jeecg.modules.pd.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * @Description: 应商入库用量明细 用于导出
 * @Author: jiangxz
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Data
public class PdSupplierRecordExcel {

	/**主键*/
	private String id;
	@Excel(name = "供应商", width = 15)
	private String supplierName;//供应商
	@Excel(name = "产品名称", width = 15)
	private String productName;//产品名称
	@Excel(name = "产品编号", width = 15)
	private String number;//产品编号
	@Excel(name = "产品类型", width = 15)
	private String productFlagName;//产品类型
	@Excel(name = "规格", width = 15)
	private String spec;//产品规格
	@Excel(name = "单位", width = 15)
	private String unitName;//单位
	@Excel(name = "单价", width = 15)
	private BigDecimal purchasePrice;//单价
	@Excel(name = "入库数量", width = 15)
	private Double productNum;//入库数量
	@Excel(name = "入库金额", width = 15)
	private BigDecimal inTotalPrice;//入库金额
	@Excel(name = "库存数量", width = 15)
	private Double stockNum;//库存数量
	@Excel(name = "领用数量", width = 15)
	private Double outProductNum;//领用数量
	@Excel(name = "领用金额", width = 15)
	private BigDecimal outTotalPrice;//领用金额
	@Excel(name = "中标号", width = 15)
	private String bidingNumber;//中标号
	@Excel(name = "注册证号", width = 15)
	private String registration;//注册证号
	@Excel(name = "生产厂家", width = 15)
	private String venderName;//生产厂家
}
