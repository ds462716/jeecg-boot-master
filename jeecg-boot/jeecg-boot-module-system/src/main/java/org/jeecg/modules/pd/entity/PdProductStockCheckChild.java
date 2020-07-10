package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 盘点明细表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@ApiModel(value="pd_product_stock_check对象", description="盘点记录表")
@Data
@TableName("pd_product_stock_check_child")
public class PdProductStockCheckChild extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "id")
	private String id;
	/**盘点编号*/
	@ApiModelProperty(value = "盘点编号")
	private String checkNo;
	/**产品id*/
	@Excel(name = "产品id", width = 15)
	@ApiModelProperty(value = "产品id")
	private String productId;
	/**产品条码*/
	@Excel(name = "产品条码", width = 15)
	@ApiModelProperty(value = "产品条码")
	private String productBarCode;
	/**产品批号*/
	@Excel(name = "产品批号", width = 15)
	@ApiModelProperty(value = "产品批号")
	private String batchNo;
	/**产品有效期*/
	@Excel(name = "产品有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "产品有效期")
	private Date expDate;
	/**库存明细id*/
	@Excel(name = "库存明细id", width = 15)
	@ApiModelProperty(value = "库存明细id")
	private String stockId;
	/**实际盘点数量*/
	@Excel(name = "实际盘点数量", width = 15)
	@ApiModelProperty(value = "实际盘点数量")
	private Double checkNum;
	/**盈亏数量*/
	@Excel(name = "盈亏数量", width = 15)
	@ApiModelProperty(value = "盈亏数量")
	private Double profitLossCount;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	@ApiModelProperty(value = "修改人")
	private String updateBy;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remarks;



    /*不是该表字段**/
	@TableField(exist = false)
	@Excel(name = "产品名称", width = 15)
	private String productName;//产品名称
	@TableField(exist = false)
	@Excel(name = "产品编号", width = 15)
	private String number;//产品编号
	@TableField(exist = false)
	@Excel(name = "产品规格", width = 15)
	private String spec;//产品规格
	@TableField(exist = false)
	@Excel(name = "单位名称", width = 15)
	private String unitName;//单位名称

	@TableField(exist = false)
	@Excel(name = "生产厂家", width = 15)
	private String venderName;//生产厂家
	@TableField(exist = false)
	@Excel(name = "供应商", width = 15)
	private String supplierName;//供应商

	@TableField(exist = false)
	@Excel(name = "理论产品数量", width = 15)
	private Double stockNum;//理论产品数量



}
