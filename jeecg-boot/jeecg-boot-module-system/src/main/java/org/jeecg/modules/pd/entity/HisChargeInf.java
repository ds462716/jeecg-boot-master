package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 收费项目基础信息
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("ex_his_charge_inf")
public class HisChargeInf extends BaseEntity {
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

    private String fsfXmbh;//收费项目编码
    private String fsfXmmc;//收费项目名称
    private String fsfXmgg;//收费项目规格
    private String fsfSpec;//规格
    private String fsfVersion;//型号
    private String fsfUnitName;//单位
    private String fsfVender;//生产厂家
    private String fsfSupplier;//供应商
    private String fsfKsbh;//收费科室代码
    private String fsfKs;//收费科室名称
    private String fsfXmlb;//收费类别标识
    private BigDecimal fsfJe;//收费金额
    private String py;//拼音码
    private String wb;//五笔码
    @TableField(exist = false)
    private String departName;//科室名称
    @TableField(exist = false)
    private BigDecimal priceDifference; //价格差
}
