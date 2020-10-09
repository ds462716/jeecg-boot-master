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
import java.util.List;

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
	@ApiModelProperty(value = "产品id")
	private String productId;
	/**产品条码*/
	@ApiModelProperty(value = "产品条码")
	private String productBarCode;
	/**产品批号*/
	@ApiModelProperty(value = "产品批号")
	private String batchNo;
	/**产品有效期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "产品有效期")
	private Date expDate;
	/**库存明细id*/
	@ApiModelProperty(value = "库存明细id")
	private String stockId;
	/**实际盘点数量*/
	@ApiModelProperty(value = "实际盘点数量")
	private Double checkNum;
	/**盈亏数量*/
	@ApiModelProperty(value = "盈亏数量")
	private Double profitLossCount;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	/**修改人*/
	@ApiModelProperty(value = "修改人")
	private String updateBy;
	/**备注*/
	@ApiModelProperty(value = "备注")
	private String remarks;

    /*不是该表字段**/
	@TableField(exist = false)
	private String productName;//产品名称
	@TableField(exist = false)
	private String number;//产品编号
	@TableField(exist = false)
	private String spec;//产品规格
	@TableField(exist = false)
	private String unitName;//单位名称

	@TableField(exist = false)
	private String venderName;//生产厂家
	@TableField(exist = false)
	private String supplierName;//供应商
//	@TableField(exist = false)
	private Double stockNum;//理论产品数量

	@TableField(exist = false)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date auditDate;
	@TableField(exist = false)
	private String targetDepartId;
	@TableField(exist = false)
	private String recordNo;
	@TableField(exist = false)
	private String inType;
	@TableField(exist = false)
	private String outType;
	@TableField(exist = false)
	private String type;
	@TableField(exist = false)
	private String version;
	@TableField(exist = false)
	private String targetDepartName;

	/** 查询日期起始 **/
	@TableField(exist = false)
	private String queryDateStart;
	/** 查询日期结束 **/
	@TableField(exist = false)
	private String queryDateEnd;
	@TableField(exist = false)
	private String queryExpDateStart;
	@TableField(exist = false)
	private String queryExpDateEnd;
	@TableField(exist = false)
	private String venderId;//生产厂家ID
	@TableField(exist = false)
	private String supplierId;//供应商ID

//	@TableField(exist = false)
//	private String inDepartIds; //批量查询用
//	@TableField(exist = false)
//	private List<String> inDepartIdList;
}
