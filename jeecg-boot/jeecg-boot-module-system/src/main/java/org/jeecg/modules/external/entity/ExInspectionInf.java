package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Data
@TableName("ex_inspection_inf")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ExInspectionInf {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "lis系统主键")
	private String jyId;
    @Excel(name = "检查项目代码", width = 15)
    @ApiModelProperty(value = "检查项目代码")
    private String code;
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**备注*/
    @Excel(name = "备注", width = 15)
    private String remarks;
    /**产品ID*/
    @Excel(name = "产品ID", width = 15)
    private String productId;
    /**
     * 所属部门
     */
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    private String departId;
    /**
     * 所属顶级部门（医院id）
     */
    @TableField(strategy = FieldStrategy.NOT_EMPTY)
    private String departParentId;

    @TableField(exist = false)
    private String productName;

    @TableField(exist = false)
    private String number;

    @TableField(exist = false)
    private String spec;
    @TableField(exist = false)
    private String version;
    @TableField(exist = false)
    private String unitName;
    @TableField(exist = false)
    private Double count;
    @TableField(exist = false)
    private String testItemName;
}
