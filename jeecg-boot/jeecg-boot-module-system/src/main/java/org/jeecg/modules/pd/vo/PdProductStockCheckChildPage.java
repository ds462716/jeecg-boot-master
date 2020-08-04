package org.jeecg.modules.pd.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 盘点明细表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Data
public class PdProductStockCheckChildPage {

	@Excel(name = "盘点编号", width = 15)
	private String checkNo;
	@Excel(name = "盘点库房", width = 15)
	private String targetDepartName;
	@Excel(name = "盘点日期", width = 15, format = "yyyy-MM-dd")
	private Date auditDate;
	@Excel(name = "入库单号", width = 15)
	private String recordNo;
	@Excel(name = "盈亏类型", width = 15)
	private String type;
	@Excel(name = "理论数量", width = 15)
	private Double stockNum;
	@Excel(name = "实际盘点数量", width = 15)
	private Double checkNum;
	@Excel(name = "盈亏数量", width = 15)
	private Double profitLossCount;
	@Excel(name = "产品编号", width = 15)
	private String number;//产品编号
	@Excel(name = "产品名称", width = 15)
	private String productName;//产品名称
	@Excel(name = "规格", width = 15)
	private String spec;//产品规格
	@Excel(name = "型号", width = 15)
	private String version;//产品规格
	@Excel(name = "批号", width = 15)
	private String batchNo;
	@Excel(name = "单位", width = 15)
	private String unitName;//单位名称
	@Excel(name = "产品有效期", width = 15, format = "yyyy-MM-dd")
	private Date expDate;
	@Excel(name = "生产厂家", width = 15)
	private String venderName;//生产厂家
	@TableField(exist = false)
	@Excel(name = "供应商", width = 15)
	private String supplierName;//供应商
}
