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
 * @Description: pd_rejected
 * @Author: jiangxz
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Data
@TableName("pd_rejected")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pd_rejected对象", description="pd_rejected")
public class PdRejected extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**rejectedNo*/
	@Excel(name = "rejectedNo", width = 15)
    @ApiModelProperty(value = "rejectedNo")
    private String rejectedNo;
	/**rejectedDate*/
	@Excel(name = "rejectedDate", width = 15)
    @ApiModelProperty(value = "rejectedDate")
    private String rejectedDate;
	/**departId*/
	@Excel(name = "departId", width = 15)
    @ApiModelProperty(value = "departId")
    private String departId;
	/**departName*/
//	@Excel(name = "departName", width = 15)
//    @ApiModelProperty(value = "departName")
//    private String departName;
	/**supplierId*/
	@Excel(name = "supplierId", width = 15)
    @ApiModelProperty(value = "supplierId")
    private String supplierId;
	/**supplierName*/
//	@Excel(name = "supplierName", width = 15)
//    @ApiModelProperty(value = "supplierName")
//    private String supplierName;
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
	/**所属父部门*/
	@Excel(name = "所属父部门", width = 15)
    @ApiModelProperty(value = "所属父部门")
    private String departParentId;
}
