package org.jeecg.modules.pd.entity;

import java.io.Serializable;
import java.util.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 调拨记录表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Data
@TableName("pd_allocation_record")
public class PdAllocationRecord extends BaseEntity {
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
	/**调拨单编号*/
    @Excel(name = "调拨单编号", width = 15)
    private String allocationNo;
	/**调拨日期*/
    @Excel(name = "调拨日期", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date allocationDate;
	/**操作人编号*/
    private String allocationBy;
	/**出库科室ID*/
    private String outDeptId;
	/**入库科室ID*/
    private String inDeptId;
	/**调拨总数量*/
    @Excel(name = "调拨总数量", width = 15)
    private Double totalNum;
	/**审核状态*/
    @Excel(name = "审核状态", width = 15)
    private String auditStatus;
	/**审核人编号*/
    private String auditBy;
	/**审核日期*/
    @Excel(name = "审核日期", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date auditDate;
	/**审核意见*/
    @Excel(name = "审核意见", width = 15)
    private String rejectReason;
	/**备注*/
    private String remarks;
	/**提交状态*/
    private String submitStatus;
	/**删除状态*/
    private String delFlag;

    @ExcelCollection(name="调拨明细表")
    private List<PdAllocationDetail> pdAllocationDetailList;
}
