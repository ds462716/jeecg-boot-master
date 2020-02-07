package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 申购订单主表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Data
@TableName("pd_purchase_order")
public class PdPurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**创建人*/
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
    private String sysOrgCode;
	/**申购编号*/
    private String orderNo;
	/**父级节点*/
    private String pid;
	/**申购人编号*/
    private String purchaseBy;
	/**是否有子节点*/
    private String hasChild;
	/**申购日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date orderDate;
	/**申购库房编号*/
    private String storeroomId;
	/**申购库房名称*/
    private String storeroomName;
	/**审核状态*/
    private String orderStatus;
	/**审核人编号*/
    private String auditBy;
	/**审核时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date auditDate;
	/**拒绝理由*/
    private String refuseReason;
	/**申购总数量*/
    private Integer amountCount;
	/**申购总金额*/
    private java.math.BigDecimal amountMoney;
	/**备注信息*/
    private String remarks;
	/**删除状态*/
    private String delFlag;
	/**提交状态*/
    private String submitStart;
}
