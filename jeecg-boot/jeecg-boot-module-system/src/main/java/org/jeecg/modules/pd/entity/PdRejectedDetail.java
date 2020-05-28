package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: pd_rejected_detail
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Data
@TableName("pd_rejected_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pd_rejected_detail对象", description="pd_rejected_detail")
public class PdRejectedDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**退货编号*/
    @ApiModelProperty(value = "退货ID")
    private String rejectedId;
	/**产品库存id*/
    @ApiModelProperty(value = "产品库存id")
    private String productStockId;
	/**产品id*/
    @ApiModelProperty(value = "产品id")
    private String productId;
    @ApiModelProperty(value = "产品条码")
	private String productBarCode;
    @ApiModelProperty(value = "批号")
    private String batchNo;
	/**退货数量*/
    @ApiModelProperty(value = "退货数量")
    private Double rejectedCount;
    @ApiModelProperty(value = "退货时库存数量")
    private Double stockNum;
	/**备注*/
    @ApiModelProperty(value = "备注")
    private String remarks;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String departId;
	/**所属父部门*/
    @ApiModelProperty(value = "所属父部门")
    private String departParentId;
    private String huoweiCode;

    @TableField(exist = false)
    private String rejectedNo;
    @TableField(exist = false)
    private String spec;
    @TableField(exist = false)
    private String unitName;
    @TableField(exist = false)
    private String huoweiName;
    @TableField(exist = false)
    private String version;//产品型号
    @TableField(exist = false)
    private String productName;
    @TableField(exist = false)
    private String productNumber;
    @TableField(exist = false)
    private String registration;//注册证号
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    private Date expDate;
    @TableField(exist = false)
    private String rejectedDate;
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date produceDate;
    @TableField(exist = false)
    private String venderId;//生产厂家ID
    @TableField(exist = false)
	private String supplierId;//供应商ID
    @TableField(exist = false)
    private String supplierName;
    @TableField(exist = false)
    private String venderName;//生产厂家名称
    @TableField(exist = false)
    private String departName;
    @TableField(exist = false)
    private String queryDateStart;
    @TableField(exist = false)
    private String queryDateEnd;
    @TableField(exist = false)
    private String queryExpDateStart;
    @TableField(exist = false)
    private String queryExpDateEnd;

    private String refBarCode;//唯一码

}
