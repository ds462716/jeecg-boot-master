package org.jeecg.modules.pd.entity;

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

import java.util.Date;

/**
 * @Description: 库存关联条码表
 * @Author: jiangxz
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Data
@TableName("pd_product_stock_unique_code")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pd_product_stock_unique_code对象", description="库存关联条码表")
public class PdProductStockUniqueCode extends BaseEntity{
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**库存明细表id*/
	@Excel(name = "库存明细表id", width = 15)
    @ApiModelProperty(value = "库存明细表id")
    private String productStockId;
	/**条码序号*/
    @Excel(name = "条码序号", width = 15)
    @ApiModelProperty(value = "条码序号")
    private Integer uniqueCodeOrder;
	/**打印类型0唯一码打印，1批量打印*/
	@Excel(name = "打印类型0唯一码打印，1批量打印", width = 15)
    @ApiModelProperty(value = "打印类型0唯一码打印，1批量打印")
    private String printType;
    @Excel(name = "打印次数", width = 15)
    @ApiModelProperty(value = "打印次数")
    private Integer printNum;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String departId;
	/**所属医院*/
	@Excel(name = "所属医院", width = 15)
    @ApiModelProperty(value = "所属医院")
    private String departParentId;
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

    @Excel(name = "条码状态0正常，1已退货，2已用完", width = 15)
    @ApiModelProperty(value = "条码状态0正常，1已退货，2已用完")
	private String codeState;
    /**
     * 批次号
     */
    @TableField(exist = false)
	private String batchNo;
    /**
     * 有效期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    private Date expDate;
    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;
    /**
     * 产品类型
     */
    @TableField(exist = false)
    private String productFlagName;
    /**
     * 查询开始编号
     */
    @TableField(exist = false)
    private Integer startOrder;
    /**
     * 查询结束编号
     */
    @TableField(exist = false)
    private Integer endOrder;
}
