package org.jeecg.modules.pd.vo;

import java.util.List;
import org.jeecg.modules.pd.entity.PdPurchaseOrder;
import org.jeecg.modules.pd.entity.PdPurchaseDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 申购订单主表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Data
public class PdPurchaseOrderPage {
	
	/**主键*/
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
	@Excel(name = "更新人", width = 15)
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	private String sysOrgCode;
	/**申购编号*/
	@Excel(name = "申购编号", width = 15)
	private String orderNo;
	/**父级节点*/
	@Excel(name = "父级节点", width = 15)
	private String pid;
	/**申购人编号*/
	@Excel(name = "申购人编号", width = 15)
	private String purchaseBy;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15)
	private String hasChild;
	/**申购日期*/
	@Excel(name = "申购日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date orderDate;
	/**申购库房编号*/
	@Excel(name = "申购库房编号", width = 15)
	private String storeroomId;
	/**申购库房名称*/
	@Excel(name = "申购库房名称", width = 15)
	private String storeroomName;
	/**审核状态*/
	@Excel(name = "审核状态", width = 15)
	private String orderStatus;
	/**审核人编号*/
	@Excel(name = "审核人编号", width = 15)
	private String auditBy;
	/**审核时间*/
	@Excel(name = "审核时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date auditDate;
	/**拒绝理由*/
	@Excel(name = "拒绝理由", width = 15)
	private String refuseReason;
	/**申购总数量*/
	@Excel(name = "申购总数量", width = 15)
	private Integer amountCount;
	/**申购总金额*/
	@Excel(name = "申购总金额", width = 15)
	private java.math.BigDecimal amountMoney;
	/**备注信息*/
	@Excel(name = "备注信息", width = 15)
	private String remarks;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
	private String delFlag;
	/**提交状态*/
	@Excel(name = "提交状态", width = 15)
	private String submitStart;
	
	@ExcelCollection(name="申购单详细表")
	private List<PdPurchaseDetail> pdPurchaseDetailList;	
	
}
