package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("pd_apply_order")
public class PdApplyOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**创建人*/
    @Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建日期*/
    @Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
    @Excel(name = "更新人", width = 15)
    private String updateBy;
	/**更新日期*/
    @Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
    @Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
	/**申领单号*/
    @Excel(name = "申领单号", width = 15)
    private String applyNo;
    /*提交状态*/
    private String submitStatus;
	/**申领人编号*/
    @Excel(name = "申领人编号", width = 15)
    private String applyBy;
	/**申领日期*/
    @Excel(name = "申领日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date applyDate;
	/**申领总数*/
    @Excel(name = "申领总数量", width = 15)
    private Double totalNum;
	/**实际领用个数*/
    @Excel(name = "实际发货总数量", width = 15)
    private Double factNum;
	/**申领单状态*/
    @Excel(name = "申领单状态", width = 15)
    private String auditStatus;
	/**审核人*/
    @Excel(name = "审核人", width = 15)
    private String auditBy;
	/**审核时间*/
    @Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date auditDate;
	/**审核意见*/
    @Excel(name = "审核意见", width = 15)
    private String refuseReason;
	/**备注*/
    @Excel(name = "备注", width = 15)
    private String remarks;
	/**是否在退货中*/
    @Excel(name = "是否在退货中", width = 15)
    private String isInRefund;
	/**是否完结，1是，0否*/
    @Excel(name = "是否完结", width = 15)
    private String isEnd;

    /**申领科室ID*/
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    private String departId;
    /** 所属父部门*/
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    private String departParentId;
}
