package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecg.modules.pd.entity.PdProductStock;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 开闭瓶记录表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Data
@TableName("pd_bottle_inf")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PdBottleInf extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**开瓶操作人*/
    @Excel(name = "开瓶操作人", width = 15)
    @ApiModelProperty(value = "开瓶操作人")
    private String boottleBy;
	/**开瓶时间*/
    @Excel(name = "开瓶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开瓶时间")
    private Date boottleDate;
	/**试剂对应条码*/
	@Excel(name = "试剂对应唯一码", width = 15)
    @ApiModelProperty(value = "试剂对应唯一码")
    private String refBarCode;
	/**对应库存明细ID*/
    @ApiModelProperty(value = "对应库存明细ID")
    private String stockId;
    /**使用規格數量*/
    @Excel(name = "使用規格數量", width = 15)
    @ApiModelProperty(value = "使用規格數量")
    private Double specNum;
	/**闭瓶时间*/
    @Excel(name = "闭瓶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "闭瓶时间")
    private Date closeDate;
	/**闭瓶操作人*/
	@Excel(name = "闭瓶操作人", width = 15)
    @ApiModelProperty(value = "闭瓶操作人")
    private String closeBy;
	/**备注*/
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**闭瓶原因*/
    @Excel(name = "闭瓶原因  0:已用完  1:已过期", width = 15)
    @ApiModelProperty(value = "闭瓶原因")
    private String closeRemarks;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String departId;
	/**所属机构*/
    @ApiModelProperty(value = "所属机构")
    private String departParentId;
    /**检验仪器代号*/
    @Excel(name = "检验仪器代号", width = 15)
    @ApiModelProperty(value = "检验仪器代号")
    private String instrCode;
    @TableField(exist = false)
    private String bottleType;//操作类型   1：开瓶   2：闭瓶
    @TableField(exist = false)
    private List<PdProductStock> pdProductStockList;
    @Excel(name = "产品名称", width = 15)
    @TableField(exist = false)
    private String productName;
    @Excel(name = "所属部门", width = 15)
    @TableField(exist = false)
    private String departName;
    @Excel(name = "单位名称", width = 15)
    @TableField(exist = false)
    private String unitName;
    /*产品规格数量*/
    @Excel(name = "产品规格数量", width = 15)
    @TableField(exist = false)
    private Double specQuantity;
    /**批次号*/
    @Excel(name = "批次号", width = 15)
    @TableField(exist = false)
    private String batchNo;
    /**有效期*/
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    private Date expDate;

    /*多个部门集合*/
    @TableField(exist = false)
    private List<String> departIdList;

    @TableField(exist = false)
    private String departIds; //批量查询用

    @TableField(exist = false)
    private String productId; //产品id
    @TableField(exist = false)
    private String nestatStatus; //产品使用状态
    /**检验仪器名称*/
    @TableField(exist = false)
    private String instrName;


}
