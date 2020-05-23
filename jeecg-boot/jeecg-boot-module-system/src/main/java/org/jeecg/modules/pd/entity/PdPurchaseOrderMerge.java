package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 合并申购单表
 * @Author: MCB
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Data
@TableName("pd_purchase_order_merge")
public class PdPurchaseOrderMerge extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**合并申购单号*/
	@Excel(name = "合并申购单号", width = 15)
    @ApiModelProperty(value = "合并申购单号")
    private String mergeOrderNo;
	/**合并申购日期*/
	@Excel(name = "合并申购日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合并申购日期")
    private Date purchaseDate;
	/**合并科室ID*/
	@Excel(name = "合并科室ID", width = 15)
    @ApiModelProperty(value = "合并科室ID")
    private String departId;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
    private String mergeBy;
	/**合并状态*/
	@Excel(name = "合并状态", width = 15)
    private String auditStatus;
	/**供应商商受理状态.0待上传,1待接收,2已接收,3已拒绝,4已收货*/
    private String supplierStatus;
	/**备注*/
	@Excel(name = "备注", width = 15)
    private String remarks;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;
	/**所属父部门ID*/
    private String departParentId;
    /*订单总数量**/
	@Excel(name = "订单总数量", width = 15, format = "yyyy-MM-dd")
	private Double orderCount;
    /*合并申购订单orderNos*/
    @TableField(exist = false)
    private String orderNos;
	/*申购订单orderNo*/
	@TableField(exist = false)
	private String orderNo;
	/*按钮操作类型  1:批量审核    2：合并并提交   3：批量拒绝*/
	@TableField(exist = false)
	private String oprtSource;
	/*审核意见*/
	@TableField(exist = false)
	private String refuseReason;
	/*提交状态*/
	@TableField(exist = false)
	private String submitStatus;
	/**操作人名称*/
	@TableField(exist = false)
	private String mergeName;
	/**科室名称*/
	@TableField(exist = false)
	private String departName;

	/** 查询日期起始 **/
	@TableField(exist = false)
	private String queryDateStart;
	/** 查询日期结束 **/
	@TableField(exist = false)
	private String queryDateEnd;

}
