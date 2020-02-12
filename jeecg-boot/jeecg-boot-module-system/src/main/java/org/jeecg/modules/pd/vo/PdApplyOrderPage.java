package org.jeecg.modules.pd.vo;

import java.util.List;
import org.jeecg.modules.pd.entity.PdApplyDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 申领单主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
public class PdApplyOrderPage {
	
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
	/**申领单号*/
	@Excel(name = "申领单号", width = 15)
	private String applyNo;
	/**申领人*/
	@Excel(name = "申领人", width = 15)
	private String applyBy;
	/**申领日期*/
	@Excel(name = "申领日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date applyDate;
	/**申领科室ID*/
	@Excel(name = "申领科室ID", width = 15)
	private String deptId;
	/**申领科室名称*/
	@Excel(name = "申领科室名称", width = 15)
	private String deptName;
	/**申领总数*/
	@Excel(name = "申领总数", width = 15)
	private Integer applyNum;
	/**实际领用个数*/
	@Excel(name = "实际领用个数", width = 15)
	private Integer factCount;
	/**申领单状态*/
	@Excel(name = "申领单状态", width = 15)
	private String applyStatus;
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
	/**删除标识*/
	@Excel(name = "删除标识", width = 15)
	private String delFlag;
	/**是否完结，1是，0否*/
	@Excel(name = "是否完结，1是，0否", width = 15)
	private String isEnd;
	private String submitStart;
	@ExcelCollection(name="申领单明细表")
	private List<PdApplyDetail> pdApplyDetailList;
	
}
