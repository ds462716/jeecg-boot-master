package org.jeecg.modules.pd.entity;

import java.util.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 盘点记录表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@ApiModel(value="pd_product_stock_check对象", description="盘点记录表")
@Data
@TableName("pd_product_stock_check")
public class PdProductStockCheck extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**盘点编号*/
	@Excel(name = "盘点编号", width = 15)
    @ApiModelProperty(value = "盘点编号")
    private String checkNo;
	/**科室ID*/
	@Excel(name = "科室ID", width = 15)
    @ApiModelProperty(value = "科室ID")
    private String deptId;
	/**盘点日期*/
	@Excel(name = "盘点日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "盘点日期")
    private Date checkDate;
	/**盘点人编号*/
	@Excel(name = "盘点人编号", width = 15)
    @ApiModelProperty(value = "盘点人编号")
    private String checkBy;
	/**理论总数量*/
	@Excel(name = "理论总数量", width = 15)
    @ApiModelProperty(value = "理论总数量")
    private Integer shouldCount;
	/**盘点总数量*/
	@Excel(name = "盘点总数量", width = 15)
    @ApiModelProperty(value = "盘点总数量")
    private Double checkCount;
	/**盘盈盘亏数量*/
	@Excel(name = "盘盈盘亏数量", width = 15)
    @ApiModelProperty(value = "盘盈盘亏数量")
    private Double profitLossCount;
	/**盘点完成状态：0：临时保存  1：盘点完成*/
	@Excel(name = "盘点完成状态：0：临时保存  1：盘点完成", width = 15)
    @ApiModelProperty(value = "盘点完成状态：0：临时保存  1：盘点完成")
    private String checkStatus;
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


    @ExcelCollection(name="盘点明细表")
    @TableField(exist = false)
    private List<PdProductStockCheckChild> pdProductStockCheckChildList;
}
