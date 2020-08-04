package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 耗材/试剂使用统计报表
 * @Author: mcb
 * @Date:   2020-08-3
 * @Version: V1.0
 */
@Data
@TableName("pd_numerical_inf")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PdNumericalInf extends BaseEntity {
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
    /**统计月份*/
    @Excel(name = "统计月份", width = 15)
    private String month;
    /**所属科室*/
    private String departId;
    @Excel(name = "所属科室", width = 15)
    @TableField(exist = false)
    private String departName;
    /**试剂入库数量*/
    @Excel(name = "试剂入库数量", width = 15)
    private Double sjInRecordNum;
    /**试剂入库金额*/
    @Excel(name = "试剂入库金额", width = 15)
    private BigDecimal sjInRecordPrice;
    /**试剂使用数量*/
    @Excel(name = "试剂使用数量", width = 15)
    private Double sjNum;
    /**试剂使用金额*/
    @Excel(name = "试剂使用金额", width = 15)
    private BigDecimal sjPrice;
    /**试剂库存数量*/
    @Excel(name = "试剂库存数量", width = 15)
    private Double sjStockNum;
    /**试剂库存金额*/
    @Excel(name = "试剂库存金额", width = 15)
    private BigDecimal sjStockPrice;
    /**耗材入库数量*/
    @Excel(name = "耗材入库数量", width = 15)
    private Double hcInRecordNum;
    /**耗材入库金额*/
    @Excel(name = "耗材入库金额", width = 15)
    private BigDecimal hcInRecordPrice;
    /**耗材使用数量*/
    @Excel(name = "耗材使用数量", width = 15)
    private Double hcNum;
    /**耗材使用金额*/
    @Excel(name = "耗材使用金额", width = 15)
    private BigDecimal hcPrice;
    /**耗材库存数量*/
    @Excel(name = "耗材库存数量", width = 15)
    private Double hcStockNum;
    /**耗材库存金额*/
    @Excel(name = "耗材库存金额", width = 15)
    private BigDecimal hcStockPrice;
    /**检验项目总数量*/
    @Excel(name = "检验项目总数量", width = 15)
    private Double itemNum;
    /**检验项目收入金额*/
    @Excel(name = "检验项目收入金额", width = 15)
    private BigDecimal itemPrice;
    @TableField(exist = false)
    private String productFlag;//试剂 or 耗材
    @TableField(exist = false)
    private List<String> departIdList;//科室集合
    @TableField(exist = false)
    private String departIds;
}
