package org.jeecg.modules.external.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 检查项目详情表
 * @Author: jiangxz
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Data
@TableName("ex_inspection_items_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ex_inspection_items_detail对象", description="检查项目详情表")
public class ExInspectionItemsDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**检查项目Id*/
	@Excel(name = "检查项目Id", width = 15)
    @ApiModelProperty(value = "检查项目Id")
    private String refId;
	/**组合名称*/
	@Excel(name = "组合名称", width = 15)
    @ApiModelProperty(value = "组合名称")
    private String testCombinationName;
	/**检查项目名称*/
	@Excel(name = "检查项目名称", width = 15)
    @ApiModelProperty(value = "检查项目名称")
    private String testitemName;
	/**检查项目代码*/
	@Excel(name = "检查项目代码", width = 15)
    @ApiModelProperty(value = "检查项目代码")
    private String testitemCode;
	/**检查项目费用*/
	@Excel(name = "检查项目费用", width = 15)
    @ApiModelProperty(value = "检查项目费用")
    private String testitemCost;
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
}
