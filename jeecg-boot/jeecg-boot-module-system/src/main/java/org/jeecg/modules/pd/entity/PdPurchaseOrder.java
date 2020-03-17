package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 申购订单主表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Data
@TableName("pd_purchase_order")
public class PdPurchaseOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**创建人*/
    @Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建日期*/
    @Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门标识*/
    @Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
	/**申购编号*/
    @Excel(name = "申购编号", width = 15)
    private String orderNo;
    /**合并申购编号*/
    @Excel(name = "合并申购编号", width = 15)
    private String mergeOrderNo;
	/**申购人编号*/
    @Excel(name = "申购人编号", width = 15)
    private String purchaseBy;
	/**申购日期*/
    @Excel(name = "申购日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date orderDate;
	/**审核状态*/
    @Excel(name = "审核状态", width = 15)
    private String auditStatus;
	/**审核人编号*/
    @Excel(name = "审核人编号", width = 15)
    private String auditBy;
	/**审核时间*/
    @Excel(name = "审核时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date auditDate;
	/**审核意见*/
    @Excel(name = "审核意见", width = 15)
    private String refuseReason;
	/**申购总数量*/
    @Excel(name = "申购总数量", width = 15)
    private Double totalNum;
	/**申购总金额*/
    @Excel(name = "申购总金额", width = 15)
    private BigDecimal totalPrice;
	/**备注信息*/
    @Excel(name = "备注信息", width = 15)
    private String remarks;
	/**提交状态*/
    @Excel(name = "提交状态", width = 15)
    private String submitStatus;

    /**申购科室编号*/
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    @Excel(name = "申购科室id", width = 15)
    private String departId;
    /** 所属父部门*/
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    private String departParentId;
    @TableField(exist = false)
    private List<String> orderNos;		// 申购单号组

    public static String dealStrData(String data){
        String newStr = data.replaceAll(",", "','");
        return "('"+newStr+"')";
    }
}



