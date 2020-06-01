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
	@Excel(name = "试剂对应条码", width = 15)
    @ApiModelProperty(value = "试剂对应条码")
    private String refBarCode;
	/**对应库存明细ID*/
	@Excel(name = "对应库存明细ID", width = 15)
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
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**闭瓶原因*/
    @Excel(name = "闭瓶原因", width = 15)
    @ApiModelProperty(value = "闭瓶原因")
    private String closeRemarks;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String departId;
	/**所属机构*/
	@Excel(name = "所属机构", width = 15)
    @ApiModelProperty(value = "所属机构")
    private String departParentId;

    @TableField(exist = false)
    private String bottleType;//操作类型   1：开瓶   2：闭瓶

    @TableField(exist = false)
    private List<PdProductStock> pdProductStockList;

    @TableField(exist = false)
    private String productName;
    @TableField(exist = false)
    private String departName;
    @TableField(exist = false)
    private String unitName;

    /*多个部门集合*/
    @TableField(exist = false)
    private List<String> departIdList;

    /*产品规格数量*/
    @TableField(exist = false)
    private String specQuantity;

    @TableField(exist = false)
    private String departIds; //批量查询用

}
