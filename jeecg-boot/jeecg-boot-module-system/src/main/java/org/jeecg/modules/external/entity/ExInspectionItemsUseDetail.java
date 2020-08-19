package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 检验项目使用详情表
 * @Author: jiangxz
 * @Date:   2020-05-11
 * @Version: V1.0
 */
@Data
@TableName("ex_inspection_items_use_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ex_inspection_items_use_detail对象", description="检验项目使用详情表")
public class ExInspectionItemsUseDetail  {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**检验项目使用id*/
	@Excel(name = "检验项目使用id", width = 15)
    @ApiModelProperty(value = "检验项目使用id")
    private String refId;
	/**产品ID*/
	@Excel(name = "产品ID", width = 15)
    @ApiModelProperty(value = "产品ID")
    private String productId;
	/**检验包的id*/
	@Excel(name = "检验包的id", width = 15)
    @ApiModelProperty(value = "检验包的id")
    private String packageId;
	/**产品条码*/
	@Excel(name = "产品条码", width = 15)
    @ApiModelProperty(value = "产品条码")
    private String productBarCode;
	/**产品批号*/
	@Excel(name = "产品批号", width = 15)
    @ApiModelProperty(value = "产品批号")
    private String batchNo;
	/**有效期*/
	@Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效期")
    private Date expDate;
	/**产品库存明细id-用于出库时记录出库库存id*/
	@Excel(name = "产品库存明细id-用于出库时记录出库库存id", width = 15)
    @ApiModelProperty(value = "产品库存明细id-用于出库时记录出库库存id")
    private String productStockId;
	/**出库货位编号*/
	@Excel(name = "出库货位编号", width = 15)
    @ApiModelProperty(value = "出库货位编号")
    private String outHuoweiCode;
	/**产品数量（出入库数量）*/
	@Excel(name = "产品数量（出入库数量）", width = 15)
    @ApiModelProperty(value = "产品数量（出入库数量）")
    private Double productNum;
	/**出库单价*/
	@Excel(name = "出库单价", width = 15)
    @ApiModelProperty(value = "出库单价")
    private BigDecimal sellingPrice;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String departId;
	/**所属父部门*/
	@Excel(name = "所属父部门", width = 15)
    @ApiModelProperty(value = "所属父部门")
    private String departParentId;
    /**唯一码*/
    @Excel(name = "唯一码", width = 15)
    private String refBarCode;//唯一码
    @TableField(exist = false)
	private String code;//检验包代码
    @TableField(exist = false)
    private String name;//检验包名称
    @TableField(exist = false)
    private String packageTrueFlag;//查询检验包产品标识
    private String packageFalseFlag;//查询检验包产品标识

    @TableField(exist = false)
    private String supplierName;
    @TableField(exist = false)
    private String venderName;

    //冗余
    @TableField(exist = false)
    private String outHuoweiName;//出库货位

    @TableField(exist = false)
    private String inHuoweiName;//入库货位
    private String inHuoweiCode;

    @TableField(exist = false)
    private String productName;//产品名称
    @TableField(exist = false)
    private String productNumber;//产品编号
    @TableField(exist = false)
    private String unitName;//单位
    @TableField(exist = false)
    private String specUnitName;//单位
    @TableField(exist = false)
    private String number;//产品编号
    @TableField(exist = false)
    private String spec;//产品规格
    @TableField(exist = false)
    private String version;//产品型号
    @TableField(exist = false)
    private String productFlagName;//产品型号
}
