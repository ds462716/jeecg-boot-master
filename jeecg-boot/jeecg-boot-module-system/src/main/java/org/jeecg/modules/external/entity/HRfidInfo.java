package org.jeecg.modules.external.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.pd.entity.BaseEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: h_rfid_info
 * @Author: mcb
 * @Date:   2020-07-13
 * @Version: V1.0
 */
@Data
@TableName("h_rfid_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HRfidInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd  HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd  HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd  HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**部门*/
	@Excel(name = "部门", width = 15)
    @ApiModelProperty(value = "部门")
    private String sysOrgCode;
	/**所属机构*/
	@Excel(name = "所属机构", width = 15)
    @ApiModelProperty(value = "所属机构")
    private String departParentId;
    /**有效期*/
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效期")
    private Date validDate;
    /**批次号*/
    @Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号")
    private String batchNo;
    /**rfid数据*/
    @Excel(name = "rfid数据", width = 15)
    @ApiModelProperty(value = "rfid数据")
    private String rfId;
    /**产品编号*/
    @Excel(name = "产品编号", width = 15)
    @ApiModelProperty(value = "产品编号")
    private String productNo;
    /**产品ID*/
    @Excel(name = "产品ID", width = 15)
    @ApiModelProperty(value = "产品ID")
    private String productId;
    /**启用标识   0：未启用   1：已启用*/
    @Excel(name = "启用标识", width = 15)
    @ApiModelProperty(value = "启用标识")
    private String isDisable;
    /**用户id*/
    @Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**对应的rkmxid*/
    @Excel(name = "对应的rkmxid", width = 15)
    @ApiModelProperty(value = "对应的rkmxid")
    private String rkmxId;
    /**条码序号*/
    @Excel(name = "条码序号", width = 15)
    @ApiModelProperty(value = "条码序号")
    private Integer uniqueCodeOrder;
    /**库存明细ID*/
    private String stockId;
    @TableField(exist = false)
    private List<String> batchNos;		// 批号组
    @TableField(exist = false)
    private List<String> rfIds;		// rfid数据组
    @TableField(exist = false)
    private String kfId;//库房ID
}
