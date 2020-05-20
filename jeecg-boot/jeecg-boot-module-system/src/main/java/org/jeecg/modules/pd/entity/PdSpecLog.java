package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 规格数量清零操作日志表
 * @Author: jiangxz
 * @Date:   2020-05-20
 * @Version: V1.0
 */
@Data
@TableName("pd_spec_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pd_spec_log对象", description="规格数量清零操作日志表")
public class PdSpecLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
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
	/**操作人姓名*/
	@Excel(name = "操作人姓名", width = 15)
    @ApiModelProperty(value = "操作人姓名")
    private String personName;
	/**操作人ID*/
	@Excel(name = "操作人ID", width = 15)
    @ApiModelProperty(value = "操作人ID")
    private String personId;
	/**清零前剩余规格数量*/
	@Excel(name = "清零前剩余规格数量", width = 15)
    @ApiModelProperty(value = "清零前剩余规格数量")
    private Double specNum;
	/**对应的库存明细ID*/
	@Excel(name = "对应的库存明细ID", width = 15)
    @ApiModelProperty(value = "对应的库存明细ID")
    private String stockId;
	/**清零原因*/
	@Excel(name = "清零原因", width = 15)
    @ApiModelProperty(value = "清零原因")
    private String reason;
	/**产品ID*/
	@Excel(name = "产品ID", width = 15)
    @ApiModelProperty(value = "产品ID")
    private String productId;
	/**所属部门ID*/
	@Excel(name = "所属部门ID", width = 15)
    @ApiModelProperty(value = "所属部门ID")
    private String departId;
	/**所属机构ID*/
	@Excel(name = "所属机构ID", width = 15)
    @ApiModelProperty(value = "所属机构ID")
    private String departParentId;


    @TableField(exist = false)
    private String departName;//科室名称
    @TableField(exist = false)
    private String productName;//产品名称
}
