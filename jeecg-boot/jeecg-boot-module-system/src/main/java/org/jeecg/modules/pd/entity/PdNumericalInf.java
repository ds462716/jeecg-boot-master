package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
    private String month;
    /**所属科室*/
    private String departId;
    /**入库数量*/
    private Double inRecordNum;
    /**入库金额*/
    private BigDecimal inRecordPrice;
    /**使用数量*/
    private Double purchaseNum;
    /**使用金额*/
    private BigDecimal purchasePrice;
    /**库存数量*/
    private Double stockNum;
    /**库存金额*/
    private BigDecimal stockPrice;
    /**调入数量*/
    private Double callInNum;
    /**调入金额*/
    private BigDecimal callInPrice;
    /**出库数量*/
    private Double callOutNum;
    /**出库金额*/
    private BigDecimal callOutPrice;
    /**差异数量*/
    private Double disNum;
    /**差异金额*/
    private BigDecimal disPrice;
    /**检验项目总数量*/
    private Double itemNum;
    /**检验项目收入金额*/
    private BigDecimal itemPrice;
    /**期初库存数量*/
    private Double theStockNum;
    /**'期初库存金额'*/
    private BigDecimal theStockPrice;
    /**使用理论规格数量*/
    private Double specQuantityNum;
    /**实际使用规格数量*/
    private Double specRealityNum;
    /**差异规格数量*/
    private Double disSpecNum;
    /**退货数量*/
    private Double rejectedNum;
    /**退货金额*/
    private BigDecimal rejectedPrice;

    /**统计类型  1：试剂   0：耗材*/
    private String tjType;

    /*扩展字段*/
    @TableField(exist = false)
    private String productFlag;//试剂 or 耗材
    @TableField(exist = false)
    private List<String> departIdList;//科室集合
    @TableField(exist = false)
    private String departIds;
    @TableField(exist = false)
    private String departName;
    @TableField(exist = false)
    private String recordType;
    @TableField(exist = false)
    private List<String> inTypeList;
    @TableField(exist = false)
    private List<String> outTypeList;
    @TableField(exist = false)
    private String auditStatus;
    @TableField(exist = false)
    private List<String> outDepartIdList;//出库科室集合
}
