package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 盘点记录表
 * @Author: jiangxz
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Data
@TableName("pd_product_stock_check")
public class PdProductStockCheck extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**盘点编号*/
	@Excel(name = "盘点编号", width = 15)
    private String checkNo;
	/**盘点日期*/
	@Excel(name = "盘点日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date checkDate;
	/**盘点人编号*/
	@Excel(name = "盘点人编号", width = 15)
    private String checkBy;
	/**理论总数量*/
	@Excel(name = "理论总数量", width = 15)
    private Double shouldCount;
	/**盘点总数量*/
	@Excel(name = "盘点总数量", width = 15)
    private Double checkCount;
	/**盘盈盘亏数量*/
	@Excel(name = "盘盈盘亏数量", width = 15)
    private Double profitLossCount;
	/**盘点完成状态：0：临时保存  1：盘点完成*/
	@Excel(name = "盘点完成状态", width = 15)
    private String checkStatus;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private String createBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    private String updateBy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    private String remarks;

	/**科室ID*/
	@TableField(strategy = FieldStrategy.NOT_EMPTY)
	@Excel(name = "科室ID", width = 15)
	private String departId;
	/** 所属父部门*/
	@TableField(strategy = FieldStrategy.NOT_EMPTY)
	private String departParentId;
    @ExcelCollection(name="盘点明细表")
    @TableField(exist = false)
    private List<PdProductStockCheckChild> pdProductStockCheckChildList;
	/**科室名称 */
	@TableField(exist = false)
	private String deptName;
	/**盘点人名称 */
	@TableField(exist = false)
	private String checkName;
	/*多个部门集合*/
	@TableField(exist = false)
	private List<String> departIdList;

	/**审核人*/
	@Excel(name = "审核人", width = 15)
	private String auditBy;
	/**审核人*/
	@TableField(exist = false)
	@Excel(name = "审核人", width = 15)
	private String auditByName;
	@TableField(exist = false)
	@Excel(name = "审核人", width = 15)
	private String submitByName;

	/**审核时间*/
	@Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date auditDate;
	/**审核状态  审核状态 1-待审核； 2-审核通过； 3-已拒绝**/
	@Excel(name = "审核状态", width = 15)
	private String auditStatus;
	/**驳回原因*/
	@Excel(name = "驳回原因", width = 15)
	private String refuseReason;
	@TableField(exist = false)
	private String submitDateStr;

	/**
	 * 锁定状态
	 */
	private String lockingState;

	/**
	 * 需要盘点的科室
	 */
	private String targetDepartId;

}
