package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

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

    @TableField(exist = false)
    @ExcelCollection(name="调拨明细表")
    private List<PdAllocationDetail> pdAllocationDetailList;

/**不在表里的字段***/
    /**操作人名称 */
    @TableField(exist = false)
    private String realName;
    /**审核人名称 */
    @TableField(exist = false)
    private String auditByName;
    /**入库科室名称 */
    @TableField(exist = false)
    private String inDeptName;
    /**出库科室名称 */
    @TableField(exist = false)
    private String outDeptName;
    /** 提交状态集合 **/
    @TableField(exist = false)
    private List<String> submitStatusList;
    /** 查询日期范围 **/
    @TableField(exist = false)
    private List<Object> queryDate;
    /** 查询日期起始 **/
    @TableField(exist = false)
    private String queryDateStart;
    /** 查询日期结束 **/
    @TableField(exist = false)
    private String queryDateEnd;
    /** 产品名称 **/
    @TableField(exist = false)
    private String productName;
    /** 产品编号 **/
    @TableField(exist = false)
    private String number;
}