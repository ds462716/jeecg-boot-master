package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: pd_package_record_detail
 * @Author: jiangxz
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@ApiModel(value="pd_package_record对象", description="pd_package_record")
@Data
@TableName("pd_package_record_detail")
public class PdPackageRecordDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "id")
	private String id;
	/**打包记录ID*/
	@ApiModelProperty(value = "打包记录ID")
	private String recordId;
	/**产品ID*/
	@Excel(name = "产品ID", width = 15)
	@ApiModelProperty(value = "产品ID")
	private String productId;
	/**库存明细ID*/
	@Excel(name = "库存明细ID", width = 15)
	@ApiModelProperty(value = "库存明细ID")
	private String productStockId;
	/**批号*/
	@Excel(name = "批号", width = 15)
	@ApiModelProperty(value = "批号")
	private String batchNo;
	/**有效期*/
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "有效期")
	private java.util.Date expDate;
	/**打包数量*/
	@Excel(name = "打包数量", width = 15)
	@ApiModelProperty(value = "打包数量")
	private Double packageNum;


	@TableField(exist = false)
	private String packageRecordId;// 打包记录ID
	@TableField(exist = false)
	private String productNumber;//产品编号
	@TableField(exist = false)
	private String productName;//产品名称
	@TableField(exist = false)
	private String spec;//产品规格
	@TableField(exist = false)
	private String version;//产品型号
	@TableField(exist = false)
	private String unitName;//单位名称
	@TableField(exist = false)
	private String unitId;//单位ID
	@TableField(exist = false)
	private Double stockNum;// 库存数量
	@TableField(exist = false)
	private String productBarCode;// 产品条码
	@TableField(exist = false)
	private Double productNum;//套包产品数量
	@TableField(exist = false)
	private BigDecimal purchasePrice; //采购价
	@TableField(exist = false)
	private BigDecimal inTotalPrice;// 入库金额
	@TableField(exist = false)
	private BigDecimal sellingPrice; //销售价
	@TableField(exist = false)
	private BigDecimal outTotalPrice;//出库金额
	@TableField(exist = false)
	private String outHuoweiCode; // 出库货位编码
	@TableField(exist = false)
	private String outHuoweiName; //出库货物名称
	@TableField(exist = false)
	private String specUnitId; //规格单位
	@TableField(exist = false)
	private String specQuantity;//规格梳理
	@TableField(exist = false)
	private String supplierId;//供应商ID
	@TableField(exist = false)
	private String produceDate;//生产日期
	@TableField(exist = false)
	private String registration;//注册号
	@TableField(exist = false)
	private String venderName;//生产厂家名称
	@TableField(exist = false)
	private String distributorId;//配送商id
}
