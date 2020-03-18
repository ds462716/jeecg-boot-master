package org.jeecg.modules.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: pd_rejected_detail
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Data
@TableName("pd_rejected_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pd_rejected_detail对象", description="pd_rejected_detail")
public class PdRejectedDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**退货编号*/
	@Excel(name = "退货编号", width = 15)
    @ApiModelProperty(value = "退货ID")
    private String rejectedId;
	/**产品库存id*/
	@Excel(name = "产品库存id", width = 15)
    @ApiModelProperty(value = "产品库存id")
    private String productStockId;
	/**产品id*/
	@Excel(name = "产品id", width = 15)
    @ApiModelProperty(value = "产品id")
    private String productId;
	/**退货数量*/
	@Excel(name = "退货数量", width = 15)
    @ApiModelProperty(value = "退货数量")
    private Integer rejectedCount;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String departId;
	/**所属父部门*/
	@Excel(name = "所属父部门", width = 15)
    @ApiModelProperty(value = "所属父部门")
    private String departParentId;
    @Excel(name = "货位Code", width = 15)
    private String huoweiCode;
}
