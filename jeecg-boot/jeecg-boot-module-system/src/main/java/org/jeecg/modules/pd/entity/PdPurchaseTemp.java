package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 申购模板主表
 * @Author: mcb
 * @Date:   2020-02-04
 * @Version: V1.0
 */
@Data
@TableName("pd_purchase_temp")
public class PdPurchaseTemp extends BaseEntity {
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
    /**模板编号*/
    @Excel(name = "模板编号", width = 15)
    private String tempNo;
    /**模板名称*/
    @Excel(name = "模板名称", width = 15)
    private String tempName;
    /**备注信息*/
    @Excel(name = "备注信息", width = 15)
    private String remarks;
    /**模板产品总数量*/
    @Excel(name = "模板产品总数量", width = 15)
    private Double totalNum;
    /**模板科室编号*/
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    private String departId;
    /** 所属父部门*/
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    private String departParentId;
    /**模板类型*/
    @Excel(name = "模板类型", width = 15)
    private String tempType;
    /**模板创建人编号*/
    @Excel(name = "模板创建人编号", width = 15)
    private String userId;
    @TableField(exist = false)
    private List<PdPurchaseTempDetail> pdPurchaseTempDetailList;
    @TableField(exist = false)
    private String departName;
    @TableField(exist = false)
    private String realname;
}



